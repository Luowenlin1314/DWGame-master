package com.third.dwgame.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.third.dwgame.R;
import com.third.dwgame.base.ActivityFragmentInject;
import com.third.dwgame.base.BaseActivity;

/**
 * 作者：Sky on 2018/6/3.
 * 用途：//TODO
 */
@ActivityFragmentInject(
        contentViewId = R.layout.activity_copy,
        hasNavigationView = false,
        hasToolbar = false
)
public class CopyActivity extends BaseActivity {

    private TextView txtFileName;

    @Override
    protected void toHandleMessage(Message msg) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findViewAfterViewCreate() {
        txtFileName = (TextView) findViewById(R.id.txt_file_name);
    }

    @Override
    protected void initDataAfterFindView() {
        registUsbDataReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegistUsbDataReceiver();
    }

    private void unRegistUsbDataReceiver(){
        if(usbDataReceiver != null){
            unregisterReceiver(usbDataReceiver);
            usbDataReceiver = null;
        }
    }

    private void registUsbDataReceiver(){
        if(usbDataReceiver == null){
            usbDataReceiver = new UsbDataReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_UPDATE);
            intentFilter.addAction(ACTION_END);
            registerReceiver(usbDataReceiver, intentFilter);
        }
    }


    public static final String ACTION_UPDATE = "intent.action.string.file.update";
    public static final String ACTION_END = "intent.action.string.file.end";
    private UsbDataReceiver usbDataReceiver;
    private class UsbDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_UPDATE)){
                String fileName = intent.getStringExtra("fileName");
                txtFileName.setText("Copying：" + fileName);

            }else if(action.equals(ACTION_END)){
                txtFileName.setText("Copy complete，afte 3 seconds exit");
                mHandler.sendEmptyMessageDelayed(1,3000);
            }
        }

    }

}
