import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @class MainSearchForm
 *
 * main search form, this is where user types keyword,
 * what file extension to search files for, and whether to
 * search content of files, or the names of
 * all the files in that directory
 */
public class MainSearchForm extends JFrame {

    public JPanel panelMain;
    public JComboBox cboSearchType;
    public JComboBox cboFileType;
    public JTextField txtKeyword;
    public JButton btnSearch;
    public JLabel lblTempBottom;
    public JButton btnBackProject;
    private JCheckBox chkIsRegex;
    private JCheckBox chkCaseSensitive;
    public JPanel titlePanel;
    public JPanel panel1;
    public JPanel panel2;
    public JPanel panel3;
    public JPanel panel4;
    public JLabel lblTemp;

    private boolean searchClicked = false;

    long runSearchRan = 0;

    public boolean clickedNext = false;
    public boolean ctrlPressed = false;
    public boolean fPressed = false;
    public long timeCtrlPressed = 0;



    public MainSearchForm() {

        JFrame self = this;

        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        loadDropdowns();

        KeyDispatcher ThisKeyDispatcher = new KeyDispatcher();
        KeyboardFocusManager ThisKbFocusMngr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        ThisKbFocusMngr.addKeyEventDispatcher(ThisKeyDispatcher);
        //return ThisKeyDispatcher;

        //changeFileType("CSS");

        cboSearchType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SearchCommand.SearchType = cboSearchType.getSelectedItem().toString();
            }
        });
        cboFileType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SearchCommand.FileType = cboFileType.getSelectedItem().toString();
            }
        });
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                runSearch();
            }
        });

        btnBackProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SearchCommand.FolderPath = SearchCommand.ProjectPath;
                NavFrames.SelectProjectFrame.setVisible(true);
            }
        });
        chkIsRegex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton absB = (AbstractButton) e.getSource();
                ButtonModel bMod = absB.getModel();
                boolean selected = bMod.isSelected();
                if (!selected) {
                    SearchCommand.IsRegex = false;
                } else {
                    SearchCommand.IsRegex = true;
                }
            }
        });

        chkCaseSensitive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton absB = (AbstractButton) e.getSource();
                ButtonModel bMod = absB.getModel();
                boolean selected = bMod.isSelected();
                if (!selected) {
                    SearchCommand.IsCaseSensitive = false;
                } else {
                    SearchCommand.IsCaseSensitive = true;
                }
            }
        });
    }

    public void changeFileType(String fileType)
    {
        cboFileType.setSelectedItem(fileType);
        SearchCommand.FileType = cboFileType.getSelectedItem().toString();
    }


    public void runSearch() {

        if (SearchCommand.searchIsClicked == false) {
            SearchCommand.searchIsClicked = true;
            if (SearchCommand.SearchType == "Text in Files") { // if is text search

                if (txtKeyword.getText() != "") { // if entered search keyword

                    List<File> FilesSearched = new ArrayList<>();
                    if (SearchCommand.FileType == "ALL") {

                        SearchCommand.FileType = "*";
                        File file = new File(SearchCommand.FolderPath);
                        FilesSearched = addFiles(null, file);

                    } else {
                        File file = new File(SearchCommand.FolderPath);
                        FilesSearched = addFiles(null, file, SearchCommand.FileType.toLowerCase());
                    }
                    int counter = 1;

                    SearchResultsTableModel TableModel = new SearchResultsTableModel();
                    List<List<Object>> resultsArr = new ArrayList<>();
                    for (File getFile : FilesSearched) {

                        int lines_count2 = 1;
                        int lines_count_all = 1;
                        try (BufferedReader br = new BufferedReader(new FileReader(getFile))) {
                            int modelCount = 0;
                            for (String line; (line = br.readLine()) != null; ) {
                                if (modelCount == 0) {

                                }
                                boolean isMatch = false;
                                if (SearchCommand.IsRegex) {
                                    if (SearchCommand.IsCaseSensitive) {
                                        Pattern use_pattern = Pattern.compile(".*" + txtKeyword.getText() + ".*");
                                        isMatch = (use_pattern.matches(".*" + txtKeyword.getText() + ".*", line));
                                    } else {
                                        Pattern use_pattern = Pattern.compile(".*" + txtKeyword.getText() + ".*", Pattern.CASE_INSENSITIVE);
                                        isMatch = (use_pattern.matches(".*" + txtKeyword.getText() + ".*", line));
                                    }
                                } else {
                                    if (SearchCommand.IsCaseSensitive) {
                                        isMatch = (line.contains(txtKeyword.getText()));
                                    } else {
                                        isMatch = (line.toLowerCase().contains(txtKeyword.getText().toLowerCase()));
                                    }
                                }
                                if (isMatch) {
                                    List<Object> eachObject = new ArrayList<>();

                                    eachObject.add(getFile.getAbsolutePath());

                                    if (SearchCommand.OnlyFileNames == false) {  // if want to see found lines
                                        eachObject.add(line);
                                    } else {
                                        eachObject.add("");
                                    }
                                    eachObject.add(lines_count_all);

                                    //System.out.println("abs_file: " + getFile.getAbsoluteFile());

                                    eachObject.add(getFile.getParent());
                                    resultsArr.add(eachObject);

                                    modelCount++;
                                    lines_count2++;
                                    counter++;
                                }
                                lines_count_all++;
                            }
                        } catch (IOException eIO) {
                            System.out.println(eIO.getMessage());
                        }
                    }
                    SearchCommand.dataModel = resultsArr;
                    SearchCommand.convertDataModel();

                    if (NavFrames.SearchResultsFrame != null) {
                        NavFrames.SearchResultsFrame.dispose();
                    }
                    NavFrames.SearchResultsFrame = new SearchResultsForm("Search Results");
                } else {
                    JOptionPane.showMessageDialog(MainSearchForm.this, "You did not enter a Search Keyword.");
                }

            } else {  // if searching names of files

                List<File> FilesSearched = new ArrayList<>();
                if (SearchCommand.FileType == "ALL") {

                    SearchCommand.FileType = "*";
                    File file = new File(SearchCommand.FolderPath);
                    FilesSearched = addFiles(null, file);

                } else {
                    File file = new File(SearchCommand.FolderPath);
                    FilesSearched = addFiles(null, file, SearchCommand.FileType.toLowerCase());
                }
                int counter = 1;

                SearchResultsTableModel TableModel = new SearchResultsTableModel();
                List<List<Object>> resultsArr = new ArrayList<>();
                for (File getFile : FilesSearched) {

                    try (BufferedReader br = new BufferedReader(new FileReader(getFile))) {

                        String fileName = getFile.getAbsolutePath().toString().replace(SearchCommand.ProjectPath, "");
                        if (txtKeyword.getText() != "") { // if entered search keyword

                            if (fileName.toLowerCase().contains(txtKeyword.getText().toLowerCase())) {
                                List<Object> eachObject = new ArrayList<>();
                                eachObject.add(getFile.getAbsolutePath());
                                eachObject.add("");
                                eachObject.add("0");
                                eachObject.add(getFile.getParent());
                                resultsArr.add(eachObject);
                            }

                        } else {
                            List<Object> eachObject = new ArrayList<>();
                            eachObject.add(getFile.getAbsolutePath());
                            eachObject.add("");
                            eachObject.add("0");
                            eachObject.add(getFile.getParent());
                            resultsArr.add(eachObject);
                        }

                    } catch (IOException eIO) {
                        System.out.println(eIO.getMessage());
                    }
                }
                SearchCommand.dataModel = resultsArr;
                SearchCommand.convertDataModel();

                if (NavFrames.SearchResultsFrame != null) {
                    NavFrames.SearchResultsFrame.dispose();
                }
                NavFrames.SearchResultsFrame = new SearchResultsForm("Search Results");
            }

            setTimeout(() -> SearchCommand.searchIsClicked = false, 500);

        }
    }

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }


    public List<File> addFiles(List<File> files, File dir) {
        if (files == null) {
            files = new ArrayList<File>();
        }
        if (!dir.isDirectory()) {
            files.add(dir);
            return files;
        }

        //if (!dir.getAbsoluteFile().getAbsolutePath().toString().contains("node_modules")) {
            for (File file : dir.listFiles()) {
                addFiles(files, file);
            }
        //}
        return files;
    }

    public List<File> addFiles(List<File> files, File dir, String extension) {
        if (files == null) {
            files = new ArrayList<File>();
        }
        if (!dir.isDirectory()) {
            if (dir.getName().endsWith(extension)) {
                files.add(dir);
            }
            return files;
        }
        for (File file : dir.listFiles()) {
            addFiles(files, file, extension);
        }
        return files;
    }

    private void loadDropdowns() {

        List<String> SearchTypes = new ArrayList<>();
        SearchTypes.add("Text in Files");
        SearchTypes.add("File Names");
        cboSearchType.setModel(new DefaultComboBoxModel(SearchTypes.toArray()));
        List<String> FileTypes = new ArrayList<>();

        FileTypes.add("ALL");
        FileTypes.add("CSS");
        FileTypes.add("HTM");
        FileTypes.add("JAVA");
        FileTypes.add("JS");
        FileTypes.add("JSON");
        FileTypes.add("PHP");
        FileTypes.add("PY");
        FileTypes.add("SQL");
        FileTypes.add("TPL");
        FileTypes.add("TWIG");
        FileTypes.add("XML");

        cboFileType.setModel(new DefaultComboBoxModel(FileTypes.toArray()));
    }
}