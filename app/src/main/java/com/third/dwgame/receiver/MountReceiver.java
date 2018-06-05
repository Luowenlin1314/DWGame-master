package com.third.dwgame.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.third.dwgame.activity.CopyActivity;
import com.third.dwgame.utils.FileUtil;

import java.io.File;

/**
 * 作者：Sky on 2018/6/3.
 * 用途：用来拷贝文件
 */

public class MountReceiver extends BroadcastReceiver{

    private String southPath = "/Download";
    private String targetPath ="/android_hurryicane";
    String path = "";

    String[] fileNames = {"龙卷风1.mp4","龙卷风2.mp4","龙卷风3.mp4",
            "龙卷风4.mp4","龙卷风5.mp4","龙卷风6.mp4"};

    @Override
    public void onReceive(final Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
            path = intent.getData().getPath();
            if (path == null) {
                return;
            }

            File filePath = new File(path);
            String[] files = filePath.list();
            if(files != null){
                if(filePath.exists() && files.length == 1){
                    path = path + File.separator +files[0];
                }
            }

            boolean result = baohanUsbPath(path);
            if(!result){
                return;
            }

            final File targetFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() ,southPath);
            final File usbFile = new File(path,targetPath);

            if(targetFile == null || !targetFile.exists()){
                targetFile.mkdirs();
            }

            if(usbFile != null && usbFile.exists() && usbFile.isDirectory()){
                final File[] sourceFileNames = usbFile.listFiles();
                if(sourceFileNames != null && sourceFileNames.length == 6){
                    Intent toStartCopy = new Intent(context,CopyActivity.class);
                    toStartCopy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(toStartCopy);

                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            for (int i = 0; i < sourceFileNames.length; i++) {
                                try {
                                    FileUtil.copyFile(sourceFileNames[i], new File(targetFile, sourceFileNames[i].getName()),null);
                                    Intent toUpdate = new Intent(CopyActivity.ACTION_UPDATE);
                                    toUpdate.putExtra("fileName",sourceFileNames[i].getName());
                                    context.sendBroadcast(toUpdate);
                                }catch (Exception e){

                                }
                            }

                            Intent toUpdate = new Intent(CopyActivity.ACTION_END);
                            context.sendBroadcast(toUpdate);
                        }
                    }.start();
                }
            }
        }
    }


    private boolean baohanUsbPath(String path){
        if (path.contains("/storage/usb0")
                || path.contains("/storage/usb1")
                || path.contains("/storage/usb2")
                || path.contains("/storage/usbhost0")
                || path.contains("/storage/usbhost1")
                || path.contains("/storage/usbhost2")
                || path.contains("/mnt/usb_storage")
                || path.contains("/mnt/usb_storage0")
                || path.contains("/mnt/usb_storage1")
                || path.contains("/mnt/usb_storage2")
                || path.contains("/mnt/usb_sda1")
                || path.contains("/mnt/usb_sdb1")
                || path.contains("/mnt/usbhost0")
                || path.contains("/mnt/usbhost1")
                || path.contains("/mnt/usbhost2")
                || path.contains("/mnt/usbhost3")
                || path.contains("/mnt/usbhost4")
                || path.contains("/mnt/usbhost5")
                || path.contains("/storage/external_storage/sda1")
                || path.contains("/mnt/usb_storage/USB_DISK0")
                || path.contains("/mnt/usb_storage/USB_DISK1")
                || path.contains("/mnt/usb_storage/USB_DISK2")
                || path.contains("/mnt/usb_storage/USB_DISK3")
                || path.contains("/mnt/usb_storage/USB_DISK4")
                || path.contains("/mnt/usb_storage/USB_DISK5")
                || path.contains( "/mnt/usbhost/Storage01")
                || path.contains( "/mnt/usbhost/Storage02")
                || path.contains( "/mnt/usb_storage/USB_DISK0")
                || path.contains( "/mnt/sda0")
                || path.contains( "/mnt/sda1")
                || path.contains( "/mnt/sda2")
                || path.contains( "/mnt/sda3")
                || path.contains( "/mnt/sda4")
                ){

            return true;
        }

        return false;
    }

}
