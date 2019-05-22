package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.misiyu.mecds21.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by misiyu on 18-5-10.
 */

public class SearchListAdapter extends BaseAdapter {

    private LayoutInflater infalter;
    private List<String> ips = new ArrayList<>();
    public SearchListAdapter(Context context) {
        infalter = LayoutInflater.from(context);
    }


    public synchronized void addItem(String ip){
        this.ips.add(ip);
    }

    //清空list
    public synchronized void clear(){
        this.ips.clear();
    }

    @Override
    public int getCount() {
        return this.ips.size();
    }

    @Override
    public Object getItem(int i) {
        String[] strings = this.ips.get(i).split(",");
        return strings[0];

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = this.infalter.inflate(R.layout.item_search_list,null);

        ImageView imageView = (ImageView)view.findViewById(R.id.search_list_image);
        TextView ipTextView = (TextView)view.findViewById(R.id.search_iptext);
        TextView degreeTextView = (TextView)view.findViewById(R.id.search_recText);
        TextView timeTextView = (TextView)view.findViewById(R.id.search_timeText) ;

        String[] strings1 = ips.get(i).split(",");
        ipTextView.setText(strings1[0]);
        degreeTextView.setText(strings1[1]+" 跳");
        //if(i!=0) {degreeTextView.setVisibility(View.INVISIBLE);}

        String time = null;
        if(Integer.parseInt(strings1[1]) == 0) time = "同一局域网中";
        else time = "与本设备距离："+strings1[1]+" 跳";
        timeTextView.setText(time);

        return view;
    }
}
