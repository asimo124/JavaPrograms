package info.alexhawley;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by AHawley on 10/16/2017.
 */
public class RecentFileChecker {

    public long fileLength = 0;

    public void checkPath(String dir) {

        String dir2 = dir.replace("\\", "/");

        File[] files = new File(dir2).listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                this.fileLength = 0;
                this.showFiles(files);
                String use_foldername = file.getName();
                this.checkPath(file.getAbsolutePath());
                //System.out.println(use_foldername + "\t" + file.);
            } else {
                String use_filename = file.getAbsoluteFile().toString().replace(Main.main_directory, "");
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");


                Date fileDate = new Date(file.lastModified());

                Date date = null;
                String input = "2012/01/20 12:05:10.321";
                DateFormat inputFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
                try {
                    date = inputFormatter.parse(input);
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }

                //MM/dd/yyyy
                DateFormat outputFormatter = new SimpleDateFormat("yyyy/MM/dd");
                String output = outputFormatter.format(date);




                boolean passedDateCheck = false;
                Date dateFrom = null;
                if (Main.days_from > 0) {
                    int days_from2 = Main.days_from * -1;
                    Calendar cal = Calendar.getInstance();

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Date date2 = new Date();
                    String date3 = dateFormat.format(date2);
                    Date date4 = null;
                    try {
                        date4 = dateFormat.parse(date3);
                    } catch (ParseException e) {
                        System.out.println(e.getMessage());
                    }
                    cal.setTime(date4);
                    cal.add(Calendar.DATE, days_from2);
                    dateFrom = cal.getTime();

                    if (fileDate.after(dateFrom) || fileDate == dateFrom) {
                        passedDateCheck = true;
                    }
                }






                System.out.println(use_filename + "\t" + sdf.format(file.lastModified()));
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
