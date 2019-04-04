import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @class SearchCommand
 *
 * General Class that holds all important
 * information as static variables to be used
 * across JFrames
 */
public class SearchCommand {

    public static String ProjectPath = "";
    public static String ProjectFileType = "PHPSymfony";
    public static String FolderPath = "";
    public static String SearchType = "Text in Files";
    public static String FileType = "ALL";
    public static Boolean OnlyFileNames = false;
    public static Boolean IsRegex = false;
    public static Boolean IsCaseSensitive = false;
    public static List<List<Object>> dataModel = new ArrayList<>();
    public static Object[][] completeDataModel = null;
    public static Object[][] completeDataModelFull = null;
    public static int MainSearchX = 0;
    public static int MainSearchY = 0;
    public static int MainSearchHeight = 0;
    public static int SearchFolderWidth = 325;
    public static int SearchFolderHeight = 1024;
    public static boolean searchIsClicked = false;

    public static String getProjectPath() {
        return ProjectPath;
    }

    public static String getProjectFileType() {
        return ProjectFileType;
    }

    public static String getFolderPath() {
        return FolderPath;
    }

    public static String getSearchType() {
        return SearchType;
    }

    public static String getFileType() {
        return FileType;
    }

    public static Boolean getOnlyFileNames() {
        return OnlyFileNames;
    }

    public static Boolean getIsRegex() {
        return IsRegex;
    }

    public static Boolean getIsCaseSensitive() {
        return IsCaseSensitive;
    }

    public static void convertDataModel() {

        completeDataModel = new Object[dataModel.size()][4];
        completeDataModelFull = new Object[dataModel.size()][2];
        int i = 0;
        for (List<Object> eachItem: dataModel) {
            completeDataModelFull[i][0] = eachItem.get(0);

            completeDataModelFull[i][1] = eachItem.get(0);
            completeDataModel[i][0] = eachItem.get(0).toString().replace(ProjectPath, "").replace("\\", "/") + ((eachItem.get(2).toString() == "0") ? "" :  ":" + eachItem.get(2).toString());
            completeDataModel[i][1] = eachItem.get(1);
            completeDataModel[i][2] = "PHPStorm";
            completeDataModel[i][3] = "Notepad++";
            i++;
        }
    }

    public static void printProps() {

        /*/
        public static String getProjectPath() {
    public static String getProjectFileType() {
    public static String getFolderPath() {
    public static String getSearchType() {
    public static String getFileType() {
    public static Boolean getOnlyFileNames() {
    public static Boolean getIsRegex() {
    public static Boolean getIsCaseSensitive() {
         */

        System.out.println("ProjectPath: " + getProjectPath());
        System.out.println("ProjectFileType: " + getProjectFileType());
        System.out.println("FolderPath: " + getFolderPath());
        System.out.println("SearchType: " + getSearchType());
        System.out.println("FileType: " + getFileType());
        System.out.println("OnlyFileNames: " + getOnlyFileNames());
        System.out.println("IsRegex: " + getIsRegex());
        System.out.println("IsCaseSensitive: " + getIsCaseSensitive());


    }

}