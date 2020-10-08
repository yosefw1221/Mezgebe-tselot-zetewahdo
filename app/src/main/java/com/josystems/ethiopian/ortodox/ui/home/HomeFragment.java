package com.josystems.ethiopian.ortodox.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josystems.ethiopian.ortodox.Adapter.HolyBookRecyclerAdapter;
import com.josystems.ethiopian.ortodox.Model.HolyBook;
import com.josystems.ethiopian.ortodox.R;

import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView.Adapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.holybook_list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.init(getContext());
        adapter = new HolyBookRecyclerAdapter(getContext(),homeViewModel.getAllHollyBooks().getValue());
        recyclerView.setAdapter(adapter);
        homeViewModel.getAllHollyBooks().observe(this, new Observer<List<HolyBook>>() {
            @Override
            public void onChanged(List<HolyBook> holyBooks) {
                adapter.notifyDataSetChanged();
            }
        });
        return root;
    }
}