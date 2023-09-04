package com.farma.appfarmaenlace.ui.opcionesdig.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farma.appfarmaenlace.databinding.FragmentOpcionDigitalizacionBinding;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpcionDigitalizacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpcionDigitalizacionFragment extends Fragment {
    private FragmentOpcionDigitalizacionBinding binding;
    private SimpleTooltip tooltip1;
    private SimpleTooltip tooltip2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OpcionDigitalizacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OpcionDigitalizacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OpcionDigitalizacionFragment newInstance(String param1, String param2) {
        OpcionDigitalizacionFragment fragment = new OpcionDigitalizacionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentOpcionDigitalizacionBinding.inflate(inflater,container,false);
        View view=binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
    }

    private void setupUI(){
        tooltip1=new SimpleTooltip.Builder(getContext())
                .anchorView(binding.btnmanual)
                .text("Digitalice documentos que requieran ingresar propiedades manuales, tales como: Fotos de apertura, recetas de antibióticos entre otros.")
                .gravity(Gravity.TOP)
                .animated(true)
                .transparentOverlay(true)
                .build();
                tooltip1.show();
        binding.btnautomatico.setOnClickListener(v -> {
            Navigation.findNavController(getView()).navigate(OpcionDigitalizacionFragmentDirections.irescaner());
        });
        binding.btnmanual.setOnClickListener(v -> {
            Navigation.findNavController(getView()).navigate(OpcionDigitalizacionFragmentDirections.irdepdocumentos());
        });
        tooltip2=new SimpleTooltip.Builder(getContext())
                .anchorView(binding.btnautomatico)
                .text("Digitalice documentos que contenga impreso el código de barra en la factura como: convenios, cupones, entre otros.")
                .gravity(Gravity.BOTTOM)
                .animated(true)
                .transparentOverlay(true)
                .build();
                tooltip2.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(tooltip1!=null&&tooltip2!=null){
            tooltip1.dismiss();
            tooltip2.dismiss();
        }
    }
}