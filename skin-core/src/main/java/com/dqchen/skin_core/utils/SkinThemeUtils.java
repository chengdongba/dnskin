package com.dqchen.skin_core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;

import com.dqchen.skin_core.R;

/**
 * 更换状态栏和底部导航栏,虚拟键盘的颜色
 */
public class SkinThemeUtils {
    //皮肤包
    public static int[] TYPEFACE_ATTR = {
            R.attr.skinTypeface
    };

    //默认状态栏
    public static int[] APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = {
            androidx.appcompat.R.attr.colorPrimaryDark
    };

    //状态栏和底部导航栏颜色
    public static int[] STATUSBAR_COLOR_STTRS = {
            android.R.attr.statusBarColor,
            android.R.attr.navigationBarColor
    };

    /**
     * 根据attrs获取资源id
     */
    public static int[] getResId(Context context, int[] attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int[] resIds = new int[attrs.length];
        for (int i = 0; i < typedArray.length(); i++) {
            int resourceId = typedArray.getResourceId(i, 0);
            resIds[i] = resourceId;
        }
        return resIds;
    }

    /**
     * 修改状态栏和底部导航栏的颜色
     */
    public static void updateStatusBar(Activity activity) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        int[] resId = getResId(activity, STATUSBAR_COLOR_STTRS);
        if (resId[0] == 0) {
            int[] ints = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS);
            if (ints[0] != 0) {
                activity.getWindow().setStatusBarColor(SkinResource.getInstance().getColor(ints[0]));
            }
        } else {
            activity.getWindow().setStatusBarColor(SkinResource.getInstance().getColor(resId[0]));
        }

        if (resId[1] != 0) {
            activity.getWindow().setNavigationBarColor(SkinResource.getInstance().getColor(resId[1]));
        }
    }

    /**
     * 获得字体
     */
    public static Typeface getTypeface(Activity activity) {
        int resId = getResId(activity, TYPEFACE_ATTR)[0];
        return SkinResource.getInstance().getTypeFace(resId);
    }

}
