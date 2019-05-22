package com.example.misiyu.mecds21;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import util.Common;
import view.FunSwitch;
import view.SubmitButton;

/**
 * Created by misiyu on 18-4-24.
 */

public class SetingActivity extends Activity {

    private SubmitButton clear_iptables_button , clear_cache;
    private ImageView settingImageView1 , settingImageView2 ;
    private int imageView1src = 0;
    private TextView textView3_2 ;
    private View view1,view2 ;
    private FunSwitch settingSwitch1,settingSwitch2 ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);

        view1 = (View)findViewById(R.id.setting_item1);
        view2 = (View)findViewById(R.id.setting_item2);
        clear_iptables_button = (SubmitButton)findViewById(R.id.seting_button1);
        clear_iptables_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String cmd = "su & iptables -F" ;
                    Runtime.getRuntime().exec(cmd);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        MyListener myListener = new MyListener();
        settingImageView1 = (ImageView)findViewById(R.id.seting_image1);
        settingImageView1.setOnClickListener(myListener);
        view1.setOnClickListener(myListener);

        settingImageView2 = (ImageView)findViewById(R.id.seting_image2);
        settingImageView2.setOnClickListener(myListener);
        view2.setOnClickListener(myListener);

        textView3_2 = (TextView) findViewById(R.id.setting_textview3_2);
        textView3_2.setText(getCacheSize());

        clear_cache = (SubmitButton)findViewById(R.id.seting_button2);
        clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String cmd = "su & rm "+Common.PackageDir+"cap*" ;
                    Runtime.getRuntime().exec(cmd);
                    textView3_2.setText("");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        settingSwitch2 = (FunSwitch)findViewById(R.id.seting_switch4);
        settingSwitch2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(!settingSwitch2.getState()) {
                        //start
                        //Common.localDefenceMode = true ;
                        settingSwich2Dialog();
                    }else{
                        System.out.println("stop ==================");
                        Common.localDefenceMode = false ;
                    }
                }
                return false;
            }
        });

    }

    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(view == settingImageView2 || view == view2){
                Intent intent = new Intent(SetingActivity.this,IptablesActivity.class);
                startActivity(intent);
            }else if(view == settingImageView1 || view == view1){
                if(imageView1src ==0){
                    settingImageView1.setImageResource(R.drawable.setting_image1_yellow);
                    imageView1src = 1;
                }else if(imageView1src == 1){
                    settingImageView1.setImageResource(R.drawable.setting_image1_green);
                    imageView1src = 2;
                }else if(imageView1src==2){
                    settingImageView1.setImageResource(R.drawable.setting_image1_red);
                    imageView1src = 0;
                }
            }
        }
    }

    private String getCacheSize(){
        Process process = null;
        String result= "";
        try{
            String cmd = "du -ch "+ Common.PackageDir ;
            process = Runtime.getRuntime().exec(cmd);

            process.waitFor();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while((line = bufferedReader.readLine())!=null){
                result = line;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  result ;
    }

    private void settingSwich2Dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("启动LAN-MEC模式");
        builder.setMessage("1. 保证您的移动设备与一个MEC服务器在同一局域网内\n" +
                "2. 该模式将会利用Arp欺骗以截取流向移动设备流量\n" );
        builder.setCancelable(true);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Common.localDefenceMode = true ;
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                settingSwitch2.setState(false);
            }
        });

        builder.create().show();
    }
}
