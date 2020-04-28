package com.dqchen.skin_core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dqchen.skin_core.utils.SkinThemeUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.jar.Attributes;

/**
 * 重写Factory2在view创建时,替换皮肤属性
 */
public class SkinLayoutFactory implements LayoutInflater.Factory2, Observer {

    private static final String[] mClassPreFixList = {
      "android.widget.",
      "android.view.",
      "android,webkit."
    };

    private Activity activity;
    //属性处理类
    private SkinAttribute skinAttribute;
    private Class<?>[] parameterTypes = new Class[]{
            Context.class,
            AttributeSet.class
    };

    private Map<String,Constructor<? extends View>> mConstructorMap = new HashMap<>();

    public SkinLayoutFactory(Activity activity, Typeface typeface){
        this.activity = activity;
        skinAttribute = new SkinAttribute(typeface);
    }



    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        //反射classLoader
        View view = createViewFromTag(name,context,attrs);
        //自定义view
        if (null == view){
            view = createView(name,context,attrs);
        }
        //筛选符合条件的view
        skinAttribute.load(view,attrs);
        return view;
    }

    private View createViewFromTag(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.contains(".")){
            return null;
        }
        for (int i = 0; i < mClassPreFixList.length; i++) {
            view = createView(mClassPreFixList[i]+name,context,attrs);
            if (view !=null){
                break;
            }
        }
        return view;
    }

    /**
     * 反射view的构造方法,创建view
     * @param name view 的全类名
     * @param context 上下文
     * @param attrs 属性
     * @return 返回view对象
     */
    private View createView(String name, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = mConstructorMap.get(name);
        if (null == constructor){
            try {
                Class<? extends View> aClass = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = aClass.getConstructor(parameterTypes);
                mConstructorMap.put(name,constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (null!= constructor){
            try {
                return constructor.newInstance(context,attrs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        //更换皮肤
        SkinThemeUtils.updateStatusBar(activity);
        Typeface typeface = SkinThemeUtils.getTypeface(activity);
        skinAttribute.applySkin(typeface);
    }
}
