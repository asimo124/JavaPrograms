import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @class StartForm
 *
 * application should be run with this class
 *
 * show starting panel - select connection type
 */
public class StartForm {

    public JPanel panelMain;
    public JComboBox cboSourceType;
    public JButton btnNext;

    public StartForm() {

        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Set Source Type
                String sourceType = cboSourceType.getSelectedItem().toString();

                if (sourceType == "Text Files") {

                } else {

                    JFrame frame = new JFrame("Set Source Connection");
                    frame.setContentPane(new SourceConnForm().panelMain);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);

                }

            }
        });
    }

    public static void main(String[] args) {

        StartForm start = new StartForm();
        JFrame frame = new JFrame("MySQL Database");
        frame.setContentPane(start.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        List<String> SourceTypes = new ArrayList<String>();
        SourceTypes.add("Text Files");
        SourceTypes.add("Database Tables");

        start.cboSourceType.setModel(new DefaultComboBoxModel(SourceTypes.toArray()));
    }
}
