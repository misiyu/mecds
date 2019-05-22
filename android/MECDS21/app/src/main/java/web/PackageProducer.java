package web;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import util.Common;

/**
 * Created by misiyu on 18-4-18.
 */

public class PackageProducer extends Thread {

    private int curSeq = 0;
    private PackageQueue packageQueue = null;
    private final int MAXFileSeq = 1000;
    private String packageDir = Common.PackageDir;
    private String basicFilename = Common.PackageBasicName;
    private boolean stop = false ;


    public PackageProducer(PackageQueue packageQueue){
        this.packageQueue = packageQueue ;
    }

    @Override
    public void run() {
        while (!this.stop){
            String filename = basicFilename+curSeq;
            if(Common.localDefenceMode){
                mydump2(filename);
            }else{
                mydump(filename);
            }
            try{
                packageQueue.put(filename);
            }catch (Exception e){
                e.printStackTrace();
            }
            curSeq = (curSeq+1)%MAXFileSeq ;
        }
    }

    public void mydump(String filename){

        System.out.println("===================tcpdum "+filename);
        String filter = "'" + Common.tcpdumpFilterRule + "'";
        String cmd ="su & /root/mydump.sh " + filter  +" "+filename;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
            if(process.exitValue() == 0){
                System.out.println("ok");
            }else{
                System.out.println("no ok");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line ;
            while((line = br.readLine()) != null) {sb.append(line).append("\n");}
            System.out.println(sb.toString());


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void mydump2(String filename){
        String filter = "'" + Common.tcpdumpFilterRule + "'";
        String cmd ="su & /root/mydump2.sh " + filter  +" "+filename;
        try{
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
            if(process.exitValue() == 0){
                System.out.println("ok");
            }else{
                System.out.println("no ok");
            }
        }catch (Exception e){

        }
    }

    /*
    a0:88:69:81:d2:1a > 60:0b:03:2c:30:02, ethertype IPv4 (0x0800), length 1322: (tos 0x0, ttl 64, id 36915, offset 0, flags [DF], proto TCP (6), length 1308)
            172.31.221.32.52268 > 220.181.112.244.443: Flags [P.], seq 2248196749:2248198017, ack 3961367534, win 347, length 1268
    a0:88:69:81:d2:1a > 60:0b:03:2c:30:02, ethertype IPv4 (0x0800), length 1440: (tos 0x0, ttl 64, id 24597, offset 0, flags [DF], proto TCP (6), length 1426)
            172.31.221.32.52256 > 220.181.112.244.443: Flags [.], seq 2310624108:2310625494, ack 2032961849, win 347, length 1386
    */
    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
