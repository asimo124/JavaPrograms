import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FileResultsForm extends JFrame {
    public JPanel panelMain;
    private JTable table;
    private JScrollPane scpMain;

    public FileResultsForm(String title) throws HeadlessException {
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
        int useX = (int) Math.round(dim2.getWidth() * 0.66) + 5;
        int useWidth = (int) Math.round(dim2.getWidth() * 0.33);

        setBounds(useX - 10, 210, useWidth + 25, (int)dim2.getHeight() - 220);

        init();
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
        FileResultsTableModel useModel = new FileResultsTableModel();
        java.util.List<StringHashMap> LogFiles = null;
        ResultSet rs = null;
        Statement stmt = null;
        PreparedStatement pstmt10 = null;

        //*/
        String sql2 = "SELECT file_path FROM prod_log_files WHERE log_id = ? ORDER BY file_path ";

        pstmt10 = null;
        try {
            pstmt10 = SQLiteConn.conn.prepareStatement(sql2);
            pstmt10.setInt(1, SearchCommand.currentId);
            rs = pstmt10.executeQuery();
        } catch (SQLException e11) {
            System.out.println(e11.getMessage());
        }

        LogFiles = SQLiteConn.resultsToList(rs);
        SearchCommand.convertDataModelFiles(LogFiles);
        useModel.data = SearchCommand.completeModelFilesData;
        table.setModel(useModel);
        //*/
    }
}
