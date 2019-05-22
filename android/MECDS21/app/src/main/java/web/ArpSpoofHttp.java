package web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import util.Common;

/**
 * Created by misiyu on 18-5-6.
 */

public class ArpSpoofHttp {
    private String uploadIp = Common.MasterIp;
    //private String uploadIp = "192.168.0.126";


    public ArpSpoofHttp(){
    }

    public boolean transferSuspectIp(String suspectIp){
        String urlPath = "http://"+uploadIp+"/android/arp_spoof.php" ;
        try{

            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);  //不使用缓存
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream os = connection.getOutputStream();
            String str = "arp_target="+suspectIp+"&spoof_ip="+Common.localIp;
            System.out.println("Post_arg============"+str);
            os.write(str.getBytes());
            os.flush();
            os.close();
            if(connection.getResponseCode() == 200){
                System.out.println("transfer suspect ip " + suspectIp +" ok");
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }


    public boolean stopArpspoof(String killIp){
        String urlPath = "http://"+uploadIp+"/android/stop_arpspoof.php?killIp="+killIp;
        try{

            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(connection.getResponseCode() == 200){
                System.out.println("close aprspoof success 66666666666666666666666");

                InputStreamReader inputStreamReader =
                        new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null)
                    builder.append(line);
                bufferedReader.close();
                inputStreamReader.close();
                System.out.println(builder.toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }


}
