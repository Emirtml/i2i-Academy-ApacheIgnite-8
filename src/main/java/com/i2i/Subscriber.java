package com.i2i;

public class Subscriber {
    // 1. Fields: Information stored for each subscriber
    private int customerId;      // Primary Key (Unique customer ID)
    private double dataUsage;    // Internet usage in MB or GB
    private int smsUsage;        // SMS usage count
    private int callUsage;       // Call usage in minutes

    // 2. Empty Constructor
    // Apache Ignite needs this empty constructor to create objects automatically.
    public Subscriber() {
    }

    // 3. Parameterized Constructor
    // Used to create a new subscriber with values easily in one line.
    public Subscriber(int customerId, double dataUsage, int smsUsage, int callUsage) {
        this.customerId = customerId;
        this.dataUsage = dataUsage;
        this.smsUsage = smsUsage;
        this.callUsage = callUsage;
    }

    // 4. Getter and Setter Methods
    // Used to get and set private field values safely 
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public double getDataUsage() { return dataUsage; }
    public void setDataUsage(double dataUsage) { this.dataUsage = dataUsage; }

    public int getSmsUsage() { return smsUsage; }
    public void setSmsUsage(int smsUsage) { this.smsUsage = smsUsage; }

    public int getCallUsage() { return callUsage; }
    public void setCallUsage(int callUsage) { this.callUsage = callUsage; }

    // 5. toString Method
    // Used to print subscriber details nicely on the console screen
    @Override
    public String toString() {
        return "Subscriber [ID=" + customerId + ", Data=" + dataUsage + " GB, SMS=" + smsUsage + ", Minutes=" + callUsage + "]";
    }
}