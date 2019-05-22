package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import adapter.MainListAdapter;
import web.ArpSpoofHttp;
import web.GetMasterIpHttp;
import web.PackageConsumer;
import web.UploadFileByHttp;

/**
 * Created by misiyu on 18-4-18.
 */

public class Main {
    public static void main(String[] args){
        //UploadFileByHttp uploadFileByHttp = new UploadFileByHttp(null);
        //MainListAdapter mainListAdapter = new MainListAdapter();
        //uploadFileByHttp.upload("/home/misiyu/nosense/tcpdump/a.txt","a.txt");
        //GetMasterIpHttp getMasterIpHttp = new GetMasterIpHttp("10.42.0.1");
        //System.out.println(getMasterIpHttp.getMasterIp(4.5,6.8));
        //PackageConsumer packageConsumer = new PackageConsumer(null,null);
        //packageConsumer.getArpTable();
        ArpSpoofHttp arpSpoofHttp = new ArpSpoofHttp();
        //arpSpoofHttp.transferSuspectIp("-t 192.168.0.126");
        arpSpoofHttp.stopArpspoof("1233455");
    }
}
