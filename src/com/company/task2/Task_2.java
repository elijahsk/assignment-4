package com.company.task2;

import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class Task_2 {
    private String inFileName = "C:\\Users\\Elijah\\Documents\\CS3219\\extract.txt";
    private String outFileName = "C:\\Users\\Elijah\\Documents\\CS3219\\Assignment 4\\task 2.txt";
    private HashMap<String, String> paperIDToPaperNameMap;
    private ArrayList<Pair<String, Integer>> paperIDToCitationNumList;

    public static void main(String[] args) {
        Task_2 temp = new Task_2();
        temp.run();
    }

    private void run() {
        readJson();
        process();
        writeFile();
    }

    private void process() {
        Collections.sort(paperIDToCitationNumList, (o1, o2) -> {
            if (o1.getValue() == o2.getValue()) {
                return 0;
            }
            else {
                return o1.getValue() > o2.getValue() ? -1 : 1;
            }
        });
    }


    private void readJson() {
        paperIDToPaperNameMap = new HashMap<String, String>();
        paperIDToCitationNumList = new ArrayList<Pair<String, Integer>>();
        JSONObject obj;
        String line = null;
        int count = 0;
        try {
            FileReader fileReader = new FileReader(inFileName);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                while ((line = bufferedReader.readLine()) != null) {
                    obj = (JSONObject) new JSONParser().parse(line);
                    if (obj.get("venue").toString().equals("ArXiv")) {
                        Integer citationNum = ((ArrayList<JSONObject>) obj.get("inCitations")).size();
                        String paperID = obj.get("id").toString();
                        String paperName = obj.get("title").toString();
                        paperIDToCitationNumList.add(new Pair(paperID, citationNum));
                        paperIDToPaperNameMap.put(paperID, paperName);
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
            bufferedWriter.write("paper,citation\n");

            for (int i = 0; i < 5; i++) {
                bufferedWriter.write(paperIDToPaperNameMap.get(paperIDToCitationNumList.get(i).getKey()) + "," + paperIDToCitationNumList.get(i).getValue().toString() + "\n");
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
