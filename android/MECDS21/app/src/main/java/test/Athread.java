package test;

import java.util.concurrent.BlockingQueue;

/**
 * Created by misiyu on 18-4-18.
 */

public class Athread extends Thread {
    BlockingQueue<String> uploadQueue  = null;
    public Athread(BlockingQueue<String> queue){
        this.uploadQueue = queue ;
    }

    @Override
    public void run() {
        while (true){
            try {
                uploadQueue.put("we");
            }catch (Exception e){

            }
            System.out.println("put a we");
            try {
                Thread.sleep(1200);
            }catch (Exception e){

            }

        }
    }
}
