package com.dqchen.skin_core.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import org.w3c.dom.Text;

/**
 * 根据资源id拿到对应的皮肤包中的资源
 */
public class SkinResource {

    //单例
    private static SkinResource ourInstance;
    private boolean isDefaultSkin = true;
    private Resources mAppResources;
    private Resources mSkinResources;
    private String mSkinPkgName;

    public SkinResource(Context context) {
        mAppResources = context.getResources();
    }

    public static void init(Context context) {
        if (null == ourInstance) {
            synchronized (SkinResource.class) {
                if (null == ourInstance) {
                    ourInstance = new SkinResource(context);
                }
            }
        }
    }

    public static SkinResource getInstance() {
        return ourInstance;
    }

    /**
     * 使用皮肤包
     * 为mSkinResource,skinPkgName和isDefaultSkin赋值
     */
    public void applySkin(Resources resources, String pkgName) {
        this.mSkinPkgName = pkgName;
        this.mSkinResources = resources;
        isDefaultSkin = mSkinResources == null || TextUtils.isEmpty(mSkinPkgName);
    }

    /**
     * 还原皮肤
     * resources置空
     * isDefaultSkin还原
     */
    public void reset() {
        mSkinResources = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
    }

    /**
     * 根据app的资源id查找皮肤包中是否有相应的id
     */
    public int getIdentifier(int resId) {
        if (isDefaultSkin) {
            return resId;
        }
        if (TextUtils.isEmpty(mSkinPkgName) || mSkinResources == null) {
            return resId;
        }
        String entryName = mAppResources.getResourceEntryName(resId);
        String typeName = mAppResources.getResourceTypeName(resId);
        return mSkinResources.getIdentifier(entryName, typeName, mSkinPkgName);
    }

    /**
     * 根据资源id获取color
     */
    public int getColor(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColor(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColor(resId);
        }
        return mSkinResources.getColor(skinId);
    }

    /**
     * 获取ColorStateList
     * @param resId
     * @return
     */
    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColorStateList(resId);
        }
        return mSkinResources.getColorStateList(skinId);
    }

    public Drawable getDrawable(int resId) {
        //如果有皮肤  isDefaultSkin false 没有就是true
        if (isDefaultSkin) {
            return mAppResources.getDrawable(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getDrawable(resId);
        }
        return mSkinResources.getDrawable(skinId);
    }


    /**
     * 可能是Color 也可能是drawable
     *
     * @return
     */
    public Object getBackground(int resId) {
        String resourceTypeName = mAppResources.getResourceTypeName(resId);

        if (resourceTypeName.equals("color")) {
            return getColor(resId);
        } else {
            // drawable
            return getDrawable(resId);
        }
    }

    public String getString(int resId) {
        try {
            if (isDefaultSkin) {
                return mAppResources.getString(resId);
            }
            int skinId = getIdentifier(resId);
            if (skinId == 0) {
                return mAppResources.getString(skinId);
            }
            return mSkinResources.getString(skinId);
        } catch (Resources.NotFoundException e) {

        }
        return null;
    }

    /**
     * 获取字体
     */
    public Typeface getTypeFace(int resId){
        String typeFacePath = getString(resId);
        if (TextUtils.isEmpty(typeFacePath)){
            return Typeface.DEFAULT;
        }
        Typeface typeface;
        if (isDefaultSkin){
            typeface = Typeface.createFromAsset(mAppResources.getAssets(),typeFacePath);
        }else {
            typeface = Typeface.createFromAsset(mSkinResources.getAssets(),typeFacePath);
        }
        return typeface;
    }
}
