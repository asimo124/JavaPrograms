package com.example.plainolnotes;

import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * @class SQLOutput - **ANDROID** class to take SQLite cursor and output command line style datagrid results
 * @author alex hawley 2017.05.04
 */
public class SQLOutput {

    enum fieldTypes {
        STRING,
        INT,
        FLOAT,
        DATE,
    }

    /**
     * method that outputs the command line style datagrid results
     *
     * @param resultset - cursor from a SQLite raw query call
     * @param fields - array of column names
     * @param types - array of type enums with SQLite type
     * @param LogKey - key to use with Log.d
     */
    public static void showGridResults(Cursor resultset, String[] fields, fieldTypes[] types, String LogKey) {
        int i = 0;
        List<Integer> colSizes = new ArrayList<>();
        for (String getField: fields) {
            colSizes.add(getField.length());
            i++;
        }
        List<String> hashKeys = new ArrayList<>();
        List<HashMap<String, String>> resultList = new ArrayList<>();
        int k = 0;
        try {
            if (resultset.moveToFirst()) {
                do {
                    i = 0;
                    HashMap<String, String> rowList = new HashMap<String, String>();
                    for (fieldTypes getFieldType : types) {
                        String tempStr = "";
                        switch (getFieldType) {
                            case STRING:
                                tempStr = resultset.getString(resultset.getColumnIndex(fields[i]));
                                break;
                            case INT:
                                int tempInt = resultset.getInt(resultset.getColumnIndex(fields[i]));
                                tempStr = Integer.toString(tempInt);
                                break;
                            case FLOAT:
                                float tempFloat = resultset.getFloat(resultset.getColumnIndex(fields[i]));
                                tempStr = Float.toString(tempFloat);
                                break;
                            case DATE:
                                tempStr = resultset.getString(resultset.getColumnIndex(fields[i]));
                                break;
                        }
                        if (tempStr.length() > colSizes.get(i)) {
                            colSizes.set(i, tempStr.length());
                        }
                        if (k == 0) {
                            hashKeys.add(fields[i]);
                        }
                        rowList.put(fields[i], tempStr);
                        i++;
                    }
                    resultList.add(rowList);
                    k++;

                } while (resultset.moveToNext());
            }
        } finally {

        }
        i = 0;
        StringBuilder outputStr = new StringBuilder();
        outputStr.append("+");
        for (String getField: fields) {
            for (int j = 0; j < colSizes.get(i) + 2; j++) {
                outputStr.append("-");
            }
            if (i < (fields.length - 1)) {
                outputStr.append("-");
            } else {
                outputStr.append("+");
            }
            i++;
        }
        i = 0;
        outputStr.append("\n");
        outputStr.append("|");
        for (String getField: fields) {
            outputStr.append(" ");
            outputStr.append(getField);
            for (int j = getField.length(); j < colSizes.get(i); j++) {
                outputStr.append(" ");
            }
            outputStr.append(" ");
            outputStr.append("|");
            i++;
        }
        outputStr.append("\n");
        outputStr.append("+");
        i = 0;
        for (String getField: fields) {
            for (int j = 0; j < colSizes.get(i) + 2; j++) {
                outputStr.append("-");
            }
            if (i < (fields.length - 1)) {
                outputStr.append("-");
            } else {
                outputStr.append("+");
            }

            i++;
        }
        outputStr.append("\n");
        outputStr.append("|");
        try {
            if (resultset.moveToFirst()) {
                i = 0;
                do {
                    HashMap<String, String> rowList = resultList.get(i);
                    int t = 0;
                    for(String key: hashKeys) {
                        outputStr.append(" ");
                        outputStr.append(rowList.get(key));
                        for (int j = rowList.get(key).length(); j < colSizes.get(t); j++) {
                            outputStr.append(" ");
                        }
                        outputStr.append(" ");
                        outputStr.append("|");
                        t++;
                    }
                    outputStr.append("\n");

                    if (i < k - 1) {
                        outputStr.append("|");
                    }
                    i++;

                } while (resultset.moveToNext());
            }
        } finally {
            resultset.close();
        }
        outputStr.append("+");
        i = 0;
        for (String getField: fields) {
            for (int j = 0; j < colSizes.get(i) + 2; j++) {
                outputStr.append("-");
            }
            if (i < (fields.length - 1)) {
                outputStr.append("-");
            } else {
                outputStr.append("+");
            }
            i++;
        }
        String finalString = outputStr.toString();
        Log.d(LogKey, finalString);
    }
}
