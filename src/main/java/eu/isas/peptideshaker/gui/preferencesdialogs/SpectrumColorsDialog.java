package eu.isas.peptideshaker.gui.preferencesdialogs;

import com.compomics.util.experiment.biology.Ion;
import com.compomics.util.experiment.biology.NeutralLoss;
import com.compomics.util.gui.spectrum.IonLabelColorTableModel;
import com.compomics.util.gui.spectrum.SpectrumPanel;
import eu.isas.peptideshaker.gui.HelpDialog;
import eu.isas.peptideshaker.gui.PeptideShakerGUI;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JColorChooser;
import no.uib.jsparklines.renderers.JSparklinesColorTableCellRenderer;

/**
 * Dialog for user selection of spectrum annotation colors.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class SpectrumColorsDialog extends javax.swing.JDialog {

    /**
     * The list of ion types.
     */
    private HashMap<Ion.IonType, ArrayList<Integer>> iontypes;
    /**
     * The list of neutral losses.
     */
    private ArrayList<NeutralLoss> neutralLosses;
    /**
     * A reference to PeptideShakerGUI.
     */
    private PeptideShakerGUI peptideShakerGUI;

    /**
     * Creates a new SpectrumColorsDialog.
     *
     * @param peptideShakerGUI
     */
    public SpectrumColorsDialog(PeptideShakerGUI peptideShakerGUI) {
        super(peptideShakerGUI, true);
        this.peptideShakerGUI = peptideShakerGUI;
        iontypes = peptideShakerGUI.getAnnotationPreferences().getIonTypes();
        neutralLosses = peptideShakerGUI.getNeutralLosses();
        initComponents();
        setUpGui();
        setLocationRelativeTo(peptideShakerGUI);
        setVisible(true);
    }

    /**
     * Set up the GUI.
     */
    private void setUpGui() {

        annotatedPeakColorPanel.setBackground(peptideShakerGUI.getUtilitiesUserPreferences().getSpectrumAnnotatedPeakColor());
        backgroundPeakColorPanel.setBackground(peptideShakerGUI.getUtilitiesUserPreferences().getSpectrumBackgroundPeakColor());

        annotatedPeakWidthSpinner.setValue(peptideShakerGUI.getUtilitiesUserPreferences().getSpectrumAnnotatedPeakWidth());
        backgroungPeakWidthSpinner.setValue(peptideShakerGUI.getUtilitiesUserPreferences().getSpectrumBackgroundPeakWidth());

        colorsTable.getTableHeader().setReorderingAllowed(false);

        // make sure that the scroll panes are see-through
        colorsScrollPane.getViewport().setOpaque(false);

        colorsTable.getColumn(" ").setMaxWidth(50);
        colorsTable.getColumn(" ").setMinWidth(50);
        colorsTable.getColumn("  ").setMaxWidth(40);
        colorsTable.getColumn("  ").setMinWidth(40);

        // set the cell renderers
        colorsTable.getColumn("  ").setCellRenderer(new JSparklinesColorTableCellRenderer());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backgroundPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        annotationColorsPanel = new javax.swing.JPanel();
        colorsScrollPane = new javax.swing.JScrollPane();
        colorsTable = new javax.swing.JTable();
        peakColorsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        annotatedPeakColorPanel = new javax.swing.JPanel();
        backgroundPeakColorPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        annotatedPeakWidthSpinner = new javax.swing.JSpinner();
        backgroungPeakWidthSpinner = new javax.swing.JSpinner();
        helpJButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Spectrum Colors");
        setMinimumSize(new java.awt.Dimension(520, 500));

        backgroundPanel.setBackground(new java.awt.Color(230, 230, 230));

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        annotationColorsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Annotation Colors"));
        annotationColorsPanel.setOpaque(false);

        colorsScrollPane.setOpaque(false);

        colorsTable.setModel(new IonLabelColorTableModel(iontypes, neutralLosses));
        colorsTable.setOpaque(false);
        colorsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorsTableMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                colorsTableMouseReleased(evt);
            }
        });
        colorsTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                colorsTableMouseMoved(evt);
            }
        });
        colorsScrollPane.setViewportView(colorsTable);

        javax.swing.GroupLayout annotationColorsPanelLayout = new javax.swing.GroupLayout(annotationColorsPanel);
        annotationColorsPanel.setLayout(annotationColorsPanelLayout);
        annotationColorsPanelLayout.setHorizontalGroup(
            annotationColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(annotationColorsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(colorsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        annotationColorsPanelLayout.setVerticalGroup(
            annotationColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(annotationColorsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(colorsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                .addContainerGap())
        );

        peakColorsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Peak Colors"));
        peakColorsPanel.setOpaque(false);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Annotated Peak Color:");

        jLabel2.setText("Backgrond Peak Color:");

        annotatedPeakColorPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        annotatedPeakColorPanel.setForeground(new java.awt.Color(255, 255, 255));
        annotatedPeakColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                annotatedPeakColorPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                annotatedPeakColorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                annotatedPeakColorPanelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout annotatedPeakColorPanelLayout = new javax.swing.GroupLayout(annotatedPeakColorPanel);
        annotatedPeakColorPanel.setLayout(annotatedPeakColorPanelLayout);
        annotatedPeakColorPanelLayout.setHorizontalGroup(
            annotatedPeakColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 27, Short.MAX_VALUE)
        );
        annotatedPeakColorPanelLayout.setVerticalGroup(
            annotatedPeakColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        backgroundPeakColorPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        backgroundPeakColorPanel.setForeground(new java.awt.Color(255, 255, 255));
        backgroundPeakColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backgroundPeakColorPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backgroundPeakColorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backgroundPeakColorPanelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout backgroundPeakColorPanelLayout = new javax.swing.GroupLayout(backgroundPeakColorPanel);
        backgroundPeakColorPanel.setLayout(backgroundPeakColorPanelLayout);
        backgroundPeakColorPanelLayout.setHorizontalGroup(
            backgroundPeakColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 27, Short.MAX_VALUE)
        );
        backgroundPeakColorPanelLayout.setVerticalGroup(
            backgroundPeakColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jLabel3.setText("Annotated Peak Width:");

        jLabel4.setText("Background Peak Width:");

        annotatedPeakWidthSpinner.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(1.0f), null, Float.valueOf(1.0f)));
        annotatedPeakWidthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                annotatedPeakWidthSpinnerStateChanged(evt);
            }
        });

        backgroungPeakWidthSpinner.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(1.0f), null, Float.valueOf(1.0f)));
        backgroungPeakWidthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                backgroungPeakWidthSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout peakColorsPanelLayout = new javax.swing.GroupLayout(peakColorsPanel);
        peakColorsPanel.setLayout(peakColorsPanelLayout);
        peakColorsPanelLayout.setHorizontalGroup(
            peakColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(peakColorsPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(peakColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(peakColorsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(backgroundPeakColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(peakColorsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(annotatedPeakColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(50, 50, 50)
                .addGroup(peakColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(peakColorsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(annotatedPeakWidthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(peakColorsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(backgroungPeakWidthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        peakColorsPanelLayout.setVerticalGroup(
            peakColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(peakColorsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(peakColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(annotatedPeakColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(annotatedPeakWidthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(peakColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(backgroundPeakColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(backgroungPeakWidthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        peakColorsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {annotatedPeakColorPanel, annotatedPeakWidthSpinner, backgroundPeakColorPanel, backgroungPeakWidthSpinner, jLabel1, jLabel2, jLabel3, jLabel4});

        helpJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help.GIF"))); // NOI18N
        helpJButton.setToolTipText("Help");
        helpJButton.setBorder(null);
        helpJButton.setBorderPainted(false);
        helpJButton.setContentAreaFilled(false);
        helpJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                helpJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                helpJButtonMouseExited(evt);
            }
        });
        helpJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout backgroundPanelLayout = new javax.swing.GroupLayout(backgroundPanel);
        backgroundPanel.setLayout(backgroundPanelLayout);
        backgroundPanelLayout.setHorizontalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(backgroundPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(helpJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(peakColorsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(annotationColorsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        backgroundPanelLayout.setVerticalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(annotationColorsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(peakColorsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(okButton)
                    .addComponent(helpJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Close the dialog.
     *
     * @param evt
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    /**
     * Changes the cursor to a hand cursor if over the color column.
     *
     * @param evt
     */
    private void colorsTableMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorsTableMouseMoved
        int row = colorsTable.rowAtPoint(evt.getPoint());
        int column = colorsTable.columnAtPoint(evt.getPoint());

        if (row != -1) {

            if (column == colorsTable.getColumn("  ").getModelIndex()) {
                this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            } else {
                this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        }
    }//GEN-LAST:event_colorsTableMouseMoved

    /**
     * Changes the cursor back to the default cursor.
     *
     * @param evt
     */
    private void colorsTableMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorsTableMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_colorsTableMouseExited

    /**
     * Opens a file chooser where the color for the ion can be changed.
     *
     * @param evt
     */
    private void colorsTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorsTableMouseReleased
        int row = colorsTable.rowAtPoint(evt.getPoint());
        int column = colorsTable.columnAtPoint(evt.getPoint());

        if (row != -1) {

            if (column == colorsTable.getColumn("  ").getModelIndex()) {
                Color newColor = JColorChooser.showDialog(this, "Pick a Color", (Color) colorsTable.getValueAt(row, column));

                if (newColor != null) {

                    int[] selectedRows = colorsTable.getSelectedRows();

                    for (int i = 0; i < selectedRows.length; i++) {
                        SpectrumPanel.setIonColor(((IonLabelColorTableModel) colorsTable.getModel()).getIonAtRow(selectedRows[i]), newColor);
                    }

                    ((IonLabelColorTableModel) colorsTable.getModel()).fireTableDataChanged();
                    peptideShakerGUI.updateSpectrumAnnotations();
                }
            }
        }
    }//GEN-LAST:event_colorsTableMouseReleased

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void annotatedPeakColorPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_annotatedPeakColorPanelMouseEntered
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_annotatedPeakColorPanelMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void annotatedPeakColorPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_annotatedPeakColorPanelMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_annotatedPeakColorPanelMouseExited

    /**
     * Update the color used for the annotated peaks.
     *
     * @param evt
     */
    private void annotatedPeakColorPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_annotatedPeakColorPanelMouseClicked
        Color newColor = JColorChooser.showDialog(this, "Pick a Color", annotatedPeakColorPanel.getBackground());

        if (newColor != null) {
            annotatedPeakColorPanel.setBackground(newColor);
            peptideShakerGUI.getUtilitiesUserPreferences().setSpectrumAnnotatedPeakColor(annotatedPeakColorPanel.getBackground());
            peptideShakerGUI.updateSpectrumAnnotations();
            annotatedPeakColorPanel.repaint();
        }
    }//GEN-LAST:event_annotatedPeakColorPanelMouseClicked

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void backgroundPeakColorPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backgroundPeakColorPanelMouseEntered
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_backgroundPeakColorPanelMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void backgroundPeakColorPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backgroundPeakColorPanelMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_backgroundPeakColorPanelMouseExited

    /**
     * Update the color used for the background peaks.
     *
     * @param evt
     */
    private void backgroundPeakColorPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backgroundPeakColorPanelMouseClicked
        Color newColor = JColorChooser.showDialog(this, "Pick a Color", backgroundPeakColorPanel.getBackground());

        if (newColor != null) {
            newColor = new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), 50); // add the default alpha level
            backgroundPeakColorPanel.setBackground(newColor);
            peptideShakerGUI.getUtilitiesUserPreferences().setSpectrumBackgroundPeakColor(backgroundPeakColorPanel.getBackground());
            peptideShakerGUI.updateSpectrumAnnotations();
            backgroundPeakColorPanel.repaint();
        }
    }//GEN-LAST:event_backgroundPeakColorPanelMouseClicked

    /**
     * Update the annotated peak width.
     *
     * @param evt
     */
    private void annotatedPeakWidthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_annotatedPeakWidthSpinnerStateChanged
        peptideShakerGUI.getUtilitiesUserPreferences().setSpectrumAnnotatedPeakWidth((Float) annotatedPeakWidthSpinner.getValue());
        peptideShakerGUI.updateSpectrumAnnotations();
    }//GEN-LAST:event_annotatedPeakWidthSpinnerStateChanged

    /**
     * Update the background peak width.
     *
     * @param evt
     */
    private void backgroungPeakWidthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_backgroungPeakWidthSpinnerStateChanged
        peptideShakerGUI.getUtilitiesUserPreferences().setSpectrumBackgroundPeakWidth((Float) backgroungPeakWidthSpinner.getValue());
        peptideShakerGUI.updateSpectrumAnnotations();
    }//GEN-LAST:event_backgroungPeakWidthSpinnerStateChanged

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void helpJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helpJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_helpJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void helpJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helpJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_helpJButtonMouseExited

    /**
     * Open the help dialog.
     *
     * @param evt
     */
    private void helpJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpJButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpDialog(peptideShakerGUI, getClass().getResource("/helpFiles/SpectrumColorsDialog.html"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_helpJButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel annotatedPeakColorPanel;
    private javax.swing.JSpinner annotatedPeakWidthSpinner;
    private javax.swing.JPanel annotationColorsPanel;
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JPanel backgroundPeakColorPanel;
    private javax.swing.JSpinner backgroungPeakWidthSpinner;
    private javax.swing.JScrollPane colorsScrollPane;
    private javax.swing.JTable colorsTable;
    private javax.swing.JButton helpJButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel peakColorsPanel;
    // End of variables declaration//GEN-END:variables
}
