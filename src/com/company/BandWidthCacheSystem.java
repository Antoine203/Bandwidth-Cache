package com.company;

import java.util.Random;
import java.util.concurrent.Semaphore;

// Antoine Jermaine Smith Jr.
// CSIS 3810
// Project 3

public class BandWidthCacheSystem {
    // Addresses
    private int[] addresses = new int[10];

    // Bandwidth Cache
    private int[] BandwidthCache = new int[10];

    // Decision Threads
    private int decision1 = 0;
    private int decision2 = 0;
    private int decision3 = 0;
    private int decision4 = 0;
    private int decision5 = 0;
    private int decision6 = 0;
    private int decision7 = 0;
    private int decision8 = 0;
    private int decision9 = 0;
    private int decision10 = 0;

    // Decision Threads Finding the Address
    private boolean found = false;

    // Rounds
    int rounds = 1;

    // Semaphore --> Coordination for communication interaction between Bandwidth Cache & Decision Threads
    Semaphore communicationCoordination = new Semaphore(1);

    // Decision Threads Query Destination Values ------> Looking up an Address inside of the Bandwidth Cache
    private void decisionThreadsQueries(){
        // Addresses between 1 - 100
        decision1 = (int)(Math.random() * (100 - 1 + 1) + 1);
        decision2 = (int)(Math.random() * (100 - 1 + 1) + 1);
        decision3 = (int)(Math.random() * (100 - 1 + 1) + 1);
        decision4 = (int)(Math.random() * (100 - 1 + 1) + 1);
        decision5 = (int)(Math.random() * (100 - 1 + 1) + 1);
        decision6 = (int)(Math.random() * (100 - 1 + 1) + 1);
        decision7 = (int)(Math.random() * (100 - 1 + 1) + 1);
        decision8 = (int)(Math.random() * (100 - 1 + 1) + 1);
        decision9 = (int)(Math.random() * (100 - 1 + 1) + 1);
        decision10 = (int)(Math.random() * (100 - 1 + 1) + 1);
    }

    // Bandwidth Values --> Range 1 - 1000
    private int bandwidthValue() {
        return (int)(Math.random() * (1000 - 1 + 1) + 1);
    }

    // Decision Thread Query Failed -----> Backup Process for that particular thread
    // Backup Query Addresses Range: 1 - 10
    private int decisionThreadBackupQuery(){
        return (int)(Math.random() * (10 - 1 + 1) + 1);
    }

    // Addresses --> Initialize Partial Portion of the Cache
    // BandwidthCache Values --> Initialized in Correspondence to Address
    public void addressCacheInitialization(){
        // Random: Used for partial initialization within the cache
        int random = (int)(Math.random() * (6 - 2 + 1) + 2);
        for(int i = 0; i < random; i++){
            addresses[i] = (int)(Math.random() * (100 - 1 + 1) + 1);
            BandwidthCache[i] = bandwidthValue();
//            UNCOMMENT TO SEE THE INITIALIZATION VALUES OF THE ADDRESS LOCATIONS & BANDWIDTH VALUES
//            System.out.println("Addresses: " + addresses[i]);
//            System.out.println("Bandwidth: " + BandwidthCache[i] + "\n");
        }
    }

    // Eviction within Bandwidth Cache. Replacing an Address with a New One
    private int updateAddress(){
        return (int)(Math.random() * (100 - 1 + 1) + 1);
    }

    // Randomized Index Value within the Bandwidth Cache for Position for Eviction
    private int evictionIndex(){
        Random rand = new Random();
        // Returns an Index value between 1 - 9
        return rand.nextInt(10);
    }

    private int decisionThreadQueryFound(int decisionThread){
        // Decision Thread 1 Search for Address in Thread
        for(int i = 0; i < addresses.length; i++) {
            if (decisionThread == addresses[i]){
                found = true;
                return i;
            }
        }
        return -1;
    }
    private int emptySlotinCache(){
        for(int i = 0; i < addresses.length;i++){
            if(addresses[i] == 0){
                return i;
            }
        }
        return -1;
    }

    // Communication between Threads & Bandwidth using Semaphore for Coordination

    public void threadBandwidthInteraction() throws InterruptedException {

        // Decision Threads Query to Bandwidth Cache
        decisionThreadsQueries();

        // Lock Acquired ---> Semaphore
        communicationCoordination.acquire();

        // Beginning of the Rounds
        System.out.println("\n_________________________________\n\nRound: "+ rounds + " Commencing.\n\n_________________________________\n");
        // Update On the Bandwidth Address & Value
        for (int i = 0; i < addresses.length; i++){
            System.out.println("Bandwidth Address " + addresses[i] + " is value: " + BandwidthCache[i]);
            if(i == (addresses.length-1)){
                System.out.println("If the Address Value is 0, that slot in the Bandwidth Address has yet to be filled.");
            }
        }

        ///////////////////////////////////////////////////////////////   START OF DECISION THREAD 1   ///////////////////////////////////////////////////////////////
        // Found the Address
        int index1 = decisionThreadQueryFound(decision1);
        System.out.println("Commencing Decision Thread 1\n_________________________________\n");
        if(index1 != -1){
            System.out.println("Decision Thread 1\n_________________________________");
            System.out.println("Query of Addresses between 1 - 100: Success");
            System.out.println("Address: " + addresses[index1]);
            System.out.println("Bandwidth Cache Value: " + BandwidthCache[index1]);
        }
        else{
            System.out.println("Backup Procedure - Decision Thread 1\n_________________________________\n");
            // Decision Thread 1 Query of addresses between 1 - 10
            decision1 = decisionThreadBackupQuery();
            // Checks For a Match within the Cache Addresses
            index1 = decisionThreadQueryFound(decision1);
            if(index1 != -1){
                System.out.println("Query of Addresses between 1 - 10: Success");
                System.out.println("Address: " + addresses[index1]);
                System.out.println("Bandwidth Value: " + BandwidthCache[index1]);
            }else{
                System.out.println("Query of Addresses between 1 - 10: Failed");
                index1 = emptySlotinCache();
                // Checking for Empty slots in the Cache
                if(index1 != -1){
                    addresses[index1] = updateAddress();
                    BandwidthCache[index1] = bandwidthValue();
                    System.out.println("Filling in Empty slots in Cache.");
                    System.out.println("Address: " + addresses[index1]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[index1]);
                }
                // Cache is Filled
                else{
                    int evict = evictionIndex();
                    addresses[evict] = updateAddress();
                    BandwidthCache[evict] = bandwidthValue();
                    System.out.println("Eviction Process.");
                    System.out.println("Address: " + addresses[evict]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[evict]);
                }
            }
        }
        System.out.println("\n_________________________________\nEnding Decision Thread 1\n_________________________________\n");
        ///////////////////////////////////////////////////////////////   END OF DECISION THREAD 1   ///////////////////////////////////////////////////////////////

        // Update On the Bandwidth Address & Value
        for (int i = 0; i < addresses.length; i++){
            System.out.println("Bandwidth Address " + addresses[i] + " is value: " + BandwidthCache[i]);
            if(i == (addresses.length-1)){
                System.out.println("If the Address Value is 0, that slot in the Bandwidth Address has yet to be filled.");
            }
        }

        ///////////////////////////////////////////////////////////////   START OF DECISION THREAD 2   ///////////////////////////////////////////////////////////////
        // Found the Address
        int index2 = decisionThreadQueryFound(decision2);
        System.out.println("\n_________________________________\nCommencing Decision Thread 2\n_________________________________\n");
        if(index2 != -1){
            System.out.println("Decision Thread 2\n_________________________________");
            System.out.println("Query of Addresses between 1 - 100: Success");
            System.out.println("Address: " + addresses[index2]);
            System.out.println("Bandwidth Cache Value: " + BandwidthCache[index2]);
        }
        else{
            System.out.println("Backup Procedure - Decision Thread 2\n_________________________________\n");
            // Decision Thread 2 Query of addresses between 1 - 10
            decision2 = decisionThreadBackupQuery();
            // Checks For a Match within the Cache Addresses
            index2 = decisionThreadQueryFound(decision2);
            if(index2 != -1){
                System.out.println("Query of Addresses between 1 - 10: Success");
                System.out.println("Address: " + addresses[index2]);
                System.out.println("Bandwidth Value: " + BandwidthCache[index2]);
            }else{
                System.out.println("Query of Addresses between 1 - 10: Failed");
                index2 = emptySlotinCache();
                // Checking for Empty slots in the Cache
                if(index2 != -1){
                    addresses[index2] = updateAddress();
                    BandwidthCache[index2] = bandwidthValue();
                    System.out.println("Filling in Empty slots in Cache.");
                    System.out.println("Address: " + addresses[index2]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[index2]);
                }
                // Cache is Filled
                else{
                    int evict = evictionIndex();
                    addresses[evict] = updateAddress();
                    BandwidthCache[evict] = bandwidthValue();
                    System.out.println("Eviction Process.");
                    System.out.println("Address: " + addresses[evict]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[evict]);
                }
            }
        }
        System.out.println("\n_________________________________\nEnding Decision Thread 2\n_________________________________\n");
        ///////////////////////////////////////////////////////////////   END OF DECISION THREAD 2   ///////////////////////////////////////////////////////////////

        // Update On the Bandwidth Address & Value
        for (int i = 0; i < addresses.length; i++){
            System.out.println("Bandwidth Address " + addresses[i] + " is value: " + BandwidthCache[i]);
            if(i == (addresses.length-1)){
                System.out.println("If the Address Value is 0, that slot in the Bandwidth Address has yet to be filled.");
            }
        }

        ///////////////////////////////////////////////////////////////   START OF DECISION THREAD 3   ///////////////////////////////////////////////////////////////
        // Found the Address
        int index3 = decisionThreadQueryFound(decision3);
        System.out.println("\n_________________________________\nCommencing Decision Thread 3\n_________________________________\n");
        if(index3 != -1){
            System.out.println("Decision Thread 3\n_________________________________");
            System.out.println("Query of Addresses between 1 - 100: Success");
            System.out.println("Address: " + addresses[index3]);
            System.out.println("Bandwidth Cache Value: " + BandwidthCache[index3]);
        }
        else{
            System.out.println("Backup Procedure - Decision Thread 3\n_________________________________\n");
            // Decision Thread 3 Query of addresses between 1 - 10
            decision3 = decisionThreadBackupQuery();
            // Checks For a Match within the Cache Addresses
            index3 = decisionThreadQueryFound(decision3);
            if(index3 != -1){
                System.out.println("Query of Addresses between 1 - 10: Success");
                System.out.println("Address: " + addresses[index3]);
                System.out.println("Bandwidth Value: " + BandwidthCache[index3]);
            }else{
                System.out.println("Query of Addresses between 1 - 10: Failed");
                index3 = emptySlotinCache();
                // Checking for Empty slots in the Cache
                if(index3 != -1){
                    addresses[index3] = updateAddress();
                    BandwidthCache[index3] = bandwidthValue();
                    System.out.println("Filling in Empty slots in Cache.");
                    System.out.println("Address: " + addresses[index3]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[index3]);
                }
                // Cache is Filled
                else{
                    int evict = evictionIndex();
                    addresses[evict] = updateAddress();
                    BandwidthCache[evict] = bandwidthValue();
                    System.out.println("Eviction Process.");
                    System.out.println("Address: " + addresses[evict]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[evict]);
                }
            }
        }
        System.out.println("\n_________________________________\nEnding Decision Thread 3\n_________________________________\n");
        ///////////////////////////////////////////////////////////////   END OF DECISION THREAD 3   ///////////////////////////////////////////////////////////////

        // Update On the Bandwidth Address & Value
        for (int i = 0; i < addresses.length; i++){
            System.out.println("Bandwidth Address " + addresses[i] + " is value: " + BandwidthCache[i]);
            if(i == (addresses.length-1)){
                System.out.println("If the Address Value is 0, that slot in the Bandwidth Address has yet to be filled.");
            }
        }

        ///////////////////////////////////////////////////////////////   START OF DECISION THREAD 4   ///////////////////////////////////////////////////////////////
        // Found the Address
        int index4 = decisionThreadQueryFound(decision4);
        System.out.println("\n_________________________________\nCommencing Decision Thread 4\n_________________________________\n");
        if(index4 != -1){
            System.out.println("Decision Thread 4\n_________________________________");
            System.out.println("Query of Addresses between 1 - 100: Success");
            System.out.println("Address: " + addresses[index4]);
            System.out.println("Bandwidth Cache Value: " + BandwidthCache[index4]);
        }
        else{
            System.out.println("Backup Procedure - Decision Thread 4\n_________________________________\n");
            // Decision Thread 4 Query of addresses between 1 - 10
            decision4 = decisionThreadBackupQuery();
            // Checks For a Match within the Cache Addresses
            index4 = decisionThreadQueryFound(decision4);
            if(index4 != -1){
                System.out.println("Query of Addresses between 1 - 10: Success");
                System.out.println("Address: " + addresses[index4]);
                System.out.println("Bandwidth Value: " + BandwidthCache[index4]);
            }else{
                System.out.println("Query of Addresses between 1 - 10: Failed");
                index4 = emptySlotinCache();
                // Checking for Empty slots in the Cache
                if(index4 != -1){
                    addresses[index4] = updateAddress();
                    BandwidthCache[index4] = bandwidthValue();
                    System.out.println("Filling in Empty slots in Cache.");
                    System.out.println("Address: " + addresses[index4]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[index4]);
                }
                // Cache is Filled
                else{
                    int evict = evictionIndex();
                    addresses[evict] = updateAddress();
                    BandwidthCache[evict] = bandwidthValue();
                    System.out.println("Eviction Process.");
                    System.out.println("Address: " + addresses[evict]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[evict]);
                }
            }
        }
        System.out.println("\n_________________________________\nEnding Decision Thread 4\n_________________________________\n");
        ///////////////////////////////////////////////////////////////   END OF DECISION THREAD 4   ///////////////////////////////////////////////////////////////

        // Update On the Bandwidth Address & Value
        for (int i = 0; i < addresses.length; i++){
            System.out.println("Bandwidth Address " + addresses[i] + " is value: " + BandwidthCache[i]);
            if(i == (addresses.length-1)){
                System.out.println("If the Address Value is 0, that slot in the Bandwidth Address has yet to be filled.");
            }
        }

        ///////////////////////////////////////////////////////////////   START OF DECISION THREAD 5   ///////////////////////////////////////////////////////////////
        // Found the Address
        int index5 = decisionThreadQueryFound(decision5);
        System.out.println("\n_________________________________\nCommencing Decision Thread 5\n_________________________________\n");
        if(index5 != -1){
            System.out.println("Decision Thread 5\n_________________________________");
            System.out.println("Query of Addresses between 1 - 100: Success");
            System.out.println("Address: " + addresses[index5]);
            System.out.println("Bandwidth Cache Value: " + BandwidthCache[index5]);
        }
        else{
            System.out.println("Backup Procedure - Decision Thread 5\n_________________________________\n");
            // Decision Thread 5 Query of addresses between 1 - 10
            decision5 = decisionThreadBackupQuery();
            // Checks For a Match within the Cache Addresses
            index5 = decisionThreadQueryFound(decision5);
            if(index5 != -1){
                System.out.println("Query of Addresses between 1 - 10: Success");
                System.out.println("Address: " + addresses[index5]);
                System.out.println("Bandwidth Value: " + BandwidthCache[index5]);
            }else{
                System.out.println("Query of Addresses between 1 - 10: Failed");
                index5 = emptySlotinCache();
                // Checking for Empty slots in the Cache
                if(index5 != -1){
                    addresses[index5] = updateAddress();
                    BandwidthCache[index5] = bandwidthValue();
                    System.out.println("Filling in Empty slots in Cache.");
                    System.out.println("Address: " + addresses[index5]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[index5]);
                }
                // Cache is Filled
                else{
                    int evict = evictionIndex();
                    addresses[evict] = updateAddress();
                    BandwidthCache[evict] = bandwidthValue();
                    System.out.println("Eviction Process.");
                    System.out.println("Address: " + addresses[evict]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[evict]);
                }
            }
        }
        System.out.println("\n_________________________________\nEnding Decision Thread 5\n_________________________________\n");
        ///////////////////////////////////////////////////////////////   END OF DECISION THREAD 5   ///////////////////////////////////////////////////////////////

        // Update On the Bandwidth Address & Value
        for (int i = 0; i < addresses.length; i++){
            System.out.println("Bandwidth Address " + addresses[i] + " is value: " + BandwidthCache[i]);
            if(i == (addresses.length-1)){
                System.out.println("If the Address Value is 0, that slot in the Bandwidth Address has yet to be filled.");
            }
        }

        ///////////////////////////////////////////////////////////////   START OF DECISION THREAD 6   ///////////////////////////////////////////////////////////////
        // Found the Address
        int index6 = decisionThreadQueryFound(decision6);
        System.out.println("\n_________________________________\nCommencing Decision Thread 6\n_________________________________\n");
        if(index6 != -1){
            System.out.println("Decision Thread 6\n_________________________________");
            System.out.println("Query of Addresses between 1 - 100: Success");
            System.out.println("Address: " + addresses[index6]);
            System.out.println("Bandwidth Cache Value: " + BandwidthCache[index6]);
        }
        else{
            System.out.println("Backup Procedure - Decision Thread 6\n_________________________________\n");
            // Decision Thread 6 Query of addresses between 1 - 10
            decision6 = decisionThreadBackupQuery();
            // Checks For a Match within the Cache Addresses
            index6 = decisionThreadQueryFound(decision6);
            if(index6 != -1){
                System.out.println("Query of Addresses between 1 - 10: Success");
                System.out.println("Address: " + addresses[index6]);
                System.out.println("Bandwidth Value: " + BandwidthCache[index6]);
            }else{
                System.out.println("Query of Addresses between 1 - 10: Failed");
                index6 = emptySlotinCache();
                // Checking for Empty slots in the Cache
                if(index6 != -1){
                    addresses[index6] = updateAddress();
                    BandwidthCache[index6] = bandwidthValue();
                    System.out.println("Filling in Empty slots in Cache.");
                    System.out.println("Address: " + addresses[index6]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[index6]);
                }
                // Cache is Filled
                else{
                    int evict = evictionIndex();
                    addresses[evict] = updateAddress();
                    BandwidthCache[evict] = bandwidthValue();
                    System.out.println("Eviction Process.");
                    System.out.println("Address: " + addresses[evict]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[evict]);
                }
            }
        }
        System.out.println("\n_________________________________\nEnding Decision Thread 6\n_________________________________\n");
        ///////////////////////////////////////////////////////////////   END OF DECISION THREAD 6   ///////////////////////////////////////////////////////////////

        // Update On the Bandwidth Address & Value
        for (int i = 0; i < addresses.length; i++){
            System.out.println("Bandwidth Address " + addresses[i] + " is value: " + BandwidthCache[i]);
            if(i == (addresses.length-1)){
                System.out.println("If the Address Value is 0, that slot in the Bandwidth Address has yet to be filled.");
            }
        }

        ///////////////////////////////////////////////////////////////   START OF DECISION THREAD 7   ///////////////////////////////////////////////////////////////
        // Found the Address
        int index7 = decisionThreadQueryFound(decision7);
        System.out.println("\n_________________________________\nCommencing Decision Thread 7\n_________________________________\n");
        if(index7 != -1){
            System.out.println("Decision Thread 7\n_________________________________");
            System.out.println("Query of Addresses between 1 - 100: Success");
            System.out.println("Address: " + addresses[index7]);
            System.out.println("Bandwidth Cache Value: " + BandwidthCache[index7]);
        }
        else{
            System.out.println("Backup Procedure - Decision Thread 7\n_________________________________\n");
            // Decision Thread 7 Query of addresses between 1 - 10
            decision7 = decisionThreadBackupQuery();
            // Checks For a Match within the Cache Addresses
            index7 = decisionThreadQueryFound(decision7);
            if(index7 != -1){
                System.out.println("Query of Addresses between 1 - 10: Success");
                System.out.println("Address: " + addresses[index7]);
                System.out.println("Bandwidth Value: " + BandwidthCache[index7]);
            }else{
                System.out.println("Query of Addresses between 1 - 10: Failed");
                index7 = emptySlotinCache();
                // Checking for Empty slots in the Cache
                if(index7 != -1){
                    addresses[index7] = updateAddress();
                    BandwidthCache[index7] = bandwidthValue();
                    System.out.println("Filling in Empty slots in Cache.");
                    System.out.println("Address: " + addresses[index7]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[index7]);
                }
                // Cache is Filled
                else{
                    int evict = evictionIndex();
                    addresses[evict] = updateAddress();
                    BandwidthCache[evict] = bandwidthValue();
                    System.out.println("Eviction Process.");
                    System.out.println("Address: " + addresses[evict]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[evict]);
                }
            }
        }
        System.out.println("\n_________________________________\nEnding Decision Thread 7\n_________________________________\n");
        ///////////////////////////////////////////////////////////////   END OF DECISION THREAD 7   ///////////////////////////////////////////////////////////////

        // Update On the Bandwidth Address & Value
        for (int i = 0; i < addresses.length; i++){
            System.out.println("Bandwidth Address " + addresses[i] + " is value: " + BandwidthCache[i]);
            if(i == (addresses.length-1)){
                System.out.println("If the Address Value is 0, that slot in the Bandwidth Address has yet to be filled.");
            }
        }

        ///////////////////////////////////////////////////////////////   START OF DECISION THREAD 8   ///////////////////////////////////////////////////////////////
        // Found the Address
        int index8 = decisionThreadQueryFound(decision8);
        System.out.println("\n_________________________________\nCommencing Decision Thread 8\n_________________________________\n");
        if(index8 != -1){
            System.out.println("Decision Thread 8\n_________________________________");
            System.out.println("Query of Addresses between 1 - 100: Success");
            System.out.println("Address: " + addresses[index8]);
            System.out.println("Bandwidth Cache Value: " + BandwidthCache[index8]);
        }
        else{
            System.out.println("Backup Procedure - Decision Thread 8\n_________________________________\n");
            // Decision Thread 8 Query of addresses between 1 - 10
            decision8 = decisionThreadBackupQuery();
            // Checks For a Match within the Cache Addresses
            index8 = decisionThreadQueryFound(decision8);
            if(index8 != -1){
                System.out.println("Query of Addresses between 1 - 10: Success");
                System.out.println("Address: " + addresses[index8]);
                System.out.println("Bandwidth Value: " + BandwidthCache[index8]);
            }else{
                System.out.println("Query of Addresses between 1 - 10: Failed");
                index8 = emptySlotinCache();
                // Checking for Empty slots in the Cache
                if(index8 != -1){
                    addresses[index8] = updateAddress();
                    BandwidthCache[index8] = bandwidthValue();
                    System.out.println("Filling in Empty slots in Cache.");
                    System.out.println("Address: " + addresses[index8]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[index8]);
                }
                // Cache is Filled
                else{
                    int evict = evictionIndex();
                    addresses[evict] = updateAddress();
                    BandwidthCache[evict] = bandwidthValue();
                    System.out.println("Eviction Process.");
                    System.out.println("Address: " + addresses[evict]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[evict]);
                }
            }
        }
        System.out.println("\n_________________________________\nEnding Decision Thread 8\n_________________________________\n");
        ///////////////////////////////////////////////////////////////   END OF DECISION THREAD 8   ///////////////////////////////////////////////////////////////

        // Update On the Bandwidth Address & Value
        for (int i = 0; i < addresses.length; i++){
            System.out.println("Bandwidth Address " + addresses[i] + " is value: " + BandwidthCache[i]);
            if(i == (addresses.length-1)){
                System.out.println("If the Address Value is 0, that slot in the Bandwidth Address has yet to be filled.");
            }
        }

        ///////////////////////////////////////////////////////////////   START OF DECISION THREAD 9   ///////////////////////////////////////////////////////////////
        // Found the Address
        int index9 = decisionThreadQueryFound(decision9);
        System.out.println("\n_________________________________\nCommencing Decision Thread 9\n_________________________________\n");
        if(index9 != -1){
            System.out.println("Decision Thread 9\n_________________________________");
            System.out.println("Query of Addresses between 1 - 100: Success");
            System.out.println("Address: " + addresses[index9]);
            System.out.println("Bandwidth Cache Value: " + BandwidthCache[index9]);
        }
        else{
            System.out.println("Backup Procedure - Decision Thread 9\n_________________________________\n");
            // Decision Thread 9 Query of addresses between 1 - 10
            decision9 = decisionThreadBackupQuery();
            // Checks For a Match within the Cache Addresses
            index9 = decisionThreadQueryFound(decision9);
            if(index9 != -1){
                System.out.println("Query of Addresses between 1 - 10: Success");
                System.out.println("Address: " + addresses[index9]);
                System.out.println("Bandwidth Value: " + BandwidthCache[index9]);
            }else{
                System.out.println("Query of Addresses between 1 - 10: Failed");
                index9 = emptySlotinCache();
                // Checking for Empty slots in the Cache
                if(index9 != -1){
                    addresses[index9] = updateAddress();
                    BandwidthCache[index9] = bandwidthValue();
                    System.out.println("Filling in Empty slots in Cache.");
                    System.out.println("Address: " + addresses[index9]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[index9]);
                }
                // Cache is Filled
                else{
                    int evict = evictionIndex();
                    addresses[evict] = updateAddress();
                    BandwidthCache[evict] = bandwidthValue();
                    System.out.println("Eviction Process.");
                    System.out.println("Address: " + addresses[evict]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[evict]);
                }
            }
        }
        System.out.println("\n_________________________________\nEnding Decision Thread 9\n_________________________________\n");
        ///////////////////////////////////////////////////////////////   END OF DECISION THREAD 9   ///////////////////////////////////////////////////////////////

        // Update On the Bandwidth Address & Value
        for (int i = 0; i < addresses.length; i++){
            System.out.println("Bandwidth Address " + addresses[i] + " is value: " + BandwidthCache[i]);
            if(i == (addresses.length-1)){
                System.out.println("If the Address Value is 0, that slot in the Bandwidth Address has yet to be filled.");
            }
        }

        ///////////////////////////////////////////////////////////////   START OF DECISION THREAD 10   ///////////////////////////////////////////////////////////////
        // Found the Address
        int index10 = decisionThreadQueryFound(decision10);
        System.out.println("\n_________________________________\nCommencing Decision Thread 10\n_________________________________\n");
        if(index10 != -1){
            System.out.println("Decision Thread 10\n_________________________________");
            System.out.println("Query of Addresses between 1 - 100: Success");
            System.out.println("Address: " + addresses[index10]);
            System.out.println("Bandwidth Cache Value: " + BandwidthCache[index10]);
        }
        else{
            System.out.println("Backup Procedure - Decision Thread 10\n_________________________________\n");
            // Decision Thread 10 Query of addresses between 1 - 10
            decision10 = decisionThreadBackupQuery();
            // Checks For a Match within the Cache Addresses
            index10 = decisionThreadQueryFound(decision10);
            if(index10 != -1){
                System.out.println("Query of Addresses between 1 - 10: Success");
                System.out.println("Address: " + addresses[index10]);
                System.out.println("Bandwidth Value: " + BandwidthCache[index10]);
            }else{
                System.out.println("Query of Addresses between 1 - 10: Failed");
                index10 = emptySlotinCache();
                // Checking for Empty slots in the Cache
                if(index10 != -1){
                    addresses[index10] = updateAddress();
                    BandwidthCache[index10] = bandwidthValue();
                    System.out.println("Filling in Empty slots in Cache.");
                    System.out.println("Address: " + addresses[index10]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[index10]);
                }
                // Cache is Filled
                else{
                    int evict = evictionIndex();
                    addresses[evict] = updateAddress();
                    BandwidthCache[evict] = bandwidthValue();
                    System.out.println("Eviction Process.");
                    System.out.println("Address: " + addresses[evict]);
                    System.out.println("Bandwidth Value: " + BandwidthCache[evict]);
                }
            }
        }
        System.out.println("\n_________________________________\nEnding Decision Thread 10\n_________________________________\n");
        ///////////////////////////////////////////////////////////////   END OF DECISION THREAD 10   ///////////////////////////////////////////////////////////////

        for (int i = 0; i < addresses.length; i++){
            System.out.println("Bandwidth Address " + addresses[i] + " is value: " + BandwidthCache[i]);
            if(i == (addresses.length-1)){
                System.out.println("If the Address Value is 0, that slot in the Bandwidth Address has yet to be filled.");
            }
        }

        // Ending Round
        System.out.println("\n_________________________________\n\nRound: "+ rounds + " Completed.\n\n_________________________________");
        // Resting the Threads
        Thread.sleep(4000);


        // Release Lock ----->  Semaphore
        communicationCoordination.release();

        // Increments Rounds
        rounds++;
    }
}
