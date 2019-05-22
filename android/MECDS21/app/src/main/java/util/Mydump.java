package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by misiyu on 18-4-22.
 */

public class Mydump extends Thread {

    private boolean stop = false ;
    private String cmd ="su & /root/mydump.sh" ;
    //private String filterRule = "(tcp)";
    private String filter = null;
    @Override
    public void run() {
        //while(!stop)
        {
            filter = "'" + Common.tcpdumpFilterRule + "'";
            cmd = "su & /root/mydump.sh " + filter ;
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
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
