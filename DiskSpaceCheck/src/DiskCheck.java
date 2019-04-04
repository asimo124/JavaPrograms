import java.io.File;

/**
 * Created by dev5 on 7/25/2016.
 */
public class DiskCheck {

    public long fileLength = 0;

    public static void main(String[] args) {

        DiskCheck checker = new DiskCheck();

        String dir2 = "";
        if (args.length == 0) {
            dir2 = "C:\\Users\\AHawley\\Documents\\Claimatics2.0\\web";
        } else {
            dir2 = args[0];
        }
        System.out.println("dir2: " + dir2);
        checker.checkPath(dir2);
    }

    public void checkPath(String dir) {

        String dir2 = dir.replace("\\", "/");

        DiskCheck checker = new DiskCheck();

        File[] files = new File(dir2).listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                checker.fileLength = 0;
                checker.showFiles(files);
                String use_foldername = file.getName();
                System.out.println(use_foldername + "\t" + checker.fileLength + " bytes");
            } else {
                String use_filename = file.getName();
                System.out.println(use_filename + "\t" + file.length() + " bytes");
            }
        }
    }
    public void showFiles(File[] files) {

        for (File file : files) {
            if (file.isDirectory()) {
                //System.out.println("Directory: " + file.getName());
                this.showFiles(file.listFiles()); // Calls same method again.
            } else {
                String use_filename = file.getName();
                this.fileLength += file.length();
            }
        }
    }
}
