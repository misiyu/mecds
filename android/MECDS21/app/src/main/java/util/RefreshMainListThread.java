package util;

import adapter.MainListAdapter;

/**
 * Created by misiyu on 18-4-23.
 */

public class RefreshMainListThread extends Thread {
    private MainListAdapter mainListAdapter = null;

    public RefreshMainListThread(MainListAdapter mla){
        this.mainListAdapter = mla ;
    }

    @Override
    public void run() {
        this.mainListAdapter.notifyDataSetChanged();
    }
}
