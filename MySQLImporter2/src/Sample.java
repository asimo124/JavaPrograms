import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Serge
 * Date: 23.06.2004
 * Time: 0:59:06
 */
public class Sample extends JFrame {
    private JPanel panelMain2;
    private JTable      table;
    private JScrollPane scrollPane;

    public Sample(String title) throws HeadlessException {
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

//    Comment this code to add table dynamically
        table.setModel(dataModel);

//    Uncomment this code to add table dynamically:
//    JTable myTable = new JTable(dataModel);
//    myTable.setPreferredScrollableViewportSize(new Dimension(400, 100));
//    scrollPane.setViewportView(myTable);

        setContentPane(panelMain2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Sample("Sample");
    }
}
