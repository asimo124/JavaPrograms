import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * @class AssignFields
 *
 * show table as JPanel
 */
public class AssignFields extends JFrame {
    private JPanel panelMain2;
    private JTable      table;
    private JScrollPane scrollPane;

    /**
     * constructor - show table as JPanel
     *
     * @param title
     * @throws HeadlessException
     */
    public AssignFields(String title) throws HeadlessException {
        super(title);

        TableModel dataModel = new
            AbstractTableModel() {
                public int getColumnCount() {
                    return 10;
                }
                public int getRowCount() {
                    return 10;
                }
                public Object getValueAt(int row, int col) {
                    return row * col;
                }
            };
        table.setModel(dataModel);
        setContentPane(panelMain2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    /**
     * main function - if you want to run this as the starting piont of the application
     *
     * @param args
     */
    public static void main(String[] args) {
        new AssignFields("AssignFields");
    }
}
