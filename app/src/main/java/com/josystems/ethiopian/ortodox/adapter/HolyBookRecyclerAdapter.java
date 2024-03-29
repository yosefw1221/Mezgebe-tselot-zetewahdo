package com.josystems.ethiopian.ortodox.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.josystems.ethiopian.ortodox.model.HolyBook;
import com.josystems.ethiopian.ortodox.R;
import com.josystems.ethiopian.ortodox.reader.HolyBookReader;
import com.josystems.ethiopian.ortodox.utils.Util;

import java.util.List;

public class HolyBookRecyclerAdapter extends RecyclerView.Adapter<HolyBookRecyclerAdapter.ViewHolder> {

    private final Context ctx;
    private final List<HolyBook> holyBookList;
    public HolyBookRecyclerAdapter(Context ctx, List<HolyBook> holyBookList){
        this.ctx = ctx;
        this.holyBookList = holyBookList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.holy_book_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final HolyBook holyBook = holyBookList.get(position);
        Util.getHolyIconWithCallback(ctx, holyBook.getId(), holyBook.getIconUrl(), new Util.CallBack() {
            @Override
            public void onFinish(Drawable drawable) {
                holder.image.setImageDrawable(drawable);
            }
        });
        holder.title.setText(holyBook.getTitle());
        holder.language.setText(holyBook.getLanguage());
    }
    @Override
    public int getItemCount() {
        return holyBookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
          ImageView image;
          TextView title;
          Chip language;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.holybook_icon);
            title = itemView.findViewById(R.id.holybook_title);
            language = itemView.findViewById(R.id.holybook_language);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ctx.startActivity(new Intent(ctx, HolyBookReader.class).putExtra("id",holyBookList.get(getAdapterPosition()).getId()));
                }
            });
        }
    }
}