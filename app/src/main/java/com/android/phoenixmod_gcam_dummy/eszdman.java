package com.android.phoenixmod_gcam_dummy;

import static android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE;

import android.content.SharedPreferences;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes2.dex */
public class eszdman {
    private static String TAG = "CameraManager2";
    public static eszdman cameraManager2;
    public final SharedPreferences SharedPreferences;
    private Set<String> mCameraIDs;

    public eszdman(CameraManager cameraManager) {
        cameraManager2 = this;
        this.mCameraIDs = new HashSet();
        SharedPreferences sharedPreferences = null;
        this.SharedPreferences = sharedPreferences;
        if (get("pref_enable_camera_key") == 0) {
            getCameraId(cameraManager);
            save();
            return;
        }
        this.mCameraIDs = sharedPreferences.getStringSet("pref_list_camera_key", null);
    }

    private boolean getBit(int i, int i2) {
        return ((i2 >> (i + -1)) & 1) == 1;
    }

    private Set<String> getCameraId(CameraManager cameraManager) {
        for (int i = 0; i < 121; i++) {
            try {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(String.valueOf(i));
                if (cameraCharacteristics != null) {
                    String parseInt = String.valueOf(i);
                    if (cameraCharacteristics.get(FLASH_INFO_AVAILABLE) == true){
                        if (!getBit(6, Integer.parseInt(parseInt))) {
                            this.mCameraIDs.add(parseInt);
                        }
                    }
                }
            } catch (Exception e) {
                this.mCameraIDs.toArray();
            }
        }
        Log.d("ID-list", "ID-List:");
        Log.d("ID-list", manualArray().toString());
        return mCameraIDs;
    }

    private boolean isTwoLens(CameraCharacteristics cameraCharacteristics) {
        int intValue = ((Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue();
        return intValue == 0 || intValue == 1;
    }

    public void SetList(String str, Set set) {
        SharedPreferences sharedPreferences = this.SharedPreferences;
        if (!sharedPreferences.contains(str)) {
            sharedPreferences.edit().putStringSet(str, set).apply();
        }
    }

    public void SetString(String str, String str2) {
        SharedPreferences sharedPreferences = this.SharedPreferences;
        if (!sharedPreferences.contains(str)) {
            sharedPreferences.edit().putString(str, str2).apply();
        }
    }

    public int get(String str) {
        SharedPreferences sharedPreferences = this.SharedPreferences;
        if (sharedPreferences.contains(str)) {
            return Integer.parseInt(sharedPreferences.getString(str, null));
        }
        return 0;
    }

    public String[] getCameraIdList() {
        Set<String> manualArray = manualArray();
        int size = manualArray.size();
        if (size == 0) {
            manualArray = this.mCameraIDs;
            size = manualArray.size();
        }
        String[] strArr = (String[]) manualArray.toArray(new String[size]);
        int[] iArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            iArr[i] = Integer.parseInt(strArr[i]);
        }
        Arrays.sort(iArr);
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = String.valueOf(iArr[i2]);
            String str = TAG;
            Log.d(str, "GotArray:" + strArr[i2]);
        }
        return strArr;
    }

    Set manualArray() {
        if (FixBSG.MenuValue("pref_switch_manual_camera_array_key") == 0) {
            return new HashSet();
        }
        String[] split = FixBSG.MenuValueString("pref_camera_array_key").split(",");
        int length = split.length;
        HashSet hashSet = new HashSet();
        for (int i = 0; length != i; i++) {
            String str = split[i];
            if (!str.isEmpty()) {
                hashSet.add(str);
            }
        }
        return hashSet;
    }

    void save() {
        SetString("pref_enable_camera_key", "1");
        SetList("pref_list_camera_key", this.mCameraIDs);
    }
}
