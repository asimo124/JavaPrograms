import java.util.*;

public class SearchCommand {

    // Fields used to query Log Search Results from Search Log Form
    public static String site;
    public static String project;
    public static String fileName;
    public static String changeDesc;
    public static String fromDate;
    public static String toDate;
    public static String orderBy = "date";
    public static String dir = "ASC";

    // Objects that hold Log Search Results Data for DataGrid
    public static Object[][] completeModelData;
    public static List<Integer> modelDataIds;

    public static Object[][] completeModelFilesData;
    public static List<Integer> modelDataFilesIds;

    // Pass ID to Load Prod Log by ID
    public static int currentId = 0;

    // whether to re-import db
    public static boolean ranImportSQL = false;

    // Arrays to fill dropdowns
    public static List<String> SiteNamesArr;
    public static List<String> SiteNamesFormArr;
    public static List<String> ProjectsArr;
    public static List<String> ProjectsFormArr;
    public static List<String> CategoriesArr;
    public static List<String> CategoriesFormArr;
    public static List<String> OrderBys;
    public static List<String> Dirs;

    /**
     * Convert SQLite Resultset to Multi-Dimensional Array for TableModel
     *
     * @param dataModel
     */
    public static void convertDataModel(List<StringHashMap> dataModel) {

        Object[][] completeDataModel = new Object[dataModel.size()][5];
        int i = 0;
        modelDataIds = new ArrayList();
        for (Map<String, String> item: dataModel) {
            int j = 0;
            int k = -1;
            Iterator it = item.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                //System.out.println(" t" + "[" + pair.getKey() + "]" + " => " + pair.getValue());
                if (k == -1) {
                    modelDataIds.add(Integer.parseInt(pair.getValue().toString()));
                } else {
                    completeDataModel[i][k] = pair.getValue();
                }
                k++;
                j++;
            }
            completeDataModel[i][3] = "Edit";
            completeDataModel[i][4] = "Delete";
            i++;
        }
        completeModelData = completeDataModel;
    }

    /**
     * Convert SQLite Resultset to Multi-Dimensional Array for TableModel
     *
     * @param dataModel
     */
    public static void convertDataModelFiles(List<StringHashMap> dataModel) {

        Object[][] completeDataModel = new Object[dataModel.size()][1];
        int i = 0;
        modelDataFilesIds = new ArrayList();
        for (Map<String, String> item: dataModel) {
            int j = 0;
            int k = -1;
            Iterator it = item.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                //System.out.println(" t" + "[" + pair.getKey() + "]" + " => " + pair.getValue());
                //if (k == -1) {

                    //modelDataFilesIds.add(Integer.parseInt(pair.getValue().toString()));
                //} else {
                    completeDataModel[i][j] = pair.getValue();
                //}
                k++;
                j++;
            }
            i++;
        }
        completeModelFilesData = completeDataModel;
    }
}
