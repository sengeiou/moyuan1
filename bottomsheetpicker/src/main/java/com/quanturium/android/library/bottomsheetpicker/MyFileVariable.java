package com.quanturium.android.library.bottomsheetpicker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class MyFileVariable {
    //创建 SingleObject 的一个对象
    private static MyFileVariable instance = new MyFileVariable();

    //让构造函数为 private，这样该类就不会被实例化
    private MyFileVariable(){}

    public static final int TAKE_FROM_SHEET = 1;
    public static final int TAKE_FROM_FOLDER = 2;
    public static final int TAKE_FROM_CAMERA = 3;

    private int takeFrom;

    private String takeFilePath;

    public Map fileMap;

    private int edit_num;

    public int getEdit_num() {
        return edit_num;
    }

    public void setEdit_num(int edit_num) {
        this.edit_num = edit_num;
    }

    //多选时文件放入map中
    public Map getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map fileMap) {
        this.fileMap = fileMap;
    }

    //获取唯一可用的对象
    public static MyFileVariable getInstance(){
        return instance;
    }

    public String getTakeFilePath() {
        return takeFilePath;
    }

    public void setTakeFilePath(String takeFilePath) {
        this.takeFilePath = takeFilePath;
    }

    public int getTakeFrom() {
        return takeFrom;
    }

    public void setTakeFrom(int takeFrom) {
        this.takeFrom = takeFrom;
    }


    public static void savePNG_After(Bitmap bitmap, String name) {
        File file = new File(name);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveJPG_After(Bitmap bitmap, String name) {
        File file = new File(name);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getImageWidth(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //Returns null, sizes are in the options variable
        BitmapFactory.decodeFile(path, options);
        return options.outWidth;
    }

    public static int getImageHeight(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //Returns null, sizes are in the options variable
        BitmapFactory.decodeFile(path, options);
        return options.outHeight;
    }


}
