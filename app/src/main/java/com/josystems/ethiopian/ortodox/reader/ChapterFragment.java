package com.josystems.ethiopian.ortodox.reader;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.josystems.ethiopian.ortodox.model.Chapter;
import com.josystems.ethiopian.ortodox.R;
import com.josystems.ethiopian.ortodox.utils.Constant;

public class ChapterFragment extends Fragment {
    AppCompatTextView output;

    public static ChapterFragment getInstance(Chapter c) {
        Bundle args = new Bundle();
        args.putSerializable("chapter", c);
        ChapterFragment f = new ChapterFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Chapter chapter = (Chapter) (getArguments() != null ? getArguments().getSerializable("chapter") : new Chapter("error", "error"));
        View root = inflater.inflate(R.layout.fragment_chapter_reader, container, false);
        SharedPreferences preferences = getContext().getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        output = root.findViewById(R.id.reader_tv);
        ChapterViewModel.init(getContext());

        ChapterViewModel viewModel = ViewModelProviders.of(this).get(ChapterViewModel.class);
        viewModel.getFontSize().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer aFloat) {
                output.setTextSize(TypedValue.COMPLEX_UNIT_SP, aFloat);
            }
        });
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.pg2);
        int fontColor = preferences.getInt(Constant.PREF_FONT_COLOR(), Constant.getDefaultFontColor());
        if (fontColor != 0)
            output.setTextColor(fontColor);
        int fontSpacing = preferences.getInt(Constant.PREF_FONT_SPACING,Constant.DEFAULT_FONT_SPACING);
        output.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,fontSpacing, getResources().getDisplayMetrics()), 1.0f);
        output.setTypeface(typeface);

        new LoadHolyBookData().execute(chapter);
        return root;
    }
     class LoadHolyBookData extends AsyncTask<Chapter, Spanned,Spanned>{

         @Override
        protected Spanned doInBackground(Chapter... chapters) {
            return Html.fromHtml(chapters[0].getChapterData());
        }

        @Override
        protected void onPostExecute(Spanned s) {
            super.onPostExecute(s);
            output.setText(s);

        }
    }
}
