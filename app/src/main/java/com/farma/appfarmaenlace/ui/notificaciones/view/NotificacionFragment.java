package com.farma.appfarmaenlace.ui.notificaciones.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farma.appfarmaenlace.R;
import com.farma.appfarmaenlace.data.api.ApiClient;
import com.farma.appfarmaenlace.data.api.ApiDigitalizacionService;
import com.farma.appfarmaenlace.data.api.ApiResponseNotificacion;
import com.farma.appfarmaenlace.data.api.GeneralApiService;
import com.farma.appfarmaenlace.databinding.FragmentHomeBinding;
import com.farma.appfarmaenlace.databinding.FragmentNotificacionBinding;
import com.farma.appfarmaenlace.ui.base.ViewModelFactory;
import com.farma.appfarmaenlace.ui.home.model.Farmacia;
import com.farma.appfarmaenlace.ui.home.view.HomeFragmentDirections;
import com.farma.appfarmaenlace.ui.home.viewmodel.HomeViewModel;
import com.farma.appfarmaenlace.ui.notificaciones.adapter.ListaNotificacionesAdapter;
import com.farma.appfarmaenlace.ui.notificaciones.viewmodel.NotificacionViewModel;
import com.farma.appfarmaenlace.utils.SharedPreferencesApp;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificacionFragment extends Fragment {
    private NotificacionViewModel viewModel;
    private SharedPreferences preferences;
    private FragmentNotificacionBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificacionFragment newInstance(String param1, String param2) {
        NotificacionFragment fragment = new NotificacionFragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificacionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return  view;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_notificacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences= SharedPreferencesApp.getPreferences(getContext());
        setupViewModel();
        setupObserver();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.cerrar_sesion).setVisible(false);
    }

    private void setupViewModel() {
        Retrofit retrofit = new ApiClient("http://192.168.240.6/ITEDigitalizacionAPI3"+ "/").retrofitInstance();
        ApiDigitalizacionService service = (retrofit.create(ApiDigitalizacionService.class));
        viewModel = ViewModelProviders
                .of(this, new ViewModelFactory(service))
                .get(NotificacionViewModel.class);
        Moshi moshi=new Moshi.Builder().build();
        JsonAdapter<Farmacia> adapter=moshi.adapter(Farmacia.class);
        Farmacia farmacia= null;
        try {
            farmacia = adapter.fromJson(preferences.getString(SharedPreferencesApp.FARMACIA_KEY,""));
            viewModel.getNotificaciones(Integer.parseInt(farmacia.getIdbodega()),"bearer "+preferences.getString(SharedPreferencesApp.TOKEN_APP_KEY,""));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void setupObserver() {
        viewModel.getNotificacionesResponse().observe(getViewLifecycleOwner(),listResource -> {
            switch (listResource.getStatus()){
                case LOADING:
                    /*binding.toolbarhome.txtNroNotificaciones.setText("00");
                    if(errorSnackbar!=null){
                        errorSnackbar.dismiss();
                    }
                    binding.progressBar.setVisibility(View.VISIBLE);*/
                    break;
                case SUCCESS:
                    ListaNotificacionesAdapter adapter=new ListaNotificacionesAdapter(listResource.getData());
                    adapter.setOnClickListener(new ListaNotificacionesAdapter.OnClickListener() {
                        @Override
                        public void click(ApiResponseNotificacion notificacion) {
                            Navigation.findNavController(getView()).navigate(NotificacionFragmentDirections.iropcionesdig());
                        }
                    });
                    binding.recyclerNotificaciones.setHasFixedSize(true);
                    binding.recyclerNotificaciones.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerNotificaciones.setAdapter(adapter);
                    /*if(errorSnackbar!=null){
                        errorSnackbar.dismiss();
                    }
                    binding.progressBar.setVisibility(View.GONE);
                    String nronot=listResource.getData().size()<10?"0"+listResource.getData().size():""+listResource.getData().size();
                    binding.toolbarhome.txtNroNotificaciones.setText(nronot);
                    if(listResource.getData().size()>0){
                        abrirDialogoNotificaciones();
                    }*/
                    break;
                case ERROR:
                    /*binding.progressBar.setVisibility(View.GONE);
                    errorSnackbar = Snackbar.make(binding.getRoot(), listResource.getMessage(), Snackbar.LENGTH_INDEFINITE);
                    errorSnackbar.setAction("Cerrar", v -> {
                        errorSnackbar.dismiss();
                        viewModel.getToken();
                    });
                    errorSnackbar.show();*/
                    break;
            }
        });
    }
}