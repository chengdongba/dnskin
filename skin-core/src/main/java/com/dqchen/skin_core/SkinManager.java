package com.dqchen.skin_core;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.dqchen.skin_core.utils.SkinPreference;
import com.dqchen.skin_core.utils.SkinResource;

import java.lang.reflect.Method;
import java.util.Observable;

/**
 * 动态换肤框架
 * 1.获取需要换肤的控件和需要换肤的属性,封装成对象
 * 2.获取皮肤包里面对应的属性id和属性值,替换
 * 3.更换状态栏和navigationBar和字体
 */
public class SkinManager extends Observable {
    private static SkinManager ourInstance = null;
    private SkinActivityLifeCircle skinActivityLifeCircle;

    private Application application;

    public SkinManager(Application application) {
        this.application = application;
        SkinPreference.init(application);
        SkinResource.init(application);
        //注册Activity的生命周期回调
        skinActivityLifeCircle = new SkinActivityLifeCircle();
        application.registerActivityLifecycleCallbacks(skinActivityLifeCircle);
        loadSkin(SkinPreference.getInstance().getSkin());
    }

    /**
     * 加载皮肤包
     *
     * @param skinPath 皮肤包路径
     */
    private void loadSkin(String skinPath) {
        if (TextUtils.isEmpty(skinPath)) {
            //还原默认皮肤包
            SkinPreference.getInstance().setSkin("");
            SkinResource.getInstance().reset();
        } else {
            AssetManager assetManager = null;
            try {
                assetManager = AssetManager.class.newInstance();
                //添加资源进资源管理器
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.setAccessible(true);
                addAssetPath.invoke(assetManager, skinPath);
                Resources resources = application.getResources();
                Resources skinResources = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
                //获取外部包包名
                PackageManager packageManager = application.getPackageManager();
                PackageInfo info = packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
                String packageName = info.packageName;
                SkinResource.getInstance().applySkin(skinResources, packageName);
                SkinPreference.getInstance().setSkin(skinPath);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void init(Application application) {
        if (null == ourInstance) {
            synchronized (SkinManager.class) {
                if (null == ourInstance) {
                    ourInstance = new SkinManager(application);
                }
            }
        }
    }

    public static SkinManager getInstance() {
        return ourInstance;
    }

    public void updateSkin(Activity activity) {
        skinActivityLifeCircle.updateSkin(activity);
    }

}
