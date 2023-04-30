package com.mirror.jmarket.view.fragment;

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

import com.mirror.jmarket.adapter.HomeItemAdapter;
import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.databinding.FragmentHomeBinding;
import com.mirror.jmarket.utils.RxAndroidUtils;
import com.mirror.jmarket.view.activity.CreateItemActivity;
import com.mirror.jmarket.view.activity.DetailItemActivity;
import com.mirror.jmarket.viewmodel.ItemViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;

    // viewModel
    private ItemViewModel itemViewModel;

    // Adapter
    private HomeItemAdapter adapter;

    private String searchItem = "";

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

        init();
        initObserve();
        initListener();
        initUtil();

    }

    void init()  {
        // recyclerview adapter 연결
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setHasFixedSize(true);
        adapter = new HomeItemAdapter();
        binding.recyclerView.setAdapter(adapter);

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        itemViewModel.getHomeItems("");
    }

    void initObserve() {
        itemViewModel.getItems().observe(getActivity(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                binding.progress.setVisibility(View.VISIBLE);

                if (items.size() == 0 && !searchItem.equals("")) {
                    binding.noticeView.setVisibility(View.VISIBLE);
                    binding.notice.setText("'" + searchItem + "'");
                } else {
                    binding.noticeView.setVisibility(View.GONE);
                }

                if (items != null) {
                    adapter.setItems(items, false);
                    binding.progress.setVisibility(View.GONE);
                } else {
                    binding.progress.setVisibility(View.GONE);
                }
            }
        });
    }

    void initListener() {
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

    void initUtil() {
        Observable<String> editTextObservable = RxAndroidUtils.getInstance().getEditTextObservable(binding.search);
        editTextObservable
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    Log.d(RxAndroidUtils.getInstance().getTag(), s);
                    String inputText = binding.search.getText().toString();
                    Log.d(RxAndroidUtils.getInstance().getTag() + "!@!@", inputText);
                    itemViewModel.getHomeItems(inputText);
                    searchItem = inputText;
                });
    }

}