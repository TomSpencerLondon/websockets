package com.example.demo;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class RaceConditionDetector {

    public static void main(String[] args) {
        // Path to the log file
        String logFilePath = "/Users/tspencer/Desktop/demo/spring-websockets-test.log";

        // Reading and processing the log file
        try {
            processLogFile(logFilePath);
        } catch (IOException e) {
            System.err.println("Error reading the log file: " + e.getMessage());
        }
    }

    public static void processLogFile(String logFilePath) throws IOException {
        // A map to track log entries by 'After count' value
        Map<String, List<String>> logGroups = new HashMap<>();
        int raceConditionCount = 0;

        // Regular expression to extract relevant parts from each log entry
        // We are capturing "Before count" and "After count" in the regex
        Pattern pattern = Pattern.compile(
                "(\\d{2}:\\d{2}:\\d{2}\\.\\d{3}) \\[([\\w\\-]+)\\] INFO com\\.example\\.demo\\.Proposal -- Before count: (\\d+) After count: (\\d+)");

        // Read the file line by line
        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    String timestamp = matcher.group(1);  // Timestamp of the log entry
                    String threadName = matcher.group(2); // Thread name
                    String beforeCount = matcher.group(3); // Before count value
                    String afterCount = matcher.group(4); // After count value

                    // Group log entries by 'After count' value
                    logGroups.putIfAbsent(afterCount, new ArrayList<>());
                    logGroups.get(afterCount).add(line);

                    // If the same 'After count' already exists, we have a potential race condition
                    if (logGroups.get(afterCount).size() > 1) {
                        raceConditionCount++;
                    }
                }
            }
        }

        // Output the grouped log entries with race conditions
        System.out.println("Race Conditions Detected: " + raceConditionCount);
        for (Map.Entry<String, List<String>> entry : logGroups.entrySet()) {
            if (entry.getValue().size() > 1) {
                System.out.println("Race Condition for After count: " + entry.getKey());
                for (String logLine : entry.getValue()) {
                    System.out.println(logLine);
                }
                System.out.println();  // Add a newline after each race condition group
            }
        }
    }
}
