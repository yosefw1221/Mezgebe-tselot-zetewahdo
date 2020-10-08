package com.josystems.ethiopian.ortodox.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.josystems.ethiopian.ortodox.Model.Chapter;
import com.josystems.ethiopian.ortodox.R;

import java.util.List;

public class ChapterPopupRecyclerAdapter extends RecyclerView.Adapter<ChapterPopupRecyclerAdapter.ViewHolder> {

    private  Context ctx;
    private  List<Chapter> chapterList;
    private com.josystems.ethiopian.ortodox.Adapter.onChapterSelectedListener onChapterSelectedListener;

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
         TextView title;
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