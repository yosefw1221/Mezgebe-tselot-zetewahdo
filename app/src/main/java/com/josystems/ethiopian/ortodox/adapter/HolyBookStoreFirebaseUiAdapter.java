package com.josystems.ethiopian.ortodox.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.josystems.ethiopian.ortodox.database.MyHolyBookDB;
import com.josystems.ethiopian.ortodox.model.HolyBook;
import com.josystems.ethiopian.ortodox.R;
import com.josystems.ethiopian.ortodox.utils.Analytics;
import com.josystems.ethiopian.ortodox.utils.Util;
import com.squareup.picasso.Picasso;

public class HolyBookStoreFirebaseUiAdapter extends FirestoreRecyclerAdapter<HolyBook, HolyBookStoreFirebaseUiAdapter.ViewHolder> {
    final MyHolyBookDB holyBookDB;
    final ProgressBar progressBar;

    public HolyBookStoreFirebaseUiAdapter(@NonNull FirestoreRecyclerOptions<HolyBook> options, Context context, ProgressBar progressBar) {
        super(options);
        holyBookDB = new MyHolyBookDB(context);
        this.progressBar = progressBar;
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull ViewHolder holder) {
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
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final HolyBook model) {
        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE)
            progressBar.setVisibility(View.GONE);
        if (model.getIconUrl()!=null&&!model.getIconUrl().isEmpty())
        Picasso.get().load(model.getIconUrl()).placeholder(R.drawable.placeholder).into(holder.image);
        holder.title.setText(model.getTitle());
        holder.language.setText(model.getLanguage());
        holder.description.setText(model.getDescription());
        setDownloadState(holder.downloadBtn, model.getId(), model.getLastUpdate());
        holder.downloadBtn.setOnClickListener(downloadClickListener(model,holder.downloadBtn,holder.contentLoadingProgressBar,holder.image.getDrawable()));
    }

    public void setDownloadState(Button view, String id, long lastUpdate) {
        switch (holyBookDB.getHollyBookState(id, lastUpdate)) {
            case 2:
                view.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_cloud_done_black_24dp, view.getContext().getTheme()), null, null, null);
                view.setTextColor(Color.GREEN);
                view.setEnabled(false);
                view.setText("ወርዷል！");
                break;
            case 1:
                view.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_baseline_update_24, view.getContext().getTheme()), null, null, null);
                view.setTextColor(Color.YELLOW);
                view.setEnabled(true);
                view.setText("አዘምን！");
                break;
            default:
                view.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_cloud_download_black_24dp, view.getContext().getTheme()), null, null, null);
                view.setTextColor(Color.BLUE);
                view.setText("አውርድ！");
                view.setEnabled(true);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holy_book_store_item, parent, false);
        return new ViewHolder(v);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
         ImageView image;
         TextView title;
         TextView description;
         Chip language;
         AppCompatButton downloadBtn;
         ProgressBar contentLoadingProgressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.holybook_store_icon);
            title = itemView.findViewById(R.id.holybook_store_title);
            language = itemView.findViewById(R.id.holybook_store_language);
            description = itemView.findViewById(R.id.holybook_store_description);
            downloadBtn = itemView.findViewById(R.id.holybook_store_download);
            contentLoadingProgressBar = itemView.findViewById(R.id.holybook_store_progressbar);
        }
    }
    private View.OnClickListener downloadClickListener(final HolyBook model, final Button downloadBtn, final ProgressBar contentLoadingProgressBar,final Drawable image){
        return new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try {
                    view.setVisibility(View.GONE);
                    contentLoadingProgressBar.setVisibility(View.VISIBLE);
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    String fileUrl = model.getDownloadUrl();
                    DocumentReference documentReference = firestore.collection("holybooks").document(fileUrl);
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            contentLoadingProgressBar.setVisibility(View.GONE);
                            downloadBtn.setVisibility(View.VISIBLE);
                            String data = null;
                            if (value != null)
                                data = (String) value.get("data");
                            if (data != null) {
                                model.setDataString(data);
                                holyBookDB.addHolyBook(model);
                                Util.downloadHolyIconToStorage(downloadBtn.getContext(), model.getId(), model.getIconUrl());
                                downloadBtn.setText("ወርዷል!");
                                downloadBtn.setEnabled(false);
                                downloadBtn.setTextColor(Color.GREEN);
                                downloadBtn.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_cloud_done_black_24dp, view.getContext().getTheme()), null, null, null);
                                Analytics.downloadEvent(view.getContext(),model,"SUCCESS");
                            } else {
                                downloadBtn.setText("እንደገና ይሞክሩ!");
                                downloadBtn.setTextColor(Color.RED);
                                downloadBtn.setEnabled(true);
                                downloadBtn.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_baseline_error_outline_24, view.getContext().getTheme()), null, null, null);
                                Analytics.downloadEvent(view.getContext(),model,"DATA IS NULL");
                            }
                        }
                    });
                } catch (Exception e) {
                    downloadBtn.setVisibility(View.VISIBLE);
                    Analytics.downloadEvent(view.getContext(),model,e.toString());
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
            }
        };
    }
}