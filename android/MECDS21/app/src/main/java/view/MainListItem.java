package view;

import com.example.misiyu.mecds21.R;

import util.MainListItemType;

/**
 * Created by misiyu on 18-4-9.
 */

public class MainListItem {
    private int imageId ;
    private String ip ;
    private int degree ;
    private MainListItemType type = null;
    private String recommendDefence = null;
    public MainListItem(int imageId , String ip , int degree){
        this.imageId = imageId ;
        this.ip = ip ;
        this.degree = degree ;
        this.setImageId();
        this.type = MainListItemType.ip;
        recommendDefence = "封锁IP: "+ip;

    }
    public MainListItem(String str){
        if(str.indexOf(':') == -1) this.type = MainListItemType.ip;
        else this.type = MainListItemType.port;
        String[] strs = str.split(",");
        if(this.type == MainListItemType.ip) {
            this.ip = "ip:" + strs[0];
            this.recommendDefence = "封锁ip:\n" + strs[0];
        }
        else if (this.type == MainListItemType.port){
            String[] sstemp = strs[0].split(":");
            this.ip = "port:" + sstemp[0];
            this.recommendDefence = "封锁TTL:" + sstemp[1];
        }
        this.degree = Integer.parseInt(strs[1]);
        this.setImageId();
        this.type = MainListItemType.ip;

    }

    private void setImageId(){
        if (degree >=70 )
            this.imageId = R.drawable.ring_red;
        else if(degree >= 40 && degree < 70)
            this.imageId = R.drawable.ring_yellow;
        else if(degree >= 0 && degree < 40)
            this.imageId = R.drawable.ring_green;
    }
    public int getImageId() {
        return imageId;
    }

    public String getIp() {
        return ip;
    }

    public int getDegree() {
        return degree;
    }

    public String getRecommendDefence() {
        return recommendDefence;
    }
}
