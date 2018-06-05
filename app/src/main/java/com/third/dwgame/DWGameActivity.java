package com.third.dwgame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.third.dwgame.activity.DWSettingActivity;
import com.third.dwgame.base.ActivityFragmentInject;
import com.third.dwgame.base.BaseActivity;
import com.third.dwgame.manager.SerialInterface;
import com.third.dwgame.utils.DWConstant;
import com.third.dwgame.widget.YTVideoView;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018/4/15.
 */
@ActivityFragmentInject(
        contentViewId = R.layout.activity_dwgame,
        hasNavigationView = false,
        hasToolbar = false
)
public class DWGameActivity extends BaseActivity {

    private static final int WHAT_TEST_COIN_IN = 10;
    private static final int WHAT_TIME_COUNT_DOWN = 11;
    private static final int WHAT_TEST_SPEED = 12;
    private static final int WHAT_SHOW_VIDEO = 13;


    private static final int WHAT_OPEN_SERIAL = 20;

    private RelativeLayout rlBgRoot;
    private YTVideoView ytVideoView;
    private TextView txtCoin;
    private TextView txtSpeed;

    private String dwFileRootPath;

    private int coin1 = 0,coin2 = 2;

    @Override
    protected void toHandleMessage(Message msg) {
        switch (msg.what){
            case WHAT_OPEN_SERIAL:
                openSerial();
                break;
            case WHAT_TEST_COIN_IN:
                playSound(1);
                break;
            case WHAT_TIME_COUNT_DOWN:
                showTimeCount();
                break;
            case WHAT_TEST_SPEED:
                break;
            case WHAT_SHOW_VIDEO:
                ytVideoView.setVideoPath((String) msg.obj);
                ytVideoView.play();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SerialInterface.serialInit(this);
        super.onCreate(savedInstanceState);
        mHandler.sendEmptyMessageDelayed(WHAT_OPEN_SERIAL,3000);
    }

    @Override
    protected void findViewAfterViewCreate() {
        rlBgRoot = (RelativeLayout) findViewById(R.id.rl_game_root);
        ytVideoView = (YTVideoView) findViewById(R.id.ytVideoView);
        txtCoin = (TextView) findViewById(R.id.txt_coin);
        txtSpeed = (TextView) findViewById(R.id.txt_speed);
    }

    @Override
    protected void initDataAfterFindView() {
        dwFileRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
        ytVideoViewOnCompletionListener();

    }

    @Override
    protected void onPause() {
        super.onPause();
        doOnPauseThings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doOnResumeThings();
    }

    @Override
    protected void onDestroy() {
        SerialInterface.closeAllSerialPort();
        super.onDestroy();

    }

    private void doOnPauseThings() {
        if (ytVideoView.isPlaying()) {
            ytVideoView.stopPlayback();
        }
        unRegisterDWProReceiver();
    }

    private void doOnResumeThings() {
        String path = ytVideoView.getVideoPath();
        if(!TextUtils.isEmpty(path)){
            ytVideoView.setVisibility(View.VISIBLE);
            ytVideoView.setVideoPath(path);
        }else{
            ytVideoView.setVisibility(View.GONE);
//            ytVideoView.setVideoPath(dwFileRootPath + "龙卷风1.mp4");
//            changeSpeedAndVideo(1);
//            changeSpeedAndVideo(1);c
        }
        registerDWProReceiver();
    }

    //设置播放完成的监听
    private void ytVideoViewOnCompletionListener() {
        ytVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ytVideoView.setVideoPath(ytVideoView.getVideoPath());
            }
        });
    }

    private DWProReceiver dwProReceiver;
    private void registerDWProReceiver(){
        dwProReceiver = new DWProReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DWConstant.INTENT_DW_COM);
        registerReceiver(dwProReceiver,intentFilter);
    }

    private void unRegisterDWProReceiver(){
        if(dwProReceiver != null){
            unregisterReceiver(dwProReceiver);
        }
    }

    private class DWProReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
             String msg = intent.getStringExtra("comValue");
             doDWSomething(msg);
        }
    }

    /**
     * 适合播放声音短，文件小
     * 可以同时播放多种音频
     * 消耗资源较小
     */
    private  SoundPool soundPool;
    private int coinIndex = 0;
    private  void playSound(int rawId) {
//        if(soundPool == null){
//            if (Build.VERSION.SDK_INT >= 21) {
//                SoundPool.Builder builder = new SoundPool.Builder();
//                //传入音频的数量
//                builder.setMaxStreams(1);
//                //AudioAttributes是一个封装音频各种属性的类
//                AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
//                //设置音频流的合适属性
//                attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
//                builder.setAudioAttributes(attrBuilder.build());
//                soundPool = builder.build();
//            } else {
//                //第一个参数是可以支持的声音数量，第二个是声音类型，第三个是声音品质
//                soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
//            }
//        }
//        //第一个参数Context,第二个参数资源Id，第三个参数优先级
//        soundPool.load(this, rawId, 1);
//        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                soundPool.play(1, 1, 1, 0, 0, 1);
//            }
//        });
        coinIndex++;
        txtCoin.setText(coinIndex + "/2");
        if(coinIndex >= 2){
            coinIndex = coinIndex - 2;
            coinEnough();
        }
        //第一个参数id，即传入池中的顺序，第二个和第三个参数为左右声道，第四个参数为优先级，第五个是否循环播放，0不循环，-1循环
        //最后一个参数播放比率，范围0.5到2，通常为1表示正常播放
//        soundPool.play(1, 1, 1, 0, 0, 1);
        //回收Pool中的资源
        //soundPool.release();

    }

    /**
     *
     */
    private int count = 5;
    private void coinEnough(){
        if(ytVideoView != null){
            if(ytVideoView.isPlaying()){
                ytVideoView.stopPlayback();
            }
        }
        ytVideoView.setVisibility(View.GONE);
        rlBgRoot.setBackgroundResource(R.drawable.v1_bg_main_center);
        txtCoin.setVisibility(View.GONE);
        mHandler.sendEmptyMessageDelayed(WHAT_TIME_COUNT_DOWN,800);
    }

    private void showTimeCount(){
        if(count == 5){
            rlBgRoot.setBackgroundResource(R.drawable.v1_time_5);
        }else if(count == 4){
            rlBgRoot.setBackgroundResource(R.drawable.v1_time_4);
        }else if(count == 3){
            rlBgRoot.setBackgroundResource(R.drawable.v1_time_3);
        }else if(count == 2){
            rlBgRoot.setBackgroundResource(R.drawable.v1_time_2);
        }else if(count == 1){
            rlBgRoot.setBackgroundResource(R.drawable.v1_time_1);
        }else{
            rlBgRoot.setBackgroundResource(R.drawable.v1_bg_main);
            txtCoin.setVisibility(View.VISIBLE);
            txtSpeed.setVisibility(View.VISIBLE);
            ytVideoView.setVisibility(View.VISIBLE);
            return;
        }
        count--;
        if(count >= 0){
            mHandler.sendEmptyMessageDelayed(WHAT_TIME_COUNT_DOWN,1000);
        }
    }

    private String lastPath = "";
    private double lastSpeed = 0.0;
    private void changeSpeedAndVideo(int index){
        Log.e("ZM","changeSpeedAndVideo = " + index);
        ytVideoView.setVisibility(View.VISIBLE);
        String tempPath = "";
        if(index == 1){
            tempPath = dwFileRootPath + "龙卷风1.mp4";
        }else if(index == 2){
            tempPath = dwFileRootPath + "龙卷风2.mp4";
        }else if(index == 3){
            tempPath = dwFileRootPath + "龙卷风3.mp4";
        }else if(index == 4){
            tempPath = dwFileRootPath + "龙卷风4.mp4";
        }else if(index == 5){
            tempPath = dwFileRootPath + "龙卷风5.mp4";
        }else if(index == 6){
            tempPath = dwFileRootPath + "龙卷风6.mp4";
        }else{
            tempPath = dwFileRootPath + "龙卷风1.mp4";
        }
//        if(TextUtils.isEmpty(lastPath)){
//            ytVideoView.setVideoPath(tempPath);
//            lastPath = tempPath;
//        }else{
//            if(!lastPath.equals(tempPath)){
//                lastPath = tempPath;
//            }
//        }
//        Message msg = Message.obtain();
//        msg.what = WHAT_SHOW_VIDEO;
//        msg.obj = tempPath;
//        mHandler.sendMessageDelayed(msg,500);
        ytVideoView.setVideoPath(tempPath);
        ytVideoView.play();
    }

    /**
     * 打开或关闭
     * @param flag
     */
    private void startOrStopVideo(boolean flag){
        if(!flag){
            if(ytVideoView.isPlaying()){
                ytVideoView.stopPlayback();
            }
            ytVideoView.setVideoPath("");
            ytVideoView.setVisibility(View.GONE);
        }else{
            ytVideoView.setVisibility(View.VISIBLE);
            if(ytVideoView.isPlaying()){
                ytVideoView.stopPlayback();
            }
        }
    }

    /**
     * double转String,保留小数点后两位
     * @param num
     * @return
     */
    public static String doubleToString(double num){
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.0").format(num);
    }


    private EditText edtPro;
    public void test(View view){
        edtPro = (EditText) findViewById(R.id.edt_pro);
        try{
            doDWSomething(edtPro.getText().toString());
        }catch (Exception e){

        }

    }

    /**
     * 协议处理
     * @param buf
     */
    private static final int LIMIT_LENGTH = 4 * 2;
    private String reciverString = "";
    private static final String PRO_HEAD = "5AA5";
    private static final String PRO_HEAD_VIDEO = "AABB";
    private static final String PRO_HEAD_1 = "5AA504";
    private static final String PRO_HEAD_2 = "5AA508";
    private static final String PRO_HEAD_3 = "5AA50A";
    private static final String PRO_HEAD_4 = "AABB09110070005500000000";   //显示视频窗口
    private static final String PRO_HEAD_5 = "AABB0216";   //播放片源
    private static final String PRO_HEAD_6 = "AABB0118";   //暂停
    private static final String PRO_HEAD_7 = "AABB0119";   //退出播放
    private static final String PRO_HEAD_1_2 = "5AA5088200802020";
    private static final String V2_GAME_OVER = "AABB0511007000";//AABB051100700055

    private int indexCount = 0;
    private long countTimeLimit = 0;
    private String lastProString = "";
    private void doDWSomething(String buf) {
        indexCount++;
        if(System.currentTimeMillis() - countTimeLimit < 200){
            countTimeLimit = System.currentTimeMillis();
            if(lastProString.equals(reciverString)){
                reciverString = "";
                return;
            }
        }
        reciverString += buf;
        lastProString = reciverString;
        if (!reciverString.contains(PRO_HEAD) && !reciverString.contains(PRO_HEAD_VIDEO)) {
            reciverString = "";
            return;
        } else {
            int startIndex = reciverString.indexOf(PRO_HEAD);
            if(startIndex == -1){
                startIndex = reciverString.indexOf(PRO_HEAD_VIDEO);
                reciverString = reciverString.substring(startIndex);
            }else{
                reciverString = reciverString.substring(startIndex);
            }
        }
        //5AA5 0582 0006
        //5AA5 0582 00 A0 0006
        //5AA5 0480 03 00 01
        //5AA5 0682 00 00 313233
        //5AA5 0582 00 04 0002
        try{
            System.out.println("reciverString-->"+reciverString);
            if (reciverString.length() >= LIMIT_LENGTH) {
                // 数据包最低长度
                if (reciverString.startsWith(PRO_HEAD_1)) {
                    if(reciverString.length() >= 14){
                        String page = reciverString.substring(12, 14);
                        //跳转到那一页
                        reciverString = reciverString.substring(14, reciverString.length());
                        Log.e("ZM", "page = " + page);
                        doHandlePage(page);
                    }else if(reciverString.equals("5AA50480")){
                        doHandlePage("01");
                        reciverString = reciverString.substring(8, reciverString.length());
                    }
                } else if (reciverString.startsWith(PRO_HEAD_2)) {
                    if(reciverString.contains(PRO_HEAD_1_2)){
                        if(reciverString.length() >= 22){
                            String num = reciverString.substring(16, 18);
                            String num2 = reciverString.substring(20, 22);
//                            handleCoins(num);
                            int coin1 = Integer.valueOf(num) - 30;
                            int coin2 = Integer.valueOf(num2) - 30;
                            txtCoin.setText(coin1 + "/" + coin2);
                            reciverString = reciverString.substring(22, reciverString.length());
                        }
                    }
                } else if (reciverString.startsWith(PRO_HEAD_3)) {
                    //显示的风速
                    if(reciverString.length() >= 26){
                        String showData = reciverString.substring(12, 20);
                        showSpeedData(showData);
                        reciverString = reciverString.substring(26, reciverString.length());
                    }
                } else if (reciverString.startsWith(PRO_HEAD_4)) {
                    if(reciverString.length() >= 24){
                        startOrStopVideo(true);
                        reciverString = reciverString.substring(24, reciverString.length());
                    }
                } else if (reciverString.startsWith(PRO_HEAD_5)) {
                    if(reciverString.length() >= 10){
                        startOrStopVideo(true);
                        String videoIndex = reciverString.substring(8, 10);
                        reciverString = reciverString.substring(10, reciverString.length());
                        changeSpeedAndVideo(Integer.valueOf(videoIndex));
                    }
                } else if (reciverString.startsWith(PRO_HEAD_6)) {
                    if(reciverString.length() >= 8){
                        ytVideoView.pause();
                        reciverString = reciverString.substring(8, reciverString.length());
                    }
                } else if (reciverString.startsWith(PRO_HEAD_7)) {
                    if(reciverString.length() >= 8){
                        startOrStopVideo(false);
                        reciverString = reciverString.substring(8, reciverString.length());
                    }
                }else if (reciverString.equals(V2_GAME_OVER)) {
                    if(reciverString.length() >= 14){
                        startOrStopVideo(false);
                        reciverString = reciverString.substring(14, reciverString.length());
                    }
                }else if (reciverString.equals(V2_GAME_OVER)) {
                    if(reciverString.length() >= 14){
                        startOrStopVideo(false);
                        reciverString = reciverString.substring(14, reciverString.length());
                    }
                }else{
                    reciverString = "";
                }

                if (reciverString.length() > 0) {
                    doDWSomething("");
                }
            }
        }catch (Exception e){
            Log.e("ZM",e.getMessage());
            reciverString = "";
        }

    }

    /**
     * 显示风速
     */
    private void showSpeedData(String showData){
        String first = showData.substring(0, 2);
        if(!first.equals("20")){
            first = (Integer.valueOf(showData.substring(0, 2)) - 30) + "";
        }else{
            first = "";
        }
        String sd = first
                + ""
                + (Integer.valueOf(showData.substring(2, 4)) - 30)
                + "."
                +(Integer.valueOf(showData.substring(6, 8)) - 30)
                + "MPH";
        txtSpeed.setText(sd);
    }

    /**
     * 处理页面跳转
     * @param page
     */
    private void doHandlePage(String page){
        if("01".equals(page)){
            coinEnough();
        }else if("20".equals(page)){

        } else if("08".equals(page)){
            Intent toSetting = new Intent(this,DWSettingActivity.class);
            startActivity(toSetting);
        }else if("09".equals(page)){
            rlBgRoot.setBackgroundResource(R.drawable.v1_bg_main_right);
            txtCoin.setVisibility(View.VISIBLE);
            txtSpeed.setVisibility(View.GONE);
        }
    }


    /**
     * 打开串口,固件串口3，波特率115200
     */
    public static String USEING_PORT = "/dev/ttyS5";
    public static int USEING_RATE = 115200;
    private void openSerial(){
        try {
            SerialInterface.openSerialPort(USEING_PORT,USEING_RATE);
            SerialInterface.changeActionReceiver(SerialInterface.getActions(USEING_PORT));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"串口打开错误，请检查串口是否正常！",Toast.LENGTH_LONG).show();
        }

    }
}
