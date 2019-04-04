import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 3/27/2017.
 */
public class SourceTablesForm {
    public JList lstSourceTables;
    public JPanel panelMain;
    private JButton btnNext;

    public SourceTargetModel SourceTarget = new SourceTargetModel();
    public DatabaseSSH database_ssh = null;
    public DatabaseLocal database_local = null;
    public Connection conn = null;

    public int[] SelectedIndexes = null;
    public List<String> SourceTables = new ArrayList<>();
    public List<String> SelectedTables = new ArrayList<>();

    public SourceTablesForm(SourceTargetModel GetSourceTarget) {

        SourceTarget = GetSourceTarget;

        // Set available tables for this database in multi-select (JList)
        setTables();

        // Set number of rows to show in scrolled multi-select (JList)
        lstSourceTables.setVisibleRowCount(8);

        /**
         * Event Listeners Below
         */
        lstSourceTables.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                SelectedIndexes = lstSourceTables.getSelectedIndices();
                SelectedTables = new ArrayList<>();
                for (int getIndex: SelectedIndexes) {
                    SelectedTables.add(SourceTables.get(getIndex));
                }
                SourceTarget.setSourceSelectedTables(SelectedTables);
            }
        });
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                TargetFilesTypeForm targetFiles = new TargetFilesTypeForm(SourceTarget);
                JFrame frame = new JFrame("Select Target - Text Files Type");
                frame.setContentPane(targetFiles.panelMain);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
    }

    public void setTables() {

        DefaultListModel<String> listModel = new DefaultListModel();

        System.out.println("isSourceSSHTunnel: " + SourceTarget.isSourceIsSSHTunnel());

        if (SourceTarget.isSourceIsSSHTunnel()) {
            List<Map<String, String>> resultset = new ArrayList<>();
            database_ssh = new DatabaseSSH(SourceTarget, DatabaseType.SOURCE);
            try {
                resultset = database_ssh.connectAndGetQuery("SHOW TABLES");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            for (Map<String, String> item : resultset) {
                Iterator it = item.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    listModel.addElement(pair.getValue().toString());
                    SourceTables.add(pair.getValue().toString());
                }
            }
        } else {

            System.out.println("No tunneling");
            database_local = new DatabaseLocal(SourceTarget, DatabaseType.SOURCE);
            Connection conn = database_local.setConn();
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SHOW TABLES");
                while (rs.next()) {
                    System.out.println(rs);
                    String table = rs.getString("Tables_in_" + SourceTarget.getSourceDBName());
                    System.out.println("table: " + table);
                    listModel.addElement(table);
                    SourceTables.add(table);
                }
                st.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        lstSourceTables.setModel(listModel);
    }
}
