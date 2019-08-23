package com.gogotalk.system.util;

import android.Manifest;
import android.app.Activity;

import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by fucc
 * Date: 2019-07-24 14:13
 */
public class PermissionsUtil {
    List<Boolean> permissionList = new ArrayList<>();
    static PermissionsUtil util;

    private PermissionsUtil() {
    }

    public static PermissionsUtil getInstance() {
        if (util == null) {
            synchronized (PermissionsUtil.class) {
                if (util == null) {
                    util = new PermissionsUtil();
                }
            }
        }
        return util;
    }

    public boolean isPermissions() {
        return permissionList.size() == 4;
    }

    public void requestPermissions(Activity activity) {
        permissionList.clear();
        RxPermissions rxPermission = new RxPermissions((FragmentActivity) activity);
        Disposable subscribe = rxPermission.requestEach(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            permissionList.add(permission.granted);
                        }
                    }
                });
    }
}
