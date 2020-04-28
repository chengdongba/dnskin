package com.dqchen.dnskin.widgit;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dqchen.dnskin.R;
import com.dqchen.skin_core.SkinSupport;
import com.dqchen.skin_core.utils.SkinResource;
import com.google.android.material.tabs.TabLayout;

public class MyTabLayout extends TabLayout implements SkinSupport {

    private int tabIndicatorColorResId;
    private int tabTextColorResId;

    public MyTabLayout(@NonNull Context context) {
        super(context);
    }

    public MyTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout,defStyleAttr,0);
        tabIndicatorColorResId = a.getResourceId(R.styleable.TabLayout_tabIndicatorColor,0);
        tabTextColorResId = a.getResourceId(R.styleable.TabLayout_tabTextColor, 0);
        a.recycle();
    }

    @Override
    public void applySkin() {
        //更换table颜色和字体颜色
        if (tabIndicatorColorResId != 0){
            int color = SkinResource.getInstance().getColor(tabIndicatorColorResId);
            setSelectedTabIndicator(color);
        }

        if (tabTextColorResId !=0 ){
            ColorStateList colorStateList = SkinResource.getInstance().getColorStateList(tabTextColorResId);
            setTabTextColors(colorStateList);
        }
    }
}
