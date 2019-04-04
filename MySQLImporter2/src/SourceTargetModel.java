import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 3/27/2017.
 */
public class SourceTargetModel {

    public boolean sourceIsSSHTunnel = false;
    public String sourceSSHHost = "";
    public String sourceSSHUsername = "";
    public String sourceSSHPassword = "";
    public String sourceSSHPort = "22";
    public String sourceDBHost = "";
    public String sourceDBUsername = "";
    public String sourceDBPassword = "";
    public String sourceDBName = "";
    public int sourceSSHPortNum = 22;

    public boolean targetIsSSHTunnel = false;
    public String targetSSHHost = "";
    public String targetSSHUsername = "";
    public String targetSSHPassword = "";
    public String targetSSHPort = "22";
    public String targetDBHost = "";
    public String targetDBUsername = "";
    public String targetDBPassword = "";
    public String targetDBName = "";
    public int targetSSHPortNum = 22;

    public List<String> SourceSelectedTables = new ArrayList<>();

    public boolean isSourceIsSSHTunnel() {
        return sourceIsSSHTunnel;
    }

    public void setSourceIsSSHTunnel(boolean sourceIsSSHTunnel) {
        this.sourceIsSSHTunnel = sourceIsSSHTunnel;
    }

    public String getSourceSSHHost() {
        return sourceSSHHost;
    }

    public void setSourceSSHHost(String sourceSSHHost) {
        this.sourceSSHHost = sourceSSHHost;
    }

    public String getSourceSSHUsername() {
        return sourceSSHUsername;
    }

    public void setSourceSSHUsername(String sourceSSHUsername) {
        this.sourceSSHUsername = sourceSSHUsername;
    }

    public String getSourceSSHPassword() {
        return sourceSSHPassword;
    }

    public void setSourceSSHPassword(String sourceSSHPassword) {
        this.sourceSSHPassword = sourceSSHPassword;
    }

    public String getSourceSSHPort() {
        return sourceSSHPort;
    }

    public void setSourceSSHPort(String sourceSSHPort) {
        if (sourceSSHPort.equals("")) {
            sourceSSHPort = "22";
            this.sourceSSHPortNum = 22;
        } else {
            this.sourceSSHPortNum = Integer.parseInt(sourceSSHPort);
        }
        this.sourceSSHPort = sourceSSHPort;
    }

    public String getSourceDBHost() {
        return sourceDBHost;
    }

    public void setSourceDBHost(String sourceDBHost) {
        this.sourceDBHost = sourceDBHost;
    }

    public String getSourceDBUsername() {
        return sourceDBUsername;
    }

    public void setSourceDBUsername(String sourceDBUsername) {
        this.sourceDBUsername = sourceDBUsername;
    }

    public String getSourceDBPassword() {
        return sourceDBPassword;
    }

    public void setSourceDBPassword(String sourceDBPassword) {
        this.sourceDBPassword = sourceDBPassword;
    }

    public String getSourceDBName() {
        return sourceDBName;
    }

    public void setSourceDBName(String sourceDBName) {
        this.sourceDBName = sourceDBName;
    }

    public boolean isTargetIsSSHTunnel() {
        return targetIsSSHTunnel;
    }

    public void setTargetIsSSHTunnel(boolean targetIsSSHTunnel) {
        this.targetIsSSHTunnel = targetIsSSHTunnel;
    }

    public String getTargetSSHHost() {
        return targetSSHHost;
    }

    public void setTargetSSHHost(String targetSSHHost) {
        this.targetSSHHost = targetSSHHost;
    }

    public String getTargetSSHUsername() {
        return targetSSHUsername;
    }

    public void setTargetSSHUsername(String targetSSHUsername) {
        this.targetSSHUsername = targetSSHUsername;
    }

    public String getTargetSSHPassword() {
        return targetSSHPassword;
    }

    public void setTargetSSHPassword(String targetSSHPassword) {
        this.targetSSHPassword = targetSSHPassword;
    }

    public String getTargetSSHPort() {
        if (targetSSHPort.equals("")) {
            targetSSHPort = "22";
            this.targetSSHPortNum = 22;
        } else {
            this.targetSSHPortNum = Integer.parseInt(targetSSHPort);
        }
        return targetSSHPort;
    }

    public void setTargetSSHPort(String targetSSHPort) {
        this.targetSSHPort = targetSSHPort;
    }

    public String getTargetDBHost() {
        return targetDBHost;
    }

    public void setTargetDBHost(String targetDBHost) {
        this.targetDBHost = targetDBHost;
    }

    public String getTargetDBUsername() {
        return targetDBUsername;
    }

    public void setTargetDBUsername(String targetDBUsername) {
        this.targetDBUsername = targetDBUsername;
    }

    public String getTargetDBPassword() {
        return targetDBPassword;
    }

    public void setTargetDBPassword(String targetDBPassword) {
        this.targetDBPassword = targetDBPassword;
    }

    public String getTargetDBName() {
        return targetDBName;
    }

    public void setTargetDBName(String targetDBName) {
        this.targetDBName = targetDBName;
    }

    public int getSourceSSHPortNum() {
        return sourceSSHPortNum;
    }

    public int getTargetSSHPortNum() {
        return targetSSHPortNum;
    }

    public List<String> getSourceSelectedTables() {
        return SourceSelectedTables;
    }

    public void setSourceSelectedTables(List<String> sourceSelectedTables) {
        SourceSelectedTables = sourceSelectedTables;
    }

    public SourceTargetModel() {

    }

    /**
     * Set Target DB Connection exactly the same as Source DB Connection
     */
    public void setTargetSameAsSource() {

        this.setTargetIsSSHTunnel(this.isSourceIsSSHTunnel());
        this.setTargetSSHHost(this.getSourceSSHHost());
        this.setTargetSSHUsername(this.getSourceSSHUsername());
        this.setTargetSSHPassword(this.getSourceSSHPassword());
        this.setTargetSSHPort(this.getSourceSSHPort());
        this.setTargetDBHost(this.getSourceDBHost());
        this.setTargetDBUsername(this.getSourceDBUsername());
        this.setTargetDBPassword(this.getSourceDBPassword());
        this.setTargetDBName(this.getSourceDBName());
    }
}