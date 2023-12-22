package com.example.proyecto2_progra5.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyecto2_progra5.CatalogoAdapter;
import com.example.proyecto2_progra5.CatalogoImageAdapter;
import com.example.proyecto2_progra5.R;
import com.example.proyecto2_progra5.databinding.FragmentHomeBinding;

import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        GetData();
        return root;
    }

SimpleAdapter ad;
    public void GetData() {
        ListView lstview = root.findViewById(R.id.listView);
        List<Map<String, String>> MyDataList = null;
        CatalogoAdapter MyData = new CatalogoAdapter();
        MyDataList = MyData.getlist();

        CatalogoImageAdapter adapter = new CatalogoImageAdapter(requireContext(), MyDataList);
        lstview.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
