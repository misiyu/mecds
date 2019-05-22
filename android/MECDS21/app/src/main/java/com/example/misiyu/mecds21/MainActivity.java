package com.example.misiyu.mecds21;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

import adapter.MainListAdapter;
import util.Common;
import util.Mydump;
import util.Util;
import view.DragLayout;
import view.DragLayout.DragListener;
import view.FunSwitch;
import view.MainListItem;
import web.SpoolManager;

import java.io.FileInputStream;
import java.util.Random;

public class MainActivity extends Activity {

	private DragLayout dl;
	private ListView list_img ;
	private MainListAdapter mainListAdapter ;
	private ListView lv;
	private TextView tv_noimg;
	private ImageView iv_icon;
	private FunSwitch funSwitch ;
	public static Handler handler = new Handler();
	private SpoolManager spoolManager = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Common.verifyStoragePermissions(this);

		Util.initImageLoader(this);
		initDragLayout();
		initView();
	}

	private void initDragLayout() {
		dl = (DragLayout) findViewById(R.id.dl);
		dl.setDragListener(new DragListener() {
			@Override
			public void onOpen() {
				lv.smoothScrollToPosition(new Random().nextInt(30));
			}

			@Override
			public void onClose() {
				shake();
			}

			@Override
			public void onDrag(float percent) {
				ViewHelper.setAlpha(iv_icon, 1 - percent);
			}
		});
	}

	private void initView() {
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		list_img = (ListView) findViewById(R.id.gv_img);
		tv_noimg = (TextView) findViewById(R.id.iv_noimg);
		funSwitch = (FunSwitch)findViewById(R.id.startSwitch);

		getLastestMasterIp() ;
		Common.localIp = getlocalIp();
		Common.tcpdumpFilterRule = Common.tcpdumpFilterRule+"&&(not src host "+Common.localIp+")";

		list_img.setFastScrollEnabled(true);
		mainListAdapter = new MainListAdapter(MainActivity.this);

		spoolManager = new SpoolManager(mainListAdapter);
		mainListAdapter.addItem(new MainListItem("192.168.0.165,71"));
		mainListAdapter.addItem(new MainListItem("9000:62,54"));
		mainListAdapter.addItem(new MainListItem("192.168.160.123,28"));
		mainListAdapter.addItem(new MainListItem(R.drawable.lay_icn_ring,"1:以上为警报样例",0));
		mainListAdapter.addItem(new MainListItem(R.drawable.lay_icn_ring,"2:右滑设置masterIP",0));
		mainListAdapter.addItem(new MainListItem(R.drawable.lay_icn_ring,"3:右滑打开监控开关",0));
		list_img.setAdapter(mainListAdapter);
		list_img.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

				Intent intent = new Intent(MainActivity.this,DetailActivity.class);

				MainListItem mainListItem = (MainListItem)mainListAdapter.getItem(position);
				intent.putExtra("ip",mainListItem.getIp());
				intent.putExtra("degree",mainListItem.getDegree()+"");
				intent.putExtra("recommend",mainListItem.getRecommendDefence());


				startActivity(intent);

			}
		});

		lv = (ListView) findViewById(R.id.lv);
		lv.setAdapter(new ArrayAdapter<String>(MainActivity.this,
				R.layout.item_text, new String[] { "发现MEC服务器", "一键防御",
						"查封历史", "清空防火墙","设置" }));
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
				Util.t(getApplicationContext(), "click " + position);
				if(position == 0){
					Intent intent = new Intent(MainActivity.this,GetMasterIpActivity.class);
					startActivity(intent);
				}else if(position == 1){
					Intent intent = new Intent(MainActivity.this,LoginActivity.class);
					startActivity(intent);
				}else if(position == 2){
					Intent intent = new Intent(MainActivity.this,IptablesActivity.class);
					startActivity(intent);
				}else if(position == 3){
					showFireWallDialog();
				}else if(position == 4){
					Intent intent = new Intent(MainActivity.this,SetingActivity.class);
					startActivity(intent);
				}
			}
		});
		iv_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dl.open();
			}
		});



		funSwitch.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {

				if(motionEvent.getAction() == MotionEvent.ACTION_UP){
					if(!funSwitch.getState()) {
						showDialog(Common.MasterIp);
					}else{
						System.out.println("stop ==================");
						spoolManager.stopThread();
					}
				}
				return false;
			}
		});
	}

	private void showDialog(String ip){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("启动流量监控");
		String msg = "当前master ip 为：" + ip ;
		if(Common.localDefenceMode) {msg = msg + "\n当前处于LAN-MEC模式" ;}
		builder.setMessage(msg);
		builder.setCancelable(true);
		builder.setPositiveButton("启动", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				spoolManager.start();
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				funSwitch.setState(false);
			}
		});
		builder.create().show();
	}

	private void getLastestMasterIp(){
		String historyFile = "history";
		byte[] buffer = new byte[1024];
		FileInputStream fileInputStream = null;
		String strtmp = "";
		try{
			fileInputStream = openFileInput(historyFile);
			int len = 0;
			while((len = fileInputStream.read(buffer))!=-1){
				String tmp = new String(buffer,0,len);
				strtmp += tmp;
			}
			fileInputStream.close();
			String strs[] = strtmp.split(",");
			if(strs.length >= 1){
				Common.MasterIp = strs[0];
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	private void shake() {
		iv_icon.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
	}

	private void showFireWallDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("清空防火墙");
		builder.setMessage("清空您所有防火墙规则?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				try {
					Runtime.getRuntime().exec("su & iptables -F");
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		builder.create().show();
	}

	private String getlocalIp(){
		WifiManager wm=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		//检查Wifi状态
		if(!wm.isWifiEnabled())
			wm.setWifiEnabled(true);
		WifiInfo wi=wm.getConnectionInfo();
		//获取32位整型IP地址
		int ipAdd=wi.getIpAddress();
		//把整型地址转换成“*.*.*.*”地址
		String ip=intToIp(ipAdd);
		return ip;
	}
	private String intToIp(int i) {
		return (i & 0xFF ) + "." +
				((i >> 8 ) & 0xFF) + "." +
				((i >> 16 ) & 0xFF) + "." +
				( i >> 24 & 0xFF) ;
	}
}
