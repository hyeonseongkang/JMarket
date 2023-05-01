package com.mirror.jmarket.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.adapter.HomeItemAdapter;
import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.databinding.FragmentMyCompleteSalesListBinding;
import com.mirror.jmarket.view.activity.MainActivity;
import com.mirror.jmarket.viewmodel.ItemViewModel;

import java.util.List;


public class MyCompleteSalesListFragment extends Fragment {

    private static final String TAG = "MyCompleteSalesListFragment";

    private FragmentMyCompleteSalesListBinding binding;

    // viewModel
    private ItemViewModel itemViewModel;

    // Adapter
    private HomeItemAdapter adapter;

    private FirebaseUser user;

    public MyCompleteSalesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyCompleteSalesListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        initObserve();

    }

    void init() {
        user = MainActivity.USER;

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setHasFixedSize(true);
        adapter = new HomeItemAdapter();
        binding.recyclerView.setAdapter(adapter);

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        itemViewModel.getMyCompleteSalesItems(user.getUid());
    }

    void initObserve() {
        itemViewModel.getMyCompleteSalesItems().observe(getActivity(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                if (items != null) {
                    adapter.setItems(items, false);
                }
            }
        });
    }
}