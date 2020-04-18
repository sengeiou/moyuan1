package xin.banghua.moyuan;

import android.Manifest;
import android.app.Activity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

public class CheckPermission {
    //1.检测权限
    public static void verifyStoragePermission(Activity activity) {
        //1.检测权限
        int permission1 = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA);
        int permission2 = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.RECORD_AUDIO);
        int permission3 = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission4 = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission1 != PermissionChecker.PERMISSION_GRANTED || permission2 != PermissionChecker.PERMISSION_GRANTED || permission3 != PermissionChecker.PERMISSION_GRANTED || permission4 != PermissionChecker.PERMISSION_GRANTED) {
            //2.没有权限，弹出对话框申请
            ActivityCompat.requestPermissions(activity,new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
        }else {
            //intentMain();
        }
    }
}
