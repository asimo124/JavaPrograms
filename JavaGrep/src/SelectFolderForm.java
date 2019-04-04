import javax.swing. *;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt. *;
import java.awt.event.KeyEvent;
import java.io.File;


/**
 * @class SelectFolderForm
 *
 * Directory Tree is populated for the selected
 * Project from SelectProjectForm, when user
 * selects a node, that is saved for later for
 * what directory to search on MainSearchForm
 */
public class SelectFolderForm extends JFrame {
    private JButton button1;
    public JTree tree;
    public JPanel mainPanel;
    private int extraTopPadding = 20;

    public boolean clickedNext = false;
    public boolean ctrlPressed = false;
    public boolean fPressed = false;
    public long timeCtrlPressed = 0;

    public SelectFolderForm() throws HeadlessException {

        super ("Project Directory");
    }

    public void loadFiles() {

        File dir = new File(SearchCommand.ProjectPath);
        tree = new JTree(addNodes(dir));
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setBorder(new LineBorder(new Color(0, 0, 0)));
        tree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);

        KeyDispatcher ThisKeyDispatcher = new KeyDispatcher();
        KeyboardFocusManager ThisKbFocusMngr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        ThisKbFocusMngr.addKeyEventDispatcher(ThisKeyDispatcher);
        //return ThisKeyDispatcher;

        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                treeValueChanged(e);
            }
        });
        tree.setCellRenderer(new FileTreeCellRenderer());
        JScrollPane treeView = new JScrollPane(tree);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int useWidth = (int)Math.round(dim.getWidth() * 0.25);
        SearchCommand.SearchFolderWidth = useWidth;

        treeView.setPreferredSize(new Dimension(useWidth, 768));

        setContentPane(treeView);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        setLocation(0, 0);

        setBounds(0, 0, SearchCommand.SearchFolderWidth, SearchCommand.SearchFolderHeight);

        setVisible(true);
    }

    public Dimension getMinimumSize() {
        return new Dimension(500, 768);
    }
    public Dimension getPreferredSize() {
        return new Dimension(500, 768);
    }

    private void treeValueChanged(TreeSelectionEvent e) {

        TreePath path = e.getPath();
        String paths = path.toString();
        String[] pathArr2 = paths.split(", ");
        int lastIndex = pathArr2.length - 1;
        String[] pathArr = pathArr2[lastIndex].split("]");
        String curFolderPath = pathArr[0];
        File newFile = new File(curFolderPath);

        if (newFile.isDirectory()) {

            SearchCommand.FolderPath = curFolderPath;
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
        }
    }

    public DefaultMutableTreeNode addNodes(File dir) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(dir);
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                node.add(addNodes(file));
            } else {
                node.add(new DefaultMutableTreeNode(file));
            }
        }
        return node;
    }

    public class FileTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if (value instanceof DefaultMutableTreeNode) {
                value = ((DefaultMutableTreeNode)value).getUserObject();
                if (value instanceof File) {
                    value = ((File) value).getName();
                }
            }
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }
    }
}