# i2i-Academy-ApacheIgnite-8
Telecom Subscriber Usage Simulation with Apache Ignite 3

This repository implements a distributed, in-memory data processing pipeline designed to simulate real-time telecom subscriber usage updates. The application utilizes **Apache Ignite 3**, **Java Thin Client**, and **Docker** to achieve ultra-low latency data operations.

---

## 🚀 Project Description

In high-velocity telecom environments, processing call detail records (CDRs) and data usage metrics requires extreme performance. This project demonstrates an architectural solution by deploying a distributed Apache Ignite 3 cluster in a containerized environment. A lightweight Java application connects to the cluster via a Thin Client interface, establishes a relational schema in memory, injects test subscribers, and executes a real-time tracking and simulation loop.

## 🛠️ Tech Stack & Prerequisites

*   **Language:** Java 17 / 21
*   **Database Platform:** Apache Ignite 3.0 (Distributed In-Memory Computing)
*   **Containerization:** Docker Desktop
*   **Build & Lifecycle Tool:** Apache Maven 3.9.6
*   **Integrated Development Environment (IDE):** Visual Studio Code (VS Code)
*   **Execution Environment:** Windows Command Prompt (CMD)

---

## ⚙️ Key Features & Architecture Flow

1.  **Automated Initialization:** Every execution performs an automated schema reset (`DROP TABLE` and `DELETE`) to guarantee a completely fresh state for data tracking.
2.  **Relational Memory Schema:** Defines a comprehensive `subscriber` table directly in RAM with optimized fields for critical telecom metrics (Subscriber ID, Name, MSISDN/Phone, Package ID, Data MB, SMS, and Call Minutes).
3.  **Data Ingestion:** Populates the distributed memory space with 5 distinct default subscriber records to establish a simulation base.
4.  **Real-Time Usage Simulation:** Queries active cluster rows, generates random utilization metrics using a randomized distribution, and flushes the modified balances back via distributed SQL `UPDATE` queries.
5.  **Resource Management:** Uses a `try-with-resources` architecture to automatically release socket connections and ensure safe system termination.

---

## 🔍 Key Troubleshooting Insight

### The Handshake Error & Cluster Initialization
*   **The Challenge:** During initial deployment, the Java Thin Client failed to connect to the node (`127.0.0.1:10800`), throwing a critical `HandshakeException`.
*   **The Underlying Cause:** In Apache Ignite 3, server nodes boot up in a secure, locked down state by default. The cluster is online but will reject all thin client connections until the cluster management group is explicitly initialized.
*   **The Engineering Solution:** We resolved this by sending an HTTP POST request to the Ignite REST API (`port 10300`) using a temporary curl container. This successfully initialized the cluster management node (`defaultNode`) and opened up network ports for transactional client traffic.

---

## 💻 How to Run the Project

Follow these steps to deploy the environment and execute the simulation sequence via Windows CMD:

### Step 1: Initialize the Apache Ignite 3 Cluster Node
Ensure Docker Desktop is running. Execute the following command in your terminal to initialize the cluster management group:
```cmd
docker run --rm --network host curlimages/curl -X POST "http://localhost:10300/management/v1/cluster/init" -H "content-type: application/json" -d "{\"metaStorageNodes\":[\"apache-node\"],\"clusterName\":\"defaultNode\"}"
