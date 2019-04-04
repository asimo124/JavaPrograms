import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyDispatcher implements KeyEventDispatcher {

    public boolean clickedNext = false;
    public boolean ctrlPressed = false;
    public boolean fPressed = false;
    public boolean jPressed = false;
    public long timeCtrlPressed = 0;
    public long timeFirstJPressed = 0;
    public long runSearchRan = 0;

    public boolean dispatchKeyEvent(final KeyEvent ThatEvent) {

        KeyboardFocusManager ThisKbFocusMngr    = null;
        Component            ThisComponent      = null;
        Container            ThisRoot           = null;
        Window               ThisWindow         = null;
        int                  ThisKeyStateEvent  = 0;

        try {
            ThisKbFocusMngr       = KeyboardFocusManager  . getCurrentKeyboardFocusManager();
            ThisComponent         = ThisKbFocusMngr       . getFocusOwner();
            ThisRoot              = ThisKbFocusMngr       . getCurrentFocusCycleRoot();
            ThisWindow            = ThisKbFocusMngr       . getActiveWindow();
            ThisKeyStateEvent     = ThatEvent.getID();   // i.ThatEvent.  KeyEvent.KEY_RELEASED

            if (!ThatEvent.isConsumed()) {
                boolean       RetBool          = false;
                if (ThatEvent.getID() == KeyEvent.KEY_PRESSED && ThatEvent.getKeyCode() == 17) {
                    ctrlPressed = true;
                    timeCtrlPressed = System.currentTimeMillis();
                    RetBool = true;
                }
                if (ThatEvent.getID() == KeyEvent.KEY_PRESSED && (ThatEvent.getKeyCode() == 76 || ThatEvent.getKeyCode() == 70)) {  // if CTRL + F or CTRL + L
                    fPressed = true;
                    long timeNow = System.currentTimeMillis();
                    long timeSinceCtrl = timeNow - timeCtrlPressed;
                    if (timeSinceCtrl < 350) {
                        NavFrames.makeAllVisible();
                        ctrlPressed = false;
                        fPressed = false;
                    } else {
                        ctrlPressed = false;
                        fPressed = false;
                    }
                    RetBool = true;
                }

                if (ThatEvent.getID() == KeyEvent.KEY_PRESSED && ThatEvent.getKeyCode() == 80)  {  // if CTRL + P
                    fPressed = true;
                    long timeNow = System.currentTimeMillis();
                    long timeSinceCtrl = timeNow - timeCtrlPressed;
                    if (timeSinceCtrl < 350) {
                        if (SearchCommand.ProjectFileType.equals("PHPSymfony") || SearchCommand.ProjectFileType.equals("PHPLegacy")) {
                            NavFrames.MainSearchForm.changeFileType("PHP");
                        } else {
                            NavFrames.MainSearchForm.changeFileType("PY");
                        }
                        NavFrames.makeAllVisible();
                        ctrlPressed = false;
                        fPressed = false;
                    } else {
                        ctrlPressed = false;
                        fPressed = false;
                    }
                    RetBool = true;
                }

                if (ThatEvent.getID() == KeyEvent.KEY_PRESSED && ThatEvent.getKeyCode() == 74)  {  // if CTRL + J

                    long timeNow = System.currentTimeMillis();
                    long timeSinceCtrl = timeNow - timeCtrlPressed;
                    if (timeSinceCtrl < 350) {

                        jPressed = true;
                        if (SearchCommand.ProjectFileType.equals("PHPSymfony") || SearchCommand.ProjectFileType.equals("PHPLegacy")) {
                            NavFrames.MainSearchForm.changeFileType("JS");
                        } else {
                            NavFrames.MainSearchForm.changeFileType("JAVA");
                        }
                        NavFrames.makeAllVisible();
                        ctrlPressed = false;
                        timeFirstJPressed = System.currentTimeMillis();

                    } else if (jPressed == true) {

                        long timeNow2 = System.currentTimeMillis();
                        long timeSinceJPressed = timeNow2 - timeFirstJPressed;
                        if (timeSinceJPressed < 450) {  // if CTRL + J + J

                            NavFrames.MainSearchForm.changeFileType("JSON");
                            NavFrames.makeAllVisible();
                            ctrlPressed = false;
                            jPressed = false;
                        }
                    }
                    RetBool = true;
                }

                if (ThatEvent.getID() == KeyEvent.KEY_PRESSED && ThatEvent.getKeyCode() == 67)  {  // if CTRL + C
                    fPressed = true;
                    long timeNow = System.currentTimeMillis();
                    long timeSinceCtrl = timeNow - timeCtrlPressed;
                    if (timeSinceCtrl < 350) {
                        NavFrames.MainSearchForm.changeFileType("CSS");
                        NavFrames.makeAllVisible();
                        ctrlPressed = false;
                        fPressed = false;
                    } else {
                        ctrlPressed = false;
                        fPressed = false;
                    }
                    RetBool = true;
                }

                if (ThatEvent.getID() == KeyEvent.KEY_PRESSED && ThatEvent.getKeyCode() == 72)  {  // if CTRL + H
                    fPressed = true;
                    long timeNow = System.currentTimeMillis();
                    long timeSinceCtrl = timeNow - timeCtrlPressed;
                    if (timeSinceCtrl < 350) {
                        NavFrames.MainSearchForm.changeFileType("HTM");
                        NavFrames.makeAllVisible();
                        ctrlPressed = false;
                        fPressed = false;
                    } else {
                        ctrlPressed = false;
                        fPressed = false;
                    }
                    RetBool = true;
                }

                if (ThatEvent.getID() == KeyEvent.KEY_PRESSED && ThatEvent.getKeyCode() == 84)  {  // if CTRL + T
                    fPressed = true;
                    long timeNow = System.currentTimeMillis();
                    long timeSinceCtrl = timeNow - timeCtrlPressed;
                    if (timeSinceCtrl < 350) {

                        if (SearchCommand.ProjectFileType.equals("PHPSymfony")) {
                            NavFrames.MainSearchForm.changeFileType("TWIG");
                        } else if (SearchCommand.ProjectFileType.equals("PHPLegacy")) {
                            NavFrames.MainSearchForm.changeFileType("TPL");
                        }

                        NavFrames.makeAllVisible();
                        ctrlPressed = false;
                        fPressed = false;
                    } else {
                        ctrlPressed = false;
                        fPressed = false;
                    }
                    RetBool = true;
                }

                if (ThatEvent.getID() == KeyEvent.KEY_PRESSED && ThatEvent.getKeyCode() == 83)  {  // if CTRL + S
                    fPressed = true;
                    long timeNow = System.currentTimeMillis();
                    long timeSinceCtrl = timeNow - timeCtrlPressed;
                    if (timeSinceCtrl < 350) {
                        NavFrames.MainSearchForm.changeFileType("SQL");
                        NavFrames.makeAllVisible();
                        ctrlPressed = false;
                        fPressed = false;
                    } else {
                        ctrlPressed = false;
                        fPressed = false;
                    }
                    RetBool = true;
                }

                if (ThatEvent.getID() == KeyEvent.KEY_PRESSED && ThatEvent.getKeyCode() == 88)  {  // if CTRL + X
                    fPressed = true;
                    long timeNow = System.currentTimeMillis();
                    long timeSinceCtrl = timeNow - timeCtrlPressed;
                    if (timeSinceCtrl < 350) {
                        NavFrames.MainSearchForm.changeFileType("XML");
                        NavFrames.makeAllVisible();
                        ctrlPressed = false;
                        fPressed = false;
                    } else {
                        ctrlPressed = false;
                        fPressed = false;
                    }
                    RetBool = true;
                }

                if (ThatEvent.getID() == KeyEvent.KEY_PRESSED && ThatEvent.getKeyCode() == 10) {
                    NavFrames.MainSearchForm.runSearch();
                }
                if (RetBool) {
                    ThatEvent.consume();
                    return true;
                }
            }
        }
        catch( Throwable e ) {
            //LogThis(". ", e);
        }
        return false;
    }
}