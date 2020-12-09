package com.company;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Antoine Jermaine Smith Jr.
// CSIS 3810
// Project 3
public class Main {

    public static void main(String[] args) throws InterruptedException {
        BandWidthCacheSystem band = new BandWidthCacheSystem();

        ExecutorService executor = Executors.newCachedThreadPool();
        // Give values to Bandwidth Cache & Creates its Addresses for Some of the Cache
        band.addressCacheInitialization();
        // Run threads
        for (int i = 0; i < 500; i++){
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try{
                        band.threadBandwidthInteraction();
                    }catch (Exception e){
                        System.out.println(e);
                    }

                }
            });
        }
        executor.shutdown();

        executor.awaitTermination(1, TimeUnit.DAYS);
    }
}
