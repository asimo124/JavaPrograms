import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ahawley on 9/12/2017.
 */
public class SearchLogForm {

    private JComboBox cboSite;
    private JTextField txtFileName;
    private JTextField txtChangeDesc;
    private JTextField txtFromDate;
    private JTextField txtToDate;
    private JPanel titlePanel;
    public JPanel panelMain;
    private JButton btnSearch;
    private JButton btnGoToAdd;
    private JComboBox cboOrderBy;
    private JComboBox cboDir;
    private JComboBox cboProject;

    /**
     * Begin Program
     *
     * @param args
     */
    public static void main(String args[]) {
        NavFrames.SearchLogForm = new SearchLogForm();
        NavFrames.SearchLogFrame = new JFrame("Win Grep");
        NavFrames.SearchLogFrame.setContentPane(NavFrames.SearchLogForm.panelMain);
        NavFrames.SearchLogFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        NavFrames.SearchLogFrame.pack();
        NavFrames.SearchLogFrame.setVisible(true);
        NavFrames.SearchLogFrame.setLocationRelativeTo(null);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int useX = (int)Math.round(dim.getWidth() * 0.33);
        int useWidth = (int)Math.round(dim.getWidth() * 0.67);

        NavFrames.SearchLogFrame.setBounds(useX - 10, 0, useWidth, 210);

        fillDropdowns();
        init();
    }

    public SearchLogForm() {

        SQLiteConn.connect();
        if (SearchCommand.ranImportSQL == false) {
            SQLiteConn.importSQL();
            SearchCommand.ranImportSQL = true;
        }

        /*/
        try {
            String sql19 = "SELECT * FROM prod_logs ";
            Statement stmt19 = SQLiteConn.conn.createStatement();
            ResultSet rs19 = stmt19.executeQuery(sql19);
            java.util.List<StringHashMap> getResults19 = SQLiteConn.resultsToList(rs19);
            PrintUtilities.printTable(getResults19);
        } catch (SQLException e19) {
            System.out.println(e19.getMessage());
        }
        //*/

        /**
         * Click "Add Log" on Form - Go to Add Form
         */
        btnGoToAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                openAddForm();
            }
        });

        /**
         * Click "Search" on Form
         */
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                runSearch();
            }
        });

        /**
         * Update Site dropdown value for Search
         */
        cboSite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchCommand.site = cboSite.getSelectedItem().toString();
            }
        });
        cboProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchCommand.project = cboProject.getSelectedItem().toString();
            }
        });
    }

    public void runSearch() {
        /**
         * Get Filters and save to global object to build query
         */
        if (!txtFileName.getText().trim().equals("")) {
            SearchCommand.fileName = txtFileName.getText().trim();
        } else {
            SearchCommand.fileName = "";
        }
        if (!txtChangeDesc.getText().trim().equals("")) {
            SearchCommand.changeDesc = txtChangeDesc.getText().trim();
        } else {
            SearchCommand.changeDesc = "";
        }
        String moveDateFrom = txtFromDate.getText().trim();
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(moveDateFrom);
            SearchCommand.fromDate = moveDateFrom;
        } catch (Exception e3) {
            SearchCommand.fromDate = "";
        }
        String moveDateTo = txtToDate.getText().trim();
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(moveDateTo);
            SearchCommand.toDate = moveDateTo;
        } catch (Exception e3) {
            SearchCommand.toDate = "";
        }
        SearchCommand.orderBy = cboOrderBy.getSelectedItem().toString();
        SearchCommand.dir = cboDir.getSelectedItem().toString();

        // Load DataGrid
        if (NavFrames.SearchResultsFrame != null) {
            NavFrames.SearchResultsFrame.dispose();
        }
        NavFrames.SearchResultsFrame = new SearchResultsForm("Search Results");
    }

    public void openAddForm() {

        if (NavFrames.AddLogForm == null) {
            NavFrames.AddLogForm = new AddLogForm();
            NavFrames.AddLogFrame = new JFrame("Win Grep");
            NavFrames.AddLogFrame.setContentPane(NavFrames.AddLogForm.panelMain);
            NavFrames.AddLogFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            NavFrames.AddLogFrame.pack();
            NavFrames.AddLogFrame.setVisible(true);
            NavFrames.AddLogFrame.setLocationRelativeTo(null);

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            int useWidth = (int)Math.round(dim.getWidth() * 0.33);

            NavFrames.AddLogFrame.setBounds(0, 0, useWidth, (int)dim.getHeight());

        } else {
            NavFrames.AddLogForm.init();
            NavFrames.AddLogFrame.setVisible(true);
        }
    }

    /**
     * Fill all dropdowns
     */
    public static void fillDropdowns() {

        SearchCommand.SiteNamesArr = new ArrayList<>();
        SearchCommand.SiteNamesFormArr = new ArrayList<>();
        File file = new File("C:\\Temp\\production_logs\\prod_log_site_names.txt");
        SearchCommand.SiteNamesArr.add("- Select One -");
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            int i = 0;
            for (String line; (line = br.readLine()) != null; ) {
                SearchCommand.SiteNamesArr.add(line);
                SearchCommand.SiteNamesFormArr.add(line);
                i++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Collections.sort(SearchCommand.SiteNamesArr);
        Collections.sort(SearchCommand.SiteNamesFormArr);

        SearchCommand.ProjectsArr = new ArrayList<>();
        SearchCommand.ProjectsFormArr = new ArrayList<>();
        File file2 = new File("C:\\Temp\\production_logs\\prod_log_projects.txt");
        SearchCommand.ProjectsArr.add("- Select One -");
        try(BufferedReader br = new BufferedReader(new FileReader(file2))) {
            int i = 0;
            for (String line; (line = br.readLine()) != null; ) {
                SearchCommand.ProjectsArr.add(line);
                SearchCommand.ProjectsFormArr.add(line);
                i++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Collections.sort(SearchCommand.ProjectsArr);
        Collections.sort(SearchCommand.ProjectsFormArr);

        SearchCommand.CategoriesArr = new ArrayList<>();
        SearchCommand.CategoriesFormArr = new ArrayList<>();
        File file3 = new File("C:\\Temp\\production_logs\\prod_log_categories.txt");
        //SearchCommand.ProjectsArr.add("- Select One -");
        try(BufferedReader br = new BufferedReader(new FileReader(file3))) {
            int i = 0;
            for (String line; (line = br.readLine()) != null; ) {
                SearchCommand.CategoriesArr.add(line);
                SearchCommand.CategoriesFormArr.add(line);
                i++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Collections.sort(SearchCommand.CategoriesArr);
        Collections.sort(SearchCommand.CategoriesFormArr);

        SearchCommand.CategoriesArr.add(0, "- Select One -");

        SearchCommand.OrderBys = new ArrayList<String>();
        SearchCommand.OrderBys.add("date");
        SearchCommand.OrderBys.add("site");
        SearchCommand.OrderBys.add("change_desc");

        SearchCommand.Dirs = new ArrayList<String>();
        SearchCommand.Dirs.add("ASC");
        SearchCommand.Dirs.add("DESC");
    }

    /**
     * Set Dropdown Models
     */
    public static void init() {
        NavFrames.SearchLogForm.cboSite.setModel(new DefaultComboBoxModel(SearchCommand.SiteNamesArr.toArray()));
        NavFrames.SearchLogForm.cboProject.setModel(new DefaultComboBoxModel(SearchCommand.ProjectsArr.toArray()));
        NavFrames.SearchLogForm.cboOrderBy.setModel(new DefaultComboBoxModel(SearchCommand.OrderBys.toArray()));
        NavFrames.SearchLogForm.cboDir.setModel(new DefaultComboBoxModel(SearchCommand.Dirs.toArray()));
        NavFrames.SearchLogForm.runSearch();
        NavFrames.SearchLogForm.openAddForm();

        NavFrames.SearchLogFrame.getContentPane().setBackground(Color.WHITE);
        NavFrames.SearchLogForm.titlePanel.setBackground(Color.WHITE);
        NavFrames.SearchLogForm.btnGoToAdd.setBackground(new Color(50, 118, 177));
        NavFrames.SearchLogForm.btnGoToAdd.setForeground(Color.WHITE);
        NavFrames.SearchLogForm.btnSearch.setBackground(new Color(50, 118, 177));
        NavFrames.SearchLogForm.btnSearch.setForeground(Color.WHITE);
    }
}
