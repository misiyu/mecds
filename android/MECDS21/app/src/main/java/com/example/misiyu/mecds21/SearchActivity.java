package com.example.misiyu.mecds21;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import adapter.SearchListAdapter;
import util.Common;
import view.SubmitButton;
import web.GetMasterIpHttp;

/**
 * Created by misiyu on 18-5-4.
 */

public class SearchActivity extends Activity {

    private TextView textView1 ;
    private LocationClient locationClient = null;
    private LocationClientOption clientOption = new LocationClientOption();
    private double latitude;
    private double longitude ;
    private boolean isSuccess = false ;
    private SubmitButton button1 ;
    private CardView searchCard1 ;
    private View searchLargeView2 ;
    private static SearchListAdapter searchListAdapter ;
    private static CardView searchCard2 ;
    private static TextView  searchResult2;

    private ListView searchListView ;

    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String masterIp = (String) msg.obj;
            //searchCard2.setVisibility(View.VISIBLE);
            searchListAdapter.clear();
            String[] ips = masterIp.split(":");
            for(String s:ips){ searchListAdapter.addItem(s); }
            searchCard2.setVisibility(View.VISIBLE);
            searchListAdapter.notifyDataSetChanged();
            System.out.println(masterIp);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        textView1 = (TextView)findViewById(R.id.search_textview1);
        textView1.setVisibility(View.VISIBLE);
        searchCard2 = (CardView)findViewById(R.id.search_card2);
        searchCard1 = (CardView)findViewById(R.id.search_card1);
        searchLargeView2 = (View) findViewById(R.id.search_largeView2);
        //searchResult = (TextView)findViewById(R.id.search_result_textview);
        searchResult2 = (TextView)findViewById(R.id.search_textview3);
        locationClient = new LocationClient(getApplicationContext());
        optionSetting();
        locationClient.setLocOption(clientOption);
        locationClient.registerLocationListener(new MyLocationListener());
        locationClient.start();

        button1 = (SubmitButton)findViewById(R.id.search_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchCard1.setVisibility(View.INVISIBLE);
                searchLargeView2.setVisibility(View.VISIBLE);
                GetMasterIpHttp getMasterIpHttp = new GetMasterIpHttp(latitude,longitude);
                getMasterIpHttp.start();
                //showDialog(result);
            }
        });



        View v1 = (View)findViewById(R.id.search_progressView1);


        this.searchListAdapter = new SearchListAdapter(getApplicationContext());
        this.searchListView = (ListView)findViewById(R.id.search_listview);
        searchListView.setAdapter(searchListAdapter);
        searchListView.setFastScrollEnabled(true);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ip = (String) searchListAdapter.getItem(i);
                Common.MasterIp = ip ;
                showDialog(ip);
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

    private void optionSetting(){
        clientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        clientOption.setCoorType("bd09ll");
        clientOption.setScanSpan(1000);
        clientOption.setOpenGps(true);
        clientOption.setLocationNotify(true);
        clientOption.setIgnoreKillProcess(false);
        clientOption.SetIgnoreCacheException(false);
        clientOption.setWifiCacheTimeOut(5*60*1000);
        clientOption.setEnableSimulateGps(false);

        clientOption.setIsNeedAddress(true);
        clientOption.setIsNeedLocationDescribe(true);
        clientOption.setIsNeedAltitude(true);
        clientOption.setIsNeedLocationPoiList(true);
        clientOption.setNeedDeviceDirect(true);

    }

    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            String locationMessage = "";
            textView1.append(location.getTime()+"\n");
            //textView1.append("当前位置\n");
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            textView1.append("纬度 : " + latitude+"\n");
            textView1.append("经度 : " + longitude+"\n");
            textView1.append("精度 : " + location.getRadius() + "米\n");
            textView1.append("地址 : " + location.getAddrStr() + ",\n");
            textView1.append("       " +location.getLocationDescribe()+"\n");
            //poi = location.getPoiList();
            textView1.append("\n周边 : "+"\n\n");// POI信息
            if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                for (int i = 0; i < location.getPoiList().size(); i++) {
                    Poi poi = (Poi) location.getPoiList().get(i);
                    textView1.append(poi.getName() + ";"+"\n");
                }
            }
            int locTypeTmp = location.getLocType();
            if(locTypeTmp == BDLocation.TypeGpsLocation){
                locationMessage = "GPS定位成功";
                isSuccess = true ;
            }else if(BDLocation.TypeNetWorkLocation == locTypeTmp){
                locationMessage = "网络定位成功";
                isSuccess = true ;
            }else if (locTypeTmp == BDLocation.TypeServerError) {
                locationMessage = "服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因"+"\n";
            } else if (locTypeTmp == BDLocation.TypeNetWorkException) {
                locationMessage = "网络不同导致定位失败，请检查网络是否通畅"+"\n";
            } else if (locTypeTmp == BDLocation.TypeCriteriaException) {
                locationMessage ="无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机"+"\n";
            }
            textView1.append("\n"+locationMessage +"\n");
            locationClient.stop();
            String str = textView1.getText().toString();
            System.out.println(str);
            searchResult2.setText(str);
            System.out.println(latitude);
        }
    }

}
