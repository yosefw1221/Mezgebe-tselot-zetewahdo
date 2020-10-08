package com.josystems.ethiopian.ortodox.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.josystems.ethiopian.ortodox.Model.HolyBook;
import com.josystems.ethiopian.ortodox.R;
import com.josystems.ethiopian.ortodox.Reader.HolyBookReader;
import com.josystems.ethiopian.ortodox.utils.Util;

import java.util.List;

public class HolyBookRecyclerAdapter extends RecyclerView.Adapter<HolyBookRecyclerAdapter.ViewHolder> {

    private  Context ctx;
    private  List<HolyBook> holyBookList;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HolyBook holyBook = holyBookList.get(position);
        holder.image.setImageDrawable(Util.getHolyIconFromStorage(ctx,holyBook.getId()));
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