package com.example.lenovo.materialdesign.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lenovo.materialdesign.R;
import com.example.lenovo.materialdesign.activities.MainActivity;
import com.example.lenovo.materialdesign.adapters.InfoAdapter;
import com.example.lenovo.materialdesign.pojo.Information;

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
        //infoAdapter.setClickListner(this);
        recyclerView.setAdapter(infoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListner(getActivity(), recyclerView, new ClickListner() {
            @Override
            public void onClicked(View view, int position) {
                Toast.makeText(getActivity(), "Clicked - > " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClicked(View view, int position) {
                Toast.makeText(getActivity(), "Long Clicked - > " + position, Toast.LENGTH_SHORT).show();
            }
        }));
        return layout;
    }

    public static List<Information> getData() {
        List<Information> list = new ArrayList<Information>();
        Information info;
        int iconIds[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher};
        String[] titles = {"Title 1", "Title 2"};
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
                ((MainActivity) getActivity()).onDrawerSlide(slideOffset);
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }

            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
                if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
                    mDrawerLayout.openDrawer(containerView);
                }
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

    /*@Override
    public void itemClicked(View view, int position) {
        startActivity(new Intent(getActivity(), SubActivity.class));
    }*/

    class RecyclerTouchListner implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListner clickListner;

        public RecyclerTouchListner(Context context, final RecyclerView recyclerView, final ClickListner clickListner) {
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null && clickListner != null) {
                        clickListner.onLongClicked(childView, recyclerView.getChildAdapterPosition(childView));
                    }
                }
            });
            this.clickListner = clickListner;
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && clickListner != null && gestureDetector.onTouchEvent(e)) {
                clickListner.onClicked(childView, rv.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListner {
        public void onClicked(View view, int position);

        public void onLongClicked(View view, int position);
    }
}
