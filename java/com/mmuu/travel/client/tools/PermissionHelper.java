package com.mmuu.travel.client.tools;

/**
 * Created by HuangYuan on 2017/2/6.
 */

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.mmuu.travel.client.bean.mfinterface.DialogClickListener;
import com.mmuu.travel.client.widget.dialog.OneDialog;


/**
 * 正常级别权限：开发者仅仅需要在AndroidManifext.xml上声明，那么应用就会被允许拥有该权限，如：android.permission.INTERNET</li>
 * 危险级别权限：开发者需要在AndroidManifext.xml上声明，并且在运行时进行申请，而且用户允许了，应用才会被允许拥有该权限，如：android.permission.WRITE_EXTERNAL_STORAGE</li>
 */
public class PermissionHelper {


    /**
     * 小tips:这里的int数值不能太大，否则不会弹出请求权限提示，测试的时候,改到1000就不会弹出请求了
     */
    private final static int READ_PHONE_STATE_CODE = 101;
    private final static int WRITE_EXTERNAL_STORAGE_CODE = 102;
    private final static int ACCESS_FINE_LOCATION = 104;
    private final static int CAMERA = 105;
    private final static int CALL_PHONE = 106;
    private final static int REQUEST_OPEN_APPLICATION_SETTINGS_CODE = 12345;
    private OneDialog oneDialog;

    public static final PermissionModel READ_PHONE_STATE_MODEL = new PermissionModel(Manifest.permission.READ_PHONE_STATE, READ_PHONE_STATE_CODE);

    public static final PermissionModel WRITE_EXTERNAL_STORAGE_MODEL = new PermissionModel(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_CODE);

    public static final PermissionModel ACCESS_FINE_LOCATION_MODEL = new PermissionModel(Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_FINE_LOCATION);

    public static final PermissionModel CAMERA_MODEL = new PermissionModel(Manifest.permission.CAMERA, CAMERA);
    public static final PermissionModel CALL_PHONE_MODEL = new PermissionModel(Manifest.permission.CALL_PHONE, CALL_PHONE);

    private Activity mActivity;


    public PermissionHelper(Activity activity) {
        mActivity = activity;
        oneDialog = new OneDialog(mActivity, "请在设置界面-权限管理中开启申请的权限，以正常使用本应用", "去设置");
        oneDialog.setDialogClickListener(new DialogClickListener() {
            @Override
            public void onLeftClick(View v, Dialog d) {

            }

            @Override
            public void onRightClick(View v, Dialog d) {
                d.dismiss();
                openApplicationSettings(REQUEST_OPEN_APPLICATION_SETTINGS_CODE);
            }
        });
    }

    public boolean checkPermission(PermissionModel model) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(mActivity, model.permission);
    }

    /**
     * 对应Activity的 {@code onRequestPermissionsResult(...)} 方法
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        // 如果用户不允许，引导用户到应用页面手动打开
        if (grantResults.length > 0 && PackageManager.PERMISSION_GRANTED != grantResults[0]) {
            if (!oneDialog.isShowing()) {
                oneDialog.show();
            }
        }
        return;
    }

    /**
     * 打开应用设置界面
     */
    private boolean openApplicationSettings(int requestCode) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + mActivity.getPackageName()));
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            // Android L 之后Activity的启动模式发生了一些变化
            // 如果用了下面的 Intent.FLAG_ACTIVITY_NEW_TASK ，并且是 startActivityForResult
            // 那么会在打开新的activity的时候就会立即回调 onActivityResult
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivityForResult(intent, requestCode);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static class PermissionModel {

        /**
         * 请求的权限
         */
        public String permission;


        /**
         * 请求代码
         */
        public int requestCode;

        public PermissionModel(String permission, int requestCode) {
            this.permission = permission;
            this.requestCode = requestCode;
        }
    }
}
