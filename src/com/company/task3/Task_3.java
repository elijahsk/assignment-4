package com.company.task3;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class Task_3 {
    private String inFileName = "C:\\Users\\Elijah\\Documents\\CS3219\\extract.txt";
    private String outFileName = "C:\\Users\\Elijah\\Documents\\CS3219\\Assignment 4\\task 3.txt";
    private static final int MAX_YEAR_BOUND = 9999999;
    private static final int MIN_YEAR_BOUND = 0;
    private HashMap<String, Integer> idToYearMap;
    private HashMap<Integer, Integer> yearToCountMap;
    private int minYear;
    private int maxYear;

    public static void main(String[] args) {
        Task_3 temp = new Task_3();
        temp.run();
    }

    private void run() {
        readJson();
        process();
        writeFile();
    }

    private void process() {
        yearToCountMap = new HashMap<Integer, Integer>();
        minYear = MAX_YEAR_BOUND;
        maxYear = MIN_YEAR_BOUND;
        Set entrySet = idToYearMap.entrySet();
        Iterator it = entrySet.iterator();
        while(it.hasNext()){
            Map.Entry me = (Map.Entry)it.next();
            int year = (int) me.getValue();
            minYear = year < minYear ? year : minYear;
            maxYear = year > maxYear ? year : maxYear;
            if (yearToCountMap.containsKey(year)) {
                yearToCountMap.put(year, yearToCountMap.get(year) + 1);
            }
            else {
                yearToCountMap.put(year, 1);
            }
        }
        for (int i = minYear; i < maxYear; i++) {
            if (!yearToCountMap.containsKey(i)) {
                yearToCountMap.put(i, 0);
            }
        }
    }


    private HashMap<String, Integer> readJson() {
        idToYearMap = new HashMap<String, Integer>();
        JSONObject obj;
        String line = null;
        int count = 0;
        try {
            FileReader fileReader = new FileReader(inFileName);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                while ((line = bufferedReader.readLine()) != null) {
                    obj = (JSONObject) new JSONParser().parse(line);
                    if (obj.get("venue").toString().equals("ICSE")) {
                        idToYearMap.put(obj.get("id").toString(), Integer.parseInt(obj.get("year").toString()));
                    }
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
        return idToYearMap;
    }

    private void writeFile() {
        try {
            FileWriter fileWriter = new FileWriter(outFileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("year count\n");

            for (int i = minYear; i <= maxYear; i++) {
                bufferedWriter.write(i + " " + yearToCountMap.get(i) + "\n");
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
