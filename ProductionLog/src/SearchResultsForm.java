import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by ahawley on 9/12/2017.
 */
public class SearchResultsForm extends JFrame {
    private JPanel panelMain;
    private JTable table;
    private JScrollPane scpMain;

    /**
     * @param title
     * @throws HeadlessException
     */
    public SearchResultsForm(String title) throws HeadlessException {
        super(title);
        Dimension dim = new Dimension();
        dim.setSize(15, 7);
        table.setRowHeight(22);
        table.setIntercellSpacing(dim);
        setContentPane(panelMain);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        setVisible(true);

        Dimension dim2 = Toolkit.getDefaultToolkit().getScreenSize();
        int useX = (int) Math.round(dim2.getWidth() * 0.33);
        int useWidth = (int) Math.round(dim2.getWidth() * 0.33);

        setBounds(useX - 10, 210, useWidth + 25, (int)dim2.getHeight() - 220);

        init();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTable target = (JTable)e.getSource();
                int row = target.getSelectedRow();
                int column = target.getSelectedColumn();
                switch (column) {
                    case 3:
                        SearchCommand.currentId = SearchCommand.modelDataIds.get(row);
                        if (NavFrames.EditLogForm == null) {
                            NavFrames.EditLogForm = new EditLogForm();
                            NavFrames.EditLogFrame = new JFrame("Edit Log");
                            NavFrames.EditLogFrame.setContentPane(NavFrames.EditLogForm.panelMain);
                            NavFrames.EditLogFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            NavFrames.EditLogFrame.pack();
                            NavFrames.EditLogFrame.setVisible(true);
                            NavFrames.EditLogFrame.setLocationRelativeTo(null);

                            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                            int useWidth = (int)Math.round(dim.getWidth() * 0.33);

                            NavFrames.EditLogFrame.setBounds(0, 0, useWidth, (int)dim.getHeight());
                        } else {
                            NavFrames.EditLogForm.loadData();
                            NavFrames.EditLogFrame.setVisible(true);
                        }
                        NavFrames.EditLogFrame.getContentPane().setBackground(Color.WHITE);
                        NavFrames.EditLogForm.titlePanel.setBackground(Color.WHITE);
                        NavFrames.EditLogForm.btnAdd.setBackground(new Color(50, 118, 177));
                        NavFrames.EditLogForm.btnAdd.setForeground(Color.WHITE);


                        if (NavFrames.FileResultsFrame != null) {
                            NavFrames.FileResultsFrame.dispose();
                        }
                        NavFrames.FileResultsFrame = new FileResultsForm("Moved Files");
                        break;
                    case 4:
                        SearchCommand.currentId = SearchCommand.modelDataIds.get(row);

                        String sql = "DELETE FROM prod_logs WHERE id = ? ";
                        try {
                            PreparedStatement pstmt = SQLiteConn.conn.prepareStatement(sql);
                            pstmt.setInt(1, SearchCommand.currentId);
                            pstmt.executeUpdate();
                        } catch (SQLException e2) {
                            System.out.println(e2.getMessage());
                        }
                        sql = "DELETE FROM prod_log_files WHERE log_id = ? ";
                        try {
                            PreparedStatement pstmt = SQLiteConn.conn.prepareStatement(sql);
                            pstmt.setInt(1, SearchCommand.currentId);
                            pstmt.executeUpdate();
                        } catch (SQLException e2) {
                            System.out.println(e2.getMessage());
                        }

                        JOptionPane.showMessageDialog(null, "Production Log Deleted.");
                        // Load DataGrid
                        if (NavFrames.SearchResultsFrame != null) {
                            NavFrames.SearchResultsFrame.dispose();
                        }
                        NavFrames.SearchResultsFrame = new SearchResultsForm("Search Results");

                    default:

                }
            }
        });
    }
    public void init() {

        setModel();
        panelMain.setBackground(Color.WHITE);
        scpMain.getViewport().setBackground(Color.WHITE);
        table.setBackground(Color.WHITE);
    }

    /**
     * Set TableModel for Production Logs Datagrid
     */
    public void setModel() {
        SearchResultsTableModel useModel = new SearchResultsTableModel();
        java.util.List<StringHashMap> ProdLogs = null;
        ResultSet rs = null;
        Statement stmt = null;
        PreparedStatement pstmt10 = null;

        if (SearchCommand.fromDate == null || SearchCommand.fromDate.equals("")) {
            SearchCommand.fromDate = "1900-01-01";
        }
        if (SearchCommand.toDate == null || SearchCommand.toDate.equals("")) {
            SearchCommand.toDate = "2300-01-01";
        }
        int fromDateIndex = 1;
        int toDateIndex = 2;
        int curIndex = 2;
        int siteParamIndex = 0;
        int projectParamIndex = 0;
        int fileNameParamIndex = 0;
        int changeDescParamIndex = 0;
        String sql2 = "SELECT l.id, site, change_desc, strftime('%m/%d/%Y', date) " +
                "FROM prod_logs l " +
                "LEFT JOIN prod_log_files f " +
                    "ON l.id = f.log_id " +
                "WHERE l.date BETWEEN ? and ? ";
        if (SearchCommand.site != null && !SearchCommand.site.equals("- Select One -")) {
            sql2 += "AND l.site = ? ";
            curIndex++;
            siteParamIndex = curIndex;
        }
        if (SearchCommand.project != null && !SearchCommand.project.equals("- Select One -")) {
            sql2 += "AND l.project = ? ";
            curIndex++;
            projectParamIndex = curIndex;
        }
        if (SearchCommand.fileName != null && !SearchCommand.fileName.equals("")) {
            sql2 += "AND f.file_path LIKE ? ";
            curIndex++;
            fileNameParamIndex = curIndex;
        }
        if (SearchCommand.changeDesc != null && !SearchCommand.changeDesc.equals("")) {
            sql2 += "AND l.change_desc LIKE ? ";
            curIndex++;
            changeDescParamIndex = curIndex;
        }
        sql2 += "GROUP BY l.id " +
                "ORDER BY " + SearchCommand.orderBy + " " + SearchCommand.dir;

        pstmt10 = null;
        try {
            pstmt10 = SQLiteConn.conn.prepareStatement(sql2);
        } catch (SQLException e11) {
            System.out.println(e11.getMessage());
        }
        try {
            pstmt10.setString(fromDateIndex, SearchCommand.fromDate);
            pstmt10.setString(toDateIndex, SearchCommand.toDate);
            rs = pstmt10.executeQuery();
        } catch (SQLException e14) {
            System.out.println(e14.getMessage());
        }
        if (SearchCommand.site != null && !SearchCommand.site.equals("- Select One -")) {
            try {
                pstmt10.setString(siteParamIndex, SearchCommand.site);
                rs = pstmt10.executeQuery();
            } catch (SQLException e14) {
                System.out.println(e14.getMessage());
            }
        }
        if (SearchCommand.project != null && !SearchCommand.project.equals("- Select One -")) {
            try {
                pstmt10.setString(projectParamIndex, SearchCommand.project);
                rs = pstmt10.executeQuery();
            } catch (SQLException e14) {
                System.out.println(e14.getMessage());
            }
        }
        if (SearchCommand.fileName != null && !SearchCommand.fileName.equals("")) {
            try {
                pstmt10.setString(fileNameParamIndex, "%" + SearchCommand.fileName + "%");
                rs = pstmt10.executeQuery();
            } catch (SQLException e14) {
                System.out.println(e14.getMessage());
            }
        }
        if (SearchCommand.changeDesc != null && !SearchCommand.changeDesc.equals("")) {
            try {
                pstmt10.setString(changeDescParamIndex, "%" + SearchCommand.changeDesc + "%");
                rs = pstmt10.executeQuery();
            } catch (SQLException e14) {
                System.out.println(e14.getMessage());
            }
        }
        ProdLogs = SQLiteConn.resultsToList(rs);
        SearchCommand.convertDataModel(ProdLogs);
        useModel.data = SearchCommand.completeModelData;
        table.setModel(useModel);
    }
}
