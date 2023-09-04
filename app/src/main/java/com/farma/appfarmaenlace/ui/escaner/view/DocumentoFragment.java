package com.farma.appfarmaenlace.ui.escaner.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.farma.appfarmaenlace.data.api.ApiClient;
import com.farma.appfarmaenlace.data.api.ApiDigitalizacionService;
import com.farma.appfarmaenlace.data.api.ApiResponseCatalogo;
import com.farma.appfarmaenlace.data.api.ApiResponseEnvioInformacion;
import com.farma.appfarmaenlace.data.api.GeneralApiService;
import com.farma.appfarmaenlace.data.api.Propiedades;
import com.farma.appfarmaenlace.databinding.FragmentDocumentoBinding;
import com.farma.appfarmaenlace.ui.base.ViewModelFactory;
import com.farma.appfarmaenlace.ui.escaner.adapter.ListaPropiedadesAdapter;
import com.farma.appfarmaenlace.ui.escaner.model.ItemPropiedad;
import com.farma.appfarmaenlace.ui.escaner.viewmodel.DocumentoViewModel;
import com.farma.appfarmaenlace.utils.SharedPreferencesApp;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocumentoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentoFragment extends Fragment {
    private FragmentDocumentoBinding binding;
    private DocumentoViewModel viewModel;

    private String token;
    private String serie;
    private int codigo;
    private String catOcr;
    private List<Propiedades> propiedades;
    private Map<String, Object> verificarMap;
    private Snackbar errorSnackbar;
    private SharedPreferences preferences;
    private Moshi moshi;
    private ActivityResultLauncher<Intent> scannerResultLauncher;
    //private IntentIntegrator scanIntegrator;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DocumentoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EscanerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DocumentoFragment newInstance(String param1, String param2) {
        DocumentoFragment fragment = new DocumentoFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentDocumentoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = SharedPreferencesApp.getPreferences(getContext());
        moshi = new Moshi.Builder().build();
        setupUI();
        setupEscaner();
        setupViewModel();
        setupObserver();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (errorSnackbar != null) {
            errorSnackbar.dismiss();
        }
    }

    public void setupEscaner() {
        scannerResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null) {
                    String code = result.getData().getStringExtra("coderesult");
                    if (code != null) {
                        String scanContent = code;
                        viewModel.getTipoDocumento(scanContent);
                        serie = scanContent;
                    }
                }
            }
        });
        launchScannerActivity();
    }

    public void setupUI() {
        List<ItemPropiedad> list = new ArrayList<>();
        ListaPropiedadesAdapter adapter = new ListaPropiedadesAdapter(list);
        binding.recyclerPropiedades.setHasFixedSize(true);
        binding.recyclerPropiedades.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerPropiedades.setAdapter(adapter);
        binding.progressBar.setVisibility(View.GONE);
        binding.btnImagenes.setOnClickListener(v -> {
            Navigation.findNavController(getView()).navigate((NavDirections) DocumentoFragmentDirections.irImagenesFragment());
        });
        binding.btnEscanear.setOnClickListener(v -> {
            launchScannerActivity();
        });

        binding.btnBuscar.setOnClickListener(v -> {
            serie="002F"+binding.editCodigoBarras.getText().toString();
            viewModel.getTipoDocumento(serie);
        });
    }

    public void setupViewModel() {
        String ipfarmacia = preferences.getString(SharedPreferencesApp.IP_FARMACIA_KEY, "");
        String urldigit = preferences.getString(SharedPreferencesApp.URL_DIGITALIZACION_KEY, "");

        Retrofit retrofit = new ApiClient("http://" + ipfarmacia + "/").retrofitInstance();
        //Retrofit retrofit = new ApiClient("http://192.168.237.187/").retrofitInstance();
        GeneralApiService service = (retrofit.create(GeneralApiService.class));

        Retrofit retrofit2 = new ApiClient(urldigit + "/").retrofitInstance();
        ApiDigitalizacionService service2 = (retrofit2.create(ApiDigitalizacionService.class));

        viewModel = ViewModelProviders
                .of(this, new ViewModelFactory(service, service2))
                .get(DocumentoViewModel.class);

        token = "bearer " + preferences.getString(SharedPreferencesApp.TOKEN_APP_KEY, "");

    }

    public void setupObserver() {
        viewModel.getTipoDocumentoResponse().observe(getViewLifecycleOwner(), apiResponseResource -> {
            if (errorSnackbar != null) {
                errorSnackbar.dismiss();
            }
            switch (apiResponseResource.getStatus()) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);

                    ocultarFuncionBuscar();

                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    String msg = (String) apiResponseResource.getData().getMensaje();
                    String[] splitmsg = msg.split("\\|");
                    codigo = Integer.parseInt(splitmsg[1]);

                    preferences.edit()
                            .putInt(SharedPreferencesApp.CODIGO_ESCANER_KEY, codigo)
                            .apply();
                    String nombret = splitmsg[2];

                    viewModel.getEstructuraDocumento(codigo, token);
                    binding.tipoDocumento.setText(nombret);

                    binding.btnImagenes.setVisibility(View.VISIBLE);
                    ocultarFuncionBuscar();

                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    showErrorSnackbar(apiResponseResource.getMessage());

                    binding.btnImagenes.setVisibility(View.GONE);
                    mostrarFuncionBuscar();

                    break;
            }
        });
        viewModel.getEstructuraDocResponse().observe(getViewLifecycleOwner(), apiResponseEnvioInformacionResource -> {
            if (errorSnackbar != null) {
                errorSnackbar.dismiss();
            }
            switch (apiResponseEnvioInformacionResource.getStatus()) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    ApiResponseEnvioInformacion est = apiResponseEnvioInformacionResource.getData();
                    viewModel.getCatalogo(est.getEmpresaCodigo(), est.getDepartamentoCodigo(), token);
                    propiedades = est.getPropiedades();
                    binding.progressBar.setVisibility(View.GONE);

                    JsonAdapter<ApiResponseEnvioInformacion> adapter = moshi.adapter(ApiResponseEnvioInformacion.class);
                    String jsonstring = adapter.toJson(est);
                    preferences.edit()
                            .putString(SharedPreferencesApp.ESTRUCTURA_DOC_KEY, jsonstring)
                            .apply();

                    break;
                case ERROR:
                    showErrorSnackbar(apiResponseEnvioInformacionResource.getMessage());
                    break;
            }
        });
        viewModel.getCatalogoResponse().observe(getViewLifecycleOwner(), listResource -> {
            if (errorSnackbar != null) {
                errorSnackbar.dismiss();
            }
            switch (listResource.getStatus()) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    List<ApiResponseCatalogo> catalogo = listResource.getData();
                    for (ApiResponseCatalogo cat : catalogo) {
                        if (cat.getCat_codigo() == codigo) {
                            catOcr = cat.getCat_ocr();
                            break;
                        }
                    }
                    viewModel.verificarFactura(serie + "|" + catOcr);
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    showErrorSnackbar(listResource.getMessage());

                    binding.btnImagenes.setVisibility(View.GONE);
                    mostrarFuncionBuscar();
                    break;
            }
        });
        viewModel.getVerificarFacResponse().observe(getViewLifecycleOwner(), apiResponseResource -> {
            if (errorSnackbar != null) {
                errorSnackbar.dismiss();
            }
            switch (apiResponseResource.getStatus()) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                   try {
                       Map<String, Object> mensaje = (Map<String, Object>) apiResponseResource.getData().getMensaje();
                       verificarMap = mensaje;
                       ListaPropiedadesAdapter adapter = new ListaPropiedadesAdapter(listaItmsPropiedad());
                       binding.recyclerPropiedades.setAdapter(adapter);
                   }catch (Exception e){
                       showErrorSnackbar(e.getMessage());
                       Log.i("DocumentosFragment.java",e.getMessage());
                   }

                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    showErrorSnackbar(apiResponseResource.getMessage());
                    break;
            }
        });
    }

    private List<ItemPropiedad> listaItmsPropiedad() throws Exception {
        List<ItemPropiedad> list = new ArrayList<>();
        Log.i("Veriicar Map",verificarMap.toString());
        //Log.i(" Map",propiedades.toString());
        for (Propiedades p : propiedades) {
            Log.i("prd_codigo", p.getPrd_codigo() + "");
            Object valor = verificarMap.get(String.valueOf(p.getPrd_codigo()));
            //Log.i("prd_codigo", valor.toString());
            String etiqueta = p.getPrd_nombre();
            if(valor==null){
                throw new Exception("El valor de la propiedad "+p.getPrd_codigo()+" no se encontró.");
            }
            p.setDatos(valor.toString());
            list.add(new ItemPropiedad(etiqueta, valor.toString()));
        }

        Type listMyData = Types.newParameterizedType(List.class, Propiedades.class);
        JsonAdapter<List<Propiedades>> adapter1 = moshi.adapter(listMyData);
        String listjson = adapter1.toJson(propiedades);
        preferences.edit().putString(SharedPreferencesApp.PROPIEDADES_DOC_KEY, listjson).apply();
        return list;
    }

    private void showErrorSnackbar(String msg) {
        errorSnackbar = Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_INDEFINITE);
        errorSnackbar.setAction("Cerrar", v -> {
            errorSnackbar.dismiss();
        });
        errorSnackbar.show();
    }

    private void launchScannerActivity() {
        Intent intent = new Intent(getContext(), CaptureActivityPortrait.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        scannerResultLauncher.launch(intent);
    }

    private void mostrarFuncionBuscar() {
        binding.btnEscanear.setVisibility(View.VISIBLE);
        binding.btnBuscar.setVisibility(View.VISIBLE);
        binding.editCodigoBarras.setVisibility(View.VISIBLE);
    }

    private void ocultarFuncionBuscar() {
        binding.btnEscanear.setVisibility(View.GONE);
        binding.btnBuscar.setVisibility(View.GONE);
        binding.editCodigoBarras.setVisibility(View.GONE);
        binding.info.setVisibility(View.GONE);
    }
}