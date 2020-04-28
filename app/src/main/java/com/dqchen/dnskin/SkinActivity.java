package com.dqchen.dnskin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.dqchen.dnskin.skin.Skin;
import com.dqchen.dnskin.skin.SkinUtils;
import com.dqchen.skin_core.SkinManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SkinActivity extends AppCompatActivity {

    //从服务器拉取的皮肤表
    List<Skin> skins = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
        skins.add(new Skin("3d9af30c0895e48f14c91066cc2be3d2", "1111111.skin", "app-skin-debug.apk"));
    }

    /**
     * 下载皮肤包
     */
    public void selectSkin(Skin skin) {
        //根据skin对象的getSkin()得到皮肤包File
        //如果不存在,根据url创建file
        File theme = new File(getFilesDir(), "theme");
        if (theme.exists()) {
            theme.delete();
        }
        theme.mkdirs();
        File skinFile = skin.getSkinFile(theme);
        if (skinFile.exists()) {
            Log.i("dqchen", "皮肤包已存在,开始换肤");
        } else {
            Log.i("dqchen", "皮肤包不存在,开始下载皮肤包");
        }
        //创建一个副本
        File tempSkin = new File(skinFile.getParent(), ".temp");
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            fos = new FileOutputStream(tempSkin);
            is = getAssets().open(skin.url);
            byte[] buffer = new byte[10240];
            int lendth = 0;
            while ((lendth = is.read(buffer)) != -1) {
                fos.write(buffer, 0, lendth);
            }
            Log.i("dqchen", "皮肤包下载完成,开始校验");
            if (TextUtils.equals(SkinUtils.getSkinMd5(tempSkin), skin.md5)) {
                Log.i("dqchen", "校验成功,修改文件名");
                tempSkin.renameTo(skinFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void change(View view){
        Skin skin = skins.get(0);
        selectSkin(skin);
        SkinManager.getInstance().loadSkin(skin.path);
    }

    public void restore(View view){
        SkinManager.getInstance().loadSkin(null);
    }
}
