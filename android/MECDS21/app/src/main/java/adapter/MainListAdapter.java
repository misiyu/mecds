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

import view.MainListItem;

/**
 * Created by misiyu on 18-4-9.
 */

public  class  MainListAdapter extends BaseAdapter {
    private LayoutInflater infalter;
    private List<MainListItem> itemsList = new ArrayList<MainListItem>();
    public MainListAdapter(Context context) {
        infalter = LayoutInflater.from(context);
    }


    public synchronized void addItem(MainListItem mainListItem){
        this.itemsList.add(mainListItem);
    }

    //清空list
    public synchronized void clear(){
        this.itemsList.clear();
    }

    @Override
    public int getCount() {
        return this.itemsList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.itemsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = this.infalter.inflate(R.layout.item_image,null);

        MainListItem mainListItem = itemsList.get(i);
        ImageView imageView = (ImageView)view.findViewById(R.id.iv_item);
        TextView ipTextView = (TextView)view.findViewById(R.id.IPtextView);
        TextView degreeTextView = (TextView)view.findViewById(R.id.degreeTextView);
        TextView timeTextView = (TextView)view.findViewById(R.id.item_textview_time) ;

        imageView.setImageResource(mainListItem.getImageId());
        ipTextView.setText(mainListItem.getIp());
        degreeTextView.setText(mainListItem.getDegree()+"");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data = df.format(new Date());
        timeTextView.setText(data);

        return view;
    }
}
