package com.dqchen.dnskin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dqchen.dnskin.fragment.MusicFragment;
import com.dqchen.dnskin.fragment.RadioFragment;
import com.dqchen.dnskin.fragment.VideoFragment;
import com.dqchen.dnskin.widgit.MyTabLayout;
import com.dqchen.skin_core.SkinManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyTabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MusicFragment());
        fragments.add(new VideoFragment());
        fragments.add(new RadioFragment());
        List<String> titles = new ArrayList<>();
        titles.add("音乐");
        titles.add("视频");
        titles.add("电台");
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,fragments,titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        SkinManager.getInstance().updateSkin(this);
    }

    public void skinSelect(View view) {
        startActivity(new Intent(this,SkinActivity.class));
    }
}
