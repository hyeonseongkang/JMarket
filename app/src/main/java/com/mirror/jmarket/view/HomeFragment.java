package com.mirror.jmarket.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirror.jmarket.R;
import com.mirror.jmarket.adapter.HomeItemAdapter;
import com.mirror.jmarket.adapter.HomeItemPhotoAdapter;
import com.mirror.jmarket.classes.Item;
import com.mirror.jmarket.databinding.FragmentHomeBinding;
import com.mirror.jmarket.viewmodel.ChatViewModel;
import com.mirror.jmarket.viewmodel.ItemViewModel;
import com.mirror.jmarket.viewmodel.LoginViewModel;

import java.util.List;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;

    // viewModel
    private ItemViewModel itemViewModel;

    // Adapter
    private HomeItemAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        // recyclerview adapter 연결
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setHasFixedSize(true);
        adapter = new HomeItemAdapter();
        binding.recyclerView.setAdapter(adapter);

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        itemViewModel.getHomeItems();
        itemViewModel.getItems().observe(getActivity(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                binding.progress.setVisibility(View.VISIBLE);

                if (items != null) {
                    adapter.setItems(items, false);
                    binding.progress.setVisibility(View.GONE);
                } else {
                    binding.progress.setVisibility(View.GONE);
                }
            }
        });

        adapter.setOnItemClickListener(new HomeItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                Intent intent = new Intent(getActivity(), DetailItemActivity.class);
                intent.putExtra("key", item.getKey());
                startActivity(intent);
            }
        });

        binding.createItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateItemActivity.class);
                startActivity(intent);
            }
        });

    }
}