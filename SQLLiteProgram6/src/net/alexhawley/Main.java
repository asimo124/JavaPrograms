package net.alexhawley;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            System.out.print("# ");
            Scanner scan = new Scanner(System.in);
            String input = scan.nextLine();
            if (input.equals("exit")) {
                break;
            }
            String[] command_args = input.split("\\s+");
            parseCommand(command_args);
        }
    }
    public static void parseCommand(String[] command_args) {
        SQLLiteCompany Company = new SQLLiteCompany();
        switch (command_args[0]) {
            case "show":
                Company.showRecords();
            break;
            case "insert":
                Company.insertRecordPrompt();
            break;
            case "delete":
                if (command_args.length < 2) {
                    System.out.println("Please enter an ID to delete");
                }
                else {
                    int id = Integer.parseInt(command_args[1]);
                    Company.deleteRecord(id);
                }
            break;
        }
    }
}

