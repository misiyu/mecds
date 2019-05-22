package com.example.misiyu.mecds21;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by misiyu on 18-5-5.
 */

public class GetMasterIpActivity extends Activity {

    View view1 ,view2 ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getmasterip);
        view1 = (View) findViewById(R.id.getmasterip_hand);
        MyOnclickListener myOnclickListener = new MyOnclickListener();
        view1.setOnClickListener(myOnclickListener);
        view2 = (View)findViewById(R.id.getmasterip_location);
        view2.setOnClickListener(myOnclickListener);
    }

    class MyOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(view == view1){
                Intent intent = new Intent(GetMasterIpActivity.this,SetIPActivity.class);
                startActivity(intent);
            }else if(view == view2){
                Intent intent = new Intent(GetMasterIpActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        }
    }
}
