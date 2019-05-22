package util;

import android.content.Context;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.util.List;

/**
 * Created by misiyu on 18-5-5.
 */

public class MyBDLocation {
    private LocationClient locationClient = null;
    private LocationClientOption clientOption = new LocationClientOption();
    private String time ;
    private String locationMessage ;
    private double latitude ;    //纬度
    private double longitude ;  //经度
    private float radius;
    private String country;
    private String city ;
    private String district;
    private String street ;
    private String addr ;
    private int isIndoor ;
    private String locationDesrcibe ;
    private List<Poi> poi = null;
    private boolean isSuccess = false ;
    private TextView textView =null;

    private MyBDLocation(Context context, TextView textView){
        locationClient = new LocationClient(context);
        this.optionSetting();
        this.locationClient.setLocOption(this.clientOption);
        locationClient.registerLocationListener(new MyLocationListener());
        this.textView = textView;
    }


    public MyBDLocation(Context context){
        locationClient = new LocationClient(context);
        this.optionSetting();
        this.locationClient.setLocOption(this.clientOption);
        locationClient.registerLocationListener(new MyLocationListener());
    }
    public void getLocationOnce(){
        this.locationClient.start();
    }
    private class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            radius = location.getRadius();
            time = location.getTime();
            country = location.getCountry();
            city = location.getCity();
            district = location.getDistrict();
            street = location.getStreet();
            isIndoor = location.getUserIndoorState();
            addr = location.getAddrStr();
            locationDesrcibe = location.getLocationDescribe();
            poi = location.getPoiList();
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
            locationClient.stop();
            System.out.println(locationMessage);
            System.out.println(addr);
        }


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

    public String getTime() {
        return time;
    }

    public String getLocationMessage() {
        return locationMessage;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getRadius() {
        return radius;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getStreet() {
        return street;
    }

    public String getAddr() {
        return addr;
    }

    public int getIsIndoor() {
        return isIndoor;
    }

    public String getLocationDesrcibe() {
        return locationDesrcibe;
    }
}
