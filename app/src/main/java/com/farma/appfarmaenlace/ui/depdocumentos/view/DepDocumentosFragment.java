package com.farma.appfarmaenlace.ui.depdocumentos.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.farma.appfarmaenlace.data.api.ApiClient;
import com.farma.appfarmaenlace.data.api.ApiDigitalizacionService;
import com.farma.appfarmaenlace.data.api.ApiResponseCatalogo;
import com.farma.appfarmaenlace.data.api.ApiResponseEnvioInformacion;
import com.farma.appfarmaenlace.data.api.EstrucDepartamentos;
import com.farma.appfarmaenlace.databinding.FragmentDepDocumentosBinding;
import com.farma.appfarmaenlace.ui.base.ViewModelFactory;
import com.farma.appfarmaenlace.ui.depdocumentos.viewmodel.DepDocumentoViewModel;
import com.farma.appfarmaenlace.ui.dialogos.AceptarDialogo;
import com.farma.appfarmaenlace.ui.dialogos.EnvioDialogo;
import com.farma.appfarmaenlace.utils.SharedPreferencesApp;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DepDocumentosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DepDocumentosFragment extends Fragment {
    private DepDocumentoViewModel viewModel;
    private FragmentDepDocumentosBinding binding;
    private SharedPreferences preferences;
    private String token;
    private Snackbar errorSnackbar;
    private Moshi moshi;
    private AceptarDialogo aceptarDialogo;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DepDocumentosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DepDocumentosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DepDocumentosFragment newInstance(String param1, String param2) {
        DepDocumentosFragment fragment = new DepDocumentosFragment();
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
        binding=FragmentDepDocumentosBinding.inflate(inflater,container,false);
        View view=binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences= SharedPreferencesApp.getPreferences(getContext());
        token="bearer "+preferences.getString(SharedPreferencesApp.TOKEN_APP_KEY,"");
        moshi=new Moshi.Builder().build();
        setupViewModel();
        setupObserver();
        setupUI();
        viewModel.getDepartamentos(token);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(errorSnackbar!=null){
            errorSnackbar.dismiss();
        }
    }

    private void setupUI(){
        binding.btnSiguiente.setEnabled(false);
        binding.departamentos.setEnabled(false);
        binding.documentos.setEnabled(false);

        binding.btnSiguiente.setOnClickListener(v -> {
            Navigation.findNavController(getView()).navigate(DepDocumentosFragmentDirections.irdibujarpropiedades());
        });
    }

    private void setupViewModel(){
        String url=preferences.getString(SharedPreferencesApp.URL_DIGITALIZACION_KEY,"");
        Retrofit retrofit = new ApiClient(url+ "/").retrofitInstance();
        ApiDigitalizacionService service = retrofit.create(ApiDigitalizacionService.class);
        viewModel = ViewModelProviders
                .of(this, new ViewModelFactory(service))
                .get(DepDocumentoViewModel.class);
    }
    private void setupObserver(){
        viewModel.getDepartamentosResponse().observe(getViewLifecycleOwner(),listResource -> {
            switch (listResource.getStatus()){
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),listResource.getMessage(),Toast.LENGTH_LONG).show();
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    ArrayAdapter<EstrucDepartamentos> listadapter=new ArrayAdapter<EstrucDepartamentos>(getContext(),android.R.layout.simple_spinner_dropdown_item,listResource.getData());
                    listadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.departamentos.setAdapter(listadapter);
                    binding.departamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            EstrucDepartamentos es=(EstrucDepartamentos)parent.getItemAtPosition(position);
                            viewModel.getCatalogo(es.getCom_codigo(),es.getAmb_codigo(),token);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    binding.departamentos.setEnabled(true);
                    break;
                case LOADING:
                    binding.departamentos.setEnabled(false);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        });
        viewModel.getCatalogoResponse().observe(getViewLifecycleOwner(),listResource -> {
           /* List<ApiResponseCatalogo> docFiltrados=listResource
                    .getData()
                    .stream()
                    .filter(doc->doc.getCat_ocr().equals("N"))
                    .collect(Collectors.toList());*/
            switch (listResource.getStatus()){
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),listResource.getMessage(),Toast.LENGTH_LONG).show();
                    binding.btnSiguiente.setEnabled(false);
                    break;
                case SUCCESS:
                    List<ApiResponseCatalogo> docFiltrados=listResource
                            .getData()
                            .stream()
                            .filter(doc->doc.getCat_ocr().equals("N"))
                            .collect(Collectors.toList());
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnSiguiente.setEnabled(true);
                    binding.documentos.setEnabled(true);
                    if(listResource.getData().size()==0){
                        binding.btnSiguiente.setEnabled(false);
                        binding.documentos.setEnabled(false);
                        Toast.makeText(getContext(),"No existe documentos para este departamento",Toast.LENGTH_LONG).show();
                    }else if(docFiltrados.isEmpty()){
                        binding.btnSiguiente.setEnabled(false);
                        binding.documentos.setEnabled(false);
                        aceptarDialogo = new AceptarDialogo("Estimado usuario, recuerde que convenios y cupones se debe digitalizar por la opción \"ESCANEAR CÓD. BARRA FACTURA\".");
                        aceptarDialogo.show(getParentFragmentManager(), "Estimado usuario, recuerde que convenios y cupones se debe digitalizar por la opción \"ESCANEAR CÓD. BARRA FACTURA\".");
                        //Toast.makeText(getContext(),"Estimado usuario, recuerde que convenios y cupones se debe digitalizar por la opción \"ESCANEAR CÓD. BARRA FACTURA\".",Toast.LENGTH_LONG).show();
                    }
                    ArrayAdapter<ApiResponseCatalogo> listadapter=new ArrayAdapter<ApiResponseCatalogo>(getContext(),android.R.layout.simple_spinner_dropdown_item,docFiltrados);
                    listadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.documentos.setAdapter(listadapter);
                    binding.documentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ApiResponseCatalogo es=(ApiResponseCatalogo) parent.getItemAtPosition(position);
                            viewModel.getEstructuraDocumento(es.getCat_codigo(),token);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    binding.documentos.setEnabled(true);
                    break;
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnSiguiente.setEnabled(false);
                    binding.documentos.setEnabled(false);
                    break;
            }
        });
        viewModel.getEstructuraDocResponse().observe(getViewLifecycleOwner(),apiResponseEnvioInformacionResource -> {
            if(errorSnackbar!=null){
                errorSnackbar.dismiss();
            }
            switch (apiResponseEnvioInformacionResource.getStatus()){
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    ApiResponseEnvioInformacion est=apiResponseEnvioInformacionResource.getData();
                    binding.progressBar.setVisibility(View.GONE);

                    JsonAdapter<ApiResponseEnvioInformacion> adapter=moshi.adapter(ApiResponseEnvioInformacion.class);
                    String jsonstring=adapter.toJson(est);
                    preferences.edit()
                            .putString(SharedPreferencesApp.ESTRUCTURA_DOC_KEY,jsonstring)
                            .apply();
                    Log.i("Json Estructura",jsonstring);

                    break;
                case ERROR:
                    showErrorSnackbar(apiResponseEnvioInformacionResource.getMessage());
                    break;
            }
        });
    }

    private void showErrorSnackbar(String msg){
        errorSnackbar = Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_INDEFINITE);
        errorSnackbar.setAction("Cerrar", v -> {
            errorSnackbar.dismiss();
        });
        errorSnackbar.show();
    }
}