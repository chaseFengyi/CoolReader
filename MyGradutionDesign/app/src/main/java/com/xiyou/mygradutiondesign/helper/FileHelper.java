package com.xiyou.mygradutiondesign.helper;

import android.os.Environment;

import java.io.File;

/**
 * Created by fengyi on 16/3/15.
 */
public class FileHelper {

    private static final String DIR_NAME = "TrafficSign";

    public static File createRootDir(){
        return new File(getSDCardPath(), DIR_NAME);
    }

    /**
     * @param fileDir
     * @param fileName
     * @return
     */
    public static File newFilePath(String fileDir, String fileName){
        File file = new File(createRootDir(), fileDir);
        if (!file.exists()){
            file.mkdirs();
        }
        return new File(file, fileName);
    }

    public static File newFile(String fileName){
        File file = new File(createRootDir(), fileName);
        if (!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    public static File getSDCardPath(){
        File sdDir = null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            sdDir = Environment.getExternalStorageDirectory();//获取根目录
        }
        return sdDir;
    }

}
