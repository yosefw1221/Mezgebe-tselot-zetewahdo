package com.josystems.ethiopian.ortodox.ui.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.josystems.ethiopian.ortodox.Adapter.HolyBookStoreFirebaseUiAdapter;
import com.josystems.ethiopian.ortodox.Model.HolyBook;
import com.josystems.ethiopian.ortodox.R;

public class StoreFragment extends Fragment {

    private HolyBookStoreFirebaseUiAdapter uiAdapter;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_store, container, false);
        final SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        final ProgressBar progressBar = root.findViewById(R.id.store_progressBar);
        recyclerView = root.findViewById(R.id.holybook_store_list);
        Query query = FirebaseFirestore.getInstance()
                .collection("holybookstore");
        FirestoreRecyclerOptions<HolyBook> recyclerOptions = new FirestoreRecyclerOptions.Builder<HolyBook>()
                .setQuery(query, HolyBook.class)
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
        uiAdapter = new HolyBookStoreFirebaseUiAdapter(recyclerOptions, getContext(), progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(uiAdapter);
        updateRecyclerView();
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        uiAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    public void updateRecyclerView() {
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.recycler_layout_animation);
        recyclerView.setLayoutAnimation(controller);
        uiAdapter.startListening();
        uiAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}