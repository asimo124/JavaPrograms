import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @class SelectProjectForm
 *
 * JFrame that allows user to Select Project,
 * which will tell the JFrame - SelectFolderForm
 * which directory to use to populate the Directory Tree
 */
public class SelectProjectForm {

    public JPanel panelMain;
    public JComboBox cboSelProject;
    private JButton btnNext;
    private JPanel titlePanel;

    public List<String> ProjectFileTypes = new ArrayList<>();
    public List<String> ProjectPaths = new ArrayList<>();
    public String CurProjectPath = "";
    public String CurProjectFileType = "";

    public boolean clickedNext = false;
    public boolean ctrlPressed = false;
    public boolean fPressed = false;
    public long timeCtrlPressed = 0;


    public SelectProjectForm() {

        KeyDispatcher ThisKeyDispatcher = new KeyDispatcher();
        KeyboardFocusManager ThisKbFocusMngr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        ThisKbFocusMngr.addKeyEventDispatcher(ThisKeyDispatcher);
        //return ThisKeyDispatcher;

        cboSelProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurProjectPath = ProjectPaths.get(cboSelProject.getSelectedIndex());
                CurProjectFileType = ProjectFileTypes.get(cboSelProject.getSelectedIndex());

                SearchCommand.ProjectPath = CurProjectPath;
                SearchCommand.ProjectFileType = CurProjectFileType;
            }
        });
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (clickedNext == false) {
                    clickedNext = true;
                    NavFrames.SelectFolderForm = new SelectFolderForm();
                    NavFrames.SelectFolderForm.loadFiles();



                    SearchCommand.FolderPath = SearchCommand.ProjectPath;
                    NavFrames.MainSearchForm = new MainSearchForm();
                    if (NavFrames.MainSearchFrame != null) {

                        NavFrames.MainSearchFrame.setVisible(true);

                    } else {
                        NavFrames.MainSearchFrame = new JFrame("Win Grep");
                        NavFrames.MainSearchFrame.setContentPane(NavFrames.MainSearchForm.panelMain);
                        NavFrames.MainSearchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        NavFrames.MainSearchFrame.pack();
                        NavFrames.MainSearchFrame.setVisible(true);
                        NavFrames.MainSearchFrame.setLocationRelativeTo(null);
                        NavFrames.MainSearchFrame.setLocation(SearchCommand.SearchFolderWidth - 15, 0);

                        Dimension dim2 = Toolkit.getDefaultToolkit().getScreenSize();
                        int useWidth = (int) Math.round(dim2.getWidth() * 0.75);



                        NavFrames.MainSearchFrame.setBounds(SearchCommand.SearchFolderWidth - 15, 0, useWidth + 25, 220);
                        SearchCommand.MainSearchX = NavFrames.MainSearchForm.lblTempBottom.getLocationOnScreen().x;
                        SearchCommand.MainSearchY = NavFrames.MainSearchForm.lblTempBottom.getLocationOnScreen().y;

                        NavFrames.MainSearchFrame.getContentPane().setBackground(Color.WHITE);
                        NavFrames.MainSearchForm.titlePanel.setBackground(Color.WHITE);
                        NavFrames.MainSearchForm.panel1.setBackground(Color.WHITE);
                        NavFrames.MainSearchForm.panel2.setBackground(Color.WHITE);
                        NavFrames.MainSearchForm.panel3.setBackground(Color.WHITE);
                        NavFrames.MainSearchForm.panel4.setBackground(Color.WHITE);
                        NavFrames.MainSearchForm.btnSearch.setBackground(new Color(50, 118, 177));
                        NavFrames.MainSearchForm.btnSearch.setForeground(Color.WHITE);
                        NavFrames.MainSearchForm.btnBackProject.setBackground(new Color(50, 118, 177));
                        NavFrames.MainSearchForm.btnBackProject.setForeground(Color.WHITE);
                    }








                    clickedNext = false;
                }
            }
        });
    }

    public static void main(String args[]) {
        NavFrames.SelectProjectForm = new SelectProjectForm();
        NavFrames.SelectProjectFrame = new JFrame("Win Grep");
        NavFrames.SelectProjectFrame.setContentPane(NavFrames.SelectProjectForm.panelMain);
        NavFrames.SelectProjectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        NavFrames.SelectProjectFrame.pack();
        NavFrames.SelectProjectFrame.setVisible(true);
        NavFrames.SelectProjectFrame.setLocationRelativeTo(null);

        NavFrames.SelectProjectForm.loadProjects();
        NavFrames.SelectProjectForm.CurProjectPath = NavFrames.SelectProjectForm.ProjectPaths.get(0);
        SearchCommand.ProjectPath = NavFrames.SelectProjectForm.CurProjectPath;

        NavFrames.SelectProjectFrame.getContentPane().setBackground(Color.WHITE);
        NavFrames.SelectProjectForm.titlePanel.setBackground(Color.WHITE);
        NavFrames.SelectProjectForm.btnNext.setBackground(new Color(50, 118, 177));
        NavFrames.SelectProjectForm.btnNext.setForeground(Color.WHITE);
    }

    void loadProjects() {

        List<String> ProjectNames = new ArrayList<String>();
        ProjectPaths = new ArrayList<>();

        HashMap<String, String> PathsHash = new HashMap<>();
        HashMap<String, String> FileTypesHash = new HashMap<>();
        List<String> NamesArr = new ArrayList<>();

        File file = new File("C:\\Temp\\vb_grep_dropdown_paths.txt");
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            for (String line; (line = br.readLine()) != null; ) {
                String[] lineArr = line.split("\\t");

                NamesArr.add(lineArr[1]);
                PathsHash.put(lineArr[1], lineArr[0]);
                FileTypesHash.put(lineArr[1], lineArr[2]);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Collections.sort(NamesArr);

        for (String getName: NamesArr) {
            ProjectPaths.add(PathsHash.get(getName));
            ProjectFileTypes.add(FileTypesHash.get(getName));
            ProjectNames.add(getName);
        }

        cboSelProject.setModel(new DefaultComboBoxModel(ProjectNames.toArray()));
    }
}
