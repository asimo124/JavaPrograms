import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * @class SearchResultsForm
 *
 * Datagrid that shows search results, first column
 * is filepath, second column is line that matched
 * regex search.
 */
public class SearchResultsForm extends JFrame {

    private JPanel panelMain;
    private JTable      table;
    private JScrollPane scpMain;

    private int extraTopPadding = 20;
    private int leftMargin = -27;

    public boolean clickedNext = false;
    public boolean ctrlPressed = false;
    public boolean fPressed = false;
    public long timeCtrlPressed = 0;

    /**
     * @param title
     * @throws HeadlessException
     */
    public SearchResultsForm(String title) throws HeadlessException {
        super(title);

        SearchResultsTableModel useModel = new SearchResultsTableModel();
        useModel.data = SearchCommand.completeDataModel;
        table.setModel(useModel);

        Dimension dim = new Dimension();
        dim.setSize(15, 7);
        table.setRowHeight(22);
        table.setIntercellSpacing(dim);

        setContentPane(panelMain);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        setLocation(SearchCommand.SearchFolderWidth - 15, SearchCommand.MainSearchY + SearchCommand.MainSearchHeight + this.extraTopPadding);
        setVisible(true);

        Dimension dim2 = Toolkit.getDefaultToolkit().getScreenSize();
        int useWidth = (int) Math.round(dim2.getWidth() * 0.75);

        setBounds(SearchCommand.SearchFolderWidth - 15, SearchCommand.MainSearchY + SearchCommand.MainSearchHeight + this.extraTopPadding, useWidth + 25, 820);

        panelMain.setBackground(Color.WHITE);
        scpMain.getViewport().setBackground(Color.WHITE);
        table.setBackground(Color.WHITE);

        KeyDispatcher ThisKeyDispatcher = new KeyDispatcher();
        KeyboardFocusManager ThisKbFocusMngr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        ThisKbFocusMngr.addKeyEventDispatcher(ThisKeyDispatcher);
        //return ThisKeyDispatcher;

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                JTable target = (JTable)e.getSource();
                int row = target.getSelectedRow();
                int column = target.getSelectedColumn();
                switch (column) {
                    case 0:
                        try {
                            Runtime.getRuntime().exec("explorer.exe /select," + SearchCommand.completeDataModelFull[row][1].toString());
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                        break;
                    case 2:
                        try {
                            //System.out.println("path: " + SearchCommand.completeDataModelFull[row][0].toString());
                            Runtime.getRuntime().exec("PhpStorm.exe " + SearchCommand.completeDataModelFull[row][0].toString());
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                        break;
                    case 3:
                        try {
                            Runtime.getRuntime().exec("start notepad++ " + SearchCommand.completeDataModelFull[row][0].toString());
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                        break;
                    default:

                }

            }
        });
    }

    /**
     * main function - if you want to run this as the starting piont of the application
     *
     * @param args
     */
    public static void main(String[] args) {
        //new SearchResultsForm("AssignFields");
    }
}
