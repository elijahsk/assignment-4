package com.company.task1;

import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class Task_1 {
    private String inFileName = "C:\\Users\\Elijah\\Documents\\CS3219\\extract.txt";
    private String outFileName = "C:\\Users\\Elijah\\Documents\\CS3219\\Assignment 4\\task 1.txt";
    private HashMap<String, String> authorIDToName;
    private HashMap<String, Integer> authorIDToCount;
    private ArrayList<Pair<String, Integer>> authorIDToCountList;

    public static void main(String[] args) {
        Task_1 temp = new Task_1();
        temp.run();
    }

    private void run() {
        readJson();
        process();
        writeFile();
    }

    private void process() {
        Set<String> authorIDs = authorIDToCount.keySet();
        authorIDToCountList = new ArrayList<Pair<String, Integer>>();
        for (String authorID : authorIDs) {
            authorIDToCountList.add(new Pair(authorID, authorIDToCount.get(authorID)));
        }
        Collections.sort(authorIDToCountList, (o1, o2) -> {
            if (o1.getValue() == o2.getValue()) {
                return 0;
            }
            else {
                return o1.getValue() > o2.getValue() ? -1 : 1;
            }
        });
    }


    private void readJson() {
        authorIDToCount = new HashMap<String, Integer>();
        authorIDToName = new HashMap<String, String>();
        JSONObject obj;
        String line = null;
        int count = 0;
        try {
            FileReader fileReader = new FileReader(inFileName);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                while ((line = bufferedReader.readLine()) != null) {
                    obj = (JSONObject) new JSONParser().parse(line);
                    if (obj.get("venue").toString().equals("ArXiv")) {
                        // System.out.println(obj.get("authors").toString());
                        ArrayList<JSONObject> authors = (ArrayList<JSONObject>) obj.get("authors");
                        for (JSONObject author : authors) {
                            if (((ArrayList<String>) author.get("ids")).size() > 0) {
                                String authorID = ((ArrayList<String>) author.get("ids")).get(0);
                                String authorName = author.get("name").toString();
                                // System.out.println(authorID + authorName);
                                if (authorIDToCount.containsKey(authorID)) {
                                    authorIDToCount.put(authorID, authorIDToCount.get(authorID) + 1);
                                } else {
                                    authorIDToCount.put(authorID, 1);
                                }
                                authorIDToName.put(authorID, authorName);
                            }
                        }
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
            bufferedWriter.write("author,count\n");

            for (int i = 0; i < 10; i++) {
                bufferedWriter.write(authorIDToName.get(authorIDToCountList.get(i).getKey()) + "," + authorIDToCountList.get(i).getValue().toString() + "\n");
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
