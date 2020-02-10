package eu.isas.peptideshaker.processing;

import com.compomics.util.exceptions.ExceptionHandler;
import com.compomics.util.experiment.identification.Identification;
import com.compomics.util.experiment.identification.matches.SpectrumMatch;
import com.compomics.util.experiment.identification.spectrum_annotation.spectrum_annotators.PeptideSpectrumAnnotator;
import com.compomics.util.experiment.io.biology.protein.SequenceProvider;
import com.compomics.util.parameters.identification.IdentificationParameters;
import com.compomics.util.threading.SimpleArrayListIterator;
import com.compomics.util.waiting.WaitingHandler;
import static eu.isas.peptideshaker.PeptideShaker.TIMEOUT_DAYS;
import eu.isas.peptideshaker.fileimport.PsmImportRunnable;
import eu.isas.peptideshaker.ptm.ModificationLocalizationScorer;
import eu.isas.peptideshaker.scoring.maps.InputMap;
import eu.isas.peptideshaker.scoring.psm_scoring.BestMatchSelection;
import eu.isas.peptideshaker.validation.MatchesValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Iterates the spectrum matches and saves assumption probabilities, selects
 * best hits, scores modification localization, and refines protein mapping
 * accordingly.
 *
 * @author Marc Vaudel
 */
public class PsmProcessor {

    /**
     * The identification object.
     */
    private final Identification identification;

    /**
     * Constructor.
     *
     * @param identification the identification
     */
    public PsmProcessor(
            Identification identification
    ) {

        this.identification = identification;

    }

    /**
     * Iterates the spectrum matches and saves assumption probabilities, selects
     * best hits, scores modification localization, and refines protein mapping
     * accordingly.
     *
     * @param inputMap The input map.
     * @param identificationParameters The identification parameters.
     * @param matchesValidator The matches validator.
     * @param modificationLocalizationScorer Post-translational modifications
     * scorer.
     * @param sequenceProvider Protein sequence provider.
     * @param proteinCount Map of the protein occurrence.
     * @param nThreads The number of threads to use.
     * @param waitingHandler Waiting handler to display progress and allow
     * canceling the import.
     * @param exceptionHandler The handler of exceptions.
     *
     * @throws java.lang.InterruptedException Exception thrown if a thread is
     * interrupted.
     * @throws java.util.concurrent.TimeoutException Exception thrown if the
     * process timed out.
     */
    public void processPsms(
            InputMap inputMap,
            IdentificationParameters identificationParameters,
            MatchesValidator matchesValidator,
            ModificationLocalizationScorer modificationLocalizationScorer,
            SequenceProvider sequenceProvider,
            HashMap<String, Integer> proteinCount,
            int nThreads,
            WaitingHandler waitingHandler,
            ExceptionHandler exceptionHandler
    ) throws InterruptedException, TimeoutException {

        ArrayList<Long> spectrumKeys = new ArrayList<>(identification.getSpectrumIdentificationKeys());

        waitingHandler.setSecondaryProgressCounterIndeterminate(false);
        waitingHandler.setMaxSecondaryProgressCounter(spectrumKeys.size());

        SimpleArrayListIterator<Long> spectrumMatchKeysIterator = new SimpleArrayListIterator<>(spectrumKeys);

        ExecutorService importPool = Executors.newFixedThreadPool(nThreads);

        ArrayList<PsmProcessorRunnable> importRunnables = new ArrayList<>(nThreads);

        for (int i = 0; i < nThreads; i++) {

            importRunnables.add(
                    new PsmProcessorRunnable(
                            spectrumMatchKeysIterator,
                            identification,
                            identificationParameters,
                            inputMap,
                            matchesValidator,
                            modificationLocalizationScorer,
                            sequenceProvider,
                            proteinCount,
                            waitingHandler,
                            exceptionHandler
                    )
            );
        }

        importRunnables.forEach(
                worker -> importPool.submit(worker)
        );

        importPool.shutdown();

        if (!importPool.awaitTermination(TIMEOUT_DAYS, TimeUnit.DAYS)) {

            throw new TimeoutException("Analysis timed out (time out: " + TIMEOUT_DAYS + " days)");

        }

        waitingHandler.setSecondaryProgressCounterIndeterminate(true);

    }
}