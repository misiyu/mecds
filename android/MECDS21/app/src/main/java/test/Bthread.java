package test;

import java.util.concurrent.BlockingQueue;

/**
 * Created by misiyu on 18-4-18.
 */

public class Bthread extends Thread {
    BlockingQueue<String> uploadQueue  = null;
    public Bthread(BlockingQueue<String> queue){
        this.uploadQueue = queue ;
    }
    @Override
    public void run() {
        while (true){
            try{
                String str = uploadQueue.take();
                System.out.println("b get a " +str);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            }catch (Exception e){

            }

        }
    }
}
