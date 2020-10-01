package eu.isas.peptideshaker.gui;

import com.compomics.util.io.IoUtil;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import no.uib.jsparklines.data.XYDataPoint;

/**
 * A dialog where the order of the fractions can be decided.
 *
 * @author Harald Barsnes
 */
public class FractionDetailsDialog extends javax.swing.JDialog {

    /**
     * The PeptideShakerGUI parent.
     */
    private final PeptideShakerGUI peptideShakerGUI;
    /**
     * The table column header tooltips.
     */
    private ArrayList<String> tableToolTips;

    /**
     * Creates a new FractionDetailsDialog.
     *
     * @param peptideShakerGUI the PeptideShakerGUI parent
     * @param modal if the dialog is modal or not
     */
    public FractionDetailsDialog(
            PeptideShakerGUI peptideShakerGUI,
            boolean modal
    ) {

        super(peptideShakerGUI, modal);
        initComponents();

        this.peptideShakerGUI = peptideShakerGUI;

        // set table properties
        setUpTable();

        // add the data to the table
        addData();

        setLocationRelativeTo(peptideShakerGUI);
        setVisible(true);
    }

    /**
     * Add the data to the table.
     */
    private void addData() {

        ArrayList<String> fractions = peptideShakerGUI.getIdentification().getFractions();

        HashMap<String, XYDataPoint> expectedMolecularWeightRanges = peptideShakerGUI.getIdentificationParameters().getFractionParameters().getFractionMolecularWeightRanges();
        int lineNumber = 1;

        for (String fraction : fractions) {

            Double lower = 0.0;
            Double upper = 0.0;

            if (expectedMolecularWeightRanges != null) {

                XYDataPoint dataPoint = expectedMolecularWeightRanges.get(fraction);

                if (dataPoint != null) {

                    lower = dataPoint.getX();
                    upper = dataPoint.getY();

                }
            }

            ((DefaultTableModel) fractionTable.getModel()).addRow(
                    new Object[]{
                        lineNumber++,
                        IoUtil.getFileName(fraction),
                        lower,
                        upper
                    }
            );

        }
    }

    /**
     * Set up the table.
     */
    private void setUpTable() {

        fractionTable.getTableHeader().setReorderingAllowed(false);

        // correct the color for the upper right corner
        JPanel proteinCorner = new JPanel();
        proteinCorner.setBackground(fractionTable.getTableHeader().getBackground());
        fractionJScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, proteinCorner);

        //fractionTable.setAutoCreateRowSorter(true);
        // make sure that the scroll panes are see-through
        fractionJScrollPane.getViewport().setOpaque(false);

        tableToolTips = new ArrayList<>();
        tableToolTips.add(null);
        tableToolTips.add("Fraction File");
        tableToolTips.add("Expected Lower Molecular Weight For Fraction (kDa)");
        tableToolTips.add("Expected Upper Molecular Weight For Fraction (kDa)");

        // the index column
        fractionTable.getColumn(" ").setMaxWidth(50);
        fractionTable.getColumn(" ").setMinWidth(50);

        fractionTable.getColumn("Lower Range (kDa)").setMaxWidth(120);
        fractionTable.getColumn("Lower Range (kDa)").setMinWidth(120);
        fractionTable.getColumn("Upper Range (kDa)").setMaxWidth(120);
        fractionTable.getColumn("Upper Range (kDa)").setMinWidth(120);

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
        fractionsPanel = new javax.swing.JPanel();
        fractionJScrollPane = new javax.swing.JScrollPane();
        fractionTable = new JTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        String tip = (String) tableToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        orderSettingsPanel = new javax.swing.JPanel();
        moveUpButton = new javax.swing.JButton();
        moveTopButton = new javax.swing.JButton();
        moveDownButton = new javax.swing.JButton();
        moveBottomButton = new javax.swing.JButton();
        importFractionRangesButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        helpLabel = new javax.swing.JLabel();
        disclamierPanel = new javax.swing.JPanel();
        disclaimerLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fraction Details");

        backgroundPanel.setBackground(new java.awt.Color(230, 230, 230));

        fractionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Fractions"));
        fractionsPanel.setOpaque(false);

        fractionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " ", "Fraction", "Lower Range (kDa)", "Upper Range (kDa)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        fractionTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        fractionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fractionTableMouseReleased(evt);
            }
        });
        fractionTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fractionTableKeyReleased(evt);
            }
        });
        fractionJScrollPane.setViewportView(fractionTable);

        orderSettingsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        orderSettingsPanel.setOpaque(false);

        moveUpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrowUp_grey.png"))); // NOI18N
        moveUpButton.setToolTipText("Move Up");
        moveUpButton.setBorderPainted(false);
        moveUpButton.setContentAreaFilled(false);
        moveUpButton.setEnabled(false);
        moveUpButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrowUp.png"))); // NOI18N
        moveUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveUpButtonActionPerformed(evt);
            }
        });

        moveTopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrowUpTop_grey.png"))); // NOI18N
        moveTopButton.setToolTipText("Move to Top");
        moveTopButton.setBorderPainted(false);
        moveTopButton.setContentAreaFilled(false);
        moveTopButton.setEnabled(false);
        moveTopButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrowUpTop.png"))); // NOI18N
        moveTopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveTopButtonActionPerformed(evt);
            }
        });

        moveDownButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrowDown_grey.png"))); // NOI18N
        moveDownButton.setToolTipText("Move Down");
        moveDownButton.setBorderPainted(false);
        moveDownButton.setContentAreaFilled(false);
        moveDownButton.setEnabled(false);
        moveDownButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrowDown.png"))); // NOI18N
        moveDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveDownButtonActionPerformed(evt);
            }
        });

        moveBottomButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrowDownBottom_grey.png"))); // NOI18N
        moveBottomButton.setToolTipText("Move to Bottom");
        moveBottomButton.setBorderPainted(false);
        moveBottomButton.setContentAreaFilled(false);
        moveBottomButton.setEnabled(false);
        moveBottomButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrowDownBottom.png"))); // NOI18N
        moveBottomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveBottomButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout orderSettingsPanelLayout = new javax.swing.GroupLayout(orderSettingsPanel);
        orderSettingsPanel.setLayout(orderSettingsPanelLayout);
        orderSettingsPanelLayout.setHorizontalGroup(
            orderSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, orderSettingsPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(orderSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(moveUpButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(moveDownButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(moveBottomButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(moveTopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        orderSettingsPanelLayout.setVerticalGroup(
            orderSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, orderSettingsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(moveTopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(moveUpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(moveDownButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(moveBottomButton)
                .addContainerGap())
        );

        importFractionRangesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/list2.gif"))); // NOI18N
        importFractionRangesButton.setToolTipText("Import fraction ranges from file");
        importFractionRangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importFractionRangesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fractionsPanelLayout = new javax.swing.GroupLayout(fractionsPanel);
        fractionsPanel.setLayout(fractionsPanelLayout);
        fractionsPanelLayout.setHorizontalGroup(
            fractionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fractionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fractionJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fractionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(orderSettingsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(importFractionRangesButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        fractionsPanelLayout.setVerticalGroup(
            fractionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fractionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fractionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fractionsPanelLayout.createSequentialGroup()
                        .addComponent(orderSettingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(importFractionRangesButton))
                    .addComponent(fractionJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE))
                .addContainerGap())
        );

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        helpLabel.setFont(helpLabel.getFont().deriveFont((helpLabel.getFont().getStyle() | java.awt.Font.ITALIC)));
        helpLabel.setText("Choose the order in which you want to display the fractions.");

        disclamierPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Disclaimer"));
        disclamierPanel.setOpaque(false);

        disclaimerLabel.setText("<html>\nThe fraction data is based on estimations of the confidence of a peptide/protein if found in a fraction alone in the context of the whole<br>\nanalysis. <i>These are <u><b>not</b></u> equal to the confidence in the peptide/protein identifications when processing the fractions independently!</i><br><br>\nIndependant fractions (like different donors, measurements) or replicates <u><b>should be processed separately</b></u>.<br>\n</html>");

        javax.swing.GroupLayout disclamierPanelLayout = new javax.swing.GroupLayout(disclamierPanel);
        disclamierPanel.setLayout(disclamierPanelLayout);
        disclamierPanelLayout.setHorizontalGroup(
            disclamierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(disclamierPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(disclaimerLabel)
                .addContainerGap())
        );
        disclamierPanelLayout.setVerticalGroup(
            disclamierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(disclamierPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(disclaimerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout backgroundPanelLayout = new javax.swing.GroupLayout(backgroundPanel);
        backgroundPanel.setLayout(backgroundPanelLayout);
        backgroundPanelLayout.setHorizontalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fractionsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(helpLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(disclamierPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        backgroundPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        backgroundPanelLayout.setVerticalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fractionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(disclamierPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton)
                    .addComponent(helpLabel))
                .addGap(4, 4, 4))
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
     * Close the dialog without saving the changes.
     *
     * @param evt the action event
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Move the selected row to the top.
     *
     * @param evt the action event
     */
    private void moveTopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveTopButtonActionPerformed
        int[] selectedRows = fractionTable.getSelectedRows();

        if (selectedRows.length > 0 && selectedRows[0] > 0) {
            DefaultTableModel model = ((DefaultTableModel) fractionTable.getModel());
            model.moveRow(selectedRows[0], selectedRows[selectedRows.length - 1], 0);
            fractionTable.setRowSelectionInterval(0, selectedRows.length - 1);
            resetTableIndexes();
            fractionTableMouseReleased(null);
        }
    }//GEN-LAST:event_moveTopButtonActionPerformed

    /**
     * Move the selected row one row up.
     *
     * @param evt the action event
     */
    private void moveUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveUpButtonActionPerformed
        int[] selectedRows = fractionTable.getSelectedRows();

        if (selectedRows.length > 0 && selectedRows[0] > 0) {
            DefaultTableModel model = ((DefaultTableModel) fractionTable.getModel());
            model.moveRow(selectedRows[0], selectedRows[selectedRows.length - 1], selectedRows[0] - 1);
            fractionTable.setRowSelectionInterval(selectedRows[0] - 1, selectedRows[0] - 1 + selectedRows.length - 1);
            resetTableIndexes();
            fractionTableMouseReleased(null);
        }
    }//GEN-LAST:event_moveUpButtonActionPerformed

    /**
     * Move the selected row one down.
     *
     * @param evt the action event
     */
    private void moveDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveDownButtonActionPerformed
        int[] selectedRows = fractionTable.getSelectedRows();

        if (selectedRows.length > 0 && selectedRows[selectedRows.length - 1] < fractionTable.getRowCount() - 1) {
            DefaultTableModel model = ((DefaultTableModel) fractionTable.getModel());
            model.moveRow(selectedRows[0], selectedRows[selectedRows.length - 1], selectedRows[0] + 1);
            fractionTable.setRowSelectionInterval(selectedRows[0] + 1, selectedRows[0] + selectedRows.length);
            resetTableIndexes();
            fractionTableMouseReleased(null);
        }
    }//GEN-LAST:event_moveDownButtonActionPerformed

    /**
     * Move the selected row to the bottom.
     *
     * @param evt the action event
     */
    private void moveBottomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveBottomButtonActionPerformed
        int[] selectedRows = fractionTable.getSelectedRows();

        if (selectedRows.length > 0 && selectedRows[selectedRows.length - 1] < fractionTable.getRowCount() - 1) {
            DefaultTableModel model = ((DefaultTableModel) fractionTable.getModel());
            model.moveRow(selectedRows[0], selectedRows[selectedRows.length - 1], fractionTable.getRowCount() - selectedRows.length);
            fractionTable.setRowSelectionInterval(fractionTable.getRowCount() - selectedRows.length, fractionTable.getRowCount() - 1);
            resetTableIndexes();
            fractionTableMouseReleased(null);
        }
    }//GEN-LAST:event_moveBottomButtonActionPerformed

    /**
     * Enable/disable the move options based on which row that is selected.
     *
     * @param evt the key event
     */
    private void fractionTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fractionTableKeyReleased
        fractionTableMouseReleased(null);
    }//GEN-LAST:event_fractionTableKeyReleased

    /**
     * Save the order of the fractions and close the dialog.
     *
     * @param evt the action event
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed

        ArrayList<String> spectrumFiles = new ArrayList<>();
        HashMap<String, XYDataPoint> fractionRanges = new HashMap<>();

        for (int i = 0; i < fractionTable.getRowCount(); i++) {

            String fileName = (String) fractionTable.getValueAt(i, fractionTable.getColumn("Fraction").getModelIndex());
            spectrumFiles.add(IoUtil.removeExtension(fileName));

            Double lower = (Double) fractionTable.getValueAt(i, fractionTable.getColumn("Lower Range (kDa)").getModelIndex());
            Double upper = (Double) fractionTable.getValueAt(i, fractionTable.getColumn("Upper Range (kDa)").getModelIndex());
            fractionRanges.put(fileName, new XYDataPoint(lower, upper));
        }

        peptideShakerGUI.setUpdated(PeptideShakerGUI.PROTEIN_FRACTIONS_TAB_INDEX, false);

        if (peptideShakerGUI.getSelectedTab() == PeptideShakerGUI.PROTEIN_FRACTIONS_TAB_INDEX) {
            peptideShakerGUI.getProteinFractionsPanel().displayResults();
        }

        peptideShakerGUI.getIdentification().setFractions(spectrumFiles);
        peptideShakerGUI.getIdentificationParameters().getFractionParameters().setFractionMolecularWeightRanges(fractionRanges);
        this.setVisible(false);
        this.dispose();

    }//GEN-LAST:event_okButtonActionPerformed

    /**
     * Enable/disable the move options based on which rows that are selected.
     *
     * @param evt the mouse event
     */
    private void fractionTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fractionTableMouseReleased
        int selectedRows[] = fractionTable.getSelectedRows();

        if (selectedRows.length > 0) {
            moveUpButton.setEnabled(selectedRows[0] > 0);
            moveTopButton.setEnabled(selectedRows[0] > 0);
            moveDownButton.setEnabled(selectedRows[selectedRows.length - 1] < fractionTable.getRowCount() - 1);
            moveBottomButton.setEnabled(selectedRows[selectedRows.length - 1] < fractionTable.getRowCount() - 1);
        } else {
            moveUpButton.setEnabled(false);
            moveTopButton.setEnabled(false);
            moveDownButton.setEnabled(false);
            moveBottomButton.setEnabled(false);
        }
    }//GEN-LAST:event_fractionTableMouseReleased

    /**
     * Import the fraction ranges from file.
     *
     * @param evt the action event
     */
    private void importFractionRangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importFractionRangesButtonActionPerformed

        // get the file to get the fraction ranges from
        final File selectedFile = peptideShakerGUI.getUserSelectedFile(null, ".txt", "Tab separated text file (.txt)", "Import...", true);

        if (selectedFile != null) {

            ArrayList<XYDataPoint> lowerAndUpper = new ArrayList<>();

            try {
                FileReader r = new FileReader(selectedFile);
                BufferedReader br = new BufferedReader(r);
                String line = br.readLine();

                while (line != null) {
                    String[] values = line.split("\\t");
                    lowerAndUpper.add(new XYDataPoint(Double.valueOf(values[0]), Double.valueOf(values[1])));
                    line = br.readLine();
                }

                br.close();
                r.close();

            } catch (FileNotFoundException e) {

                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Could not find the file \'" + selectedFile.getAbsolutePath() + "\'.",
                        "File Not Found",
                        JOptionPane.WARNING_MESSAGE
                );

            } catch (IOException e) {

                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "An error occurred when parsing the file \'" + selectedFile.getAbsolutePath() + "\'.",
                        "File Error",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (NumberFormatException e) {

                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "One of the values in the file is not a number.\nPlease include tab separated values only.",
                        "File Error",
                        JOptionPane.WARNING_MESSAGE
                );
            }

            if (lowerAndUpper.size() > fractionTable.getRowCount()) {

                JOptionPane.showMessageDialog(
                        this,
                        "There are more values in the file than there are rows in the table!\nSome rows will be ignored.",
                        "File Error",
                        JOptionPane.WARNING_MESSAGE
                );
            }

            for (int i = 0; i < lowerAndUpper.size(); i++) {

                ((DefaultTableModel) fractionTable.getModel())
                        .setValueAt(
                                lowerAndUpper.get(i).getX(),
                                i,
                                2
                        );
                ((DefaultTableModel) fractionTable.getModel())
                        .setValueAt(
                                lowerAndUpper.get(i).getY(),
                                i,
                                3
                        );
            }
        }
    }//GEN-LAST:event_importFractionRangesButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel disclaimerLabel;
    private javax.swing.JPanel disclamierPanel;
    private javax.swing.JScrollPane fractionJScrollPane;
    private javax.swing.JTable fractionTable;
    private javax.swing.JPanel fractionsPanel;
    private javax.swing.JLabel helpLabel;
    private javax.swing.JButton importFractionRangesButton;
    private javax.swing.JButton moveBottomButton;
    private javax.swing.JButton moveDownButton;
    private javax.swing.JButton moveTopButton;
    private javax.swing.JButton moveUpButton;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel orderSettingsPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * Resets the table indexes.
     */
    private void resetTableIndexes() {

        for (int i = 0; i < fractionTable.getRowCount(); i++) {
            fractionTable.setValueAt((i + 1), i, 0);
        }
    }

    /**
     * Moves all rows contained between the positions start and end to the
     * position specified by destination.
     *
     * @param model the model
     * @param start the start index
     * @param end the end index
     * @param destination the destination index
     */
    public static void moveRows(
            DefaultTableModel model,
            int start,
            int end,
            int destination
    ) {

        int count = end - start;

        if (count <= 0) {

            return;

        }

        if (destination > start) {

            destination = Math.max(start, destination - count);

        }

        end--;
        model.moveRow(
                start,
                end,
                destination
        );

    }
}
