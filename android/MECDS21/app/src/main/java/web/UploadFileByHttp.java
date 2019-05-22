package web;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import adapter.MainListAdapter;
import util.Common;
import view.MainListItem;

/**
 * Created by misiyu on 18-4-18.
 */

public class UploadFileByHttp extends Thread{
    private String uploadIp = Common.MasterIp;
    //private String uploadIp = "10.42.0.233";//"192.168.0.126";
    private String urlPath = "http://"+uploadIp+"/android/file_upload.php" ;

    private String filePath,filename;
    private MainListAdapter mainListAdapter = null;

    public UploadFileByHttp(MainListAdapter mla){
        this.mainListAdapter = mla ;
        //this.mainListAdapter = MainActivity.mainListAdapter ;
    }
    public UploadFileByHttp(String filePath,String filename){
        this.filePath = filePath ;
        this.filename = filename ;
    }
    public void run(){
        upload2(this.filePath,this.filename);
    }

    public boolean upload2(String filePath,String filename){
        HttpURLConnection connection = null;
        try{
            URL url = new URL(this.urlPath);
            connection = (HttpURLConnection)url.openConnection();
            //设置每次传输流的大小，防止手机因内存不足崩溃
            connection.setChunkedStreamingMode(51200);  //128k
            connection.setUseCaches(false);  //不使用缓存
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedReader inbufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            OutputStream os = connection.getOutputStream();

            String line1 = null;
            String abuffer = "filename="+filename+"&content=\n";

            int lineCount = 0;
            while((line1 = inbufferedReader.readLine()) != null){
                lineCount ++ ;
                String line2 = inbufferedReader.readLine();
                line1 = line1 +"," +line2 +"\n";
                line1 = line1.replaceAll(">|:",",").replaceAll("ttl|Flags|  *","");
                String strsTemp[] = line1.split(",");
                String line3 = strsTemp[1]+","+strsTemp[7]+","+strsTemp[8]+","+strsTemp[9] + "\n";
                abuffer = abuffer+line3 ;
                if(lineCount%50 == 0){
                    os.write(abuffer.getBytes(),0,abuffer.length());
                    os.flush();
                    abuffer="";
                }

            }
            os.write(abuffer.getBytes(),0,abuffer.length());
            os.flush();
            os.close();
            inbufferedReader.close();
            fileInputStream.close();
            //获取返回数据
            if(connection.getResponseCode() == 200){
                connection.disconnect();

            }else{
                System.out.println("==============================请检查网络 "+connection.getResponseCode());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        return true ;
    }


    public boolean upload(String filePath,String filename){
        HttpURLConnection connection = null;
        try{
            URL url = new URL(this.urlPath);
            connection = (HttpURLConnection)url.openConnection();
            //设置每次传输流的大小，防止手机因内存不足崩溃
            connection.setChunkedStreamingMode(51200);  //128k
            connection.setUseCaches(false);  //不使用缓存
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedReader inbufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            OutputStream os = connection.getOutputStream();

            String line1 = null;
            String abuffer = "filename="+filename+"&content=\n";

            int lineCount = 0;
            while((line1 = inbufferedReader.readLine()) != null){
                lineCount ++ ;
                String line2 = inbufferedReader.readLine();
                line1 = line1 +"," +line2 +"\n";
                line1 = line1.replaceAll(">|:",",").replaceAll("ttl|Flags|  *","");
                String strsTemp[] = line1.split(",");
                String line3 = strsTemp[1]+","+strsTemp[7]+","+strsTemp[8]+","+strsTemp[9] + "\n";
                abuffer = abuffer+line3 ;
                if(lineCount%50 == 0){
                    os.write(abuffer.getBytes(),0,abuffer.length());
                    os.flush();
                    abuffer="";
                }

            }
            os.write(abuffer.getBytes(),0,abuffer.length());

            os.flush();
            os.close();


            inbufferedReader.close();
            fileInputStream.close();


            //获取返回数据
            if(connection.getResponseCode() == 200){
                System.out.println("发送成功");
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


                String strTmp = builder.toString().replace("(","");
                updateMainList(strTmp);

            }else{
                System.out.println("==============================请检查网络 "+connection.getResponseCode());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        return true ;
    }

    private void updateMainList(String message){
        mainListAdapter.clear();
        String[] suspectIP = message.split("\\)");
        int size = suspectIP.length;
        for(int i = 0 ;i < size ; i++){
            System.out.println(suspectIP[i]);
            if(suspectIP[i].indexOf(',')!=-1)
                mainListAdapter.addItem(new MainListItem(suspectIP[i]));
        }
        Common.refreshMainList(mainListAdapter);
    }
}
