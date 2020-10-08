package com.josystems.ethiopian.ortodox.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.josystems.ethiopian.ortodox.Database.MyHolyBookDB;
import com.josystems.ethiopian.ortodox.Model.HolyBook;
import com.josystems.ethiopian.ortodox.R;
import com.josystems.ethiopian.ortodox.utils.Util;
import com.squareup.picasso.Picasso;

public class HolyBookStoreFirebaseUiAdapter extends FirestoreRecyclerAdapter<HolyBook, HolyBookStoreFirebaseUiAdapter.ViewHolder> {
    MyHolyBookDB holyBookDB;
    ProgressBar progressBar;

    public HolyBookStoreFirebaseUiAdapter(@NonNull FirestoreRecyclerOptions<HolyBook> options, Context context, ProgressBar progressBar) {
        super(options);
        holyBookDB = new MyHolyBookDB(context);
        this.progressBar = progressBar;
    }
    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
        Log.e(">>>>>FIREBASE UI ERROR ", e.toString());
        if (progressBar!=null)progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull ViewHolder holder) {
        Log.e(">>>>>>>>> holder", ">>>>>onFailedToRecycleView ");
        return super.onFailedToRecycleView(holder);

    }

    @Override
    public void updateOptions(@NonNull FirestoreRecyclerOptions<HolyBook> options) {
        super.updateOptions(options);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (getItemCount() == 0) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull HolyBook model) {
        if (progressBar!=null&&progressBar.getVisibility()==View.VISIBLE)progressBar.setVisibility(View.GONE);
        Picasso.get().load(model.getIconUrl()).placeholder(R.drawable.mary).into(holder.image);
        holder.title.setText(model.getTitle());
        holder.language.setText(model.getLanguage());
        holder.description.setText(model.getDescription());
        if (holyBookDB.isAlreadyDownloaded(model.getId())) {
            holder.downloadBtn.setText("ወርዷል");
            holder.downloadBtn.setCompoundDrawablesWithIntrinsicBounds(holder.title.getResources().getDrawable(R.drawable.ic_cloud_done_black_24dp), null, null, null);
        } else {
            holder.downloadBtn.setText("አውርድ");
        }
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holy_book_store_item, parent, false);
        return new ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView description;
        Chip language;
        Button downloadBtn;
        ProgressBar contentLoadingProgressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.holybook_store_icon);
            title = itemView.findViewById(R.id.holybook_store_title);
            language = itemView.findViewById(R.id.holybook_store_language);
            description = itemView.findViewById(R.id.holybook_store_description);
            downloadBtn = itemView.findViewById(R.id.holybook_store_download);
            contentLoadingProgressBar = itemView.findViewById(R.id.holybook_store_progressbar);

            downloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    view.setVisibility(View.GONE);
                    contentLoadingProgressBar.setVisibility(View.VISIBLE);
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    Toast.makeText(view.getContext(), "url " + getItem(getAdapterPosition()).getDownloadUrl(), Toast.LENGTH_SHORT).show();
                    String fileUrl = getItem(getAdapterPosition()).getDownloadUrl();
                    DocumentReference documentReference = firestore.collection("holybooks").document(fileUrl);
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            contentLoadingProgressBar.setVisibility(View.GONE);
                            downloadBtn.setVisibility(View.VISIBLE);
                            if (error != null)
                                downloadBtn.setText("እንደገና ይሞክሩ");
                            else if (value != null) {
                                String data = (String) value.get("data");
                                if (data != null) {
                                    HolyBook holyBook = getItem(getAdapterPosition());
                                    holyBook.setDataString(data);
                                    holyBookDB.addHolyBook(holyBook);
                                    Util.saveHolyIcontoStorage(image.getContext(), holyBook.getId(), image.getDrawable());
                                    downloadBtn.setText("ወርዷል");
                                    downloadBtn.setCompoundDrawablesWithIntrinsicBounds(title.getResources().getDrawable(R.drawable.ic_cloud_done_black_24dp), null, null, null);
                                } else {
                                    downloadBtn.setText("እንደገና ይሞክሩ");
                                }
                            }
                        }
                    });
                }
            });
        }
    }
}