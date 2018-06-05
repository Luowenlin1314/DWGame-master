package com.third.dwgame.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.third.dwgame.R;
import com.third.dwgame.base.ActivityFragmentInject;
import com.third.dwgame.base.BaseActivity;
import com.third.dwgame.utils.DWConstant;

/**
 * Created by Administrator on 2018/4/15.
 */
@ActivityFragmentInject(
        contentViewId = R.layout.activity_game_setting,
        hasNavigationView = false,
        hasToolbar = false
)
public class DWSettingActivity extends BaseActivity {


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

    }

    @Override
    protected void findViewAfterViewCreate() {
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
    }

    @Override
    protected void initDataAfterFindView() {
        showLeftIconIndex("00");
        showItemIconIndex(-1);
        registerDWSettingProReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterDWProReceiver();
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
            imgHardware1.setVisibility(View.INVISIBLE);
            imgSaveChanges.setVisibility(View.INVISIBLE);
        }else if(index.equals("02")){
            imgGameConfigure.setVisibility(View.VISIBLE);
            imgGameConfigure1.setVisibility(View.INVISIBLE);
            imgGameConfigure2.setVisibility(View.INVISIBLE);
            imgAccount.setVisibility(View.INVISIBLE);
            imgHardware.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.INVISIBLE);
            imgSaveChanges.setVisibility(View.INVISIBLE);
        }else if(index.equals("03")){
            imgGameConfigure.setVisibility(View.INVISIBLE);
            imgGameConfigure1.setVisibility(View.VISIBLE);
            imgGameConfigure2.setVisibility(View.INVISIBLE);
            imgAccount.setVisibility(View.INVISIBLE);
            imgHardware.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.INVISIBLE);
            imgSaveChanges.setVisibility(View.INVISIBLE);
        }else if(index.equals("04")){
            imgGameConfigure.setVisibility(View.INVISIBLE);
            imgGameConfigure1.setVisibility(View.INVISIBLE);
            imgGameConfigure2.setVisibility(View.VISIBLE);
            imgAccount.setVisibility(View.INVISIBLE);
            imgHardware.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.INVISIBLE);
            imgSaveChanges.setVisibility(View.INVISIBLE);
        }else if(index.equals("05")) {
            imgGameConfigure.setVisibility(View.INVISIBLE);
            imgGameConfigure1.setVisibility(View.INVISIBLE);
            imgGameConfigure2.setVisibility(View.INVISIBLE);
            imgAccount.setVisibility(View.VISIBLE);
            imgHardware.setVisibility(View.INVISIBLE);
            imgHardware1.setVisibility(View.INVISIBLE);
            imgSaveChanges.setVisibility(View.INVISIBLE);
        }else if(index.equals("06")){
            imgGameConfigure.setVisibility(View.INVISIBLE);
            imgGameConfigure1.setVisibility(View.INVISIBLE);
            imgGameConfigure2.setVisibility(View.INVISIBLE);
            imgAccount.setVisibility(View.INVISIBLE);
            imgHardware.setVisibility(View.VISIBLE);
            imgHardware1.setVisibility(View.INVISIBLE);
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
            imgHardware1.setVisibility(View.INVISIBLE);
            imgSaveChanges.setVisibility(View.VISIBLE);
        }
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
        }else if(index == 1){
            imgAccountYes.setVisibility(View.INVISIBLE);
            imgAccountNo.setVisibility(View.VISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
        }else if(index == 2){
            imgAccountYes.setVisibility(View.INVISIBLE);
            imgAccountNo.setVisibility(View.INVISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
        }else if(index == 3){
            imgAccountYes.setVisibility(View.INVISIBLE);
            imgAccountNo.setVisibility(View.INVISIBLE);
            imgTxtMiddle.setVisibility(View.VISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
        }else if(index == 4){
            imgAccountYes.setVisibility(View.INVISIBLE);
            imgAccountNo.setVisibility(View.INVISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.VISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
        }else if(index == 5){
            imgAccountYes.setVisibility(View.INVISIBLE);
            imgAccountNo.setVisibility(View.INVISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.VISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
        }else if(index == 6){
            imgAccountYes.setVisibility(View.INVISIBLE);
            imgAccountNo.setVisibility(View.INVISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.VISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
        }else if(index == 7){
            imgAccountYes.setVisibility(View.INVISIBLE);
            imgAccountNo.setVisibility(View.INVISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.VISIBLE);
        }else{
            imgAccountYes.setVisibility(View.INVISIBLE);
            imgAccountNo.setVisibility(View.INVISIBLE);
            imgTxtMiddle.setVisibility(View.INVISIBLE);
            imgTxtLeft.setVisibility(View.INVISIBLE);
            imgTxtRight.setVisibility(View.INVISIBLE);
            imgTxtStrobe.setVisibility(View.INVISIBLE);
            imgTxtSmoke.setVisibility(View.INVISIBLE);
        }
    }

    private DWSettingProReceiver dwProReceiver;
    private void registerDWSettingProReceiver(){
        dwProReceiver = new DWSettingProReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DWConstant.INTENT_DW_COM);
        registerReceiver(dwProReceiver,intentFilter);
    }

    private void unRegisterDWProReceiver(){
        if(dwProReceiver != null){
            unregisterReceiver(dwProReceiver);
        }
    }

    private class DWSettingProReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("comValue");
            doDWSomething(msg);
        }
    }


    /**
     * 协议处理
     * @param buf
     */
    private static final int LIMIT_LENGTH = 8 * 2;
    private String reciverString = "";
    private static final String PRO_HEAD = "5AA5";
    private static final String PRO_HEAD_1 = "5AA505";
    private static final String PRO_HEAD_1_1 = "5AA5058200A0";
    private static final String V2_PRO_HEAD_1_1 = "5AA505820070";
    private static final String PRO_HEAD_1_2 = "5AA505820006";
    private static final String PRO_HEAD_1_3 = "5AA505820008";
    private static final String PRO_HEAD_2 = "5AA506";
    private static final String PRO_HEAD_2_1 = "5AA506820010";

    private void doDWSomething(String buf) {
        reciverString += buf;
        if (!reciverString.contains(PRO_HEAD)) {
            reciverString = "";
            return;
        } else {
            int startIndex = reciverString.indexOf(PRO_HEAD);
            reciverString = reciverString.substring(startIndex);

        }
        //5AA50480030003 5AA50480030003
        //5AA5 0480 03 00 01
        //5AA5 0682 00 00 313233
        //5AA5 0582 00 04 0002
        try{
            System.out.println("reciverString-->"+reciverString);
            if (reciverString.length() >= LIMIT_LENGTH) {
                // 数据包最低长度
                if (reciverString.contains(PRO_HEAD_1)) {
                    if(reciverString.contains(PRO_HEAD_1_1)){
                        if(reciverString.length() >= 16){
                            String icons = reciverString.substring(14, 16);
                            showLeftIconIndex(icons);
                            reciverString = reciverString.substring(16, reciverString.length());
                        }
                    }else if(reciverString.contains(PRO_HEAD_1_2)){
                        if(reciverString.length() >= 16){
                            String tickets = reciverString.substring(14, 16);
                            handleNumbers(tickets);
                            reciverString = reciverString.substring(16, reciverString.length());
                        }
                    }else if(reciverString.contains(PRO_HEAD_1_3)){
                        if(reciverString.length() >= 16){
                            String totalCoins = reciverString.substring(12, 16);
                            int t1 = (Integer.valueOf(totalCoins.substring(0, 2)) - 30);
                            int t2 = (Integer.valueOf(totalCoins.substring(2, 4)) - 30);
                            txtCoinTotal.setText("" + t1 + t2);
                            reciverString = reciverString.substring(16, reciverString.length());
                        }
                    }else if(reciverString.contains(V2_PRO_HEAD_1_1)){
                        if(reciverString.length() >= 16){
                            String icons = reciverString.substring(14, 16);
                            int index = Integer.valueOf(icons);
                            showItemIconIndex(index);
                            reciverString = reciverString.substring(16, reciverString.length());
                        }
                    }

                } else if (reciverString.contains(PRO_HEAD_2)) {
                    if(reciverString.length() >= 16){
                        if(reciverString.contains(PRO_HEAD_2_1)){
                            String totalTickets = reciverString.substring(12, 16);
                            int t1 = (Integer.valueOf(totalTickets.substring(0, 2)) - 30);
                            int t2 = (Integer.valueOf(totalTickets.substring(2, 4)) - 30);
                            txtTicketTotal.setText("" + t1 + t2);
                            reciverString = reciverString.substring(16, reciverString.length());
                        }
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


    private void handleNumbers(String num){
        try{
            if("02".compareTo(num) <= 0 && "06".compareTo(num) >= 0){
                int coin = Integer.valueOf(num);
                txtCoinPlay.setText((coin - 1) + "");
            }else if("07".compareTo(num) <= 0 && "0B".compareTo(num) >= 0){
                if("07".equals(num)){
                    txtTicketPlay.setText("1");
                }else if("08".equals(num)){
                    txtTicketPlay.setText("2");
                }else if("09".equals(num)){
                    txtTicketPlay.setText("3");
                }else if("0A".equals(num)){
                    txtTicketPlay.setText("4");
                }else if("0B".equals(num)){
                    txtTicketPlay.setText("5");
                }
            }else if("0C".compareTo(num) <= 0 && "0F".compareTo(num) >= 0){

            }else if("10".equals(num)){
                showItemIconIndex(0);
            }else if("11".equals(num)){
                showItemIconIndex(1);
            }else if("00".equals(num)){
                showItemIconIndex(-1);
            }
        }catch (Exception e){

        }
    }

}
