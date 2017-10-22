package com.company;

import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        HashMap<String, Object> idToCitationMap = new HashMap<>();
        JSONObject obj;
        String inFileName = "C:\\Users\\Elijah\\Documents\\CS3219\\extract.txt";
        String line = null;
        int count = 0;
        try {
            FileReader fileReader = new FileReader(inFileName);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                while ((line = bufferedReader.readLine()) != null) {
                    obj = (JSONObject) new JSONParser().parse(line);
                    idToCitationMap.put(obj.get("id").toString(), obj.get("inCitations"));
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<Pair<Integer, Integer>> edges = new ArrayList<Pair<Integer, Integer>>();
        HashMap<String, Integer> nodesToSeq = new HashMap<String, Integer>();
        HashMap<Integer, String> seqToNodes = new HashMap<Integer, String>();
        LinkedList<String> queue = new LinkedList<String>();
        LinkedList<Integer> tierQueue = new LinkedList<Integer>();
        int nodesCount = 0;
        String initialID = "36adf8c327b95bdffe2778bf022e0234d433454a";
        queue.add(initialID);
        nodesToSeq.put(initialID, nodesCount);
        seqToNodes.put(nodesCount, initialID);
        tierQueue.add(0);

        while (queue.size() > 0) {
                String currID = queue.poll();
                int currCount = nodesToSeq.get(currID);
                int tier = tierQueue.poll();
                if (idToCitationMap.containsKey(currID)) {
                    ArrayList<String> nextIDs = (ArrayList<String>) idToCitationMap.get(currID);
                    for (String ID : nextIDs) {
                        if ((!nodesToSeq.containsKey(ID)) && tier < 2) {
                            nodesCount++;
                            queue.add(ID);
                            tierQueue.add(tier + 1);
                            nodesToSeq.put(ID, nodesCount);
                            seqToNodes.put(nodesCount, ID);
                            edges.add(new Pair(currCount, nodesCount));
                        }
                    }
                }
            }
        System.out.println(nodesCount);
        System.out.println(edges.size());

        String outFileName = "C:\\Users\\Elijah\\Documents\\CS3219\\task 4 json.txt";
        try {
            FileWriter fileWriter = new FileWriter(outFileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("{");
            bufferedWriter.write("\"nodes\":[");
            for (int i = 0; i <= nodesCount; i++) {
                bufferedWriter.write("{\"name\":\"" + seqToNodes.get(i) + "\",\"group\":1},\n");
            }
            bufferedWriter.write("],");
            bufferedWriter.write("\"links\":[");
            for (Pair<Integer, Integer> edge : edges) {
                bufferedWriter.write("{\"source\":" + edge.getKey().toString() + ",\"target\":" + edge.getValue().toString() + ",\"weight\": 1},\n"  );
            }
            bufferedWriter.write("]");
            bufferedWriter.write("}");
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
