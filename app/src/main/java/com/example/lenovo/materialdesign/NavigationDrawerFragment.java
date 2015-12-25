package com.example.lenovo.materialdesign;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    private RecyclerView recyclerView;
    public static final String PREF_fILE_NAME = "testPref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private InfoAdapter infoAdapter;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreference(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        infoAdapter = new InfoAdapter(getActivity(), getData());
        recyclerView.setAdapter(infoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }

    public static List<Information> getData() {
        List<Information> list = new ArrayList<Information>();
        Information info;
        int iconIds[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
        String[] titles = {"Title 1", "Title 2", "Title 3", "Title 4"};
        for (int i = 0; i < iconIds.length && i < titles.length; i++) {
            info = new Information();
            info.iconId = iconIds[i];
            info.title = titles[i];
            list.add(info);
        }
        return list;

    }

    public void setUp(int fragment_navigation_drawer, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragment_navigation_drawer);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreference(getActivity(), KEY_USER_LEARNED_DRAWER, "true");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }

            }
        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static void saveToPreference(Context context, String preferencename, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_fILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferencename, preferenceValue);
        editor.apply();
    }

    public static String readFromPreference(Context context, String preferencename, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_fILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferencename, defaultValue);
    }
}
