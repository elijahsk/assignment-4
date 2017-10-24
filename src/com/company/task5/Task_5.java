package com.company.task5;

import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class Task_5 {

    private String inFileName = "C:\\Users\\Elijah\\Documents\\CS3219\\extract.txt";
    private String outFileName = "C:\\Users\\Elijah\\Documents\\CS3219\\Assignment 4\\task 5.txt";
    private HashMap<String, Integer> venueToCount;

    public static void main(String[] args) {
        Task_5 temp = new Task_5();
        temp.run();
    }

    private void run() {
        readJson();
        System.out.println(venueToCount.keySet().size());
        writeFile();
    }

    private void readJson() {
        venueToCount = new HashMap<String, Integer>();

        JSONObject obj;
        String line = null;
        int count = 0;
        try {
            FileReader fileReader = new FileReader(inFileName);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                while ((line = bufferedReader.readLine()) != null) {
                    obj = (JSONObject) new JSONParser().parse(line);
                    String venue = obj.get("venue").toString();
                    if (venueToCount.containsKey(venue)) {
                        venueToCount.put(venue, venueToCount.get(venue) + 1);
                    }
                    else {
                        venueToCount.put(venue, 1);
                    }
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void writeFile() {
        try {
            FileWriter fileWriter = new FileWriter(outFileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("venue,count\n");

            Set<String> keys = venueToCount.keySet();
            int others = 0;
            int othersCount = 0;
            for (String key : keys) {
                if (venueToCount.get(key) < 500) {
                    others += venueToCount.get(key);
                    othersCount += 1;
                }
                else {
                    if (!key.equals("")) {
                        String abbrevKey = key;
                        if (key.length() > 25) {
                            abbrevKey = key.substring(0, 25) + "...";
                        }
                        bufferedWriter.write(abbrevKey + "," + venueToCount.get(key).toString() + "\n");
                    }
                }
            }
            //bufferedWriter.write("Others," + others + "\n");
            bufferedWriter.close();
            System.out.println(othersCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
