package com.example.proyecto2_progra5.ui.catalog;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto2_progra5.databinding.FragmentCatalogoBinding;

public class Catalogo extends Fragment {

    private FragmentCatalogoBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CatalogoViewModel catalogoViewModel =
                new ViewModelProvider(this).get(CatalogoViewModel.class);

        binding = FragmentCatalogoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}