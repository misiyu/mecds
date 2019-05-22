package com.example.misiyu.mecds21;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import util.Common;
import util.FileIO;
import view.SubmitButton;

/**
 * Created by misiyu on 18-4-9.
 */

public class SetIPActivity extends Activity {

    private SubmitButton setMasterIpSubmit ;
    private EditText masterIp ;
    private ListView masterIpHistoryList ;
    private String historyFile =  "history";
    private List<String> iplist =new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setip);
        setMasterIpSubmit = (SubmitButton)findViewById(R.id.master_ip_submit);
        masterIp = (EditText)findViewById(R.id.master_ip_editText);
        masterIpHistoryList = (ListView)findViewById(R.id.master_ip_history);
        this.readHistory();
        iplist.add("192.168.60.1");
        arrayAdapter = new ArrayAdapter<String>(SetIPActivity.this,R.layout.item_text,iplist);
        masterIpHistoryList.setAdapter(arrayAdapter);

        masterIpHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                masterIp.setText(iplist.get(position));
            }
        });

        setMasterIpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = masterIp.getText().toString();
                showDialog(str);
                Common.MasterIp = str ;
                iplist.add(0,str);
                arrayAdapter.notifyDataSetChanged();
                writeHistory();

                //StringBuilder stringBuilder= fileIO.read();
                //System.out.println(stringBuilder.toString()+"=========================");
            }
        });
    }
    private void showDialog(String ip){
        new AlertDialog.Builder(this)
                .setTitle("ip设置")
                .setMessage("将masterip设置为: "+ip)
                .setPositiveButton("是",null)
                .setNegativeButton("否",null)
                .show() ;
    }



    private void readHistory(){
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        String strtmp = "";
        iplist.clear();
        try{
            fileInputStream = openFileInput(historyFile);
            int len = 0;
            while((len = fileInputStream.read(buffer))!=-1){
                String tmp = new String(buffer,0,len);
                strtmp += tmp;
            }
            fileInputStream.close();
            String strs[] = strtmp.split(",");
            for(String str : strs){
                iplist.add(str);
                if(iplist.size() > 10) break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void writeHistory(){
        String strtmp = "";
        int size = iplist.size();
        for(int i = 0 ;i < size ; i++){
            strtmp+=iplist.get(i);
            if(i<size-1) strtmp = strtmp+",";
        }
        try{
            FileOutputStream fileOutputStream = openFileOutput(historyFile, Context.MODE_PRIVATE);
            fileOutputStream.write(strtmp.getBytes());
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
