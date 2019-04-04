
import oracle.jrockit.jfr.JFR;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @class NavFrames
 *
 * class that holds instances of all JFrames,
 * so that user can navigate between them
 */
public class NavFrames {

    public static JFrame SearchLogFrame = null;
    public static JFrame AddLogFrame = null;
    public static JFrame EditLogFrame = null;
    public static JFrame SearchResultsFrame = null;
    public static JFrame FileResultsFrame = null;

    public static SearchLogForm SearchLogForm = null;
    public static AddLogForm AddLogForm = null;
    public static EditLogForm EditLogForm = null;
    public static SearchResultsForm SearchResultsForm = null;
    public static FileResultsForm FileResultsForm = null;

}