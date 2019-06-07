package eu.isas.peptideshaker.scoring.maps;

import com.compomics.util.parameters.identification.advanced.PsmScoringParameters;
import com.compomics.util.waiting.WaitingHandler;
import eu.isas.peptideshaker.scoring.targetdecoy.TargetDecoyMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * This class contains basic information about the hits as imported from the
 * various search engine result files.
 *
 * @author Marc Vaudel
 */
public class InputMap implements Serializable {

    /**
     * Serial version UID for post-serialization compatibility.
     */
    static final long serialVersionUID = 1117083720476649996L;
    /**
     * Map of the hits as imported. One target/decoy map per identification
     * advocate (referenced by their compomics utilities index).
     */
    private final HashMap<Integer, TargetDecoyMap> inputMap = new HashMap<>();
    /**
     * Map of the hits per file as imported. advocate index &gt; file name &gt;
     * target decoy map
     */
    private final HashMap<Integer, HashMap<String, TargetDecoyMap>> inputSpecificMap = new HashMap<>();
    /**
     * Map of the intermediate scores. Name of the file &gt; advocate index &gt;
     * score index
     */
    private final HashMap<String, HashMap<Integer, HashMap<Integer, TargetDecoyMap>>> intermediateScores = new HashMap<>();
    /**
     * Map of the search engine contribution. Advocate Id &gt; Spectrum file
     * name &gt; number of validated hits.
     */
    private HashMap<Integer, HashMap<String, Integer>> advocateContribution;
    /**
     * Map of the search engine contribution. Advocate Id &gt; Spectrum file
     * name &gt; number of validated hits found by this advocate only.
     */
    private HashMap<Integer, HashMap<String, Integer>> advocateUniqueContribution;
    /**
     * The PeptideShaker number of validated hits for every file.
     */
    private HashMap<String, Integer> fileIdRate;
    /**
     * PeptideShaker unique contribution.
     */
    private HashMap<String, Integer> peptideShakerUniqueContribution;

    /**
     * Returns true for multiple search engines investigations.
     *
     * @return true for multiple search engines investigations
     */
    public boolean isMultipleAlgorithms() {
        return inputMap.size() > 1;
    }

    /**
     * Returns the number of algorithms in the input map.
     *
     * @return the number of algorithms in the input map
     */
    public int getNalgorithms() {
        return inputMap.size();
    }

    /**
     * Returns a set containing the indexes of the algorithms scored in this
     * input map.
     *
     * @return a set containing the indexes of the algorithms scored in this
     * input map
     */
    public Set<Integer> getInputAlgorithms() {
        return inputMap.keySet();
    }

    /**
     * Returns the indexes of the algorithms scored in this input map.
     *
     * @return a set containing the indexes of the algorithms scored in this
     * input map
     */
    public TreeSet<Integer> getInputAlgorithmsSorted() {

        return new TreeSet<>(inputMap.keySet());

    }

    /**
     * Returns the algorithms having an intermediate score for the given
     * spectrum file.
     *
     * @param fileName the name of the spectrum file of interest
     *
     * @return the algorithm having an intermediate score for this file
     */
    public Set<Integer> getIntermediateScoreInputAlgorithms(
            String fileName
    ) {
        return intermediateScores.get(fileName).keySet();
    }

    /**
     * Returns the target decoy map attached to the given algorithm.
     *
     * @param algorithm the utilities index of the algorithm of interest
     *
     * @return the target decoy map of interest
     */
    public TargetDecoyMap getTargetDecoyMap(
            int algorithm
    ) {
        return inputMap.get(algorithm);
    }

    /**
     * Returns the target decoy map attached to the given algorithm for a
     * specific spectrum file. Null if not found.
     *
     * @param algorithm the utilities index of the algorithm of interest
     * @param fileName the name of the spectrum file of interest
     *
     * @return the target decoy map of interest
     */
    public TargetDecoyMap getTargetDecoyMap(
            int algorithm,
            String fileName
    ) {

        HashMap<String, TargetDecoyMap> algorithmInput = inputSpecificMap.get(algorithm);

        return algorithmInput == null ? null : algorithmInput.get(fileName);

    }

    /**
     * Returns the first target/decoy map of the input map in case a single
     * algorithm was used.
     *
     * @return the first target/decoy map of the input map
     */
    public TargetDecoyMap getMap() {

        if (inputMap == null || inputMap.isEmpty()) {
            throw new IllegalArgumentException("No algorithm input found.");
        }

        if (isMultipleAlgorithms()) {
            throw new IllegalArgumentException("Multiple search engine results found.");
        }

        for (TargetDecoyMap firstMap : inputMap.values()) {
            return firstMap;
        }

        return null;
    }

    /**
     * Estimates the posterior error probability for each search engine.
     *
     * @param waitingHandler the handler displaying feedback to the user
     */
    public void estimateProbabilities(
            WaitingHandler waitingHandler
    ) {

        waitingHandler.setSecondaryProgressCounterIndeterminate(false);
        waitingHandler.resetSecondaryProgressCounter();
        waitingHandler.setMaxSecondaryProgressCounter(getNEntries() + getNEntriesSpecific());

        for (TargetDecoyMap targetDecoyMap : inputMap.values()) {

            targetDecoyMap.estimateProbabilities(waitingHandler);

            waitingHandler.increaseSecondaryProgressCounter();

            if (waitingHandler.isRunCanceled()) {
                return;
            }
        }

        for (HashMap<String, TargetDecoyMap> algorithmMaps : inputSpecificMap.values()) {

            for (TargetDecoyMap targetDecoyMap : algorithmMaps.values()) {

                targetDecoyMap.estimateProbabilities(waitingHandler);

                waitingHandler.increaseSecondaryProgressCounter();

                if (waitingHandler.isRunCanceled()) {
                    return;
                }
            }
        }

        waitingHandler.setSecondaryProgressCounterIndeterminate(true);

    }

    /**
     * returns the posterior error probability associated to the given e-value
     * for the given search-engine (indexed by its utilities index)
     *
     * @param searchEngine The search engine
     * @param eValue The e-value
     *
     * @return the posterior error probability corresponding
     */
    public double getProbability(
            int searchEngine,
            double eValue
    ) {
        
        TargetDecoyMap targetDecoyMap = inputMap.get(searchEngine);
        
        return targetDecoyMap == null ? 1.0 : targetDecoyMap.getProbability(eValue);

    }

    /**
     * Constructor.
     */
    public InputMap() {
    }

    /**
     * Adds an entry to the input map.
     *
     * @param searchEngine The search engine used referenced by its compomics
     * index
     * @param spectrumFileName the name of the inspected spectrum file
     * @param eValue The search engine e-value
     * @param isDecoy boolean indicating whether the hit was decoy
     */
    public void addEntry(
            int searchEngine,
            String spectrumFileName,
            double eValue,
            boolean isDecoy
    ) {

        TargetDecoyMap targetDecoyMap = inputMap.get(searchEngine);

        if (targetDecoyMap == null) {

            targetDecoyMap = createTargetDecoyInputMap(searchEngine);
        }

        targetDecoyMap.put(eValue, isDecoy);

        HashMap<String, TargetDecoyMap> algorithmMap = inputSpecificMap.get(searchEngine);

        if (algorithmMap == null) {

            algorithmMap = createIntermediateInputSpecificMap(searchEngine);

        }

        targetDecoyMap = algorithmMap.get(spectrumFileName);

        if (targetDecoyMap == null) {

            targetDecoyMap = createTargetDecoySpecificMap(spectrumFileName, algorithmMap);

        }

        targetDecoyMap.put(eValue, isDecoy);

    }

    /**
     * Creates the target decoy input map for the given search engine unless
     * already done by another thread.
     *
     * @param searchEngine the index of the search engine
     *
     * @return the corresponding map
     */
    private synchronized TargetDecoyMap createTargetDecoyInputMap(
            int searchEngine
    ) {

        TargetDecoyMap targetDecoyMap = inputMap.get(searchEngine);

        if (targetDecoyMap == null) {

            targetDecoyMap = new TargetDecoyMap();
            inputMap.put(searchEngine, targetDecoyMap);

        }

        return targetDecoyMap;

    }

    /**
     * Creates the intermediate input specific map for the given search engine
     * unless already done by another thread.
     *
     * @param searchEngine the index of the search engine
     *
     * @return the corresponding map
     */
    private synchronized HashMap<String, TargetDecoyMap> createIntermediateInputSpecificMap(
            int searchEngine
    ) {

        HashMap<String, TargetDecoyMap> algorithmMap = inputSpecificMap.get(searchEngine);

        if (algorithmMap == null) {

            algorithmMap = new HashMap<>(2);
            inputSpecificMap.put(searchEngine, algorithmMap);

        }

        return algorithmMap;

    }

    /**
     * Creates the target-decoy input specific map for the given search engine
     * unless already done by another thread.
     *
     * @param spectrumFileName the name of the spectrum file
     * @param algorithmMap the algorithm map
     *
     * @return the corresponding map
     */
    private synchronized TargetDecoyMap createTargetDecoySpecificMap(
            String spectrumFileName,
            HashMap<String, TargetDecoyMap> algorithmMap
    ) {

        TargetDecoyMap targetDecoyMap = algorithmMap.get(spectrumFileName);

        if (targetDecoyMap == null) {

            targetDecoyMap = new TargetDecoyMap();
            algorithmMap.put(spectrumFileName, targetDecoyMap);

        }

        return targetDecoyMap;

    }

    /**
     * Returns the number of entries in the input map.
     *
     * @return the number of entries
     */
    public int getNEntries() {

        return inputMap.values().stream()
                .mapToInt(
                        map -> map.getMapSize()
                )
                .sum();

    }

    /**
     * Returns the number of entries in the specific input map.
     *
     * @return the number of entries
     */
    public int getNEntriesSpecific() {

        return inputSpecificMap.values().stream()
                .flatMap(
                        map -> map.values().stream()
                )
                .mapToInt(
                        map -> map.getMapSize()
                )
                .sum();

    }

    /**
     * Resets the advocate contribution mappings for the given file.
     *
     * @param fileName the file of interest
     */
    public void resetAdvocateContributions(
            String fileName
    ) {

        if (advocateContribution == null) {

            advocateContribution = new HashMap<>();

        } else {

            for (HashMap<String, Integer> advocateMapping : advocateContribution.values()) {

                advocateMapping.put(fileName, 0);

            }
        }

        if (advocateUniqueContribution == null) {

            advocateUniqueContribution = new HashMap<>();

        } else {

            for (HashMap<String, Integer> advocateMapping : advocateUniqueContribution.values()) {

                advocateMapping.put(fileName, 0);

            }
        }
    }

    /**
     * Resets the advocate contribution mappings.
     */
    public void resetAdvocateContributions() {

        if (advocateContribution == null) {

            advocateContribution = new HashMap<>();

        } else {

            advocateContribution.clear();

        }

        if (advocateUniqueContribution == null) {

            advocateUniqueContribution = new HashMap<>();

        } else {

            advocateUniqueContribution.clear();

        }

        if (fileIdRate == null) {

            fileIdRate = new HashMap<>();

        } else {

            fileIdRate.clear();

        }

        if (peptideShakerUniqueContribution == null) {

            peptideShakerUniqueContribution = new HashMap<>();

        } else {

            peptideShakerUniqueContribution.clear();

        }
    }

    /**
     * Adds an advocate contribution.
     *
     * @param advocateId the index of the advocate
     * @param fileName the name of the spectrum file of interest
     * @param unique boolean indicating whether the advocate was the only
     * advocate for the considered assumption
     */
    public synchronized void addAdvocateContribution(
            int advocateId,
            String fileName,
            boolean unique
    ) {

        HashMap<String, Integer> advocateContributions = advocateContribution.get(advocateId);

        if (advocateContributions == null) {

            advocateContributions = new HashMap<>();
            advocateContribution.put(advocateId, advocateContributions);

        }

        Integer contribution = advocateContributions.get(fileName);

        if (contribution == null) {

            advocateContributions.put(fileName, 1);

        } else {

            advocateContributions.put(fileName, contribution + 1);

        }

        if (unique) {

            HashMap<String, Integer> advocateUniqueContributions = advocateUniqueContribution.get(advocateId);

            if (advocateUniqueContributions == null) {

                advocateUniqueContributions = new HashMap<>();
                advocateUniqueContribution.put(advocateId, advocateUniqueContributions);

            }

            Integer uniqueContribution = advocateUniqueContributions.get(fileName);

            if (uniqueContribution == null) {

                advocateUniqueContributions.put(fileName, 1);

            } else {

                advocateUniqueContributions.put(fileName, uniqueContribution + 1);

            }
        }
    }

    /**
     * Adds a PeptideShaker hit for the given file.
     *
     * @param fileName the name of the spectrum file of interest
     * @param unique boolean indicating whether the advocate was the only
     * advocate for the considered assumption
     */
    public synchronized void addPeptideShakerHit(
            String fileName,
            boolean unique
    ) {

        Integer contribution = fileIdRate.get(fileName);

        if (contribution == null) {

            fileIdRate.put(fileName, 1);

        } else {

            fileIdRate.put(fileName, contribution + 1);

        }

        if (unique) {

            contribution = peptideShakerUniqueContribution.get(fileName);

            if (contribution == null) {

                peptideShakerUniqueContribution.put(fileName, 1);

            } else {

                peptideShakerUniqueContribution.put(fileName, contribution + 1);

            }
        }
    }

    /**
     * Returns the contribution of validated hits of the given advocate for the
     * given file.
     *
     * @param advocateId the advocate index
     * @param fileName the name of the spectrum file
     *
     * @return the number of validated hits supported by this advocate
     */
    public int getAdvocateContribution(
            int advocateId,
            String fileName
    ) {

        HashMap<String, Integer> advocateContributions = advocateContribution.get(advocateId);

        if (advocateContributions != null) {

            Integer contribution = advocateContributions.get(fileName);

            if (contribution != null) {

                return contribution;

            }
        }

        return 0;

    }

    /**
     * Returns the number of validated hits for the given file.
     *
     * @param fileName the name of the spectrum file
     *
     * @return the number of validated hits supported by this advocate
     */
    public int getPeptideShakerHits(
            String fileName
    ) {

        Integer contribution = fileIdRate.get(fileName);

        if (contribution != null) {

            return contribution;

        }

        return 0;

    }

    /**
     * Returns the contribution of validated hits of the given advocate for the
     * entire dataset.
     *
     * @param advocateId the advocate index
     *
     * @return the number of validated hits supported by this advocate
     */
    public int getAdvocateContribution(
            int advocateId
    ) {

        HashMap<String, Integer> advocateContributions = advocateContribution.get(advocateId);

        if (advocateContributions != null) {

            int contribution = 0;

            for (int tempContribution : advocateContributions.values()) {

                contribution += tempContribution;

            }

            return contribution;

        }

        return 0;

    }

    /**
     * Returns the number of PeptideShaker validated hits for the entire
     * dataset.
     *
     * @return the number of validated hits supported by this advocate
     */
    public int getPeptideShakerHits() {

        int contribution = 0;

        for (int tempContribution : fileIdRate.values()) {

            contribution += tempContribution;

        }

        return contribution;

    }

    /**
     * Returns the contribution of unique validated hits of the given advocate
     * for the given file.
     *
     * @param advocateId the advocate index
     * @param fileName the name of the spectrum file
     *
     * @return the number of validated hits uniquely supported by this advocate
     */
    public int getAdvocateUniqueContribution(
            int advocateId,
            String fileName
    ) {

        HashMap<String, Integer> advocateContributions = advocateUniqueContribution.get(advocateId);

        if (advocateContributions != null) {

            Integer contribution = advocateContributions.get(fileName);

            if (contribution != null) {

                return contribution;

            }
        }

        return 0;

    }

    /**
     * Returns the contribution of unique validated hits from PeptideShaker for
     * the given file.
     *
     * @param fileName the name of the spectrum file
     *
     * @return the number of validated hits uniquely supported by PeptideShaker
     */
    public int getPeptideShakerUniqueContribution(
            String fileName
    ) {

        Integer contribution = peptideShakerUniqueContribution.get(fileName);

        if (contribution != null) {

            return contribution;

        }

        return 0;

    }

    /**
     * Returns the contribution of unique validated hits of the given advocate
     * for the entire dataset.
     *
     * @param advocateId the advocate index
     *
     * @return the number of validated hits uniquely supported by this advocate
     */
    public int getAdvocateUniqueContribution(
            int advocateId
    ) {

        HashMap<String, Integer> advocateContributions = advocateUniqueContribution.get(advocateId);

        return advocateContributions == null ? 0
                : advocateContributions.values().stream()
                        .mapToInt(a -> a)
                        .sum();

    }

    /**
     * Returns the contribution of unique validated hits by PeptideShaker for
     * the entire dataset.
     *
     * @return the number of validated hits uniquely supported by this advocate
     */
    public int getPeptideShakerUniqueContribution() {
        
        return peptideShakerUniqueContribution.values().stream()
                .mapToInt(a -> a)
                .sum();
        
    }

    /**
     * Returns a list of all target decoy maps contained in this mapping.
     *
     * @return all target decoy maps contained in this mapping
     */
    public ArrayList<TargetDecoyMap> getTargetDecoyMaps() {
        
        return inputSpecificMap.values().stream()
                .flatMap(advocateMapping -> advocateMapping.values().stream())
                .collect(Collectors.toCollection(ArrayList::new));
        
    }

    /**
     * Indicates whether the advocate contributions are present in this map.
     *
     * @return a boolean indicating whether the advocate contributions are
     * present in this map
     */
    public boolean hasAdvocateContribution() {
        
        return advocateContribution != null;
    
    }

    /**
     * Adds an intermediate score for a given match.
     *
     * @param fileName the name of the spectrum file of interest
     * @param advocateIndex the index of the advocate
     * @param scoreIndex the index of the score
     * @param score the score of the match
     * @param decoy indicates whether the match maps to a target or a decoy
     * protein
     * @param psmScoringPreferences the psm scoring preferences
     */
    public void setIntermediateScore(
            String fileName, 
            int advocateIndex, 
            int scoreIndex, 
            double score, 
            boolean decoy, 
            PsmScoringParameters psmScoringPreferences
    ) {

        HashMap<Integer, HashMap<Integer, TargetDecoyMap>> advocateMap = intermediateScores.get(fileName);

        if (advocateMap == null) {

            advocateMap = createIntermediateScoreMap(fileName);

        }

        HashMap<Integer, TargetDecoyMap> scoreMap = advocateMap.get(advocateIndex);

        if (scoreMap == null) {

            scoreMap = createIntermediateScoreMap(advocateIndex, advocateMap);

        }

        TargetDecoyMap targetDecoyMap = scoreMap.get(scoreIndex);

        if (targetDecoyMap == null) {

            targetDecoyMap = createTargetDecoyMap(scoreIndex, scoreMap, psmScoringPreferences);

        }

        targetDecoyMap.put(score, decoy);

    }

    /**
     * Creates the intermediate score map for the given file if not set before
     * by another thread.
     *
     * @param fileName the name of the file
     *
     * @return the intermediate score map
     */
    private synchronized HashMap<Integer, HashMap<Integer, TargetDecoyMap>> createIntermediateScoreMap(
            String fileName
    ) {
    
        HashMap<Integer, HashMap<Integer, TargetDecoyMap>> advocateMap = intermediateScores.get(fileName);
        
        if (advocateMap == null) {
        
            advocateMap = new HashMap<>();
            intermediateScores.put(fileName, advocateMap);
        
        }
        
        return advocateMap;
    
    }

    /**
     * Creates the intermediate score map for the given advocate if not set
     * before by another thread.
     *
     * @param advocateIndex the index of the advocate
     * @param advocateMap the map for the current file
     *
     * @return the intermediate score map
     */
    private synchronized HashMap<Integer, TargetDecoyMap> createIntermediateScoreMap(
            int advocateIndex, 
            HashMap<Integer, HashMap<Integer, TargetDecoyMap>> advocateMap
    ) {
        
        HashMap<Integer, TargetDecoyMap> scoreMap = advocateMap.get(advocateIndex);
        
        if (scoreMap == null) {
        
            scoreMap = new HashMap<>();
            advocateMap.put(advocateIndex, scoreMap);
        
        }
        
        return scoreMap;
    
    }

    /**
     * Creates the target-decoy map for the given score index if not created
     * before by another thread.
     *
     * @param scoreIndex the index of the score
     * @param scoreMap the intermediate map for this advocate
     * @param psmScoringPreferences the psm scoring preferences
     *
     * @return the target-decoy map
     */
    private synchronized TargetDecoyMap createTargetDecoyMap(
            int scoreIndex, 
            HashMap<Integer, TargetDecoyMap> scoreMap, 
            PsmScoringParameters psmScoringPreferences
    ) {
    
        TargetDecoyMap targetDecoyMap = scoreMap.get(scoreIndex);
        
        if (targetDecoyMap == null) {
        
            targetDecoyMap = new TargetDecoyMap(psmScoringPreferences.getDecoysInFirstBin());
            scoreMap.put(scoreIndex, targetDecoyMap);
        
        }
        
        return targetDecoyMap;
    
    }

    /**
     * Returns the target decoy map associated to a given spectrum file,
     * advocate and score type. Null if not found.
     *
     * @param fileName the name of the spectrum file
     * @param advocateIndex the index of the advocate
     * @param scoreIndex the index of the score
     *
     * @return the target decoy map associated to the given spectrum file,
     * advocate and score type
     */
    public TargetDecoyMap getIntermediateScoreMap(
            String fileName, 
            int advocateIndex, 
            int scoreIndex
    ) {
    
        HashMap<Integer, HashMap<Integer, TargetDecoyMap>> advocateMap = intermediateScores.get(fileName);
        
        if (advocateMap != null) {
        
            HashMap<Integer, TargetDecoyMap> scoreMap = advocateMap.get(advocateIndex);
            
            if (scoreMap != null) {
            
                TargetDecoyMap targetDecoyMap = scoreMap.get(scoreIndex);
                return targetDecoyMap;
            
            }
        }
        
        return null;
    
    }

}
