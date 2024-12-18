package com.example.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RaceConditionDetector {
    public static void main(String[] args) throws Exception {
        String logFilePath = "/Users/tspencer/Desktop/websockets/spring-websockets.log";
        Pattern pattern = Pattern.compile("Before count:\\s*(\\d+) After count:\\s*(\\d+)");
        BufferedReader reader = new BufferedReader(new FileReader(logFilePath));

        Map<Integer, List<String>> afterLinesMap = new HashMap<>();

        reader.lines().forEach(line -> {
            Matcher m = pattern.matcher(line);
            if (m.find()) {
                int after = Integer.parseInt(m.group(2));
                afterLinesMap.computeIfAbsent(after, k -> new ArrayList<>()).add(line);
            }
        });

        reader.close();

        boolean foundIssue = false;
        for (Map.Entry<Integer, List<String>> entry : afterLinesMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                foundIssue = true;
                System.out.println("Suspicious duplicate After count: " + entry.getKey());
                for (String suspiciousLine : entry.getValue()) {
                    System.out.println(" - " + suspiciousLine);
                }
            }
        }

        if (!foundIssue) {
            System.out.println("No duplicate After counts found.");
        }
    }
}
