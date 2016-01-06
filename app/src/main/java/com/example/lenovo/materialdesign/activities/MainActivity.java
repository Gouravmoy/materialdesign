package com.example.lenovo.materialdesign.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.lenovo.materialdesign.R;
import com.example.lenovo.materialdesign.extras.SortListener;
import com.example.lenovo.materialdesign.fragments.FragmentBoxOffice;
import com.example.lenovo.materialdesign.fragments.FragmentSearch;
import com.example.lenovo.materialdesign.fragments.FragmentUpcoming;
import com.example.lenovo.materialdesign.fragments.NavigationDrawerFragment;
import com.example.lenovo.materialdesign.services.MyService;
import com.example.lenovo.materialdesign.views.SlidingTabLayout;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG_SORT_NAME = "sortName";
    private static final String TAG_SORT_DATE = "sortDate";
    private static final String TAG_SORT_RATINGS = "sortRatings";
    private static final int JOB_ID = 100;
    private static final int POL_FREQUENCY = 12000000;
    //private static final int POL_FREQUENCY = 5000;
    private JobScheduler jobScheduler;
    private Toolbar toolbar;
    private SlidingTabLayout mTabs;
    private ViewPager mPager;
    private MyPagerAdapter adapter;
    FloatingActionButton actionButton;
    FloatingActionMenu actionMenu;
    public final int MOVIES_SEARCH_RESULTS = 0;
    public final int MOVIES_HITS = 1;
    public final int MOVIES_UPCOMING = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jobScheduler = JobScheduler.getInstance(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                constructJob();
            }
        }, 30000);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        mPager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });
        mTabs.setViewPager(mPager);

        createFAB();
    }

    private void constructJob() {
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, MyService.class));
        builder.setPeriodic(POL_FREQUENCY)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);
        jobScheduler.schedule(builder.build());
    }

    private void createFAB() {
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.sort);
        actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();
        ImageView iconSortName = new ImageView(this); // Create an icon
        iconSortName.setImageResource(R.drawable.ic_tab1);
        ImageView iconSortDate = new ImageView(this); // Create an icon
        iconSortDate.setImageResource(R.drawable.ic_tab2);
        ImageView iconSortRaiting = new ImageView(this); // Create an icon
        iconSortRaiting.setImageResource(R.drawable.ic_tab3);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        SubActionButton buttonSortByName = itemBuilder.setContentView(iconSortName).build();
        SubActionButton buttonSortByDate = itemBuilder.setContentView(iconSortDate).build();
        SubActionButton buttonSortByRating = itemBuilder.setContentView(iconSortRaiting).build();
        buttonSortByName.setTag(TAG_SORT_NAME);
        buttonSortByDate.setTag(TAG_SORT_DATE);
        buttonSortByRating.setTag(TAG_SORT_RATINGS);
        buttonSortByName.setOnClickListener(this);
        buttonSortByDate.setOnClickListener(this);
        buttonSortByRating.setOnClickListener(this);
        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonSortByName)
                .addSubActionView(buttonSortByDate)
                .addSubActionView(buttonSortByRating)
                .attachTo(actionButton)
                .build();
    }

    private void toggleTranslateFAB(float slideOffset) {
        if (actionMenu != null) {
            if (actionMenu.isOpen()) {
                actionMenu.close(true);
            }
            actionButton.setTranslationX(slideOffset * 400);
        }
    }

    public void onDrawerSlide(float slideOffset) {
        toggleTranslateFAB(slideOffset);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigate) {
            startActivity(new Intent(this, SubActivity.class));
        }
        if (id == R.id.action_library) {
            startActivity(new Intent(this, ActivityUsingTabLibrary.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = (Fragment) adapter.instantiateItem(mPager, mPager.getCurrentItem());
        if (fragment instanceof SortListener) {

            if (v.getTag().equals(TAG_SORT_NAME)) {
                ((SortListener) fragment).onSortByName();
            }
            if (v.getTag().equals(TAG_SORT_DATE)) {
                ((SortListener) fragment).onSortByDate();
            }
            if (v.getTag().equals(TAG_SORT_RATINGS)) {
                ((SortListener) fragment).onSortByRating();
            }
        }
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {

        int[] icons = {R.drawable.ic_tab1, R.drawable.ic_tab2, R.drawable.ic_tab3};
        String[] tabText = getResources().getStringArray(R.array.tabs);

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabText = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment myFragment = null;
            switch (position) {
                case MOVIES_SEARCH_RESULTS:
                    myFragment = FragmentSearch.newInstance("", "");
                    break;
                case MOVIES_HITS:
                    myFragment = FragmentBoxOffice.newInstance("", "");
                    break;
                case MOVIES_UPCOMING:
                    myFragment = FragmentUpcoming.newInstance("", "");
                    break;
            }
            return myFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable drawable = getResources().getDrawable(icons[position]);
            drawable.setBounds(0, 0, 96, 96);
            ImageSpan imageSpan = new ImageSpan(drawable);
            SpannableString spannableString = new SpannableString(" ");
            spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;

        }

        @Override
        public int getCount() {
            return 3;
        }
    }


}
