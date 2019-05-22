package com.example.misiyu.mecds21;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by misiyu on 18-4-25.
 */

public class IptablesActivity extends Activity {

    private TextView textView1 ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iptables);

        textView1 = (TextView)findViewById(R.id.iptables_textview);

        try{
            Process process = Runtime.getRuntime().exec("su & iptables -L");
            process.waitFor();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while((line = bufferedReader.readLine())!=null){
                textView1.append(line+"\n");
            }
            bufferedReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
