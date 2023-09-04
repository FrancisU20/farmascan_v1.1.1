package com.farma.appfarmaenlace.ui.home.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.farma.appfarmaenlace.R;
import com.farma.appfarmaenlace.data.api.ApiClient;
import com.farma.appfarmaenlace.data.api.ApiDigitalizacionService;
import com.farma.appfarmaenlace.data.api.GeneralApiService;
import com.farma.appfarmaenlace.databinding.FragmentHomeBinding;
import com.farma.appfarmaenlace.ui.base.ViewModelFactory;
import com.farma.appfarmaenlace.ui.dialogos.AceptarDialogo;
import com.farma.appfarmaenlace.ui.dialogos.EnvioDialogo;
import com.farma.appfarmaenlace.ui.home.model.Atribucion;
import com.farma.appfarmaenlace.ui.home.model.Farmacia;
import com.farma.appfarmaenlace.ui.home.viewmodel.HomeViewModel;
import com.farma.appfarmaenlace.ui.notificaciones.view.NotificacionFragmentDirections;
import com.farma.appfarmaenlace.utils.SharedPreferencesApp;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.Map;

import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private Farmacia farmacia;
    private String ipfarmacia;
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private static final int LONG_DELAY = 6000;
    private Snackbar errorSnackbar;
    private SharedPreferences preferences;
    private EnvioDialogo dialogoNoti;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences=SharedPreferencesApp.getPreferences(getContext());
        getArgsAction();
        setupUI();
        setupViewModel();
        setupObserver();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(errorSnackbar!=null){
            errorSnackbar.dismiss();
        }
    }

    private void getArgsAction(){
        farmacia=HomeFragmentArgs.fromBundle(getArguments()).getFarmacia();
        ipfarmacia=HomeFragmentArgs.fromBundle(getArguments()).getIpfarmacia();

        Moshi moshi=new Moshi.Builder().build();
        JsonAdapter<Farmacia> adapter=moshi.adapter(Farmacia.class);
        String jsonf=adapter.toJson(farmacia);

       preferences.edit()
                .putString(SharedPreferencesApp.IP_FARMACIA_KEY,ipfarmacia)
                .putString(SharedPreferencesApp.FARMACIA_KEY,jsonf)
                .apply();
    }

    @SuppressLint("WrongConstant")
    private void setupUI(){
        binding.progressBar.setVisibility(View.GONE);
        binding.button.setVisibility(View.GONE);
        for(Atribucion a: farmacia.getAtribuciones()){
            if(a.getModulo().equals("mod_digitalizacion")){
                binding.button.setVisibility(View.VISIBLE);
                break;
            }
        }
        binding.button.setOnClickListener(v -> {
            Navigation.findNavController(getView()).navigate(HomeFragmentDirections.iropcionesdig());
        });
        binding.toolbarhome.txtnombrecorto.setText(farmacia.getNombreCorto());
        binding.toolbarhome.txtfarmacia.setText(farmacia.getFarmacia());

        binding.imageView.setImageResource(R.drawable.backgroundhome);
        binding.imageView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        binding.imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        binding.imageView.setAdjustViewBounds(false);
        binding.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    private void setupViewModel() {
        Retrofit retrofit = new ApiClient("http://" +ipfarmacia+ "/").retrofitInstance();
        //Retrofit retrofit = new ApiClient("http://192.168.237.187/").retrofitInstance();
        GeneralApiService service = (retrofit.create(GeneralApiService.class));
        viewModel = ViewModelProviders
                .of(this, new ViewModelFactory(service,null))
                .get(HomeViewModel.class);
        viewModel.getDigitalizacion();
    }

    private void setupObserver() {
        viewModel.getDigitalizacionResponse().observe(getViewLifecycleOwner(),apiResponseResource -> {
            switch (apiResponseResource.getStatus()) {
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    errorSnackbar = Snackbar.make(binding.getRoot(), apiResponseResource.getMessage(), Snackbar.LENGTH_INDEFINITE);
                    errorSnackbar.setAction("Cerrar", v -> {
                        errorSnackbar.dismiss();
                    });
                    errorSnackbar.show();
                    break;
                case SUCCESS:
                    if(errorSnackbar!=null){
                        errorSnackbar.dismiss();
                    }
                    Map<String,Object> mensaje=(Map<String, Object>) apiResponseResource.getData().getMensaje();
                    //TODO remplazar ip digitalizaciòn
                   /*preferences.edit()
                            .putString(SharedPreferencesApp.URL_DIGITALIZACION_KEY,mensaje.get("cfg_url_api").toString())
                            .apply();*/

                    preferences.edit()
                            .putString(SharedPreferencesApp.URL_DIGITALIZACION_KEY,"http://192.168.240.6/ITEDigitalizacionAPI3")
                            .apply();

                    //Retrofit retrofit = new ApiClient(mensaje.get("cfg_url_api").toString()+ "/").retrofitInstance();
                    Retrofit retrofit = new ApiClient("http://192.168.240.6/ITEDigitalizacionAPI3"+ "/").retrofitInstance();
                    ApiDigitalizacionService service = (retrofit.create(ApiDigitalizacionService.class));
                    viewModel.setDigitalizacionService(service);
                    viewModel.getToken();
                    binding.progressBar.setVisibility(View.GONE);
                    break;
                case LOADING:
                    if(errorSnackbar!=null){
                        errorSnackbar.dismiss();
                    }
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        });
        viewModel.getTokenResponse().observe(getViewLifecycleOwner(),apiResponseTokenResource -> {
            switch (apiResponseTokenResource.getStatus()){
                case LOADING:
                    if(errorSnackbar!=null){
                        errorSnackbar.dismiss();
                    }
                    binding.button.setEnabled(false);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    if(errorSnackbar!=null){
                        errorSnackbar.dismiss();
                    }
                    binding.button.setEnabled(true);
                    binding.progressBar.setVisibility(View.GONE);
                    preferences.edit()
                            .putString(SharedPreferencesApp.TOKEN_APP_KEY,apiResponseTokenResource.getData().getAccess_token()).apply();

                    viewModel.getNotificaciones(Integer.parseInt(farmacia.getIdbodega()),"bearer "+apiResponseTokenResource.getData().getAccess_token());
                    break;
                case ERROR:
                    binding.button.setEnabled(false);
                    binding.progressBar.setVisibility(View.GONE);
                    errorSnackbar = Snackbar.make(binding.getRoot(), apiResponseTokenResource.getMessage(), Snackbar.LENGTH_INDEFINITE);
                    errorSnackbar.setAction("Cerrar", v -> {
                        errorSnackbar.dismiss();
                        viewModel.getToken();
                    });
                    errorSnackbar.show();
                    break;
            }
        });
    }

    private void abrirDialogoNotificaciones(){
        Fragment dialog=getChildFragmentManager().findFragmentByTag("Notif");
        if(dialog==null){
            dialogoNoti.show(getChildFragmentManager(),"Notif");
        }
    }
}