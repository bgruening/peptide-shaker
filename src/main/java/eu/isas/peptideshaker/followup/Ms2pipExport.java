package eu.isas.peptideshaker.followup;

import com.compomics.util.exceptions.ExceptionHandler;
import com.compomics.util.experiment.biology.ions.Ion;
import com.compomics.util.experiment.biology.proteins.Peptide;
import com.compomics.util.experiment.biology.ions.impl.PeptideFragmentIon;
import com.compomics.util.experiment.identification.Identification;
import com.compomics.util.experiment.identification.matches.IonMatch;
import com.compomics.util.experiment.identification.matches.SpectrumMatch;
import com.compomics.util.experiment.identification.matches_iterators.SpectrumMatchesIterator;
import com.compomics.util.experiment.identification.peptide_fragmentation.models.ms2pip.features_configuration.FeaturesMap;
import com.compomics.util.experiment.identification.peptide_fragmentation.models.ms2pip.features_configuration.Ms2pipFeature;
import com.compomics.util.experiment.identification.peptide_fragmentation.models.ms2pip.features_generation.FeaturesGenerator;
import com.compomics.util.experiment.identification.spectrum_annotation.AnnotationParameters;
import com.compomics.util.experiment.identification.spectrum_annotation.SpecificAnnotationParameters;
import com.compomics.util.experiment.identification.spectrum_annotation.spectrum_annotators.PeptideSpectrumAnnotator;
import com.compomics.util.experiment.identification.spectrum_assumptions.PeptideAssumption;
import com.compomics.util.experiment.io.biology.protein.SequenceProvider;
import com.compomics.util.experiment.mass_spectrometry.SimpleNoiseDistribution;
import com.compomics.util.experiment.mass_spectrometry.SpectrumFactory;
import com.compomics.util.experiment.mass_spectrometry.spectra.Spectrum;
import com.compomics.util.io.compression.ZipUtils;
import com.compomics.util.parameters.identification.IdentificationParameters;
import com.compomics.util.parameters.identification.advanced.SequenceMatchingParameters;
import com.compomics.util.parameters.identification.search.ModificationParameters;
import com.compomics.util.waiting.WaitingHandler;
import com.compomics.util.experiment.identification.peptide_shaker.PSParameter;
import com.compomics.util.experiment.identification.validation.MatchValidationLevel;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

/**
 * Export for ms2pip training files.
 *
 * @author Marc Vaudel
 */
public class Ms2pipExport {

    /**
     * A handler for the exceptions.
     */
    private final ExceptionHandler exceptionHandler;
    /**
     * A waiting handler providing feedback to the user and allowing canceling
     * the process.
     */
    private final WaitingHandler waitingHandler;
    /**
     * The end of line separator.
     */
    public static final String END_LINE = System.getProperty("line.separator");
    /**
     * The columns separator.
     */
    public final static char separator = ' ';
    /**
     * The columns separator.
     */
    public final static String documentationSeparator = "\t";
    /**
     * The name of the target file.
     */
    public final static String fileName = "targets";
    /**
     * The name of the documentation file.
     */
    public final static String documentationFileName = "features_description.txt";
    /**
     * Encoding for the file according to the second rule.
     */
    public static final String encoding = "UTF-8";
    /**
     * The spectrum factory.
     */
    private final SpectrumFactory spectrumFactory = SpectrumFactory.getInstance();
    /**
     * Array of buffered writers for the target files.
     */
    private BufferedWriter[] bufferedWriters;
    /**
     * Array of mutexes for the writing.
     */
    private Semaphore[] semaphores;
    /**
     * The features map to use.
     */
    private FeaturesMap featuresMap;

    /**
     * Constructor.
     *
     * @param waitingHandler a waiting handler
     * @param exceptionHandler an exception handler
     */
    public Ms2pipExport(WaitingHandler waitingHandler, ExceptionHandler exceptionHandler) {

        this.waitingHandler = waitingHandler;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Exports the ms2pip features and associated targets in a file along with
     * documentation.
     *
     * @param identificationParameters the identification parameters
     * @param destinationFolder the folder where to write the results
     * @param cpsFileName the name of the cps file
     * @param identification the identification object containing the PSMs
     * @param sequenceProvider the sequence provider
     * @param featuresMap the ms2pip features map to use
     * @param nThreads the number of threads to use
     *
     * @throws IOException thrown whenever an error occurred while writing or
     * reading a file
     * @throws InterruptedException thrown whenever a thread got interrupted.
     */
    public void exportFeatures(IdentificationParameters identificationParameters, File destinationFolder, String cpsFileName, Identification identification, SequenceProvider sequenceProvider, FeaturesMap featuresMap, int nThreads) throws IOException, InterruptedException {

        this.featuresMap = featuresMap;

        writeDocumentation(destinationFolder);

        bufferedWriters = new BufferedWriter[2];
        semaphores = new Semaphore[2];

        String header = getHeaderLine();

        for (int i = 0; i < 2; i++) {
            File destinationFile = getFeaturesFile(destinationFolder, i);
            FileOutputStream fileStream = new FileOutputStream(destinationFile);
            GZIPOutputStream gzipStream = new GZIPOutputStream(fileStream);
            OutputStreamWriter encoder = new OutputStreamWriter(gzipStream, encoding);
            BufferedWriter bw = new BufferedWriter(encoder);
            bufferedWriters[i] = bw;
            semaphores[i] = new Semaphore(1);
            writeLine(i, header);
        }

        SpectrumMatchesIterator psmIterator = identification.getSpectrumMatchesIterator(waitingHandler);

        ExecutorService pool = Executors.newFixedThreadPool(nThreads);

        for (int i = 0; i < nThreads; i++) {
            PsmProcessor psmProcessor = new PsmProcessor(sequenceProvider, psmIterator, identificationParameters);
            pool.submit(psmProcessor);
        }

        waitingHandler.setSecondaryProgressCounterIndeterminate(false);
        waitingHandler.setMaxSecondaryProgressCounter(identification.getSpectrumIdentificationSize());

        pool.shutdown();
        if (!pool.awaitTermination(7, TimeUnit.DAYS)) {
            throw new InterruptedException("Features extraction timed out. Please contact the developers.");
        }

        for (BufferedWriter bw : bufferedWriters) {
            bw.close();
        }

        waitingHandler.setSecondaryProgressCounterIndeterminate(true);

        packageResults(destinationFolder, cpsFileName);

    }

    /**
     * Packages the target files and documentation in a single zip file.
     *
     * @param destinationFolder the folder where to write the results
     *
     * @throws IOException thrown whenever an error occurred while reading or
     * writing a file
     */
    private void packageResults(File destinationFolder, String cpsFileName) throws IOException {

        File destinationFile = new File(destinationFolder, cpsFileName + "_" + fileName + ".zip");

        try (FileOutputStream fos = new FileOutputStream(destinationFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                ZipOutputStream out = new ZipOutputStream(bos)) {

            File documentationFile = getDocumentationFile(destinationFolder);
            ZipUtils.addFileToZip(documentationFile, out);
            documentationFile.delete();
            for (int i = 0; i < 2; i++) {
                int index = i;
                File featuresFile = getFeaturesFile(destinationFolder, index);
                ZipUtils.addFileToZip(featuresFile, out);
                featuresFile.delete();
            }
        }
    }

    /**
     * Writes the documentation file.
     *
     * @param destinationFolder the folder where to write the file
     *
     * @throws IOException thrown whenever an error occurred while reading or
     * writing a file
     */
    private void writeDocumentation(File destinationFolder) throws IOException {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getDocumentationFile(destinationFolder)))) {

            StringBuilder stringBuilder = new StringBuilder();

            int index = 0;

            for (String category : featuresMap.getSortedFeaturesList()) {

                for (Ms2pipFeature ms2pipFeature : featuresMap.getFeatures(category)) {

                    stringBuilder.append(index++).append(documentationSeparator).append(category).append(documentationSeparator).append(ms2pipFeature.getDescription()).append(END_LINE);
                    bw.write(stringBuilder.toString());
                    stringBuilder = new StringBuilder(stringBuilder.length());

                }
            }
        }
    }

    /**
     * Returns the file to use for documentation.
     *
     * @param destinationFolder the folder where to write the file
     *
     * @return the file to use for documentation
     */
    public static File getDocumentationFile(File destinationFolder) {
        return new File(destinationFolder, documentationFileName);
    }

    /**
     * Returns the file to use for targets.
     *
     * @param destinationFolder the folder where to write the file
     * @param index the index of the ion
     *
     * @return the file to use for targets
     */
    public static File getFeaturesFile(File destinationFolder, int index) {
        char ion = index == 0 ? 'b' : 'y';
        return new File(destinationFolder, fileName + "_" + ion);
    }

    /**
     * Writes the given line to the writer at the given index.
     *
     * @param index the index of the ion
     * @param line the line to write
     *
     * @throws IOException thrown whenever an error occurred while writing or
     * reading a file
     * @throws InterruptedException thrown whenever a thread got interrupted.
     */
    private void writeLine(int index, String line) throws InterruptedException, IOException {

        BufferedWriter bw = bufferedWriters[index];

        Semaphore semaphore = semaphores[index];

        semaphore.acquire();

        bw.write(line);

        semaphore.release();
    }

    /**
     * Returns the header line for the target file.
     *
     * @return the header line for the target file
     */
    private String getHeaderLine() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("psmid").append(separator).append("target");

        int index = 0;

        for (String category : featuresMap.getSortedFeaturesList()) {

            int nFeatures = featuresMap.getFeatures(category).length;

            for (int i = 0; i < nFeatures; i++) {

                stringBuilder.append(separator).append(index++);

            }
        }

        stringBuilder.append(END_LINE);

        return stringBuilder.toString();
    }

    /**
     * Returns the line for the given content.
     *
     * @param psmId the psm id
     * @param target the target value
     * @param features the features in an array
     *
     * @return the line to write
     */
    private String getLine(String psmId, double target, int[] features) throws UnsupportedEncodingException {

        StringBuilder stringBuilder = new StringBuilder();
        String encodedPsmId = URLEncoder.encode(psmId, encoding);
        stringBuilder.append(encodedPsmId).append(separator).append(target);
        for (int feature : features) {
            stringBuilder.append(separator).append(feature);
        }
        stringBuilder.append(END_LINE);
        return stringBuilder.toString();
    }

    /**
     * Private runnable to process a sequence.
     */
    private class PsmProcessor implements Runnable {

        /**
         * The identification parameters.
         */
        private final IdentificationParameters identificationParameters;
        /**
         * The iterator to go through the PSMs.
         */
        private final SpectrumMatchesIterator psmIterator;
        /**
         * The spectrum annotator.
         */
        private final PeptideSpectrumAnnotator spectrumAnnotator = new PeptideSpectrumAnnotator();
        /**
         * The ms2pip features generator.
         */
        private final FeaturesGenerator featuresGenerator;
        /**
         * The protein sequence provider.
         */
        private final SequenceProvider sequenceProvider;

        /**
         * Constructor.
         *
         * @param sequenceProvider the protein sequence provider
         * @param psmIterator the psm iterator
         * @param identificationParameters the identification parameters
         */
        private PsmProcessor(SequenceProvider sequenceProvider, SpectrumMatchesIterator psmIterator, IdentificationParameters identificationParameters) {

            this.psmIterator = psmIterator;
            this.identificationParameters = identificationParameters;
            featuresGenerator = new FeaturesGenerator(featuresMap);
            this.sequenceProvider = sequenceProvider;

        }

        @Override
        public void run() {

            try {

                PSParameter psParameter = new PSParameter();

                SpectrumMatch spectrumMatch;
                while ((spectrumMatch = psmIterator.next()) != null && !waitingHandler.isRunCanceled()) {

                    PeptideAssumption peptideAssumption = spectrumMatch.getBestPeptideAssumption();

                    if (peptideAssumption != null) {

                        String spectrumKey = spectrumMatch.getSpectrumKey();

                        psParameter = (PSParameter) spectrumMatch.getUrParam(psParameter);

                        if (psParameter.getMatchValidationLevel() == MatchValidationLevel.confident) {

                            ModificationParameters modificationParameters = identificationParameters.getSearchParameters().getModificationParameters();
                            SequenceMatchingParameters modificationSequenceMatchingParameters = identificationParameters.getModificationLocalizationParameters().getSequenceMatchingParameters();
                            AnnotationParameters annotationParameters = identificationParameters.getAnnotationParameters();
                            SpecificAnnotationParameters specificAnnotationSettings = annotationParameters.getSpecificAnnotationParameters(spectrumKey, peptideAssumption, modificationParameters, sequenceProvider, modificationSequenceMatchingParameters, spectrumAnnotator);

                            Spectrum spectrum = spectrumFactory.getSpectrum(spectrumKey, false);

                            Peptide peptide = peptideAssumption.getPeptide();
                            int charge = peptideAssumption.getIdentificationCharge();

                            IonMatch[] ionMatches = spectrumAnnotator.getSpectrumAnnotation(annotationParameters, specificAnnotationSettings, spectrum, peptide, modificationParameters, sequenceProvider, modificationSequenceMatchingParameters, false);

                            SimpleNoiseDistribution binnedCumulativeFunction = spectrum.getIntensityLogDistribution();

                            for (IonMatch ionMatch : ionMatches) {

                                Ion ion = ionMatch.ion;

                                if (ion.getType() == Ion.IonType.PEPTIDE_FRAGMENT_ION) {

                                    PeptideFragmentIon peptideFragmentIon = (PeptideFragmentIon) ion;

                                    if (peptideFragmentIon.getNeutralLosses() == null || peptideFragmentIon.getNeutralLosses().length == 0) {

                                        double intensity = ionMatch.peak.intensity;

                                        int index;
                                        int aaIndex;
                                        int[] features;
                                        switch (peptideFragmentIon.getSubType()) {
                                            case PeptideFragmentIon.A_ION:
                                            case PeptideFragmentIon.B_ION:
                                            case PeptideFragmentIon.C_ION:
                                                index = 0;
                                                aaIndex = peptideFragmentIon.getNumber() - 1;
                                                features = featuresGenerator.getForwardIonsFeatures(peptide, charge, aaIndex, modificationParameters, sequenceProvider, modificationSequenceMatchingParameters);
                                                break;
                                            case PeptideFragmentIon.X_ION:
                                            case PeptideFragmentIon.Y_ION:
                                            case PeptideFragmentIon.Z_ION:
                                                index = 1;
                                                aaIndex = peptide.getSequence().length() - peptideFragmentIon.getNumber();
                                                features = featuresGenerator.getComplementaryIonsFeatures(peptide, charge, aaIndex, modificationParameters, sequenceProvider, modificationSequenceMatchingParameters);
                                                break;
                                            default:
                                                throw new UnsupportedOperationException("Peptide fragment ion of type " + ion.getSubTypeAsString() + " not supported.");
                                        }

                                        double pMinusLog = -binnedCumulativeFunction.getBinnedCumulativeProbabilityLog(intensity);

                                        String line = getLine(spectrumKey, pMinusLog, features);

                                        writeLine(index, line);

                                    }
                                }
                            }
                        }
                    }

                    waitingHandler.increaseSecondaryProgressCounter();

                }

            } catch (Exception e) {
                exceptionHandler.catchException(e);
            }
        }

    }

}
