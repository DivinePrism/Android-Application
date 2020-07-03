package com.example.wang.advanceandroidprograming;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.UUID;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class HomePagerActivity  extends AppCompatActivity
        implements HomeFragment.Callbacks{

    private static final String EXTRA_HOME_ID =
            "extra_home";

    private ViewPager mViewPager;
    private List<Home> mHomes;

    public static Intent newIntent(Context packageContext, UUID homeId) {
        Intent intent = new Intent(packageContext, HomePagerActivity.class);
        intent.putExtra(EXTRA_HOME_ID,homeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pager);



        UUID homeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_HOME_ID);
        mViewPager = (ViewPager) findViewById(R.id.HomePagerActivity);

        mHomes = HomeStorage.get(this).getItem();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Home home = mHomes.get(position);
                return HomeFragment.newInstance(home.getId());
            }

            @Override
            public int getCount() {
                return mHomes.size();
            }


        });
        for (int i = 0; i < mHomes.size(); i++) {
            //Log.d(String.valueOf( "???"+mHomes.size()+"???"),"test");
            if (mHomes.get(i).getId().equals(homeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onHomeUpdated(Home home) {

    }
}