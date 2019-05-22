package com.example.misiyu.mecds21;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import util.Common;
import view.SubmitButton;
import web.ArpSpoofHttp;

public class DetailActivity extends Activity {
    private TextView head , suspect , attackType,otherMessage , recommendDefence;
    private SubmitButton defenceButton ;

    private String ip ;
    private String ttl ;
    private boolean ipOrPort = true ;
    private String port ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);

        head = (TextView)findViewById(R.id.report_head);
        suspect = (TextView)findViewById(R.id.report_suspect);
        attackType = (TextView)findViewById(R.id.report_attack_type);
        otherMessage = (TextView)findViewById(R.id.report_other_message);
        recommendDefence = (TextView)findViewById(R.id.report_recommend_defence);

        defenceButton = (SubmitButton)findViewById(R.id.report_button);

        String iptmp = getIntent().getStringExtra("ip");
        String degree = getIntent().getStringExtra("degree");
        String recommendText = getIntent().getStringExtra("recommend");
        head.setText(iptmp);

        String strs[] = iptmp.split(":");
        this.ip = strs[1];
        this.port = strs[1];
        otherMessage.setText("其它信息: 收到syn数-"+degree);

        recommendDefence.setText(recommendText);

        if(recommendText!=null && recommendText.indexOf("TTL")!=-1){

            String[] stringsTmp = recommendText.split(":");
            this.ttl = stringsTmp[1];
            this.ipOrPort = false ;
        }

        int degreeInt = Integer.parseInt(degree);
        if(degreeInt >= 70){
            suspect.setText("可疑度： 99.99%");
        }else{
            double susD = degreeInt*100/70.0;
            suspect.setText("可疑度： "+Math.round(susD*100)/100+"%");
        }


        defenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String cmd = null;
                    if(ipOrPort == true){
                        cmd = "su & iptables -A INPUT -s "+ip+" -j DROP";
                        Common.tcpdumpFilterRule = Common.tcpdumpFilterRule + "&&(not host "+ip+ ")";
                    }else{
                        cmd = "su & iptables -A INPUT -p tcp --dport "+port+" -m ttl --ttl-eq "+ttl+" -j DROP";
                        Common.tcpdumpFilterRule = Common.tcpdumpFilterRule + "&&!((dst port "+port+")&&(ip[8] == "+ttl+"))";
                    }

                    Process process = Runtime.getRuntime().exec(cmd);
                    process.waitFor();
                    if(process.exitValue() == 0){
                        System.out.println("ok =====================");
                    }else{
                        System.out.println("no ok =====================");
                    }


                    if(Common.localDefenceMode){
                        new StopArpSpoofThread().start();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    private class StopArpSpoofThread extends Thread{
        @Override
        public void run() {
            ArpSpoofHttp arpSpoofHttp = new ArpSpoofHttp();
            arpSpoofHttp.stopArpspoof(ip);
        }
    }
}
