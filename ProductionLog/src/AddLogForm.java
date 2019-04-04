import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * @author AMH
 */
public class AddLogForm {

    public JPanel panelMain;
    private JComboBox cboSite;
    private JTextArea txaChangeDesc;
    private JTextField txtMoveDate;
    private JTextArea txaFileList;
    private JButton btnAdd;
    private JLabel lblMessage;
    private JComboBox cboProject;
    private JPanel titlePanel;
    private boolean fileTextTouched = false;


    public AddLogForm()
    {
        init();

        panelMain.setBackground(Color.WHITE);
        titlePanel.setBackground(Color.WHITE);
        btnAdd.setBackground(new Color(50, 118, 177));
        btnAdd.setForeground(Color.WHITE);

        /**
         * Click "Add" to create Log
         */
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (validateForm()) {  // if form valid

                    // Insert Record
                    java.util.Date moveDate2 = null;
                    String moveDate = txtMoveDate.getText().trim();
                    String moveDate3 = null;
                    try {
                        moveDate2 = new SimpleDateFormat("yyyy-MM-dd").parse(moveDate);
                        moveDate3 = new SimpleDateFormat("yyyy-MM-dd").format(moveDate2);
                    } catch (Exception e3) {
                        System.out.println(e3.getMessage());
                    }

                    String project = cboProject.getSelectedItem().toString();

                    String sql = "INSERT INTO prod_logs (site, change_desc, project, date) VALUES (?, ?, ?, ?) ";
                    try {
                        PreparedStatement pstmt = SQLiteConn.conn.prepareStatement(sql);
                        pstmt.setString(1, cboSite.getSelectedItem().toString());
                        pstmt.setString(2, txaChangeDesc.getText().trim());
                        pstmt.setString(3, project);
                        pstmt.setString(4, moveDate3);
                        pstmt.executeUpdate();
                    } catch (SQLException e2) {
                        System.out.println(e2.getMessage());
                    }
                    ResultSet rs7 = null;
                    String sql7 = "select last_insert_rowid() as id2";
                    try {
                        Statement stmt7 = SQLiteConn.conn.createStatement();
                        rs7 = stmt7.executeQuery(sql7);
                    } catch (SQLException e8) {
                        System.out.println(e8.getMessage());
                    }
                    int lastId = 0;
                    try {
                        if (rs7.next()) {
                            lastId = rs7.getInt("id2");
                        }
                    } catch (SQLException e9) {
                        System.out.println(e9.getMessage());
                    }
                    if (fileTextTouched == true) { // if changes were made to File List Text Area

                        // Delete current set of log files
                        String fileList = txaFileList.getText().trim();
                        String lines[] = fileList.split("\\r?\\n");
                        String sql12 = "DELETE FROM prod_log_files WHERE log_id = ? ";
                        try {
                            PreparedStatement pstmt12 = SQLiteConn.conn.prepareStatement(sql12);
                            pstmt12.setInt(1, lastId);
                            pstmt12.executeUpdate();
                        } catch (SQLException e2) {
                            System.out.println(e2.getMessage());
                        }

                        // Re-add files from text area, split by line breaks
                        String sql10 = "INSERT INTO prod_log_files (log_id, file_path) VALUES (?, ?) ";
                        PreparedStatement pstmt10 = null;
                        try {
                            pstmt10 = SQLiteConn.conn.prepareStatement(sql10);
                        } catch (SQLException e11) {
                            System.out.println(e11.getMessage());
                        }
                        for (String line : lines) {
                            try {
                                pstmt10.setInt(1, lastId);
                                pstmt10.setString(2, line);
                                pstmt10.executeUpdate();
                            } catch (SQLException e14) {
                                System.out.println(e14.getMessage());
                            }
                        }
                    }

                    // Log Inserted
                    fileTextTouched = false;
                    lblMessage.setText("");
                    JOptionPane.showMessageDialog(null, "Move to Production Log Inserted.");
                    clearFields();
                    NavFrames.SearchLogForm.runSearch();
                }
            }
        });

        /**
         * Focus Received for Files Text Area
         */
        txaFileList.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);

                // Will update Files List
                fileTextTouched = true;
            }
        });
    }

    /**
     * Initialize Form
     */
    public void init() {
        fileTextTouched = false;
        loadDropdowns();
        clearFields();
        txtMoveDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
    }

    /**
     * Validate form before saving
     *
     * @return
     */
    public boolean validateForm()
    {
        String moveDate = txtMoveDate.getText().trim();
        if (txaChangeDesc.getText().trim().equals("")) {
            lblMessage.setText("\"Change Desc\" Field is required");
            return false;
        /*} else if (txaFileList.getText().trim().equals("")) {
            lblMessage.setText("\"File List\" Field is required");
            return false;*/
        } else if (moveDate.equals("")) {
            lblMessage.setText("\"Move Date\" Field is required");
            return false;
        } else {
            try {
                new SimpleDateFormat("yyyy-MM-dd").parse(moveDate);
            } catch (Exception e) {
                lblMessage.setText("You did not enter a valid date for \"Move Date\"");
                return false;
            }
        }
        return true;
    }
    public void loadDropdowns() {
        cboSite.setModel(new DefaultComboBoxModel(SearchCommand.SiteNamesFormArr.toArray()));
        cboProject.setModel(new DefaultComboBoxModel(SearchCommand.ProjectsFormArr.toArray()));

        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        txaFileList.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        txaChangeDesc.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    public void clearFields() {
        cboSite.setSelectedIndex(0);
        txaChangeDesc.setText("");
        txtMoveDate.setText("");
        txaFileList.setText("");
        lblMessage.setText("");
    }
}