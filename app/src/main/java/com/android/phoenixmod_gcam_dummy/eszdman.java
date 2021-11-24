package com.android.phoenixmod_gcam_dummy;

import static android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE;
import static android.hardware.camera2.CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS;
import static android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE;
import static android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE;

import android.content.SharedPreferences;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.util.Log;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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

    private void getCameraId(CameraManager cameraManager) {
        String IDofDepth = null;
        TreeMap<Double, Integer> TM = new TreeMap<>();
        for (int i = 0; i < 121; i++) {
            try {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(String.valueOf(i));
                if (cameraCharacteristics != null) {
                    String parseInt = String.valueOf(i);
                    if (i >= 2 && cameraCharacteristics.get(FLASH_INFO_AVAILABLE)){
                        float FocalLength = cameraCharacteristics.get(LENS_INFO_AVAILABLE_FOCAL_LENGTHS) [0];
                        int PixelArrayWidth = cameraCharacteristics.get(SENSOR_INFO_PIXEL_ARRAY_SIZE).getWidth();
                        float SensorWidth = cameraCharacteristics.get(SENSOR_INFO_PHYSICAL_SIZE).getWidth();
                        double AngleOfView = calculateAngleOfView(FocalLength, SensorWidth, PixelArrayWidth);
                        TM.put(AngleOfView, i);
                        Log.d("AOV", "SensorAngleOfView ID: "+ i + " Angle of View: "+ (int)AngleOfView);
                    } else if (i <=1) {
                        this.mCameraIDs.add(parseInt);
                    } else {
                        IDofDepth = parseInt;
                    }
                }
            } catch (Exception e) {
                this.mCameraIDs.toArray();
            }
        }
        for (Double key : new TreeSet<>(TM.descendingKeySet())) this.mCameraIDs.add(TM.get(key).toString());
        if (IDofDepth != null){
            this.mCameraIDs.add(IDofDepth);
        }
        Log.d("CameraIDs","IDs:"+ mCameraIDs);
    }

    public static float calculatePixelSize(int pixelArrayWidth, float sensorWidth) {
        return (sensorWidth / ((float) pixelArrayWidth)) * 1000.0f;
    }

    public static Double calculateAngleOfView(float focalLength, float sensorSize, int pixelArraySize) {
        float pixelSize = calculatePixelSize(pixelArraySize, sensorSize);
        return Math.toDegrees(Math.atan(Math.sqrt(Math.pow(sensorSize * pixelSize, 2.0d)
                + Math.pow(sensorSize * pixelSize, 2.0d)) / ((double) (2.0f * focalLength))) * 2.0d);
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
