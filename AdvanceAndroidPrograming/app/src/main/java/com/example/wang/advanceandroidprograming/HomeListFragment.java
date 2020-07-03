package com.example.wang.advanceandroidprograming;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.fragment.app.Fragment;

 import android.content.Context;
 import android.content.Intent;
 import android.os.Bundle;
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;
 import android.view.LayoutInflater;
 import android.view.Menu;
 import android.view.MenuInflater;
 import android.view.MenuItem;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.TextView;
 import java.util.List;

public class  HomeListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mCrimeRecyclerView;
    private HomeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onHomeSelected(Home home);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.home_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_home_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_home:
                Home home = new Home();
                HomeStorage.get(getActivity()).addItem(home);
                Intent intent = HomePagerActivity
                        .newIntent(getActivity(), home.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        HomeStorage hs = HomeStorage.get(getActivity());
        int homeCount = hs.getItem().size();
        String subtitle = getString(R.string.subtitle_format, homeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    public void updateUI() {
        HomeStorage homeSt = HomeStorage.get(getActivity());
        List<Home> home = homeSt.getItem();
        if (mAdapter == null) {
       mAdapter = new HomeAdapter(home);
       mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setItems(home);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }




    private class HomeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Home mHome;
        private TextView mTitleTextView;
        private TextView  mValueTextView;


        public HomeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_home, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.home_name);
            mValueTextView = (TextView) itemView.findViewById(R.id.home_value_1);
        }

        /*
        Bind to display on screen. Show the name and value on sceen
         */
        public void bind(Home home) {
            mHome = home;
           mTitleTextView.setText(mHome.getName());
           String str1 = Integer.toString(mHome.getValue());
            mValueTextView.setText("$"+str1);


        }

        /*
        When item is clicked use pager to go to next screen
         */
        @Override
        public void onClick(View view) {
            Intent intent = HomePagerActivity.newIntent(getActivity(), mHome.getId());
            startActivity(intent);
        }
    }

    private class HomeAdapter extends RecyclerView.Adapter<HomeHolder> {
        private List<Home> mHome;
        public HomeAdapter(List<Home> home) {
            mHome = home;
        }
        @Override
        public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new HomeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(HomeHolder holder, int position) {
            Home home = mHome.get(position);
            holder.bind(home);
        }

        @Override
        public int getItemCount() {
            return mHome.size();
        }

        public void setItems(List<Home> home) {
            mHome = home;
        }
    }
}
