package com.example.wang.advanceandroidprograming;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public class HomeListActivity extends SingleFragmentActivity
    implements HomeListFragment.Callbacks, HomeFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new HomeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onHomeSelected(Home home) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = HomePagerActivity.newIntent(this, home.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = HomeFragment.newInstance(home.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    public void onHomeUpdated(Home home) {
        HomeListFragment listFragment = (HomeListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }



}
