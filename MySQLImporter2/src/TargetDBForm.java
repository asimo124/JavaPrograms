import javax.swing.*;

/**
 * Created by AHawley on 3/24/2017.
 */
public class TargetDBForm {
    public JPanel panelMain;
    public JComboBox cboSelTargetDB;
    public JButton btnNext;

    public TargetDBForm() {








    }

    /**
     * function to fill source databases list
     * after entering source connection info
     */
    public void fillDatabasesDropdown(DatabaseSSH importer) {


        /*/
        List<String> dbList = importer.getDatabasesList(importer);
        String[] dbs = dbList.toArray(new String[dbList.size()]);
        cboSelTargetDB.setEnabled(true);
        cboSelTargetDB.setModel(new DefaultComboBoxModel(dbs));
        if (text_SourceDatabase.isEnabled() && text_SourceDatabase.getText() != "") {
            int i = 0;
            for (String db : dbList) {
                if (db.equals(text_SourceDatabase.getText().toString().trim())) {
                    cboSelSourceDB.setSelectedIndex(i);
                }
                i++;
            }
        }
        text_SourceDatabase.setEnabled(false);
        //*/
    }

}
