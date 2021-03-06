package net.alexhawley;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        public static void main(String[] args) {

            while (true) {
                System.out.print("# ");
                Scanner scan = new Scanner(System.in);
                String input = scan.nextLine();
                if (input.equals("exit")) {
                    break;
                }
                parseCommand(input);
            }
        }

        public static void parseCommand(String command) {

            net.alexhawley.SQLLiteCompany Company = new net.alexhawley.SQLLiteCompany();
            switch (command) {
                case "show":
                    Company.showRecords();
                    break;
                case "insert":
                    Company.insertRecordPrompt();
                    break;
            }
        }
    }
}
