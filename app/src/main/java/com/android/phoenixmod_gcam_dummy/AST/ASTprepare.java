package com.android.phoenixmod_gcam_dummy.AST;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.android.phoenixmod_gcam_dummy.FixBSG;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ASTprepare {
    private final Activity a;

    //CameraActivity new ASTprepare(this);
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ASTprepare(Activity activity) {
        this.a = activity;
    }

    //start process
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startProcess(){
        saveSharedPreferences("astold.xml");
        loadXML("ast.xml");
        onRestart();
    };


    //save user-Settings
    @RequiresApi(api = Build.VERSION_CODES.N)
    public final void saveSharedPreferences(String str) {
        String str2;
        createFolderConfig("/GCam/Configs7/Butchercam/AST/");
        File fileSharedPreferences = getFileSharedPreferences();
        File file = new File(Environment.getExternalStorageDirectory(), "/GCam/Configs7/Butchercam/AST/" + str);
        try {
            FileChannel channel = new FileInputStream(fileSharedPreferences).getChannel();
            FileChannel channel2 = new FileOutputStream(file).getChannel();
            channel2.transferFrom(channel, 0, channel.size());
            channel.close();
            channel2.close();
            str2 = "Saved config is : " + str;
        } catch (FileNotFoundException e) {
            str2 = e.getMessage();
        } catch (IOException e2) {
            str2 = e2.getMessage();
        }
        Toast.makeText(this.a, str2, Toast.LENGTH_SHORT).show();

    }

    public final void createFolderConfig(String str) {
        File file = new File(Environment.getExternalStorageDirectory() + str);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public final File getFileSharedPreferences() {
        String replace = (this.a.getFilesDir().getAbsolutePath() + File.separator).replace("files/", "");
        String defaultSharedPreferencesName = PreferenceManager.getDefaultSharedPreferencesName(this.a);
        File file = new File(replace + "shared_prefs" + File.separator + defaultSharedPreferencesName + ".xml");
        if (file.isDirectory()) {
            file.delete();
        }
        return file;
    }

    //load AST-Settings + Nightmode with Astromode on (look activity to extract in the xml)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadXML(String str) {
        String absolutePath = getFileSharedPreferences().getAbsolutePath();
        copyFile(new File(Environment.getExternalStorageDirectory() + "/Gcam/Configs7/Butchercam/AST/" + str).getAbsolutePath(), absolutePath);
    }


    public final void copyFile(String str, String str2) {
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
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Restart App
    public void onRestart(){
        FixBSG.onRestart();
    }

}
