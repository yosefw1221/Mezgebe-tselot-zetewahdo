package com.josystems.ethiopian.ortodox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.josystems.ethiopian.ortodox.R;
import com.josystems.ethiopian.ortodox.model.Chapter;

import java.util.List;

public class ChapterPopupRecyclerAdapter extends RecyclerView.Adapter<ChapterPopupRecyclerAdapter.ViewHolder> {

    private final Context ctx;
    private final List<Chapter> chapterList;
    private final com.josystems.ethiopian.ortodox.adapter.onChapterSelectedListener onChapterSelectedListener;

    public ChapterPopupRecyclerAdapter(Context ctx, List<Chapter> chapterList,onChapterSelectedListener onChapterSelectedListener){
        this.ctx = ctx;
        this.chapterList = chapterList;
        this.onChapterSelectedListener = onChapterSelectedListener;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.chapter_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(chapterList.get(position).getChapterName());
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        AppCompatButton title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.chapter_list_item_tv);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onChapterSelectedListener.onSelected(getAdapterPosition());
                }
            });
        }
    }
}