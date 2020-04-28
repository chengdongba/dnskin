package com.dqchen.skin_core;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.dqchen.skin_core.utils.SkinResource;
import com.dqchen.skin_core.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * 属性处理类
 */
public class SkinAttribute {
    private static final List<String> mAttributes = new ArrayList<>();
    private List<SkinView> mSkinViews = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");

        mAttributes.add("skinTypeface");
    }

    private final Typeface typeface;

    public SkinAttribute(Typeface typeface) {
        this.typeface = typeface;
    }

    public void load(View view, AttributeSet attrs) {
        List<SkinPair> skinPairs = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attributeName = attrs.getAttributeName(i);
            int resId;
            if (mAttributes.contains(attributeName)) {
                //获取resId
                String attributeValue = attrs.getAttributeValue(i);
                if (attributeValue.startsWith("#")) {
                    //写死了不管了
                    continue;
                }
                if (attributeValue.startsWith("?")) {
                    int attrsId = Integer.parseInt(attributeName.substring(1));
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrsId})[0];
                } else {
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                if (resId != 0) {
                    SkinPair skinPair = new SkinPair(attributeName, resId);
                    skinPairs.add(skinPair);
                }
            }
        }

        if (!skinPairs.isEmpty() || view instanceof TextView || view instanceof SkinSupport) {
            SkinView skinView = new SkinView(view, skinPairs);
            skinView.applySkin(typeface);
            mSkinViews.add(skinView);
        }

    }

    //换皮肤
    public void applySkin(Typeface typeface) {
        for (SkinView mSkinView : mSkinViews) {
            mSkinView.applySkin(typeface);
        }
    }

    static class SkinPair {
        public String attributeName;
        public int resId;

        public SkinPair(String attributrName, int resId) {
            this.attributeName = attributrName;
            this.resId = resId;
        }
    }

    /**
     * 将view和可以替换皮肤的属性封装起来
     */
    static class SkinView {
        public View view;
        public List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        public void applySkin(Typeface typeface) {
            if (view == null){
                return;
            }
            applySkinTypeface(typeface);
            applySkinSupport();
            for (SkinPair skinPair : skinPairs) {
                Drawable left = null, top = null, right = null, bottom = null;
                String attributeName = skinPair.attributeName;
                switch (attributeName) {
                    case "background":
                        Object background = SkinResource.getInstance().getBackground(skinPair.resId);
                        //Color
                        if (background instanceof Integer){
                            view.setBackgroundColor((Integer) background);
                        }else {
//                            view.setBackground((Drawable) background);
                            if (null!=view){
                                ViewCompat.setBackground(view, (Drawable) background);
                            }
                        }
                        break;
                    case "src":
                        background = SkinResource.getInstance().getBackground(skinPair.resId);
                        //Color
                        if (background instanceof Integer){
                            ((ImageView)view).setImageDrawable(new ColorDrawable((Integer) background));
                        }else {
                            ((ImageView)view).setImageDrawable((Drawable) background);
                        }
                        break;
                    case "textColor":
                        ((TextView)view).setTextColor(SkinResource.getInstance().getColorStateList(skinPair.resId));
                        break;
                    case "drawableLeft":
                        left = SkinResource.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableTop":
                        top = SkinResource.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableRight":
                        right = SkinResource.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResource.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "skinTypeface":
                        Typeface typeface1 = SkinResource.getInstance().getTypeFace(skinPair.resId);
                        applySkinTypeface(typeface1);
                        break;
                    default:
                        break;
                }
                if (null != left || null != right || null != top || null != bottom) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right,
                            bottom);
                }
            }
        }

        private void applySkinSupport() {
            if (view instanceof SkinSupport) {
                ((SkinSupport) view).applySkin();
            }
        }

        private void applySkinTypeface(Typeface typeface) {
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(typeface);
            }
        }
    }
}
