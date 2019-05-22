package web;

import android.os.Environment;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by misiyu on 18-4-18.
 */

public class PackageQueue {
    private final int MaxSeq = 1000 ;

    private BlockingQueue<String> uploadQueue = new ArrayBlockingQueue<String>(MaxSeq/10);

    public void put(String newFile) throws Exception{
        uploadQueue.put(newFile);
    }
    public String get() throws Exception{
        return uploadQueue.take();
    }

    public boolean isEmpty(){
        return uploadQueue.isEmpty();
    }

}
