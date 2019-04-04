import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by AHawley on 3/28/2017.
 */
public class TargetFilesTypeForm {
    private JComboBox cboSourceFileTypes;
    private JButton btnNext;
    public JPanel panelMain;

    public SourceTargetModel SourceTarget = new SourceTargetModel();

    public TargetFilesTypeForm(SourceTargetModel GetSourceTarget) {

        SourceTarget = GetSourceTarget;

        String[] fileTypes = {"CSV File", "Tab Delimited File", "Other Delimited File"};
        cboSourceFileTypes.setModel(new DefaultComboBoxModel(fileTypes));


        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new AssignFields("Assign Fields");
            }
        });
    }
}
