package info.alexhawley.database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @class PrintUtilities
 *
 * functions to trace database objects,
 * such as recordsets, shown in table output
 * or arrays
 */
public class PrintUtilities {

    public PrintUtilities() {

    }

    /**
     * outputs a resultset as a printed array
     *
     * @param resultset
     */
    public void printRecordset(List<Map<String, String>> resultset) {

        int i = 0;
        for (Map<String, String>item: resultset) {
            System.out.println("[" + i + "] (");
            Iterator it = item.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println("\t" + "[" + pair.getKey() + "]" + " => " + pair.getValue());
            }
            System.out.println(")");
            i++;
        }
    }

    /**
     * outputs a resultset as a console printed table
     *
     * @param resultset
     */
    public void printTable(List<Map<String, String>> resultset) {
        List<Integer> fieldSizes = new ArrayList<>();
        List<String> headerColumns = new ArrayList<>();
        if (resultset.size() > 0) {
            Map<String, String> map = resultset.get(0);
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                String key = pair.getKey().toString();
                fieldSizes.add(key.length());
                headerColumns.add(key);
            }
        }
        int i = 0;
        for (Map<String, String>item: resultset) {
            Iterator it = item.entrySet().iterator();
            i = 0;
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                int value = pair.getValue().toString().length();
                if (value > fieldSizes.get(i)) {
                    fieldSizes.set(i, value);
                }
                i++;
            }
        }
        System.out.print("+");
        i = 0;
        for (int fieldSize: fieldSizes) {
            System.out.print("-");
            for (int j = 0; j < fieldSize; j++) {

                System.out.print("-");
            }
            System.out.print("-");
            if (i < fieldSizes.size() - 1) {
                System.out.print("-");
            } else {
                System.out.println("+");
            }
            i++;
        }
        System.out.print("|");
        i = 0;
        for (int fieldSize: fieldSizes) {
            System.out.print(" ");
            System.out.print(headerColumns.get(i));
            if (fieldSize > headerColumns.get(i).length()) {
                for (int j = headerColumns.get(i).length(); j < fieldSize; j++) {
                    System.out.print(" ");
                }
            }
            System.out.print(" ");
            if (i < fieldSizes.size() - 1) {
                System.out.print("|");
            } else {
                System.out.println("|");
            }
            i++;
        }
        System.out.print("|");
        i = 0;
        for (int fieldSize: fieldSizes) {
            System.out.print("-");
            for (int j = 0; j < fieldSize; j++) {

                System.out.print("-");
            }
            System.out.print("-");
            if (i < fieldSizes.size() - 1) {
                System.out.print("-");
            } else {
                System.out.println("|");
            }
            i++;
        }
		System.out.print("|");
        int t = 0;
        for (Map<String, String>item: resultset) {
            Iterator it = item.entrySet().iterator();
            i = 0;
            if (t > 0) {
                System.out.print("|");
            }
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.print(" ");
                System.out.print(pair.getValue().toString());
                if (fieldSizes.get(i) > pair.getValue().toString().length()) {
                    for (int j = pair.getValue().toString().length(); j < fieldSizes.get(i); j++) {
                        System.out.print(" ");
                    }
                }
                System.out.print(" ");
                if (i < fieldSizes.size() - 1) {
                    System.out.print("|");
                } else {
                    System.out.println("|");
                }
                i++;
            }
            t++;
        }
        System.out.print("+");
        i = 0;
        for (int fieldSize: fieldSizes) {
            System.out.print("-");
            for (int j = 0; j < fieldSize; j++) {

                System.out.print("-");
            }
            System.out.print("-");
            if (i < fieldSizes.size() - 1) {
                System.out.print("-");
            } else {
                System.out.println("+");
            }
            i++;
        }
    }
}
