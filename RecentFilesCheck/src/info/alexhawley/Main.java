package info.alexhawley;

public class Main {

    public static String main_directory = "";
    public static int days_from = 0;
    public static int days_to = 0;

    public static void main(String[] args) {

        RecentFileChecker checker = new RecentFileChecker();

        String dir2 = "";
        if (args.length == 0) {
            dir2 = "C:\\Users\\AHawley\\Documents\\";
        } else if (args.length > 0) {
            dir2 = "C:\\Users\\AHawley\\Documents\\" + args[0] + "\\";
        } else if (args.length > 1) {
            Main.days_from = Integer.parseInt(args[1]);
        } else if (args.length > 2) {
            Main.days_to = Integer.parseInt(args[2]);
        }
        main_directory = dir2;

        checker.checkPath(dir2);
    }
}
