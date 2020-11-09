package com.josystems.ethiopian.ortodox.reader;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.josystems.ethiopian.ortodox.model.Chapter;

import java.util.List;

public class ChapterFragmentAdapter extends FragmentPagerAdapter {
    private final List<Chapter> chapterList;

    public ChapterFragmentAdapter(FragmentManager fm, List<Chapter> chapterList) {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.chapterList = chapterList;
    }

    @Override
    public Fragment getItem(int position) {
        Chapter chapter = chapterList.get(position);
        return ChapterFragment.getInstance(chapter);
    }

    @Override
    public int getCount() {
        return chapterList.size();
    }
}
