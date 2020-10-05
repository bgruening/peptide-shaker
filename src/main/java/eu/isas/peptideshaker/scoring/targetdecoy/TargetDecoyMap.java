package eu.isas.peptideshaker.scoring.targetdecoy;

import com.compomics.util.experiment.personalization.ExperimentObject;
import com.compomics.util.waiting.WaitingHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This map contains the information of a target/decoy strategy.
 *
 * @author Marc Vaudel
 */
public class TargetDecoyMap extends ExperimentObject {

    /**
     * Serial version UID for post-serialization compatibility.
     */
    static final long serialVersionUID = 7333389442377322662L;
    /**
     * The hit map containing the indexed target/decoy points.
     */
    private HashMap<Double, TargetDecoyPoint> hitMap = new HashMap<>();
    /**
     * The scores imported in the map.
     */
    private ArrayList<Double> scores;
    /**
     * The number of decoy matches to include in the first bin to set the bin
     * size nmax. Two means that two consecutive decoys will be used.
     */
    private Integer minDecoysInBin = 2;
    /**
     * The bin size, by default the maximal number of target hits comprised
     * between minDecoysInBin subsequent decoy hits.
     */
    private Integer nmax;
    /**
     * The window size for pep estimation.
     */
    private Integer windowSize;
    /**
     * The number of target hits found before the first decoy hit.
     */
    private Integer nTargetOnly;
    /**
     * The minimal FDR which can be achieved on the dataset.
     */
    private double minFDR = 1.0;
    /**
     * The results computed on this map.
     */
    private TargetDecoyResults targetDecoyResults = new TargetDecoyResults();

    /**
     * Constructor.
     */
    public TargetDecoyMap() {
    }

    /**
     * Constructor.
     *
     * @param minDecoysInBin the number of decoy matches to include in the first
     * bin to set the bin size
     */
    public TargetDecoyMap(Integer minDecoysInBin) {

        this.minDecoysInBin = minDecoysInBin;

    }

    /**
     * Returns the posterior error probability estimated at the given score.
     *
     * @param score the given score
     * @return the estimated posterior error probability
     */
    public double getProbability(double score) {
        
        
        
        TargetDecoyPoint point = hitMap.get(score);
        
        if (point != null) {
        
            return point.p;
        
        } else if (score >= scores.get(scores.size() - 1)) {
        
            return hitMap.get(scores.get(scores.size() - 1)).p;
        
        } else {
        
            int indexDown = 0;
            int indexUp = scores.size() - 1;
            int indexTemp;
            
            while (indexUp - indexDown > 1) {
            
                indexTemp = (indexUp - indexDown) / 2 + indexDown;
                
                if (scores.get(indexTemp) > score) {
                
                    indexUp = indexTemp;
                
                } else {
                
                    indexDown = indexTemp;
                
                }
            }
            
            return (hitMap.get(scores.get(indexUp)).p + hitMap.get(scores.get(indexDown)).p) / 2;
        
        }
    }

    /**
     * Returns the number of target hits found at the given score.
     *
     * @param score the given score
     * @return the number of target hits found at the given score
     */
    public int getNTarget(double score) {
        
        return hitMap.get(score).nTarget;
    }

    /**
     * Returns the number of decoy hits found at the given score.
     *
     * @param score the given score
     * @return the number of decoy hits found at the given score
     */
    public int getNDecoy(double score) {
        return hitMap.get(score).nDecoy;
    }

    /**
     * Puts a new point in the target/decoy map at the given score.
     *
     * @param score The given score
     * @param isDecoy boolean indicating whether the hit is decoy
     */
    public void put(double score, boolean isDecoy) {
        
        
        
        TargetDecoyPoint targetDecoyPoint = hitMap.get(score);
        
        if (targetDecoyPoint == null) {
        
            targetDecoyPoint = createTargetDecoyPoint(score);
        
        }

        if (isDecoy) {

            targetDecoyPoint.increaseDecoy();

        } else {

            targetDecoyPoint.increaseTarget();

        }
    }

    /**
     * Creates the target decoy point of the map at the given score if no other
     * thread has done it before.
     *
     * @param score the score of interest
     *
     * @return the target decoy point of the map at the given score
     */
    public synchronized TargetDecoyPoint createTargetDecoyPoint(double score) {
        
        
        
        TargetDecoyPoint targetDecoyPoint = hitMap.get(score);
        
        if (targetDecoyPoint == null) {
        
            targetDecoyPoint = new TargetDecoyPoint();
            hitMap.put(score, targetDecoyPoint);

        }

        return targetDecoyPoint;

    }

    /**
     * Removes a point in the target/decoy map at the given score. Note: it is
     * necessary to run cleanUp() afterwards to clean up the map.
     *
     * @param score the given score
     * @param isDecoy boolean indicating whether the hit is decoy
     */
    public void remove(double score, boolean isDecoy) {

        

        TargetDecoyPoint targetDecoyPoint = hitMap.get(score);

        if (!isDecoy) {

            targetDecoyPoint.decreaseTarget();

        } else {

            targetDecoyPoint.decreaseDecoy();

        }
    }

    /**
     * Removes empty points and clears dependent metrics if needed.
     */
    public synchronized void cleanUp() {

        

        boolean removed = false;

        HashSet<Double> currentScores = new HashSet<>(hitMap.keySet());

        for (double score : currentScores) {

            TargetDecoyPoint targetDecoyPoint = hitMap.get(score);

            if (targetDecoyPoint.nTarget == 0
                    && targetDecoyPoint.nDecoy == 0) {

                hitMap.remove(score);
                removed = true;

            }
        }

        if (removed) {

            scores = null;
            nmax = null;
            windowSize = null;

        }
    }

    /**
     * Estimates the metrics of the map: Nmax, NtargetOnly, minFDR. Scores of 1
     * and above will be skipped for Nmax.
     */
    private void estimateNs() {

        

        if (scores == null) {

            estimateScores();

        }

        boolean onlyTarget = true;
        nmax = 0;
        int targetCpt = 0;
        int decoyCpt = 0;
        nTargetOnly = 0;
        int targetCount = 0, decoyCount = 0;

        for (double score : scores) {

            TargetDecoyPoint point = hitMap.get(score);

            if (onlyTarget) {

                if (point.nDecoy > 0) {

                    nTargetOnly += point.nTarget / 2 + point.nTarget % 2;
                    targetCpt += point.nTarget / 2;
                    onlyTarget = false;
                    decoyCpt += point.nDecoy;

                } else {

                    nTargetOnly += point.nTarget;

                }

            } else if (point.nDecoy > 0) {

                targetCpt += point.nTarget / 2 + point.nTarget % 2;
                decoyCpt += point.nDecoy;

                if (targetCpt > nmax
                        && score < 1.0
                        && decoyCpt >= minDecoysInBin) {

                    nmax = targetCpt;

                }

                targetCpt = point.nTarget / 2;
                decoyCpt = point.nDecoy;

            } else {

                targetCpt += point.nTarget;

            }

            targetCount += point.nTarget;
            decoyCount += point.nDecoy;

            if (targetCount > 0) {

                double fdr = ((double) decoyCount) / targetCount;

                if (fdr < minFDR) {

                    minFDR = fdr;

                }
            }
        }
    }

    /**
     * Estimates the posterior error probabilities in this map.
     *
     * @param waitingHandler the handler displaying feedback to the user
     */
    public void estimateProbabilities(WaitingHandler waitingHandler) {
        
        

        if (scores == null) {

            estimateScores();

        }

        if (nmax == null) {

            estimateNs();

        }
        if (windowSize == null) {

            windowSize = nmax;

        }

        // estimate p
        double currentScore = scores.get(0);
        TargetDecoyPoint tempPoint, previousPoint = hitMap.get(currentScore);
        double nLimit = 0.5 * windowSize;
        double nTargetUp = 1.5 * previousPoint.nTarget;
        double nTargetDown = -0.5 * previousPoint.nTarget;
        double nDecoy = previousPoint.nDecoy;
        int iDown = 0;
        int iUp = 1;
        boolean oneReached = false;

        for (int i = 0; i < scores.size(); i++) {

            currentScore = scores.get(i);
            TargetDecoyPoint point = hitMap.get(currentScore);

            if (!oneReached) {

                double change = 0.5 * (previousPoint.nTarget + point.nTarget);
                nTargetDown += change;
                nTargetUp -= change;

                while (nTargetDown > nLimit) {

                    if (iDown < i) {

                        tempPoint = hitMap.get(scores.get(iDown));
                        double nTargetDownTemp = nTargetDown - tempPoint.nTarget;

                        if (nTargetDownTemp >= nLimit) {

                            nDecoy -= tempPoint.nDecoy;
                            nTargetDown = nTargetDownTemp;
                            iDown++;

                        } else {

                            break;

                        }

                    } else {

                        break;

                    }
                }

                while (nTargetUp < nLimit && iUp < scores.size()) {

                    tempPoint = hitMap.get(scores.get(iUp));
                    nTargetUp += tempPoint.nTarget;
                    nDecoy += tempPoint.nDecoy;
                    iUp++;

                }

                double nTarget = nTargetDown + nTargetUp;
                point.p = Math.max(Math.min(nDecoy / nTarget, 1), 0);

                if (point.p >= 0.98) {

                    oneReached = true;

                }

            } else {

                point.p = 1;

            }

            previousPoint = point;

            waitingHandler.increaseSecondaryProgressCounter();

            if (waitingHandler.isRunCanceled()) {

                return;

            }
        }
    }

    /**
     * Returns the Nmax metric.
     *
     * @return the Nmax metric
     */
    public int getnMax() {

        

        if (nmax == null) {

            estimateNs();

        }

        return nmax;

    }

    /**
     * Returns the minimal FDR which can be achieved in this dataset.
     *
     * @return the minimal FDR which can be achieved in this dataset
     */
    public double getMinFdr() {

        

        return minFDR;

    }

    /**
     * Returns the minimal detectable PEP variation in percent.
     *
     * @return the minimal detectable PEP variation in percent
     */
    public double getResolution() {

        double pmin = 0;
        int nMax = getnMax();

        if (nMax != 0) {

            pmin = 100.0 / nMax;

        }

        return pmin;

    }

    /**
     * Returns the number of target hits before the first decoy hit.
     *
     * @return the number of target hits before the first decoy hit
     */
    public Integer getnTargetOnly() {

        

        return nTargetOnly;

    }

    /**
     * Sorts the scores implemented in this map.
     */
    private void estimateScores() {

        

        scores = new ArrayList<>(hitMap.keySet());
        Collections.sort(scores);

    }

    /**
     * Returns the sorted scores implemented in this map.
     *
     * @return the sorted scores implemented in this map.
     */
    public ArrayList<Double> getScores() {

        

        if (scores == null) {

            estimateScores();

        }

        return scores;

    }

    /**
     * Adds all the points from another target/decoy map.
     *
     * @param anOtherMap another target/decoy map
     */
    public void addAll(TargetDecoyMap anOtherMap) {

        

        for (double score : anOtherMap.getScores()) {

            for (int i = 0; i < anOtherMap.getNDecoy(score); i++) {

                put(score, true);

            }

            for (int i = 0; i < anOtherMap.getNTarget(score); i++) {

                put(score, false);

            }
        }

        scores = null;
        nmax = null;
        windowSize = null;

    }

    /**
     * Returns a boolean indicating if a suspicious input was detected.
     *
     * @param initialFDR the minimal FDR requested for a group
     *
     * @return a boolean indicating if a suspicious input was detected
     */
    public boolean suspiciousInput(double initialFDR) {

        

        if (nmax == null) {

            estimateNs();

        }

        if (nmax < 100 || minFDR > initialFDR) {

            return true;

        }

        return false;

    }

    /**
     * Returns the current target decoy results.
     *
     * @return the current target decoy results
     */
    public TargetDecoyResults getTargetDecoyResults() {

        

        return targetDecoyResults;

    }

    /**
     * Returns the target decoy series.
     *
     * @return the target decoy series
     */
    public TargetDecoySeries getTargetDecoySeries() {

        

        return new TargetDecoySeries(hitMap);

    }

    /**
     * Returns the window size used for pep estimation.
     *
     * @return the window size used for pep estimation
     */
    public int getWindowSize() {

        

        if (windowSize == null) {

            windowSize = getnMax();

        }

        return windowSize;

    }

    /**
     * Sets the window size used for pep estimation.
     *
     * @param windowSize the window size used for pep estimation
     */
    public void setWindowSize(int windowSize) {

        

        this.windowSize = windowSize;

    }

    /**
     * Returns the size of the map.
     *
     * @return the size of the map
     */
    public int getMapSize() {

        

        return hitMap.size();

    }
}
