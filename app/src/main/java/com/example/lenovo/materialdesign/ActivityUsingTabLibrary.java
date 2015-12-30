package com.example.lenovo.materialdesign;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.lenovo.materialdesign.tabs.MyFragment;

import hkm.ui.materialtabs.MaterialTab;
import hkm.ui.materialtabs.MaterialTabHost;
import hkm.ui.materialtabs.MaterialTabListener;

public class ActivityUsingTabLibrary extends AppCompatActivity implements MaterialTabListener {

    MaterialTabHost materialTabHost;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_using_tab_library);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        materialTabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                materialTabHost.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < myPagerAdapter.getCount(); i++) {
            materialTabHost.addTab(
                    materialTabHost.newTab()
                            .setIcon(myPagerAdapter.getDrawable(i))
                            .setTabListener(this)
            );
        }
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        viewPager.setCurrentItem(materialTab.getPosition());

    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        int[] icons = {R.drawable.ic_tab1, R.drawable.ic_tab2, R.drawable.ic_tab3};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MyFragment.getInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private Drawable getDrawable(int position) {

            Drawable drawable = null;
            drawable = getResources().getDrawable(icons[position], getTheme());
            return drawable;
        }
    }
}
