package util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.ActivityCompat;

import com.example.misiyu.mecds21.MainActivity;
import com.example.misiyu.mecds21.SearchActivity;

import adapter.MainListAdapter;

/**
 * Created by misiyu on 18-4-10.
 */

public class Common {
    public static String MasterIp ="10.42.0.233";//"192.168.0.126";//"10.42.0.1" ;
    public static final String PackageDir = Environment.getExternalStorageDirectory().getPath()+"/mecds/";
    public static final String PackageBasicName = "cap";
    public static String tcpdumpFilterRule = "(tcp)";
    public static String localIp = "";
    public static boolean autoDefenceOpen = false ;
    public static boolean localDefenceMode = false ;


    //动态申请SDcard读写权限
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
    };

    public static void verifyStoragePermissions(Activity activity){
        try{
            int permission = ActivityCompat.checkSelfPermission(activity,"android.permission.WRITE_EXTERNAL_STORAGE");
            if(permission != PackageManager.PERMISSION_GRANTED){
                //Activity.requestPermissions(activity,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }


    public static void refreshMainList(MainListAdapter mainListAdapter){
        MainActivity.handler.post(new RefreshMainListThread((mainListAdapter)));
    }

    public static void showSearchResultDialog(String masterIp){
        Message message = new Message();

        message.obj = masterIp;

        SearchActivity.handler.sendMessage(message);
    }



}
