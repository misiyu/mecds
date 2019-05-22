package web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import adapter.MainListAdapter;
import util.Common;
import view.MainListItem;

/**
 * Created by misiyu on 18-5-8.
 */

public class GetArpResultThread extends Thread {
    private String uploadIp = Common.MasterIp;
    //private String uploadIp = "192.168.0.126";

    private MainListAdapter mainListAdapter ;
    private boolean stop =false ;

    public GetArpResultThread(MainListAdapter mla){
        this.mainListAdapter = mla ;
    }

    @Override
    public void run() {
        while(!this.stop){

            try {
                Thread.sleep(3000);
            }catch (Exception e){
                e.printStackTrace();
            }
            getResult();
        }
    }

    public boolean getResult(){
        String urlPath = "http://"+uploadIp+"/android/get_arpspoof_result.php" ;
        try{

            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);  //不使用缓存
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream os = connection.getOutputStream();
            String str = "spoof_ip="+Common.localIp;
            //System.out.println("Post_arg============"+str);
            os.write(str.getBytes());
            os.flush();
            os.close();
            if(connection.getResponseCode() == 200){
                InputStreamReader inputStreamReader =
                        new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null)
                    builder.append(line);

                bufferedReader.close();
                inputStreamReader.close();
                //System.out.println(builder.toString());


                String strTmp = builder.toString().replace("(","");
                updateMainList(strTmp);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
    private void updateMainList(String message){
        if(message.equals("")) return;
        mainListAdapter.clear();
        String[] suspectIP = message.split("\\)");
        int size = suspectIP.length;
        for(int i = 0 ;i < size ; i++){
            //System.out.println(suspectIP[i]);
            if(suspectIP[i].indexOf(',')!=-1)
                mainListAdapter.addItem(new MainListItem(suspectIP[i]));
        }
        Common.refreshMainList(mainListAdapter);
    }
}
