package web;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.Common;

/**
 * Created by misiyu on 18-5-5.
 */

public class GetMasterIpHttp extends Thread {
    private String webIp = "140.143.8.104";
    private String urlPath = "http://"+webIp+"/android/getMasterIp.php" ;
    //private String urlPath = "http://localhost/android/getMasterIp.php" ;
    private double latitude ;
    private double longitude ;


    public GetMasterIpHttp(double latitude , double longitude){
        this.latitude = latitude ;
        this.longitude = longitude ;
    }
    public GetMasterIpHttp(double latitude , double longitude,String webIp){
        this.urlPath = "http://"+webIp+"/android/getMasterIp.php" ;
        this.latitude = latitude ;
        this.longitude = longitude ;
    }

    public String getMasterIp(){
        String result = "192.168.0.126";

        HttpURLConnection connection = null;
        try {
            URL url = new URL(this.urlPath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream os = connection.getOutputStream();
            String postMessage = "latitude="+latitude+"&longitude="+longitude ;

            os.write(postMessage.getBytes());
            os.flush();
            os.close();
            if(connection.getResponseCode() == 200){
                System.out.println("发送成功");
                InputStreamReader inputStreamReader =
                        new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                result = bufferedReader.readLine();
                bufferedReader.close();
                inputStreamReader.close();
            }else{
                System.out.println("==============================请检查网络 "+connection.getResponseCode());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result ;
    }

    private class IPDistance implements Comparable<IPDistance>{

        public  String ip ;
        public int dis ;
        public IPDistance(String ip , int dis){
            this.ip = ip ;
            this.dis = dis ;
        }
        @Override
        public int compareTo(@NonNull IPDistance ipDistance) {
            if(this.dis > ipDistance.dis) return 1 ;
            if(this.dis < ipDistance.dis) return -1 ;
            return 0;
        }
    }
    public String conCheck(String ipstr){
        String result = "";
        String[] ips = ipstr.split(":");
        List<IPDistance> list = new ArrayList<>();
        int ttl = 0;
        for(String ip:ips){
            if((ttl=ping(ip)) != -1) list.add(new IPDistance(ip,ttl));
        }
        Collections.sort(list);
        boolean first = true ;
        for(IPDistance ipd :list){
            if(first)
            {
                result = result+ipd.ip+","+ipd.dis;
                first = false ;
            }
            else result = result+":"+ipd.ip+","+ipd.dis;
        }
        return result ;
    }
    public int ping(String ip){
        int ttl = -1;
        Process process = null;
        try {
            String cmd = "ping -c 1 "+ip ;
            process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
            if(process.exitValue() != 0) return ttl;
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            br.readLine();
            String line = br.readLine();
            String[] strings1 = line.split(" ");
            System.out.println(strings1[5]);
            String[] strings2 = strings1[5].split("=");

            int ttltmp = Integer.parseInt(strings2[1]);
            if(ttltmp >=0 && ttltmp<=64) ttl = 64-ttltmp;
            else if(ttltmp > 64 && ttltmp <= 64*2) ttl = 64*2-ttltmp;
            else if(ttltmp > 64*2 && ttltmp <= 64*3) ttl = 64*3 - ttltmp;
            else if(ttltmp > 64*3 && ttltmp <= 64*4) ttl = 64*4 - ttltmp;

            br.close();
            isr.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ttl;
    }
    @Override
    public void run() {
        String ipsTemp = getMasterIp();
        //System.out.println(ipsTemp);
        String masterIp = conCheck(ipsTemp);
        Common.showSearchResultDialog(masterIp);
    }
}
