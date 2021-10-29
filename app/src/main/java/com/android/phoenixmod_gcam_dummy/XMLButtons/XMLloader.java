package com.android.phoenixmod_gcam_dummy.XMLButtons;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;

import androidx.annotation.RequiresApi;

import com.android.phoenixmod_gcam_dummy.FixBSG;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Scanner;

public class XMLloader {
    static final Application INSTANCE;
    private static Context staticContext;

    static {
        Context context = staticContext;
        try {
            INSTANCE = (Application) Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication", new Class[0]).invoke((Object) null, new Object[0]);
        } catch (Throwable th) {
            throw new AssertionError(th);
        }
    }

    public static Context getContext() {
        return staticContext == null ? INSTANCE.getApplicationContext() : staticContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void whichXmlDay() {
        if (FixBSG.MenuValue("pref_xml_button_key") == 0) {
            return;
        }
        if (Build.MODEL.equals("RMX1971")) {
            loadXML("RMX1971_Day.xml");
        } else if (Build.DEVICE.equals("raphael")) {
            loadXML("raphael_Day.xml");
        } else {
            loadXML("Day.xml");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void whichXmlNight() {
        if (FixBSG.MenuValue("pref_xml_button_key") == 0) {
            return;
        }
        if (Build.MODEL.equals("RMX1971")) {
            loadXML("RMX1971_Night.xml");
        } else if (Build.DEVICE.equals("raphael")) {
            loadXML("raphael_Day.xml");
        } else {
            loadXML("Night.xml");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static void loadXML(String str) {
        String absolutePath = getFileSharedPreferences().getAbsolutePath();
        copyFile(new File(Environment.getExternalStorageDirectory() + "/Gcam/Configs7/Butchercam/DeviceXMLs/" + str).getAbsolutePath(), absolutePath);
    }

    public static void copyFile(String str, String str2) {
        XMLloader.savePersistents();
        try {
            File file = new File(str);
            File file2 = new File(str2);
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileInputStream.close();
                    fileOutputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static File getFileSharedPreferences() {
        String defaultSharedPreferencesName = PreferenceManager.getDefaultSharedPreferencesName(staticContext);
        String replace = (staticContext.getFilesDir().getAbsolutePath() + File.separator).replace("files/", "");
        File file = new File(replace + "shared_prefs" + File.separator + defaultSharedPreferencesName + ".xml");
        if (file.isDirectory()) {
            file.delete();
        }
        return file;
    }

    public static boolean savePersistents() {
        new File(Environment.getExternalStorageDirectory().getPath(), "Gcam/Configs7/Butchercam/DeviceXMLs/persist").mkdirs();
        try {
            new File(Environment.getExternalStorageDirectory() + "/Gcam/Configs7/Butchercam/DeviceXMLs/persist/persist.txt").createNewFile();
            FileWriter myWriter = new FileWriter(Environment.getExternalStorageDirectory() + "/Gcam/Configs7/Butchercam/DeviceXMLs/persist/persist.txt");
            for (String str2 : XMLstrings.persistentSettings) {
                if (XMLstrings.stringSettings.contains(str2)) {
                    myWriter.write(str2 + ";" + FixBSG.MenuValueString(str2)+System.getProperty( "line.separator" ));
                } else {
                    myWriter.write(str2 + ";" + FixBSG.MenuValue(str2)+System.getProperty( "line.separator" ));
                }
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return true;
    }

    private void readsettings() {
        File myFile = new File(Environment.getExternalStorageDirectory() + "/Gcam/Configs7/Butchercam/DeviceXMLs/persist/persist.txt");
        try {
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] arrOfStr = data.split(";");
                String key = arrOfStr[0];
                String value = arrOfStr[1];
                FixBSG.setMenuValue(key,value);
            }
            myReader.close();
            System.out.println("Successfully read the file.");
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (Throwable th) {
            myFile.delete();
            throw th;
        }
        myFile.delete();
    }
}
