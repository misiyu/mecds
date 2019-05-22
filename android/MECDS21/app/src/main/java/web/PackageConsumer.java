package web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import adapter.MainListAdapter;
import util.Common;

/**
 * Created by misiyu on 18-4-18.
 */

public class PackageConsumer extends Thread {

    private PackageQueue packageQueue = null;
    private String fileDir = Common.PackageDir;
    //private String fileDir = "123";
    private boolean stop = false ;

    private ArpSpoofHttp arpSpoofHttp ;
    //private UploadFileByHttp uploadFileByHttp ;
    private GetArpResultThread getArpResultThread ;
    private List<String> hasSpoofIp = new ArrayList<>();


    public PackageConsumer(PackageQueue packageQueue,MainListAdapter mla){
        this.packageQueue = packageQueue ;
        //this.uploadFileByHttp = new UploadFileByHttp(mla);
        this.arpSpoofHttp = new ArpSpoofHttp();
        this.getArpResultThread = new GetArpResultThread(mla);
    }

    @Override
    public void run() {
        //if(Common.localDefenceMode) {
            this.getArpResultThread.start();
        //}
        while(!this.stop || !packageQueue.isEmpty()){
            try {
                String filename = packageQueue.get();
                String filePath = fileDir+filename ;
                if(Common.localDefenceMode){
                    consumer2(filePath);
                }else{
                    //uploadFileByHttp.upload(filePath,filename);
                    new UploadFileByHttp(filePath,filename).start();
                }
                //删除文件 ， 这里可加一条上传是否成功的判断语句
                //File file = new File(filePath);
                //file.delete();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(Common.localDefenceMode) {
            this.getArpResultThread.setStop(true);
        }
    }


    public void getArpTable(Map<String,String> arpTable){
        Process process = null;
        try {

            process = Runtime.getRuntime().exec("cat /proc/net/arp");
            process.waitFor();
            if(process.exitValue() == 0){
                System.out.println("ok");
            }else{
                System.out.println("no ok");
            }
            InputStream is = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            bufferedReader.readLine();
            while((line = bufferedReader.readLine())!=null){
                //System.out.println(line);
                String[] strs = line.split("  *");
                //System.out.println(strs[0]+" "+strs[3]);
                arpTable.put(strs[3],strs[0]);
            }
            bufferedReader.close();
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void consumer2(String filePath){
        Map<String,String> arpTable = new HashMap<>();
        getArpTable(arpTable);

        Iterator it = arpTable.keySet().iterator();
        while(it.hasNext()){
            String key = (String)it.next();
            System.out.println(key + " " + arpTable.get(key));
        }

        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        String suspectIp = "";
        Map<String,Integer> map = new HashMap<>();
        String synCode = "[S]";
        String ackCode = "[R]";
        System.out.println("consumer ========== "+ filePath);
        try{
            fileInputStream = new FileInputStream(filePath);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line1 = "";
            while((line1 = bufferedReader.readLine())!=null){
                String line2 = bufferedReader.readLine();

                //System.out.println(line1);

                int value = 0;
                if(line2.indexOf(synCode) != -1){
                    value = 1;
                }else if(line2.indexOf(ackCode)!=-1){
                    value = -1 ;
                }else{
                    continue;
                }
                //System.out.println(line1);
                String macAddr = line1.substring(0,17);
                Integer num = map.get(macAddr);
                if(num == null || num == 0){
                    map.put(macAddr,value);
                }else{
                    map.put(macAddr,num+value);
                }
            }

            Iterator<String> iterator = map.keySet().iterator();
            while(iterator.hasNext()){
                String macAddr = iterator.next();
                System.out.println("================"+macAddr);
                Integer num = map.get(macAddr);
                String iptmp = arpTable.get(macAddr);
                if(num >= 40 && !hasSpoofIp.contains(iptmp)) {
                    suspectIp += " -t "+ iptmp ;
                    hasSpoofIp.add(iptmp);
                }
            }
            if(!suspectIp.equals("")){
                System.out.println("==========http "+ suspectIp);

                arpSpoofHttp.transferSuspectIp(suspectIp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(fileInputStream!=null){
            try{
                fileInputStream.close();
                bufferedReader.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
