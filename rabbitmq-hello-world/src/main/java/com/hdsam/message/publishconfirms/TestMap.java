package com.hdsam.message.publishconfirms;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Java program to demonstrate
 * the NavigableMap interface
 *
 * @author Yeo
 * @date 2023/7/25
 */
public class TestMap {
    public static void main(String[] args) {
        // Instantiate an object
        // Since NavigableMap
        // is an interface so we
        // use TreeMap
        TreeMap<String, Integer> nm = new TreeMap<String, Integer>();

        // Add elements using put() method
        nm.put("C", 888);
        nm.put("Y", 999);
        nm.put("A", 444);
        nm.put("T", 555);
        nm.put("B", 666);
        nm.put("A", 555);

        // Print the contents on the console
        System.out.println("Mappings of NavigableMap : " + nm);

        System.out.printf("Descending Set : %s%n", nm.descendingKeySet());
        System.out.printf("Floor Entry : %s%n", nm.floorEntry("L"));
        System.out.printf("First Entry : %s%n", nm.firstEntry());
        System.out.printf("Last Key : %s%n", nm.lastKey());
        System.out.printf("First Key : %s%n", nm.firstKey());
        System.out.printf("Original Map : %s%n", nm);
        System.out.printf("Reverse Map : %s%n", nm.descendingMap());
    }
}

