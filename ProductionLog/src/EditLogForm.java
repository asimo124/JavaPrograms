import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by ahawley on 9/12/2017.
 */
public class EditLogForm {

    public JPanel panelMain;
    private JComboBox cboSite;
    private JTextArea txaChangeDesc;
    private JTextField txtMoveDate;
    private JTextArea txaFileList;
    public JButton btnAdd;
    private JLabel lblMessage;
    private JComboBox cboProject;
    public JPanel titlePanel;
    private boolean fileTextTouched = false;

    public EditLogForm() {
        init();
        txaFileList.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                fileTextTouched = true;
            }
        });
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (validateForm()) {  // if form is valid

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
                    int id = SearchCommand.currentId;
                    String sql = "UPDATE prod_logs " +
                            "SET site = ?, " +
                            "change_desc = ?," +
                            "project = ?," +
                            "date = ? " +
                            "WHERE id = ? ";
                    try {
                        PreparedStatement pstmt = SQLiteConn.conn.prepareStatement(sql);
                        pstmt.setString(1, cboSite.getSelectedItem().toString());
                        pstmt.setString(2, txaChangeDesc.getText().trim());
                        pstmt.setString(3, cboProject.getSelectedItem().toString());
                        pstmt.setString(4, moveDate3);
                        pstmt.setInt(5, id);
                        pstmt.executeUpdate();
                    } catch (SQLException e2) {
                        System.out.println(e2.getMessage());
                    }

                    if (fileTextTouched == true) {  // if Files List Text Area modified

                        // Delete Files List
                        String fileList = txaFileList.getText().trim();
                        String lines[] = fileList.split("\\r?\\n");
                        String sql12 = "DELETE FROM prod_log_files WHERE log_id = ? ";
                        try {
                            PreparedStatement pstmt12 = SQLiteConn.conn.prepareStatement(sql12);
                            pstmt12.setInt(1, id);
                            pstmt12.executeUpdate();
                        } catch (SQLException e2) {
                            System.out.println(e2.getMessage());
                        }

                        // Re-insert Files List from Text Area split by line breaks
                        String sql10 = "INSERT INTO prod_log_files (log_id, file_path) VALUES (?, ?) ";
                        PreparedStatement pstmt10 = null;
                        try {
                            pstmt10 = SQLiteConn.conn.prepareStatement(sql10);
                        } catch (SQLException e11) {
                            System.out.println(e11.getMessage());
                        }
                        for (String line : lines) {
                            try {
                                pstmt10.setInt(1, id);
                                pstmt10.setString(2, line);
                                pstmt10.executeUpdate();
                            } catch (SQLException e14) {
                                System.out.println(e14.getMessage());
                            }
                        }
                        Statement stmt7 = null;
                        ResultSet rs15 = null;
                        String sql15 = "SELECT * FROM prod_log_files";
                        try {
                            stmt7 = SQLiteConn.conn.createStatement();
                            rs15 = stmt7.executeQuery(sql15);
                        } catch (SQLException e19) {
                            System.out.println(e19.getMessage());
                        }
                        List<StringHashMap> getResults = SQLiteConn.resultsToList(rs15);
                        PrintUtilities.printTable(getResults);
                    }

                    // Log Updated
                    fileTextTouched = false;
                    lblMessage.setText("");
                    JOptionPane.showMessageDialog(null, "Move to Production Log Updated.");
                    NavFrames.SearchLogForm.runSearch();
                } else {

                }
            }
        });
    }

    /**
     * Load Prod Log Record by ID (id comes from SearchCommand class)
     */
    public void loadData() {
        int id = 0;
        String sql = "SELECT site, change_desc, project, date FROM prod_logs WHERE id = ? LIMIT 1 ";
        try {
            id = SearchCommand.currentId;
            PreparedStatement pstmt = SQLiteConn.conn.prepareStatement(sql);
            pstmt.setDouble(1, id);
            try {
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    cboSite.setSelectedItem(rs.getString("site"));
                    txaChangeDesc.setText(rs.getString("change_desc"));
                    cboProject.setSelectedItem(rs.getString("project"));
                    txtMoveDate.setText(rs.getString("date"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch(SQLException e2){
            System.out.println(e2.getMessage());
        }

        // Load Log Files into Text Area
        Statement stmt7 = null;
        ResultSet rs15 = null;
        PreparedStatement pstmt15 = null;
        String sql15 = "SELECT * FROM prod_log_files WHERE log_id = ? ";
        try {
            pstmt15 = SQLiteConn.conn.prepareStatement(sql15);
            pstmt15.setDouble(1, id);
            rs15 = pstmt15.executeQuery();
            StringBuilder lines = new StringBuilder("");
            while (rs15.next()) {
                if (lines.toString().equals("")) {
                    lines.append(rs15.getString("file_path"));
                } else {
                    lines.append("\n");
                    lines.append(rs15.getString("file_path"));
                }
            }
            txaFileList.setText(lines.toString());
        } catch (SQLException e19) {
            System.out.println(e19.getMessage());
        }
    }

    /**
     * Initialize Form
     */
    public void init() {
        loadDropdowns();
        loadData();
        fileTextTouched = false;
    }

    /**
     * Validate Form before saving
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

    /**
     * Load Dropdowns
     */
    public void loadDropdowns() {

        cboSite.setModel(new DefaultComboBoxModel(SearchCommand.SiteNamesFormArr.toArray()));
        cboProject.setModel(new DefaultComboBoxModel(SearchCommand.ProjectsFormArr.toArray()));

        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        txaFileList.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        txaChangeDesc.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }
}
