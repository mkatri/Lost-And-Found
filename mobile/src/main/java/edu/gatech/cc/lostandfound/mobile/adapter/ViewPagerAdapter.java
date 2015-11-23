package edu.gatech.cc.lostandfound.mobile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import edu.gatech.cc.lostandfound.mobile.fragment.FoundFragment;
import edu.gatech.cc.lostandfound.mobile.fragment.LostFragment;
import edu.gatech.cc.lostandfound.mobile.fragment.MyPostFragment;

/**
 * Created by guoweidong on 10/24/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                LostFragment lostFragment = new LostFragment();
                return lostFragment;
            case 1:
                FoundFragment foundFragment = new FoundFragment();
                return foundFragment;
            case 2:
                MyPostFragment myPostFragment = new MyPostFragment();
                return myPostFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
