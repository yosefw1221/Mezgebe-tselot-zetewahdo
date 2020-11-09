package com.josystems.ethiopian.ortodox.ui.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.josystems.ethiopian.ortodox.R;
import com.josystems.ethiopian.ortodox.adapter.HolyBookStoreFirebaseUiAdapter;
import com.josystems.ethiopian.ortodox.model.HolyBook;

public class StoreFragment extends Fragment {

    private HolyBookStoreFirebaseUiAdapter uiAdapter;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        View root = inflater.inflate(R.layout.fragment_store, container, false);
        final SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        final ProgressBar progressBar = root.findViewById(R.id.store_progressBar);
        recyclerView = root.findViewById(R.id.holybook_store_list);
        Query query = FirebaseFirestore.getInstance()
                .collection("holybookstore").orderBy("lastUpdate");
        FirestoreRecyclerOptions<HolyBook> recyclerOptions = new FirestoreRecyclerOptions.Builder<HolyBook>()
                .setQuery(query, HolyBook.class)
                .setLifecycleOwner(this)
                .build();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                synchronized (this) {
                    updateRecyclerView();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        try {
            uiAdapter = new HolyBookStoreFirebaseUiAdapter(recyclerOptions, getContext(), progressBar);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(uiAdapter);
            updateRecyclerView();
        }catch (Exception e){
            FirebaseCrashlytics.getInstance().recordException(e);
            Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return root;
    }


    public void updateRecyclerView() {
        Query query = FirebaseFirestore.getInstance()
                .collection("holybookstore").orderBy("lastUpdate");
        FirestoreRecyclerOptions<HolyBook> recyclerOptions = new FirestoreRecyclerOptions.Builder<HolyBook>()
                .setQuery(query, HolyBook.class)
                .setLifecycleOwner(this)
                .build();
        uiAdapter.updateOptions(recyclerOptions);
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.recycler_layout_animation);
        recyclerView.setLayoutAnimation(controller);
        uiAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}