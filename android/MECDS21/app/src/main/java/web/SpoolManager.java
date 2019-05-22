package web;

import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

import adapter.MainListAdapter;

/**
 * Created by misiyu on 18-4-18.
 */

public class SpoolManager extends Thread {


    private PackageQueue packageQueue = new PackageQueue();
    private PackageProducer packageProducer = new PackageProducer(packageQueue);
    private PackageConsumer packageConsumer = null;

    public SpoolManager(MainListAdapter mla){
        packageConsumer = new PackageConsumer(packageQueue,mla);
    }

    @Override
    public void run() {
        this.packageProducer.setStop(false);
        this.packageConsumer.setStop(false);
        packageProducer.start();
        packageConsumer.start();

    }

    public void stopThread(){
        this.packageConsumer.setStop(true);
        this.packageProducer.setStop(true);
    }
}
