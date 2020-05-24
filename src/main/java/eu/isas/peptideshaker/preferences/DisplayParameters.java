package eu.isas.peptideshaker.preferences;

import com.compomics.util.db.object.DbObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * This class contains the display preferences for the current project.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class DisplayParameters extends DbObject {

    /**
     * Show/hide the hidden proteins.
     */
    private boolean showHiddenProteins = true;
    /**
     * Show/hide the hidden scores.
     */
    private boolean showScores = false;
    /**
     * If true, only the validated proteins are shown.
     */
    private boolean showValidatedProteinsOnly = false;
    /**
     * The number of amino acids surrounding a peptide.
     */
    private int nAASurroundingPeptides = 2;
    /**
     * The displayed PTMs.
     */
    private final HashMap<String, Boolean> displayedPTMs = new HashMap<>();
    /**
     * If true, the ion table is shown as an intensity version, false displays
     * the standard Mascot version.
     */
    private boolean intensityIonTable = true;
    /**
     * If true, bars are shown in the bubble plot highlighting the ions.
     */
    private boolean showBars = false;
    /**
     * The text displayed in the cell of a table in case the data is not loaded.
     */
    public static final String LOADING_MESSAGE = "Loading...";

    /**
     * Constructor.
     */
    public DisplayParameters() {
    }

    /**
     * Sets whether hidden proteins should be displayed.
     *
     * @param showHiddenProteins a boolean indicating whether hidden proteins
     * should be displayed
     */
    public void showHiddenProteins(boolean showHiddenProteins) {
        writeDBMode();
        this.showHiddenProteins = showHiddenProteins;
    }

    /**
     * Returns whether hidden proteins should be displayed.
     *
     * @return true if the hidden proteins should be displayed
     */
    public boolean showHiddenProteins() {
        readDBMode();
        return showHiddenProteins;
    }

    /**
     * Sets whether scores should be displayed.
     *
     * @param showScores a boolean indicating whether scores should be displayed
     */
    public void showScores(boolean showScores) {
        writeDBMode();
        this.showScores = showScores;
    }

    /**
     * Returns whether scores should be displayed.
     *
     * @return true if the scores are to be displayed
     */
    public boolean showScores() {
        readDBMode();
        return showScores;
    }

    /**
     * Sets whether only the validated proteins should be displayed.
     *
     * @param showValidatedProteinsOnly a boolean indicating whether only the
     * validated proteins should be displayed
     */
    public void showValidatedProteinsOnly(boolean showValidatedProteinsOnly) {
        writeDBMode();
        this.showValidatedProteinsOnly = showValidatedProteinsOnly;
    }

    /**
     * Returns whether only the validated proteins should be displayed.
     *
     * @return true if only the validated proteins are to be displayed
     */
    public boolean showValidatedProteinsOnly() {
        readDBMode();
        return showValidatedProteinsOnly;
    }

    /**
     * Returns the number of amino acids surrounding a peptide sequence (1 by
     * default).
     *
     * @return the number of amino acids surrounding a peptide sequence
     */
    public int getnAASurroundingPeptides() {
        readDBMode();
        return nAASurroundingPeptides;
    }

    /**
     * Sets the number of amino acids surrounding a peptide sequence.
     *
     * @param nAASurroundingPeptides the number of amino acids surrounding a
     * peptide sequence
     */
    public void setnAASurroundingPeptides(int nAASurroundingPeptides) {
        writeDBMode();
        this.nAASurroundingPeptides = nAASurroundingPeptides;
    }

    /**
     * Sets whether a PTM shall be displayed on the sequences or not.
     *
     * @param ptmName the name of the PTM
     * @param displayed a boolean indicating whether the PTM shall be displayed
     */
    public void setDisplayedModification(String ptmName, boolean displayed) {
        writeDBMode();
        displayedPTMs.put(ptmName, displayed);
    }

    /**
     * Indicates whether a PTM shall be displayed on the interface.
     *
     * @param ptmName the name of the PTM
     * @return a boolean indicating whether the PTM shall be displayed
     */
    public boolean isDisplayedPTM(String ptmName) {

        readDBMode();
        Boolean result = displayedPTMs.get(ptmName);

        if (result == null) {
            result = false;
            setDisplayedModification(ptmName, result);
        }

        return result;
    }

    /**
     * Set the list of visible modifications.
     *
     * @param modifications the modifications to display
     */
    public void setDefaultSelection(ArrayList<String> modifications) {

        writeDBMode();

        for (String ptm : modifications) {
            setDisplayedModification(ptm, true);
        }
    }

    /**
     * Returns a list containing the names of the PTMs to display.
     *
     * @return a list containing the names of the PTMs to display
     */
    public HashSet<String> getDisplayedModifications() {

        readDBMode();

        return displayedPTMs.entrySet().stream()
                .filter(entry -> entry.getValue())
                .map(Entry::getKey)
                .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * If true, bars are shown in the bubble plot highlighting the ions.
     *
     * @return true if bars are to be shown in the bubble plot
     */
    public boolean showBars() {
        readDBMode();
        return showBars;
    }

    /**
     * Set if the bars in the bubble plot are to be shown or not.
     *
     * @param showBars if the bars in the bubble plot are to be shown
     */
    public void setShowBars(boolean showBars) {
        writeDBMode();
        this.showBars = showBars;
    }

    /**
     * If true, the ion table is shown as an intensity version, false displays
     * the standard Mascot version.
     *
     * @return if true, the ion table is shown as an intensity version, false
     * displays the standard Mascot version
     */
    public boolean useIntensityIonTable() {
        readDBMode();
        return intensityIonTable;
    }

    /**
     * Set if the intensity or m/z ion table should be shown.
     *
     * @param intensityIonTable if the intensity or m/z ion table should be
     * shown
     */
    public void setIntensityIonTable(boolean intensityIonTable) {
        writeDBMode();
        this.intensityIonTable = intensityIonTable;
    }
}
