
import info.alexhawley.ConfigLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by AHawley on 3/24/2017.
 */
public class TargetConnForm {

    public SourceTargetModel SourceTarget = new SourceTargetModel();
    public DatabaseSSH database = new DatabaseSSH(new SourceTargetModel(), DatabaseType.TARGET);

    public JPanel panelMain;
    public JTextField text_TargetHost;
    public JTextField text_TargetUsername;
    public JTextField text_TargetPassword;
    public JTextField text_TargetSSHHost;
    public JTextField text_TargetSSHUsername;
    public JTextField text_TargetSSHPassword;
    public JTextField text_TargetSSHPort;
    public JTextField text_TargetDatabase;
    public JCheckBox chkSSHTunnelCheckBox;
    public JComboBox cboSelTargetDB;
    public JButton btnCreateTarget;
    public JButton btnNext;

    public boolean isConnected = false;
    public String source_db = "";

    public boolean sourceConnected = false;
    public Connection conn = null;

    public DatabaseSSH database_ssh = new DatabaseSSH(new SourceTargetModel(), DatabaseType.TARGET);
    public DatabaseLocal database_loc = new DatabaseLocal(new SourceTargetModel(), DatabaseType.TARGET);

    public TargetConnForm(SourceTargetModel GetSourceTarget) {
        SourceTarget = GetSourceTarget;
        /**
         * Connect Button
         */
        btnCreateTarget.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String retVal = "";
                // Use Config Loader to bypass test inputting
                int debug = 1;
                if (debug == 1) {
                    loadConfigSettings();
                }
                if (text_TargetDatabase.isEnabled()) {
                    source_db = text_TargetDatabase.getText();
                } else if (cboSelTargetDB.isEnabled()) {
                    source_db = cboSelTargetDB.getSelectedItem().toString();
                }
                if (chkSSHTunnelCheckBox.isSelected()) {  // if SSH Tunneling
                    if (text_TargetSSHPort.getText().trim() == "") {
                        text_TargetSSHPort.setText("22");
                    }
                    setTargetModel();
                    database_ssh = new DatabaseSSH(SourceTarget, DatabaseType.TARGET);
                    retVal = database_ssh.setConnect();
                    if (!retVal.equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(null, "Could not connect: " + retVal);
                    } else {  // if connection is success
                        JOptionPane.showMessageDialog(null, "Successfully Connected");
                        fillDatabasesDropdown();
                        btnNext.setEnabled(true);
                    }
                } else {  // if straight MySQL Connection
                    setTargetModel();
                    text_TargetSSHPort.setText("");
                    database_loc = new DatabaseLocal(SourceTarget, DatabaseType.TARGET);
                    conn = database_loc.setConn();
                    isConnected = true;
                    JOptionPane.showMessageDialog(null, "Successfully Connected");
                    fillDatabasesDropdown();
                    btnNext.setEnabled(true);
                    try {
                        conn.close();
                    } catch (SQLException ex4) {
                        System.out.println(ex4.getMessage());
                    }
                }
            }
        });

        /**
         * SSH Tunnel Checkbox - change
         */
        chkSSHTunnelCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton absB = (AbstractButton) e.getSource();
                ButtonModel bMod = absB.getModel();
                boolean selected = bMod.isSelected();
                if (!selected) {
                    text_TargetSSHHost.setEnabled(false);
                    text_TargetSSHUsername.setEnabled(false);
                    text_TargetSSHPassword.setEnabled(false);
                    text_TargetSSHPort.setEnabled(false);
                } else {
                    text_TargetSSHHost.setEnabled(true);
                    text_TargetSSHUsername.setEnabled(true);
                    text_TargetSSHPassword.setEnabled(true);
                    text_TargetSSHPort.setEnabled(true);
                }
            }
        });

        /**
         * Target Databases Dropdown - change
         */
        cboSelTargetDB.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                source_db = cboSelTargetDB.getSelectedItem().toString();
            }
        });

        /**
         * Move to next Frame (Source Tables)
         */
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save Source Settings into Model Class which save source/target settings, and is passed between JFrames
                setTargetModel();
                SourceTablesForm targetConn = new SourceTablesForm(SourceTarget);
                JFrame frame = new JFrame("Select Source Table(s)");
                frame.setContentPane(targetConn.panelMain);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
    }

    /**
     * function to fill source databases list
     * after entering source connection info
     */
    public void fillDatabasesDropdown() {
        List<Map<String, String>> retVal = new ArrayList<>();
        List<String> dbList = new ArrayList<>();
        if (chkSSHTunnelCheckBox.isSelected()) {  // if SSH Tunnelling
            try {
                retVal = database.getQuery("SHOW DATABASES");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            for (Map<String, String> item : retVal) {
                Iterator it = item.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    dbList.add(pair.getValue().toString());
                }
            }
        } else {   // if Direct MySQL Connection
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SHOW DATABASES");
                while (rs.next()) {
                    String Database = rs.getString("Database");
                    dbList.add(Database);
                }
                st.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        String[] dbs = dbList.toArray(new String[dbList.size()]);
        cboSelTargetDB.setEnabled(true);
        cboSelTargetDB.setModel(new DefaultComboBoxModel(dbs));
        if (text_TargetDatabase.isEnabled() && text_TargetDatabase.getText() != "") {
            int i = 0;
            for (String db : dbList) {
                if (db.equals(text_TargetDatabase.getText().toString().trim())) {
                    cboSelTargetDB.setSelectedIndex(i);
                }
                i++;
            }
        }
        text_TargetDatabase.setEnabled(false);
    }

    /**
     * function to set SourceTarget object to load db connections
     */
    public void setTargetModel() {
        SourceTarget.setTargetIsSSHTunnel(chkSSHTunnelCheckBox.isSelected());
        if (chkSSHTunnelCheckBox.isSelected()) {
            SourceTarget.setSourceSSHHost(text_TargetSSHHost.getText().toString());
            SourceTarget.setSourceSSHUsername(text_TargetSSHUsername.getText().toString());
            SourceTarget.setSourceSSHPassword(text_TargetSSHPassword.getText().toString());
            SourceTarget.setSourceSSHPort(text_TargetSSHPort.getText().toString());
        }
        SourceTarget.setSourceDBHost(text_TargetHost.getText().toString());
        SourceTarget.setSourceDBUsername(text_TargetUsername.getText().toString());
        SourceTarget.setSourceDBPassword(text_TargetPassword.getText().toString());
        SourceTarget.setSourceDBName(text_TargetDatabase.getText().toString());
    }

    /**
     * load settings to avoid test input
     */
    public void loadConfigSettings() {
        ConfigLoader config = new ConfigLoader();
        text_TargetSSHHost.setText(config.getSsh_host());
        text_TargetSSHUsername.setText(config.getSsh_username());
        text_TargetSSHPassword.setText(config.getSsh_password());
        text_TargetSSHPort.setText(config.getSsh_port());
        text_TargetHost.setText(config.getDb_host());
        text_TargetUsername.setText(config.getDb_username());
        text_TargetPassword.setText(config.getDb_password());
        text_TargetDatabase.setText(config.getDb_name());
    }
}