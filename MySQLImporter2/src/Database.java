/**
 * Created by AHawley on 3/28/2017.
 */
public class Database {

    public SourceTargetModel SourceTarget = new SourceTargetModel();
    public DatabaseType databaseType = DatabaseType.SOURCE;

    /**
     * constructor
     */
    public Database(SourceTargetModel GetSourceTarget, DatabaseType getDatabaseType) {

        this.SourceTarget = GetSourceTarget;
        databaseType = getDatabaseType;
    }

}
