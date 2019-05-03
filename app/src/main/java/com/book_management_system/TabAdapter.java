package com.book_management_system;

import android.support.v4.app.Fragment;;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.book_management_system.com.AndroidUI.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentPagerAdapter
{
    private List<Fragment> fragments;
    private String[] tabTitle;

    public TabAdapter(FragmentManager fm,List<Fragment> fragments,String[] tabTitle)
    {
        super(fm);
        this.fragments=fragments;
        this.tabTitle=tabTitle;
    }//构造方法

    @Override
    public Fragment getItem(int position)
    {
        return fragments.get(position);
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return tabTitle[position];
    }
}