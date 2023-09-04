package com.farma.appfarmaenlace.ui.dibujadopropiedades.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.farma.appfarmaenlace.data.api.ApiResponseEnvioInformacion;
import com.farma.appfarmaenlace.data.api.Propiedades;
import com.farma.appfarmaenlace.databinding.FragmentDibujarPropiedadesBinding;
import com.farma.appfarmaenlace.ui.dibujadopropiedades.adapter.ListaPropiedadesAdapter;
import com.farma.appfarmaenlace.ui.dibujadopropiedades.model.ItemPropiedad;
import com.farma.appfarmaenlace.utils.SharedPreferencesApp;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DibujarPropiedadesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DibujarPropiedadesFragment extends Fragment {
    private FragmentDibujarPropiedadesBinding binding;
    private SharedPreferences preferences;
    private Moshi moshi;
    private ApiResponseEnvioInformacion estructuraEnvio;
    private List<Propiedades> propiedadesList;
    private ListaPropiedadesAdapter listaPropiedadesAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DibujarPropiedadesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DibujarPropiedadesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DibujarPropiedadesFragment newInstance(String param1, String param2) {
        DibujarPropiedadesFragment fragment = new DibujarPropiedadesFragment();
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
        binding = FragmentDibujarPropiedadesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = SharedPreferencesApp.getPreferences(getContext());
        moshi = new Moshi.Builder().build();
        try {
            getPropiedades();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupUI();
    }

    @Override
    public void onStop() {
        super.onStop();
        //ocultar teclado
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void setupUI() {
        listaPropiedadesAdapter = new ListaPropiedadesAdapter(getItemsPropiedad(), getContext());
        binding.recyclerPropiedades.setHasFixedSize(true);
        binding.recyclerPropiedades.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerPropiedades.setAdapter(listaPropiedadesAdapter);
        binding.btncargar.setOnClickListener(v -> {
            List<ItemPropiedad> list = ((ListaPropiedadesAdapter) binding.recyclerPropiedades.getAdapter()).getList();
            for (ItemPropiedad item : list) {
                if (item.getDato() == null) {
                    Toast.makeText(getContext(), "Llene todos los campos del formulario.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (item.getDato().equals("")) {
                    Toast.makeText(getContext(), "Llene todos los campos del formulario.", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            guardarEstructuraEnvio();
            DibujarPropiedadesFragmentDirections.Irescaner irescaner = DibujarPropiedadesFragmentDirections.irescaner();
            irescaner.setManual(true);
            Navigation.findNavController(getView()).navigate(irescaner);
        });
    }

    private void getPropiedades() throws IOException {
        String jsonstring = preferences.getString(SharedPreferencesApp.ESTRUCTURA_DOC_KEY, "");
        JsonAdapter<ApiResponseEnvioInformacion> adapter = moshi.adapter(ApiResponseEnvioInformacion.class);
        estructuraEnvio = adapter.fromJson(jsonstring);
        propiedadesList = estructuraEnvio.getPropiedades();
    }

    private void guardarEstructuraEnvio() {
        llenarDatosPropiedades();

        JsonAdapter<ApiResponseEnvioInformacion> adapter = moshi.adapter(ApiResponseEnvioInformacion.class);
        String jsonstring = adapter.toJson(estructuraEnvio);
        preferences.edit().putString(SharedPreferencesApp.ESTRUCTURA_DOC_KEY, jsonstring).apply();
    }

    private List<ItemPropiedad> getItemsPropiedad() {
        List<ItemPropiedad> items = new ArrayList<>();
        List<Propiedades> propiedadesRaiz = propiedadesList.stream()
                .filter(itemp -> itemp.getPrd_propiedad_raiz() == 0)
                .collect(Collectors.toList());
        List<Propiedades> propiedadesHijo= propiedadesList.stream()
                .filter(itemp->itemp.getPrd_propiedad_raiz()!=0)
                .collect(Collectors.toList());
        for (Propiedades p:propiedadesRaiz){
            ItemPropiedad item = new ItemPropiedad();
            item.setPrd_codigo(p.getPrd_codigo());
            item.setPrd_nombre(p.getPrd_nombre());
            item.setPrd_propiedad_raiz(p.getPrd_propiedad_raiz());
            item.setTpd_tipo_dato(p.getTpd_tipo_dato());
            items.add(item);
        }
        for (Propiedades p:propiedadesHijo){
            ItemPropiedad item = items.stream()
                    .filter(itemPropiedad -> itemPropiedad.getPrd_codigo() == p.getPrd_propiedad_raiz())
                    .findAny()
                    .orElse(null);
            item.getOpciones().add(p.getPrd_nombre());
        }

        /*for (Propiedades p : propiedadesList) {
            if (p.getPrd_propiedad_raiz() == 0) {
                ItemPropiedad item = new ItemPropiedad();
                item.setPrd_codigo(p.getPrd_codigo());
                item.setPrd_nombre(p.getPrd_nombre());
                item.setPrd_propiedad_raiz(p.getPrd_propiedad_raiz());
                item.setTpd_tipo_dato(p.getTpd_tipo_dato());
                items.add(item);
            } else {
                ItemPropiedad item = items.stream()
                        .filter(itemPropiedad -> itemPropiedad.getPrd_codigo() == p.getPrd_propiedad_raiz())
                        .findAny()
                        .orElse(null);
                item.getOpciones().add(p.getPrd_nombre());
            }*/

        //Log.i("Propiedades I", items.toString());
        return items;
    }

    private void llenarDatosPropiedades() {
        List<ItemPropiedad> items = listaPropiedadesAdapter.getList();
        for (Propiedades p : propiedadesList) {
            for (ItemPropiedad i : items) {
                if (p.getPrd_codigo() == i.getPrd_codigo()) {
                    p.setDatos(i.getDato());
                }
            }
        }
    }
}