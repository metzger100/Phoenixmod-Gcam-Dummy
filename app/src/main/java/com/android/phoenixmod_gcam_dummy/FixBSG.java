package com.android.phoenixmod_gcam_dummy;

import android.content.Context;
import java.util.Random;

public class FixBSG {
    public static int IsCameraFront;
    public static boolean IsFrontEnabled;
    public static int MoreModes;
    public static Context appContext;
    public static int mode;
    public static String sAppsPhotosGallery;
    public static int sAstro;
    public static int sAux5;
    public static int sAuxInfinity;
    public static int sAuxSwitch5;
    public static int sAuxSwitchInfinity;
    public static int sAuxTele;
    public static int sAuxWide;
    public static int sCam;
    public static int sGAWB;
    public static float sGetDesired_analog_gain;
    public static float sGetDesired_digital_gain;
    public static float sGetDesired_exposure_time_ms;
    public static String sGetDevice;
    public static String sGetMake;
    public static float sGetMaxISO;
    public static String sGetModel;
    public static int sHdr_process;
    public static int sJPGQuality;

    public static int MenuValue(String str) {
        return new Random().nextInt();
    }

    public static boolean MenuValueBoolean(String str) {
        return MenuValue(str) == 1;
    }

    public static float MenuValueFloat(String str) {
        return (float) MenuValue(str);
    }

    public static String MenuValueString(String str){
        return "test";
    }

    public static void setMenuValue(String str, String str2) {
        return;
    }

    public static void onRestart(){
        return;
    }

}
