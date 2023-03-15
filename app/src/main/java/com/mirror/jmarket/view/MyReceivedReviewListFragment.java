package com.mirror.jmarket.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.adapter.ReviewItemAdapter;
import com.mirror.jmarket.model.Review;
import com.mirror.jmarket.databinding.FragmentMyReceivedReviewListBinding;
import com.mirror.jmarket.viewmodel.ItemViewModel;

import java.util.List;

public class MyReceivedReviewListFragment extends Fragment {

    private static final String TAG = "MyReceivedReviewListFragment";

    private FragmentMyReceivedReviewListBinding binding;

    // viewModel
    private ItemViewModel itemViewModel;

    // apdater
    private ReviewItemAdapter adapter;

    private FirebaseUser user;

    public MyReceivedReviewListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyReceivedReviewListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = MainActivity.USER;
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setHasFixedSize(true);
        adapter = new ReviewItemAdapter();
        binding.recyclerView.setAdapter(adapter);

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        itemViewModel.getReviews().observe(getActivity(), new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                Log.d("MyReceived", String.valueOf(reviews.size()));
                adapter.setReviews(reviews);
                if (reviews != null) {


                }

            }
        });
        itemViewModel.getReviews(user.getUid(), "Received");


    }


}
