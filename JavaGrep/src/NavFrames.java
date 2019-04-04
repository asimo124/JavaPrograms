import javax.swing.*;

/**
 * @class NavFrames
 *
 * class that holds instances of all JFrames,
 * so that user can navigate between them
 */
public class NavFrames {

    public static JFrame SelectProjectFrame = null;
    public static JFrame SelectFolderFrame = null;
    public static JFrame MainSearchFrame = null;
    public static JFrame SearchResultsFrame = null;

    public static SelectProjectForm SelectProjectForm = null;
    public static SelectFolderForm SelectFolderForm = null;
    public static MainSearchForm MainSearchForm = null;
    public static SearchResultsForm SearchResultsForm = null;

    public static boolean shouldRunSearch = false;

    public static void setSelectFolderVisible() {
        SelectProjectFrame.setVisible(false);
        if (MainSearchFrame != null) {
            MainSearchFrame.setVisible(false);
        }
        if (SearchResultsFrame != null) {
            SearchResultsFrame.setVisible(false);
        }
    }

    public static void makeAllVisible() {

        if (SearchResultsFrame != null) {
            SearchResultsFrame.setVisible(true);
        }
        if (SelectFolderForm != null) {
            SelectFolderForm.setVisible(true);
        }
        if (MainSearchFrame != null) {
            MainSearchFrame.setVisible(true);
            MainSearchForm.txtKeyword.requestFocusInWindow();
            MainSearchForm.txtKeyword.selectAll();
        }
    }

}
