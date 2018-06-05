package com.third.dwgame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.third.dwgame.base.ActivityFragmentInject;
import com.third.dwgame.base.BaseActivity;
import com.third.dwgame.manager.SerialInterface;
import com.third.dwgame.utils.DWConstant;
import com.third.dwgame.utils.PreferenceUtils;
import com.third.dwgame.widget.YTVideoView;

/**
 * Created by Administrator on 2018/4/15.
 */
@ActivityFragmentInject(
        contentViewId = R.layout.activity_dwgame_v2,
        hasNavigationView = false,
        hasToolbar = false
)
public class DWGameV2Activity extends BaseActivity {

    private static final int WHAT_TEST_COIN_IN = 10;
    private static final int WHAT_TIME_COUNT_DOWN = 11;
    private static final int WHAT_TEST_SPEED = 12;
    private static final int WHAT_TEST_SPEED1 = 22;
    private static final int WHAT_SHOW_VIDEO = 13;
    private static final int WHAT_CHANGE_LEFT_ICON = 14;
    private static final int WHAT_SEND_AA = 15;

    private static final int WHAT_OPEN_SERIAL = 20;

    private RelativeLayout rlBgRoot;
    private RelativeLayout rlSettingRoot;
    private YTVideoView ytVideoView;
    private TextView txtCoin;
    private TextView txtSpeed;
    private String dwFileRootPath;
    private TextView txtGameOverTicket;

    private ImageView imgGameConfigure;
    private ImageView imgGameConfigure1;
    private ImageView imgGameConfigure2;
    private ImageView imgAccount;
    private ImageView imgHardware;
    private ImageView imgHardware1;
    private ImageView imgSaveChanges;
    private ImageView imgAccountYes;
    private ImageView imgAccountNo;
    private ImageView imgTxtMiddle;
    private ImageView imgTxtLeft;
    private ImageView imgTxtRight;
    private ImageView imgTxtStrobe;
    private ImageView imgTxtSmoke;
    private TextView txtMiddle;
    private TextView txtLeft;
    private TextView txtRight;
    private TextView txtStrobe;
    private TextView txtSmoke;
    private TextView txtCoinPlay;
    private TextView txtTicketPlay;
    private TextView txtCoinTotal;
    private TextView txtTicketTotal;
    private TextView txtGamePlayTotal;


    @Override
    protected void toHandleMessage(Message msg) {
        switch (msg.what){
            case WHAT_OPEN_SERIAL:
                openSerial();
                break;
            case WHAT_TEST_COIN_IN:
                break;
            case WHAT_TIME_COUNT_DOWN:
                showTimeCount();
                break;
            case WHAT_CHANGE_LEFT_ICON:
                handleMenuLeftIcons("02");
                break;
            case WHAT_TEST_SPEED1:
                changeVideo(4);
                break;
            case WHAT_TEST_SPEED:
                doDWSomething("5AA50480030008");
                break;
            case WHAT_SHOW_VIDEO:
                doDWSomething("AABB0511007000555AA504800300005AA50982008020202020205AA5088200802020302F33");
                break;
            case WHAT_SEND_AA:
                sendProAA();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        PreferenceUtils.init(this);
        SerialInterface.serialInit(this);
        super.onCreate(savedInstanceState);
        mHandler.sendEmptyMessageDelayed(WHAT_OPEN_SERIAL,2000);
    }

    @Override
    protected void findViewAfterViewCreate() {
        rlBgRoot = (RelativeLayout) findViewById(R.id.rl_game_root);
        rlSettingRoot = (RelativeLayout) findViewById(R.id.rl_setting_root);
        ytVideoView = (YTVideoView) findViewById(R.id.ytVideoView);
        txtCoin = (TextView) findViewById(R.id.txt_coin);
        txtSpeed = (TextView) findViewById(R.id.txt_speed);

        imgGameConfigure = (ImageView) findViewById(R.id.img_flag_game);
        imgGameConfigure1 = (ImageView) findViewById(R.id.img_flag_game_1);
        imgGameConfigure2 = (ImageView) findViewById(R.id.img_flag_game_2);
        imgAccount = (ImageView) findViewById(R.id.img_flag_account);
        imgHardware = (ImageView) findViewById(R.id.img_flag_hardware);
        imgHardware1 = (ImageView) findViewById(R.id.img_flag_hardware1);
        imgSaveChanges = (ImageView) findViewById(R.id.img_flag_save);
        imgAccountYes = (ImageView) findViewById(R.id.img_account_yes);
        imgAccountNo = (ImageView) findViewById(R.id.img_account_no);

        imgTxtMiddle = (ImageView) findViewById(R.id.img_txt_middle_blower);
        imgTxtLeft = (ImageView) findViewById(R.id.img_txt_left_blower);
        imgTxtRight = (ImageView) findViewById(R.id.img_txt_right_blower);
        imgTxtStrobe = (ImageView) findViewById(R.id.img_txt_strobe);
        imgTxtSmoke = (ImageView) findViewById(R.id.img_txt_smoke);

        txtMiddle = (TextView) findViewById(R.id.txt_middle_blower);
        txtLeft = (TextView) findViewById(R.id.txt_left_blower);
        txtRight = (TextView) findViewById(R.id.txt_right_blower);
        txtStrobe = (TextView) findViewById(R.id.txt_strobe);
        txtSmoke = (TextView) findViewById(R.id.txt_smoke);

        txtCoinPlay = (TextView) findViewById(R.id.txt_coin_play);
        txtTicketPlay = (TextView) findViewById(R.id.txt_tickets_play);
        txtCoinTotal = (TextView) findViewById(R.id.txt_coin_total);
        txtTicketTotal = (TextView) findViewById(R.id.txt_ticket_total);
        txtGamePlayTotal = (TextView) findViewById(R.id.txt_game_play_total);
        txtGameOverTicket = (TextView) findViewById(R.id.txt_game_over_ticket);
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
//        doVideo();
        registerDWProReceiver();
    }

    private void doVideo(){
        String path = ytVideoView.getVideoPath();
        if(!TextUtils.isEmpty(path)){
            ytVideoView.setVisibility(View.VISIBLE);
            ytVideoView.setVideoPath(path);
        }else{
            String videoPath = PreferenceUtils.getString("videoPath","");
            if(TextUtils.isEmpty(videoPath)){
                ytVideoView.setVisibility(View.GONE);
            }else{
                ytVideoView.setVisibility(View.VISIBLE);
                ytVideoView.setVideoPath(videoPath);
            }
        }
    }

    //设置播放完成的监听
    private void ytVideoViewOnCompletionListener() {
        ytVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(isGameing){
                    ytVideoView.setVideoPath(ytVideoView.getVideoPath());
                }else{
                    int index = getNextIndex(ytVideoView.getVideoPath());
                    changeVideo(index);
                }
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
            Log.e("ZM","msg = " + msg);
            if(!TextUtils.isEmpty(msg)){
                if(msg.length() == 64
                        && msg.lastIndexOf("5AA50480") > 0
                        && msg.contains("5AA50A82000020302E304D5048")){
                    mHandler.sendEmptyMessageDelayed(WHAT_TIME_COUNT_DOWN,500);
                }
            }
            doDWSomething(msg);
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

        //发送AA协议
        mHandler.sendEmptyMessageDelayed(WHAT_SEND_AA,600);
    }

    /**
     * 投币处理
     * @param coin
     * @param total
     */
    private void handleChangeCoins(String coin0, String coin, String total){
        //显示游戏页
        showGameOrSetting(true);
        changeCoins(coin0,coin,total);
    }

    /**
     * 投币处理超过10个
     * @param coin
     * @param total
     */
    private void handleChangeCoins2(String coin,String coin2,
                                    String total,String total2){
        //显示游戏页
        showGameOrSetting(true);
        int c1 = (Integer.valueOf(coin) - 30);
        int c2 = (Integer.valueOf(coin2) - 30);
        int t1 = (Integer.valueOf(total) - 30);
        int t2 = (Integer.valueOf(total2)- 30);
        if(c1 < 0){
            changeCoins2( c2 + "",t1 + "" + t2);
        }else{
            changeCoins2(c1 + "" + c2 + "",t1 + "" + t2);
        }
    }

    /**
     * 页面跳转
     * @param page
     */
    private long timeCountLimit = 0;
    private boolean isGameing = false;
    private void handleChangePage(String page){
        if("01".equals(page)){
            if(System.currentTimeMillis() - timeCountLimit < 2000){
                return;
            }
            isGameing = true;
            timeCountLimit = System.currentTimeMillis();
            txtGameOverTicket.setVisibility(View.GONE);
            showTimeCount();
        } else if("08".equals(page)){
            showGameOrSetting(false);
            showLeftIconIndex("02");
            txtGameOverTicket.setVisibility(View.GONE);
            txtCoinPlay.setText(gTotal + "");
//            mHandler.sendEmptyMessageDelayed(WHAT_CHANGE_LEFT_ICON,400);
        }else if("09".equals(page)){
            rlBgRoot.setBackgroundResource(R.drawable.v1_bg_main_game_over);
            txtCoin.setVisibility(View.VISIBLE);
            txtSpeed.setVisibility(View.GONE);
            txtGameOverTicket.setVisibility(View.VISIBLE);
//            txtGameOverTicket.setText("TICKETS  " + tkTotal);
            setYtVideoVisiable(false);
        }else if("00".equals(page)){
            isGameing = false;
            showGameOrSetting(true);
            changeVideo(1);
            rlBgRoot.setBackgroundResource(R.drawable.v1_bg_main_right);
            txtCoin.setVisibility(View.VISIBLE);
            txtSpeed.setVisibility(View.GONE);
            txtGameOverTicket.setVisibility(View.GONE);
//            if(!TextUtils.isEmpty(cpString)){
//                txtCoin.setText(gIn + "/" + cpString);
//            }
        }
    }


    /**
     * 倒计时画面
     */
    private void goCountDown(){
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


    /**
     * 进入倒计时画面
     */
    private int count = 5;
    private void showTimeCount(){
        if(count == 5){
            mHandler.removeMessages(WHAT_TIME_COUNT_DOWN);
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
            count = 5;
            rlBgRoot.setBackgroundResource(R.drawable.v1_bg_main);
            txtCoin.setVisibility(View.VISIBLE);
            txtSpeed.setVisibility(View.VISIBLE);
            ytVideoView.setVisibility(View.VISIBLE);
            return;
        }
        txtCoin.setVisibility(View.GONE);
        txtSpeed.setVisibility(View.GONE);
        ytVideoView.setVisibility(View.GONE);
        count--;
        if(count >= 0){
            mHandler.sendEmptyMessageDelayed(WHAT_TIME_COUNT_DOWN,1000);
        }
    }


    /**
     * 处理风速
     * @param speed
     */

    private synchronized void handleSpeed(String speed){
        showGameOrSetting(true);
        rlBgRoot.setBackgroundResource(R.drawable.v1_bg_main);
        txtCoin.setVisibility(View.VISIBLE);
        txtSpeed.setVisibility(View.VISIBLE);
        changeSpeed(speed);
        txtGameOverTicket.setVisibility(View.GONE);
//        if(!ytVideoView.isPlaying()){
//            setYtVideoVisiable(true);
//            changeVideo(1);
//        }
    }

    /**
     * 更改播放视频
     * @param index
     */
    private void handleVideoChange(int index){
        showGameOrSetting(true);
        changeVideo(index);
    }

    /**
     * 更改菜单coin
     */
    private void handleMenuConsChange(String coin){
        showGameOrSetting(false);
        menuCoins(coin);
    }

    /**
     * 更改菜单ticket
     * @param ticket
     */
    private void handleMenuTicketsChange(String ticket){
        showGameOrSetting(false);
        menuTickets(ticket);
    }

    /**
     * 更改菜单total coin
     * @param ticket
     */
    private void handleMenuTotalCoinsChange(String ticket){
        showGameOrSetting(false);
        menuTotalCoin(ticket);
    }

    /**
     * 更改菜单total ticket
     * @param ticket
     */
    private void handleMenuTotalTicketsChange(String ticket){
        showGameOrSetting(false);
        menuTotalTickets(ticket);
    }

    /**
     * 更改菜单total game play
     * @param ticket
     */
    private void handleMenuTotalGamePlayChange(String ticket){
        showGameOrSetting(false);
        menuTotalGamePlay(ticket);
    }

    private void handleMenuAccountYes(){
        //显示菜单页
        showGameOrSetting(false);
        //具体icon
//        showLeftIconIndex("05");
        showItemIconIndex(0);
    }

    private void handleMenuAccountNo(){
        //显示菜单页
        showGameOrSetting(false);
        //具体icon
//        showLeftIconIndex("05");
        showItemIconIndex(1);
        txtCoinTotal.setText("0");
        txtTicketTotal.setText("0");
        txtGamePlayTotal.setText("0");
    }

    /**
     * 处理菜单协议
     * @param index
     */
    private void handleMenuLeftIcons(String index){
        //显示菜单页
        showGameOrSetting(false);
        //具体icon
        showLeftIconIndex(index);
        showItemIconIndex(-1);
    }

    /**
     * 设置币数量
     * @param coin
     */
    private String cpString = "";
    private void menuCoins(String coin){
//        handleMenuLeftIcons("03");
        Integer iCoin = Integer.parseInt(coin,16);
        txtCoinPlay.setText(iCoin + "");
        cpString = iCoin + "";
    }

    /**
     * 设置彩票数量
     * @param coin
     */
    private int tkTotal = 3;
    private void menuTickets(String coin){
//        handleMenuLeftIcons("04");
        Integer iCoin = Integer.parseInt(coin,16);
        txtTicketPlay.setText(iCoin + "");
        tkTotal = iCoin;
    }

    /**
     * 设置总彩票数量
     * @param coin
     */
    private void menuTotalCoin(String coin){
//        handleMenuLeftIcons("05");
        Integer iCoin = Integer.parseInt(coin,16);
        txtCoinTotal.setText(iCoin + "");
    }

    /**
     * 设置彩票数量
     * @param coin
     */
    private void menuTotalTickets(String coin){
//        handleMenuLeftIcons("05");
        Integer iCoin = Integer.parseInt(coin,16);
        txtTicketTotal.setText(iCoin + "");
    }

    /**
     * 设置彩票数量
     * @param coin
     */
    private void menuTotalGamePlay(String coin){
//        handleMenuLeftIcons("05");
        Integer iCoin = Integer.parseInt(coin,16);
        txtGamePlayTotal.setText(iCoin + "");
    }

    /**
     * 处理硬件测试
     * @param index
     */
    private void handleHardware(String index){
//        handleMenuLeftIcons("06");
        showItemIconIndex(Integer.valueOf(index));
    }



    /**
     * 显示游戏页或者设置页
     * @param game
     */
    private void showGameOrSetting(boolean game){
        if(game){
            rlBgRoot.setVisibility(View.VISIBLE);
            rlSettingRoot.setVisibility(View.GONE);
        }else{
            setYtVideoVisiable(false);
            rlBgRoot.setVisibility(View.GONE);
            rlSettingRoot.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 改变游戏币
     * @param coin
     */
    private int gTotal = 3;
    private String gIn = "0";
    private synchronized void changeCoins(String coin0,String coin,String total){
        gTotal = (Integer.valueOf(total) - 30);
        int c0 = (Integer.valueOf(coin0) - 30);
        gIn = (Integer.valueOf(coin) - 30)  + "";
        if(c0 > 0){
            txtCoin.setText(c0 + "" + gIn + "/" + gTotal);
        }else{
            txtCoin.setText(gIn + "/" + gTotal);
        }
    }

    /**
     * 改变游戏币
     * @param coin
     */
    private synchronized void changeCoins2(String coin,String total){
        gTotal = (Integer.valueOf(total));
        txtCoin.setText(coin + "/" + gTotal);
    }

    /**
     * 改变风速
     * @param showData
     */
    private void changeSpeed(String showData){
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
     * 视频控件显示或隐藏
     * @param flag
     */
    private synchronized void setYtVideoVisiable(boolean flag){
        if(!flag){
            if(ytVideoView.isPlaying()){
                ytVideoView.stopPlayback();
            }
            ytVideoView.setVisibility(View.GONE);
        }else{
            ytVideoView.setVisibility(View.VISIBLE);
            if(ytVideoView.isPlaying()){
                ytVideoView.stopPlayback();
            }
        }
    }


    /**
     * 改变视频
     * @param index
     */
    private synchronized void changeVideo(int index){
        ytVideoView.setVisibility(View.VISIBLE);
        String tempPath = "";
        if(index == 0){
            tempPath = dwFileRootPath + "龙卷风1.mp4";
        }else if(index == 1){
            tempPath = dwFileRootPath + "龙卷风2.mp4";
        }else if(index == 2){
            tempPath = dwFileRootPath + "龙卷风3.mp4";
        }else if(index == 3){
            tempPath = dwFileRootPath + "龙卷风4.mp4";
        }else if(index == 4){
            tempPath = dwFileRootPath + "龙卷风5.mp4";
        }else if(index == 5){
            tempPath = dwFileRootPath + "龙卷风6.mp4";
        }else{
            tempPath = dwFileRootPath + "龙卷风2.mp4";
        }
        ytVideoView.setVideoPath(tempPath);
//        ytVideoView.play();
    }

    private synchronized int getNextIndex(String path){
        if(TextUtils.isEmpty(path)){
            return 1;
        }

        if(path.contains("龙卷风2.mp4")){
            return 2;
        }else if(path.contains("龙卷风3.mp4")){
            return 3;
        }else if(path.contains("龙卷风4.mp4")){
            return 4;
        }else if(path.contains("龙卷风5.mp4")){
            return 5;
        }else if(path.contains("龙卷风6.mp4")){
            return 1;
        }

        return 1;
    }

    /**
     * 显示选择标记
     * @param index
     */
    private void showLeftIconIndex(String index){
        if(index.equals("00")){
            imgGameConfigure.setVisibility(View.VISIBLE);
            imgGameConfigure1.setVisibility(View.INVISIBLE);
            imgGameConfigure2.setVisibility(View.INVISIBLE);
            imgAccount.setVisibility(View.INVISIBLE);
            imgHardware.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.VISIBLE);
            imgSaveChanges.setVisibility(View.INVISIBLE);
        }else if(index.equals("02")){
            imgGameConfigure.setVisibility(View.VISIBLE);
            imgGameConfigure1.setVisibility(View.INVISIBLE);
            imgGameConfigure2.setVisibility(View.INVISIBLE);
            imgAccount.setVisibility(View.INVISIBLE);
            imgHardware.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.VISIBLE);
            imgSaveChanges.setVisibility(View.INVISIBLE);
        }else if(index.equals("03")){
            imgGameConfigure.setVisibility(View.INVISIBLE);
            imgGameConfigure1.setVisibility(View.VISIBLE);
            imgGameConfigure2.setVisibility(View.INVISIBLE);
            imgAccount.setVisibility(View.INVISIBLE);
            imgHardware.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.VISIBLE);
            imgSaveChanges.setVisibility(View.INVISIBLE);
        }else if(index.equals("04")){
            imgGameConfigure.setVisibility(View.INVISIBLE);
            imgGameConfigure1.setVisibility(View.INVISIBLE);
            imgGameConfigure2.setVisibility(View.VISIBLE);
            imgAccount.setVisibility(View.INVISIBLE);
            imgHardware.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.VISIBLE);
            imgSaveChanges.setVisibility(View.INVISIBLE);
        }else if(index.equals("05")) {
            imgGameConfigure.setVisibility(View.INVISIBLE);
            imgGameConfigure1.setVisibility(View.INVISIBLE);
            imgGameConfigure2.setVisibility(View.INVISIBLE);
            imgAccount.setVisibility(View.VISIBLE);
            imgHardware.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.VISIBLE);
            imgSaveChanges.setVisibility(View.INVISIBLE);
        }else if(index.equals("06")){
            imgGameConfigure.setVisibility(View.INVISIBLE);
            imgGameConfigure1.setVisibility(View.INVISIBLE);
            imgGameConfigure2.setVisibility(View.INVISIBLE);
            imgAccount.setVisibility(View.INVISIBLE);
            imgHardware.setVisibility(View.VISIBLE);
            imgHardware1.setVisibility(View.VISIBLE);
            imgSaveChanges.setVisibility(View.INVISIBLE);
        }/*else if(index.equals("07")){
            imgGameConfigure.setVisibility(View.INVISIBLE);
            imgGameConfigure1.setVisibility(View.INVISIBLE);
            imgGameConfigure2.setVisibility(View.INVISIBLE);
            imgAccount.setVisibility(View.INVISIBLE);
            imgHardware.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.VISIBLE);
            imgSaveChanges.setVisibility(View.INVISIBLE);
        }*/else if(index.equals("07")){
            imgGameConfigure.setVisibility(View.INVISIBLE);
            imgGameConfigure1.setVisibility(View.INVISIBLE);
            imgGameConfigure2.setVisibility(View.INVISIBLE);
            imgAccount.setVisibility(View.INVISIBLE);
            imgHardware.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.VISIBLE);
            imgSaveChanges.setVisibility(View.VISIBLE);
        }
        showItemIconIndex(-1);
    }


    /**
     * 显示item标记
     * @param index
     */
    private void showItemIconIndex(int index){
        if(index == 0){
            imgAccountYes.setVisibility(View.VISIBLE);
            imgAccountNo.setVisibility(View.INVISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.VISIBLE);
        }else if(index == 1){
            imgAccountYes.setVisibility(View.INVISIBLE);
            imgAccountNo.setVisibility(View.VISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.VISIBLE);
        }else if(index == 2){
//            imgAccountYes.setVisibility(View.INVISIBLE);
//            imgAccountNo.setVisibility(View.VISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.VISIBLE);
        }else if(index == 3){
//            imgAccountYes.setVisibility(View.INVISIBLE);
//            imgAccountNo.setVisibility(View.VISIBLE);
            imgTxtMiddle.setVisibility(View.VISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.INVISIBLE);
        }else if(index == 4){
//            imgAccountYes.setVisibility(View.INVISIBLE);
//            imgAccountNo.setVisibility(View.VISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.VISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.INVISIBLE);
        }else if(index == 5){
//            imgAccountYes.setVisibility(View.INVISIBLE);
//            imgAccountNo.setVisibility(View.VISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.VISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.INVISIBLE);
        }else if(index == 6){
//            imgAccountYes.setVisibility(View.INVISIBLE);
//            imgAccountNo.setVisibility(View.VISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.VISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.INVISIBLE);
        }else if(index == 7){
//            imgAccountYes.setVisibility(View.INVISIBLE);
//            imgAccountNo.setVisibility(View.VISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.VISIBLE);
            imgHardware1.setVisibility(View.INVISIBLE);
        }else{
//            imgAccountYes.setVisibility(View.INVISIBLE);
//            imgAccountNo.setVisibility(View.VISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 协议处理
     * @param buf
     */
    //5A A5 0D 82 00 90 54 49 43 4B 45 54 53 20 31 30
    //5A A5 0D 82 00 90 54 49 43 4B 45 54 53 39 39 39
    private long countTimeLimit = 0;
    private String lastProString = "";
    private static final int LIMIT_LENGTH = 4 * 2;
    private String reciverString = "";
    private static final String PRO_HEAD = "5AA5";
    private static final String PRO_HEAD_VIDEO = "AABB";
    private static final String PRO_HEAD_PAGE = "5AA504800300";
    private static final String PRO_HEAD_COIN = "5AA508";
    private static final String PRO_HEAD_COIN_IN = "5AA50882008020";
    //                                              5AA5098200802020312F3131
    //                                              5AA50A82000020382E384D5048
    private static final String PRO_HEAD_COIN_IN2 = "5AA50982008020";
    private static final String PRO_HEAD_SPEED = "5AA50A820000";
    private static final String PRO_HEAD_VIDEO_HIDE = "AABB0101";
    private static final String PRO_HEAD_VIDEO_CHANGE = "AABB0216";
    private static final String PRO_HEAD_SETTING_ICON = "5AA5058200A000";
    private static final String PRO_HEAD_SETTING_COIN = "5AA50582001000";
    private static final String PRO_HEAD_SETTING_TICKET = "5AA50582002000";
    private static final String PRO_HEAD_SETTING_TOTAL_COIN = "5AA507820030000000";
    private static final String PRO_HEAD_SETTING_TOTAL_TICKET = "5AA507820040000000";
    private static final String PRO_HEAD_SETTING_TOTAL_GAME_PLAY = "5AA507820050000000";
    private static final String PRO_HEAD_SETTING_HARD = "5AA50582007000";
    private static final String PRO_HEAD_SETTING_ACCOUNT_YES = "5AA5058200600003";
    private static final String PRO_HEAD_SETTING_ACCOUNT_NO = "5AA5058200600002";
    private static final String PRO_HEAD_GAME_OVER_TICKET = "5AA50D8200905449434B455453";
    private static final String PRO_HEAD_4 = "AABB09110070005500000000";   //显示视频窗口
    private static final String PRO_HEAD_5 = "AABB0216";   //播放片源
    private static final String PRO_HEAD_6 = "AABB0118";   //暂停
    private static final String PRO_HEAD_7 = "AABB0119";   //退出播放
    private static final String V2_GAME_OVER = "AABB0511007000";//AABB051100700055
    private String errorTempString = "";
    private synchronized void doDWSomething(String buf) {
        //协议出错处理
        if(TextUtils.isEmpty(buf)){
            if(System.currentTimeMillis() - countTimeLimit < 50){
                if(lastProString.equals(reciverString)){
                    errorTempString = reciverString;
                    Log.e("ZM","协议出错");
                    reciverString = "";
                    return;
                }
            }
        }
        countTimeLimit = System.currentTimeMillis();
        //协议过滤
        proFilter(buf);
        lastProString = reciverString;
        Log.e("ZM","lastProString = " + lastProString);
        //具体协议处理
        try{
            if (reciverString.length() >= LIMIT_LENGTH) {
                if (reciverString.startsWith(PRO_HEAD_COIN_IN)) {
                    if(reciverString.length() >= 22){
                        sendProAA();
                        String num0 = reciverString.substring(14, 16);
                        String num = reciverString.substring(16, 18);
                        String num2 = reciverString.substring(20, 22);
                        handleChangeCoins(num0,num,num2);
                        Log.e("ZM", "num = " + num + "---num2 = " + num2);
                        reciverString = reciverString.substring(22, reciverString.length());
                    }
                }else if (reciverString.startsWith(PRO_HEAD_COIN_IN2)) {
                    if(reciverString.length() >= 24){
                        sendProAA();
                        String num = reciverString.substring(14, 16);
                        String num2 = reciverString.substring(16, 18);
                        String num3 = reciverString.substring(20, 22);
                        String num4 = reciverString.substring(22, 24);
                        handleChangeCoins2(num,num2,num3,num4);
                        reciverString = reciverString.substring(24, reciverString.length());
                    }
                }else if (reciverString.startsWith(PRO_HEAD_PAGE)) {
                    if(reciverString.length() >= 14){
                        sendProAA();
                        String page = reciverString.substring(12, 14);
                        reciverString = reciverString.substring(14, reciverString.length());
                        Log.e("ZM", "page = " + page);
                        handleChangePage(page);
                    }
                }else if (reciverString.startsWith(PRO_HEAD_SPEED)) {
                    if(reciverString.length() >= 26){
                        sendProAA();
                        String showData = reciverString.substring(12, 20);
                        handleSpeed(showData);
                        reciverString = reciverString.substring(26, reciverString.length());
                    }
                }else if (reciverString.startsWith(PRO_HEAD_VIDEO_HIDE)) {
                    sendProAA();
                    setYtVideoVisiable(false);
                    reciverString = reciverString.substring(8, reciverString.length());
                }else if (reciverString.startsWith(PRO_HEAD_VIDEO_CHANGE)) {
                    if(reciverString.length() >= 10){
                        sendProAA();
                        String index = reciverString.substring(8, 10);
                        reciverString = reciverString.substring(10, reciverString.length());
                        handleVideoChange(Integer.valueOf(index));
                    }
                }else if (reciverString.startsWith(PRO_HEAD_SETTING_ICON)) {
                    if(reciverString.length() >= 16){
                        sendProAA();
                        String icons = reciverString.substring(14, 16);
                        handleMenuLeftIcons(icons);
                        reciverString = reciverString.substring(16, reciverString.length());
                    }
                }else if (reciverString.startsWith(PRO_HEAD_SETTING_COIN)) {
                    if(reciverString.length() >= 16){
                        sendProAA();
                        String coin = reciverString.substring(14, 16);
                        reciverString = reciverString.substring(16, reciverString.length());
                        handleMenuConsChange(coin);
                    }
                }else if (reciverString.startsWith(PRO_HEAD_SETTING_TICKET)) {
                    if(reciverString.length() >= 16){
                        sendProAA();
                        String ticket = reciverString.substring(14, 16);
                        reciverString = reciverString.substring(16, reciverString.length());
                        handleMenuTicketsChange(ticket);
                    }
                }else if (reciverString.startsWith(PRO_HEAD_SETTING_TOTAL_COIN)) {
                    if(reciverString.length() >= 20){
                        sendProAA();
                        String ticket = reciverString.substring(16, 20);
                        reciverString = reciverString.substring(20, reciverString.length());
                        handleMenuTotalCoinsChange(ticket);
                    }
                }else if (reciverString.startsWith(PRO_HEAD_SETTING_TOTAL_TICKET)) {
                    if(reciverString.length() >= 20){
                        sendProAA();
                        String ticket = reciverString.substring(16, 20);
                        reciverString = reciverString.substring(20, reciverString.length());
                        handleMenuTotalTicketsChange(ticket);
                    }
                }else if (reciverString.startsWith(PRO_HEAD_SETTING_TOTAL_GAME_PLAY)) {
                    if(reciverString.length() >= 20){
                        sendProAA();
                        String ticket = reciverString.substring(16, 20);
                        reciverString = reciverString.substring(20, reciverString.length());
                        handleMenuTotalGamePlayChange(ticket);
                    }
                }else if (reciverString.startsWith(PRO_HEAD_SETTING_HARD)) {
                    if(reciverString.length() >= 16){
                        sendProAA();
                        String hd = reciverString.substring(14, 16);
                        reciverString = reciverString.substring(16, reciverString.length());
                        handleHardware(hd);
                    }
                }else if (reciverString.startsWith(PRO_HEAD_SETTING_ACCOUNT_YES)) {
                    if(reciverString.length() >= 16){
                        sendProAA();
                        reciverString = reciverString.substring(16, reciverString.length());
                        handleMenuAccountYes();
                    }
                }else if (reciverString.startsWith(PRO_HEAD_SETTING_ACCOUNT_NO)) {
                    if(reciverString.length() >= 16){
                        sendProAA();
                        reciverString = reciverString.substring(16, reciverString.length());
                        handleMenuAccountNo();
                    }
                }else if (reciverString.startsWith(PRO_HEAD_GAME_OVER_TICKET)) {
                    if(reciverString.length() >= 32){
                        sendProAA();
                        String num = reciverString.substring(26, 28);
                        String num2 = reciverString.substring(28, 30);
                        String num3 = reciverString.substring(30, 32);
                        changeGameOverTickets(num,num2,num3);
                        reciverString = reciverString.substring(32, reciverString.length());
                    }
                }

                doDWSomething("");
            }

        }catch (Exception e){
            reciverString = "";
        }
    }


    /**
     * 协议过滤
     * @param buf
     */
    private void proFilter(String buf){
        reciverString += buf;
        if (!reciverString.contains(PRO_HEAD) && !reciverString.contains(PRO_HEAD_VIDEO)) {
            reciverString = "";
            return;
        } else {
            int startIndex = reciverString.indexOf(PRO_HEAD);
            if(startIndex == -1){
                startIndex = reciverString.indexOf(PRO_HEAD_VIDEO);
                reciverString = reciverString.substring(startIndex);
            }else{
                if(startIndex != 0){
                    errorTempString += reciverString.substring(0,startIndex);
                    Log.e("ZM","error String = " + errorTempString);
                    if(errorTempString.startsWith("5AA50582002000") && errorTempString.length() == 14){
                        String tk = errorTempString.substring(12,14);
                        Integer iCoin = Integer.parseInt(tk,16);
                        txtTicketPlay.setText(iCoin + "");
                        tkTotal = iCoin;
                        errorTempString = "";
                    }
                }
                reciverString = reciverString.substring(startIndex);
            }
        }
    }

    private void changeGameOverTickets(String num,String num2,String num3){
        int c0 = (Integer.valueOf(num) - 30);
        int c1 = (Integer.valueOf(num2) - 30);
        int c2 = (Integer.valueOf(num3) - 30);
        String t1 = "";
        String t2 = "";
        String t3 = "";
        if(c0 >= 0){
            t1 = c0 + "";
        }
        if(c1 >= 0){
            t2 = c1 + "";
        }
        if(c2 >= 0){
            t3 = c2 + "";
        }

        txtGameOverTicket.setText("TICKETS  " + t1 + t2 + t3);
    }

    private EditText edtPro;
    public void test(View view){
        edtPro = (EditText) findViewById(R.id.edt_pro);
        doDWSomething(edtPro.getText().toString());
    }

    private EditText edtPro2;
    public void test2(View view){
        edtPro2 = (EditText) findViewById(R.id.edt_pro2);
        doDWSomething(edtPro2.getText().toString());
    }

    /**
     * 发送AA指令
     */
    private synchronized void sendProAA(){
        SerialInterface.sendHexMsg2SerialPort(USEING_PORT,"aa");
        Log.e("ZM","发送AA协议");
    }


    private long timeLimit = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - timeLimit < 1500) {
                finish();
            } else {
                timeLimit = System.currentTimeMillis();
                Toast.makeText(this, "click again to exit!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
