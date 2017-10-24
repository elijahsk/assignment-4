package com.company.task4;

import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class Task_4 {
    private String initialID = "36adf8c327b95bdffe2778bf022e0234d433454a";
    private String inFileName = "C:\\Users\\Elijah\\Documents\\CS3219\\extract.txt";
    private String outFileName = "C:\\Users\\Elijah\\Documents\\CS3219\\Assignment 4\\task 4.txt";
    private HashMap<String, Pair<Object, String>> idToCitationMap;
    private HashMap<String, Integer> nodesToSeq;
    private HashMap<Integer, String> seqToNodes;
    private ArrayList<Pair<Integer, Integer>> edges;
    private int nodesCount;

    public static void main(String[] args) {
        Task_4 temp = new Task_4();
        temp.run();
    }

    public void run() {
        readJson();
        process();
        writeFile();
    }

    private void process() {
        edges = new ArrayList<Pair<Integer, Integer>>();
        nodesToSeq = new HashMap<String, Integer>();
        seqToNodes = new HashMap<Integer, String>();
        LinkedList<String> queue = new LinkedList<String>();
        LinkedList<Integer> tierQueue = new LinkedList<Integer>();
        nodesCount = 0;

        queue.add(initialID);
        nodesToSeq.put(initialID, nodesCount);
        seqToNodes.put(nodesCount, initialID);
        tierQueue.add(0);

        while (queue.size() > 0) {
            String currID = queue.poll();
            int currCount = nodesToSeq.get(currID);
            int tier = tierQueue.poll();

            ArrayList<String> nextIDs = (ArrayList<String>) idToCitationMap.get(currID).getKey();
            for (String ID : nextIDs) {
                if ((idToCitationMap.containsKey(ID)) && (!nodesToSeq.containsKey(ID)) && tier < 2) {
                    nodesCount++;
                    queue.add(ID);
                    tierQueue.add(tier + 1);
                    nodesToSeq.put(ID, nodesCount);
                    seqToNodes.put(nodesCount, ID);
                    edges.add(new Pair<>(currCount, nodesCount));
                }
            }
        }

    }

    private void readJson() {
        idToCitationMap = new HashMap<String, Pair<Object, String>>();
        JSONObject obj;
        String line = null;
        int count = 0;
        try {
            FileReader fileReader = new FileReader(inFileName);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                while ((line = bufferedReader.readLine()) != null) {
                    obj = (JSONObject) new JSONParser().parse(line);
                    idToCitationMap.put(obj.get("id").toString(), new Pair<>(obj.get("inCitations"), obj.get("title").toString()));
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
        int edgeSize = edges.size();
        try {
            FileWriter fileWriter = new FileWriter(outFileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("{");
            bufferedWriter.write("\"nodes\":[");
            for (int i = 0; i < nodesCount; i++) {
                bufferedWriter.write("{\"name\":\"" + idToCitationMap.get(seqToNodes.get(i)).getValue() + "\",\"group\":1},\n");
            }
            bufferedWriter.write("{\"name\":\"" + idToCitationMap.get(seqToNodes.get(nodesCount)).getValue() + "\",\"group\":1}\n");
            bufferedWriter.write("],");
            bufferedWriter.write("\"links\":[");
            for (int i = 0; i < edgeSize - 1; i++) {
                bufferedWriter.write("{\"source\":" + edges.get(i).getKey().toString() + ",\"target\":" + edges.get(i).getValue().toString() + ",\"weight\": 1},\n"  );
            }
            bufferedWriter.write("{\"source\":" + edges.get(edgeSize -1).getKey().toString() + ",\"target\":" + edges.get(edgeSize -1).getValue().toString() + ",\"weight\": 1}\n"  );
            bufferedWriter.write("]");
            bufferedWriter.write("}");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
