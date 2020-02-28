package eu.isas.peptideshaker.gui.export;

import com.compomics.util.gui.file_handling.FileAndFileFilter;
import com.compomics.util.gui.export.report.ReportEditor;
import com.compomics.util.gui.error_handlers.HelpDialog;
import com.compomics.util.gui.file_handling.FileChooserUtil;
import com.compomics.util.gui.waiting.waitinghandlers.ProgressDialogX;
import com.compomics.util.io.export.ExportFormat;
import eu.isas.peptideshaker.export.PSExportFactory;
import com.compomics.util.io.export.ExportScheme;
import com.google.common.collect.Lists;
import eu.isas.peptideshaker.gui.PeptideShakerGUI;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Dialog for exporting identification features.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class FeaturesExportDialog extends javax.swing.JDialog {

    /**
     * PeptideShaker main GUI.
     */
    private final PeptideShakerGUI peptideShakerGUI;
    /**
     * A simple progress dialog.
     */
    private static ProgressDialogX progressDialog;
    /**
     * The export factory.
     */
    private final PSExportFactory exportFactory = PSExportFactory.getInstance();
    /**
     * List of the available export schemes.
     */
    private ArrayList<String> exportSchemesNames;

    /**
     * Creates a new ExportPreferencesDialog.
     *
     * @param peptideShakerGUI the PeptideShaker GUI parent
     */
    public FeaturesExportDialog(PeptideShakerGUI peptideShakerGUI) {
        super(peptideShakerGUI, true);
        this.peptideShakerGUI = peptideShakerGUI;
        initComponents();
        setUpGUI();
        this.pack();
        this.setLocationRelativeTo(peptideShakerGUI);
        updateReportsList();
        setVisible(true);
    }

    /**
     * Set up the GUI.
     */
    private void setUpGUI() {
        reportsTable.getTableHeader().setReorderingAllowed(false);

        reportsTable.getColumn(" ").setMaxWidth(30);
        reportsTable.getColumn(" ").setMinWidth(30);

        // make sure that the scroll panes are see-through
        reportsTableScrollPane.getViewport().setOpaque(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        reportDocumentationPopupMenu = new javax.swing.JPopupMenu();
        addReportMenuItem = new javax.swing.JMenuItem();
        removeReportMenuItem = new javax.swing.JMenuItem();
        editReportMenuItem = new javax.swing.JMenuItem();
        reportPopUpMenuSeparator = new javax.swing.JPopupMenu.Separator();
        reportDocumentationMenuItem = new javax.swing.JMenuItem();
        backgroundPanel = new javax.swing.JPanel();
        featuresPanel = new javax.swing.JPanel();
        customReportsPanel = new javax.swing.JPanel();
        reportsTableScrollPane = new javax.swing.JScrollPane();
        reportsTable = new javax.swing.JTable();
        exportReportButton = new javax.swing.JButton();
        helpLabel = new javax.swing.JLabel();
        addReportLabel = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        helpJButton = new javax.swing.JButton();

        addReportMenuItem.setText("Add");
        addReportMenuItem.setToolTipText("Add a new report type");
        addReportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addReportMenuItemActionPerformed(evt);
            }
        });
        reportDocumentationPopupMenu.add(addReportMenuItem);

        removeReportMenuItem.setText("Remove");
        removeReportMenuItem.setToolTipText("Remove the report type");
        removeReportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeReportMenuItemActionPerformed(evt);
            }
        });
        reportDocumentationPopupMenu.add(removeReportMenuItem);

        editReportMenuItem.setText("Edit");
        editReportMenuItem.setToolTipText("Edit the report");
        editReportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editReportMenuItemActionPerformed(evt);
            }
        });
        reportDocumentationPopupMenu.add(editReportMenuItem);
        reportDocumentationPopupMenu.add(reportPopUpMenuSeparator);

        reportDocumentationMenuItem.setText("Documentation");
        reportDocumentationMenuItem.setToolTipText("Export the report documentation to file");
        reportDocumentationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportDocumentationMenuItemActionPerformed(evt);
            }
        });
        reportDocumentationPopupMenu.add(reportDocumentationMenuItem);

        setTitle("Export Features");

        backgroundPanel.setBackground(new java.awt.Color(230, 230, 230));

        featuresPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Report"));
        featuresPanel.setOpaque(false);

        customReportsPanel.setBackground(new java.awt.Color(230, 230, 230));

        reportsTable.setModel(new ReportsTableModel());
        reportsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        reportsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reportsTableMouseClicked(evt);
            }
        });
        reportsTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                reportsTableKeyReleased(evt);
            }
        });
        reportsTableScrollPane.setViewportView(reportsTable);

        exportReportButton.setText("Export");
        exportReportButton.setEnabled(false);
        exportReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportReportButtonActionPerformed(evt);
            }
        });

        helpLabel.setFont(helpLabel.getFont().deriveFont((helpLabel.getFont().getStyle() | java.awt.Font.ITALIC)));
        helpLabel.setText("Right click on a row in the table for additional options.");

        addReportLabel.setText("<html> <a href>Add new report type.</a> </html>");
        addReportLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addReportLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addReportLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addReportLabelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout customReportsPanelLayout = new javax.swing.GroupLayout(customReportsPanel);
        customReportsPanel.setLayout(customReportsPanelLayout);
        customReportsPanelLayout.setHorizontalGroup(
            customReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customReportsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(customReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reportsTableScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customReportsPanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(addReportLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(helpLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 199, Short.MAX_VALUE)
                        .addComponent(exportReportButton)))
                .addContainerGap())
        );
        customReportsPanelLayout.setVerticalGroup(
            customReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customReportsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(reportsTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(customReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exportReportButton)
                    .addComponent(helpLabel)
                    .addComponent(addReportLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout featuresPanelLayout = new javax.swing.GroupLayout(featuresPanel);
        featuresPanel.setLayout(featuresPanelLayout);
        featuresPanelLayout.setHorizontalGroup(
            featuresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(featuresPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(customReportsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        featuresPanelLayout.setVerticalGroup(
            featuresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(featuresPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(customReportsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

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
                .addGap(20, 20, 20)
                .addComponent(helpJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(exitButton)
                .addContainerGap())
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(featuresPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        backgroundPanelLayout.setVerticalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(featuresPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(helpJButton)
                    .addComponent(exitButton))
                .addContainerGap())
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
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Open the help dialog.
     *
     * @param evt
     */
    private void helpJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpJButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpDialog(peptideShakerGUI, getClass().getResource("/helpFiles/FeatureExport.html"),
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/help.GIF")),
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
                "Export Features - Help");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_helpJButtonActionPerformed

    /**
     * Change the cursor back to a hand icon.
     *
     * @param evt
     */
    private void helpJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helpJButtonMouseEntered
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_helpJButtonMouseEntered

    /**
     * Change the cursor back to the default icon.
     *
     * @param evt
     */
    private void helpJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helpJButtonMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_helpJButtonMouseExited

    /**
     * Close the dialog.
     *
     * @param evt
     */
    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        try {
            exportFactory.saveFactory(exportFactory);
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }
        setVisible(false);
        dispose();
    }//GEN-LAST:event_exitButtonActionPerformed

    /**
     * Export the selected report to file.
     *
     * @param evt
     */
    private void exportReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportReportButtonActionPerformed
        writeSelectedReport();
    }//GEN-LAST:event_exportReportButtonActionPerformed

    /**
     * Export the given report or show its details.
     *
     * @param evt
     */
    private void reportsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportsTableMouseClicked

        if (evt != null && reportsTable.rowAtPoint(evt.getPoint()) != -1) {
            reportsTable.setRowSelectionInterval(reportsTable.rowAtPoint(evt.getPoint()), reportsTable.rowAtPoint(evt.getPoint()));
        }

        if (evt != null && evt.getButton() == MouseEvent.BUTTON3 && reportsTable.getSelectedRow() != -1) {
            String schemeName = (String) reportsTable.getValueAt(reportsTable.getSelectedRow(), 1);
            ExportScheme exportScheme = exportFactory.getExportScheme(schemeName);
            editReportMenuItem.setVisible(exportScheme.isEditable());
            removeReportMenuItem.setVisible(exportScheme.isEditable());
            reportDocumentationPopupMenu.show(reportsTable, evt.getX(), evt.getY());
        }

        if (evt != null && evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
            writeSelectedReport();
        }

        exportReportButton.setEnabled(reportsTable.getSelectedRow() != -1);
    }//GEN-LAST:event_reportsTableMouseClicked

    /**
     * Enable/disable the export and delete buttons.
     *
     * @param evt
     */
    private void reportsTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_reportsTableKeyReleased
        reportsTableMouseClicked(null);
    }//GEN-LAST:event_reportsTableKeyReleased

    /**
     * Export the report documentation to file.
     *
     * @param evt
     */
    private void reportDocumentationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportDocumentationMenuItemActionPerformed
        writeDocumentationOfSelectedReport();
    }//GEN-LAST:event_reportDocumentationMenuItemActionPerformed

    /**
     * Edit the selected report.
     *
     * @param evt
     */
    private void editReportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editReportMenuItemActionPerformed
        String reportName = (String) reportsTable.getValueAt(reportsTable.getSelectedRow(), 1);
        new ReportEditor(peptideShakerGUI, exportFactory, reportName, true);
        int selectedRow = reportsTable.getSelectedRow();
        updateReportsList();
        ((DefaultTableModel) reportsTable.getModel()).fireTableDataChanged();
        if (selectedRow != -1) {
            reportsTable.setRowSelectionInterval(selectedRow, selectedRow);
        }
        reportsTableMouseClicked(null);
    }//GEN-LAST:event_editReportMenuItemActionPerformed

    /**
     * Add a new report type.
     *
     * @param evt
     */
    private void addReportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addReportMenuItemActionPerformed
        new ReportEditor(peptideShakerGUI, exportFactory);
        int selectedRow = reportsTable.getSelectedRow();
        updateReportsList();
        ((DefaultTableModel) reportsTable.getModel()).fireTableDataChanged();
        if (selectedRow != -1) {
            reportsTable.setRowSelectionInterval(selectedRow, selectedRow);
        }
        reportsTableMouseClicked(null);
    }//GEN-LAST:event_addReportMenuItemActionPerformed

    /**
     * Delete the currently selected report.
     *
     * @param evt
     */
    private void removeReportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeReportMenuItemActionPerformed
        String reportName = (String) reportsTable.getValueAt(reportsTable.getSelectedRow(), 1);
        exportFactory.removeExportScheme(reportName);
        updateReportsList();
        ((DefaultTableModel) reportsTable.getModel()).fireTableDataChanged();
        reportsTableMouseClicked(null);
    }//GEN-LAST:event_removeReportMenuItemActionPerformed

    /**
     * Change the cursor back to a hand icon.
     *
     * @param evt
     */
    private void addReportLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addReportLabelMouseEntered
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_addReportLabelMouseEntered

    /**
     * Change the cursor back to the default icon.
     *
     * @param evt
     */
    private void addReportLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addReportLabelMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_addReportLabelMouseExited

    /**
     * Add a new report type.
     *
     * @param evt
     */
    private void addReportLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addReportLabelMouseClicked
        addReportMenuItemActionPerformed(null);
    }//GEN-LAST:event_addReportLabelMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addReportLabel;
    private javax.swing.JMenuItem addReportMenuItem;
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JPanel customReportsPanel;
    private javax.swing.JMenuItem editReportMenuItem;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton exportReportButton;
    private javax.swing.JPanel featuresPanel;
    private javax.swing.JButton helpJButton;
    private javax.swing.JLabel helpLabel;
    private javax.swing.JMenuItem removeReportMenuItem;
    private javax.swing.JMenuItem reportDocumentationMenuItem;
    private javax.swing.JPopupMenu reportDocumentationPopupMenu;
    private javax.swing.JPopupMenu.Separator reportPopUpMenuSeparator;
    private javax.swing.JTable reportsTable;
    private javax.swing.JScrollPane reportsTableScrollPane;
    // End of variables declaration//GEN-END:variables

    /**
     * Updates the reports list based on the information stored in the export
     * factory.
     */
    private void updateReportsList() {
        
        exportSchemesNames = Lists.newArrayList(PSExportFactory.getDefaultExportSchemesNames());
        exportSchemesNames.addAll(exportFactory.getUserSchemesNames());
    
    }

    /**
     * Writes the selected report into a file.
     */
    private void writeSelectedReport() {

        final String schemeName = (String) reportsTable.getValueAt(reportsTable.getSelectedRow(), 1);
        String lastSelectedFolderPath = peptideShakerGUI.getLastSelectedFolder().getLastSelectedFolder();
        
        String[] extensionsOptions = new String[]{".xls", ".txt", ".gz"};
        
        String textFileFilterDescription = "Tab separated text file (.txt)";
        String gzipFileFilterDescription = "Gzipped tab separated text file (.gz)";
        String excelFileFilterDescription = "Excel Workbook (.xls)";
        String[] fileFiltersDescriptionsOptions = new String[]{excelFileFilterDescription, textFileFilterDescription, gzipFileFilterDescription};
        
        FileAndFileFilter selectedFileAndFilter = FileChooserUtil.getUserSelectedFile(
                this, 
                extensionsOptions,
                fileFiltersDescriptionsOptions, 
                "Export Report", 
                lastSelectedFolderPath, 
                schemeName, 
                false, 
                true, 
                false, 
                1
        );

        if (selectedFileAndFilter != null) {

            final File selectedFile = selectedFileAndFilter.getFile();
            final ExportFormat exportFormat;
            final boolean gzip;

            if (selectedFileAndFilter.getFileFilter().getDescription().equalsIgnoreCase(textFileFilterDescription)) {

                exportFormat = ExportFormat.text;
                gzip = false;

            } else if (selectedFileAndFilter.getFileFilter().getDescription().equalsIgnoreCase(gzipFileFilterDescription)) {

                exportFormat = ExportFormat.text;
                gzip = true;

            } else {

                exportFormat = ExportFormat.excel;
                gzip = false;

            }

            progressDialog = new ProgressDialogX(
                    this, 
                    peptideShakerGUI,
                    Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
                    Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")),
                    true
            );
            progressDialog.setTitle("Exporting Report. Please Wait...");

            final String filePath = selectedFile.getPath();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        progressDialog.setVisible(true);
                    } catch (IndexOutOfBoundsException e) {
                        // ignore
                    }
                }
            }, "ProgressDialog").start();

            new Thread("ExportThread") {
                @Override
                public void run() {

                    try {
                        ExportScheme exportScheme = exportFactory.getExportScheme(schemeName);
                        progressDialog.setTitle("Exporting. Please Wait...");
                        PSExportFactory.writeExport(
                                exportScheme, 
                                selectedFile, 
                                exportFormat, 
                                gzip, 
                                peptideShakerGUI.getProjectParameters().getProjectUniqueName(),
                                peptideShakerGUI.getProjectDetails(), 
                                peptideShakerGUI.getIdentification(),
                                peptideShakerGUI.getIdentificationFeaturesGenerator(), 
                                peptideShakerGUI.getGeneMaps(), 
                                null, 
                                null, 
                                null,
                                peptideShakerGUI.getDisplayParameters().getnAASurroundingPeptides(), 
                                peptideShakerGUI.getIdentificationParameters(),
                                peptideShakerGUI.getSequenceProvider(), 
                                peptideShakerGUI.getProteinDetailsProvider(), 
                                peptideShakerGUI.getSpectrumProvider(), 
                                peptideShakerGUI.getSpectrumCountingParameters(), 
                                progressDialog
                        );

                        boolean processCancelled = progressDialog.isRunCanceled();
                        progressDialog.setRunFinished();

                        if (!processCancelled) {
                            JOptionPane.showMessageDialog(peptideShakerGUI, "Data copied to file:\n" + filePath, "Data Exported.", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (IllegalArgumentException e) {
                        if (e.getMessage().contains("Invalid row number (65536)")) {
                            progressDialog.setRunFinished();
                            JOptionPane.showMessageDialog(peptideShakerGUI,
                                    "An error occurred while generating the output. This format can contain only 65,535 lines.\n" // @TODO: update the excel export library?
                                    + "Please use a text export instead.", "Output Error.", JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        } else {
                            progressDialog.setRunFinished();
                            JOptionPane.showMessageDialog(peptideShakerGUI, "An error occurred while generating the output.", "Output Error.", JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        progressDialog.setRunFinished();
                        JOptionPane.showMessageDialog(peptideShakerGUI, "An error occurred while generating the output.", "Output Error.", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    /**
     * Writes the documentation related to the selected report into a file.
     */
    private void writeDocumentationOfSelectedReport() {

        final String schemeName = (String) reportsTable.getValueAt(reportsTable.getSelectedRow(), 1);
        String textFileFilterDescription = "Tab separated text file (.txt)";
        String excelFileFilterDescription = "Excel Workbook (.xls)";
        String lastSelectedFolderPath = peptideShakerGUI.getLastSelectedFolder().getLastSelectedFolder();
        FileAndFileFilter selectedFileAndFilter = FileChooserUtil.getUserSelectedFile(this, new String[]{".txt", ".xls"},
                new String[]{textFileFilterDescription, excelFileFilterDescription}, "Export Report", lastSelectedFolderPath, schemeName + "_documentation", false, true, false, 0);

        if (selectedFileAndFilter != null) {

            final File selectedFile = selectedFileAndFilter.getFile();
            final ExportFormat exportFormat;
            if (selectedFileAndFilter.getFileFilter().getDescription().equalsIgnoreCase(textFileFilterDescription)) {
                exportFormat = ExportFormat.text;
            } else {
                exportFormat = ExportFormat.excel;
            }

            progressDialog = new ProgressDialogX(this, peptideShakerGUI,
                    Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
                    Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")),
                    true);

            new Thread(new Runnable() {
                public void run() {
                    try {
                        progressDialog.setVisible(true);
                    } catch (IndexOutOfBoundsException e) {
                        // ignore
                    }
                }
            }, "ProgressDialog").start();

            new Thread("ExportThread") {
                @Override
                public void run() {
                    boolean error = false;
                    try {
                        ExportScheme exportScheme = exportFactory.getExportScheme(schemeName);
                        PSExportFactory.writeDocumentation(exportScheme, exportFormat, selectedFile);
                    } catch (Exception e) {
                        error = true;
                        peptideShakerGUI.catchException(e);
                    }
                    progressDialog.setRunFinished();

                    if (!error) {
                        JOptionPane.showMessageDialog(peptideShakerGUI, "Documentation saved to \'" + selectedFile.getAbsolutePath() + "\'.",
                                "Documentation Saved", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }.start();
        }
    }

    /**
     * Table model for the reports table.
     */
    private class ReportsTableModel extends DefaultTableModel {

        public ReportsTableModel() {
        }

        @Override
        public int getRowCount() {
            if (exportSchemesNames == null) {
                return 0;
            }
            return exportSchemesNames.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return " ";
                case 1:
                    return "Name";
                default:
                    return "";
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            switch (column) {
                case 0:
                    return row + 1;
                case 1:
                    return exportSchemesNames.get(row);
                default:
                    return "";
            }
        }

        @Override
        public Class getColumnClass(int columnIndex) {
            for (int i = 0; i < getRowCount(); i++) {
                if (getValueAt(i, columnIndex) != null) {
                    return getValueAt(i, columnIndex).getClass();
                }
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
