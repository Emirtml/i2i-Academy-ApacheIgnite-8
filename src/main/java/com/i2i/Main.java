package com.i2i;

import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.sql.ResultSet;
import org.apache.ignite.sql.SqlRow;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // Connect to local Apache Ignite 3 node
        try (IgniteClient client = IgniteClient.builder()
                .addresses("127.0.0.1:10800")
                .build()) {
            
            System.out.println("Connection successful!");
            
            // Drop old table first to clear the old 4-column schema from Docker RAM
            client.sql().execute(null, "DROP TABLE IF EXISTS subscriber");
            
            // Create table with all required usage fields
            String createTableSql = "CREATE TABLE IF NOT EXISTS subscriber (" +
                                    "SUBSC_ID INT PRIMARY KEY, " +
                                    "SUBSC_NAME VARCHAR, " +
                                    "MSISDN VARCHAR, " +
                                    "PACKAGE_ID INT, " +
                                    "DATA_USAGE INT, " +
                                    "SMS_USAGE INT, " +
                                    "CALL_USAGE INT)";
            client.sql().execute(null, createTableSql);
            
            // Initialization: Clear table using a DELETE query so every execution starts fresh
            client.sql().execute(null, "DELETE FROM subscriber");
            System.out.println("Table cleared for a fresh start.");
            
            // Insert 5 test subscribers into the database
            String insertSql = "INSERT INTO subscriber (SUBSC_ID, SUBSC_NAME, MSISDN, PACKAGE_ID, DATA_USAGE, SMS_USAGE, CALL_USAGE) VALUES " +
                               "(1, 'Emir Sefik', '5551234567', 101, 0, 0, 0), " +
                               "(2, 'Sevgi Can', '5559998877', 102, 0, 0, 0), " +
                               "(3, 'Ahmet Yilmaz', '5557654321', 103, 0, 0, 0), " +
                               "(4, 'Ayse Demir', '5551112233', 104, 0, 0, 0), " +
                               "(5, 'Mehmet Kaya', '5554445566', 105, 0, 0, 0)";
            client.sql().execute(null, insertSql);
            System.out.println("5 test subscribers inserted into RAM successfully.");
            
            // Simulation: Retrieve records, generate random updates, and UPDATE them back
            System.out.println("\n--- Starting Usage Simulation ---");
            Random random = new Random();
            
            String selectSql = "SELECT SUBSC_ID, SUBSC_NAME, DATA_USAGE, SMS_USAGE, CALL_USAGE FROM subscriber";
            
            try (ResultSet<SqlRow> rows = client.sql().execute(null, selectSql)) {
                while (rows.hasNext()) {
                    SqlRow row = rows.next();
                    int id = row.intValue("SUBSC_ID");
                    String name = row.stringValue("SUBSC_NAME");
                    int currentData = row.intValue("DATA_USAGE");
                    int currentSms = row.intValue("SMS_USAGE");
                    int currentCall = row.intValue("CALL_USAGE");
                    
                    // Add random usage amounts to fields
                    int newData = currentData + random.nextInt(5000) + 100; // 100 to 5100 MB
                    int newSms = currentSms + random.nextInt(50) + 5;       // 5 to 55 SMS
                    int newCall = currentCall + random.nextInt(120) + 10;   // 10 to 130 Mins
                    
                    // UPDATE records back in the Ignite table
                    String updateSql = "UPDATE subscriber SET DATA_USAGE = " + newData + 
                                       ", SMS_USAGE = " + newSms + 
                                       ", CALL_USAGE = " + newCall + 
                                       " WHERE SUBSC_ID = " + id;
                    client.sql().execute(null, updateSql);
                    System.out.println("Simulated updates applied for: " + name + " (ID: " + id + ")");
                }
            }
            
            // Print the final state of all 5 subscribers to the console and terminate
            System.out.println("\n--- Final State of All 5 Subscribers ---");
            String finalSelectSql = "SELECT SUBSC_ID, SUBSC_NAME, MSISDN, PACKAGE_ID, DATA_USAGE, SMS_USAGE, CALL_USAGE FROM subscriber";
            
            try (ResultSet<SqlRow> finalRows = client.sql().execute(null, finalSelectSql)) {
                while (finalRows.hasNext()) {
                    SqlRow row = finalRows.next();
                    System.out.println("Subscriber ID : " + row.intValue("SUBSC_ID"));
                    System.out.println("-> Name          : " + row.stringValue("SUBSC_NAME"));
                    System.out.println("-> Phone (MSISDN): " + row.stringValue("MSISDN"));
                    System.out.println("-> Package ID    : " + row.intValue("PACKAGE_ID"));
                    System.out.println("-> Data Usage    : " + row.intValue("DATA_USAGE") + " MB");
                    System.out.println("-> SMS Usage     : " + row.intValue("SMS_USAGE") + " SMS");
                    System.out.println("-> Call Usage    : " + row.intValue("CALL_USAGE") + " Min");
                    System.lineSeparator();
                    System.out.println("---------------------------------------");
                }
            }
            
            System.out.println("Simulation completed successfully. Terminating program.");
            
        } catch (Exception e) {
            System.err.println("Error during simulation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}