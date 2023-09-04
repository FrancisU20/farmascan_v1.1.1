package com.farma.appfarmaenlace.ui.login.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.farma.appfarmaenlace.R;
import com.farma.appfarmaenlace.data.api.ApiClient;
import com.farma.appfarmaenlace.data.api.Cookies;
import com.farma.appfarmaenlace.data.api.GeneralApiService;
import com.farma.appfarmaenlace.databinding.FragmentLoginBinding;
import com.farma.appfarmaenlace.ui.base.ViewModelFactory;
import com.farma.appfarmaenlace.ui.home.model.Farmacia;
import com.farma.appfarmaenlace.ui.login.viewmodel.LoginViewModel;
import com.farma.appfarmaenlace.utils.Network;
import com.farma.appfarmaenlace.utils.SharedPreferencesApp;
import com.farma.appfarmaenlace.utils.Status;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private DrawerLayout drawerLayout;

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;
    public static String APP_VERSION = "10";
    private final boolean validView=false;
    private Snackbar errorSnackbar;
    private SharedPreferences preferences;
    private Moshi moshi;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

        borrarImagenes();
        crearCarpetaImagenes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences=SharedPreferencesApp.getPreferences(getContext());
        moshi=new Moshi.Builder().build();

        setupUI();
        setupViewModel();
        setupObserver();
        validarIp(binding.txtip.getEditText().getText().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        //desbloquer drawer
        if(drawerLayout!=null){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        //mostrar actionbar
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        //ocultar teclado
        InputMethodManager imm =(InputMethodManager)  getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        if(errorSnackbar!=null){
            errorSnackbar.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //bloquear drawer
       if(drawerLayout!=null){
           drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
       }
        //ocultar actionbar
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }


    private void setupUI() {
        drawerLayout=getActivity().findViewById(R.id.drawerLayout);

        binding.progressBar.setVisibility(View.GONE);
        binding.btnLogin.setOnClickListener(v -> {
            login();
        });
        binding.imageView.setImageResource(R.drawable.fondo);
        binding.imageView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        binding.imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        binding.imageView.setAdjustViewBounds(false);
        binding.imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        binding.txtip.getEditText().setText(preferences.getString(SharedPreferencesApp.IP_INGRESADA_USUARIO,""));

        binding.txtip.getEditText().addTextChangedListener(new ValidationTextWatcher(binding.txtip));
        binding.txtusuario.getEditText().addTextChangedListener(new ValidationTextWatcher(binding.txtusuario));
        binding.txtcontrasenia.getEditText().addTextChangedListener(new ValidationTextWatcher(binding.txtcontrasenia));

        binding.txtcontrasenia.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
               login();
            }
            return false;
        });

    }

    private void setupViewModel() {
        GeneralApiService service=null;
        viewModel = ViewModelProviders
                .of(this, new ViewModelFactory(service))
                .get(LoginViewModel.class);
    }

    private void setupObserver() {
        viewModel.getLoginViewState().observe(getViewLifecycleOwner(),loginViewState -> {
            binding.txtip.setError(null);
            binding.txtusuario.setError(null);
            binding.txtcontrasenia.setError(null);
            if(loginViewState.getIpState().getStatus()== Status.ERROR){
                binding.txtip.setError(loginViewState.getIpState().getMessage());
            }
            if(loginViewState.getUsuarioState().getStatus()== Status.ERROR){
                binding.txtusuario.setError(loginViewState.getUsuarioState().getMessage());
            }
            if(loginViewState.getClaveState().getStatus()== Status.ERROR){
                binding.txtcontrasenia.setError(loginViewState.getClaveState().getMessage());
            }
            binding.btnLogin.setEnabled(loginViewState.isValidForm());
        });
        viewModel.getLoginResponse().observe(getViewLifecycleOwner(), apiResponseResource -> {
            switch (apiResponseResource.getStatus()) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    if(errorSnackbar!=null){
                        errorSnackbar.dismiss();
                    }
                    break;
                case SUCCESS:
                    if(errorSnackbar!=null){
                        errorSnackbar.dismiss();
                    }
                    preferences.edit().
                            putString(SharedPreferencesApp.IP_INGRESADA_USUARIO,binding.txtip.getEditText().getText().toString())
                            .apply();

                    binding.progressBar.setVisibility(View.GONE);
                    Map<String,Object> mensaje=(Map<String, Object>) apiResponseResource.getData().getMensaje();

                    JsonAdapter<Map> adapter=moshi.adapter(Map.class);
                    String mensajest=adapter.toJson(mensaje);
                    JsonAdapter<Farmacia> adapter1=moshi.adapter(Farmacia.class);

                    try {
                        Farmacia farmacia=adapter1.fromJson(mensajest);

                        Log.i("farmacia",farmacia.toString());

                        preferences.edit()
                                .putString(SharedPreferencesApp.PROPIEDADES_CENTROC_KEY,farmacia.getCentro_costo())
                                .putString(SharedPreferencesApp.PROPIEDADES_USUARIO_KEY,farmacia.getNombreCorto())
                                .putString(SharedPreferencesApp.PROPIEDADES_IP_KEY,binding.txtip.getEditText().getText().toString())
                                .putString(SharedPreferencesApp.PROPIETARIO_CEDULA_KEY,farmacia.getCedula())
                                .apply();

                        viewModel.saveLoginState(getCookie(farmacia.getNombreCorto()));

                        LoginFragmentDirections.Irhome action=LoginFragmentDirections.irhome(farmacia,binding.txtip.getEditText().getText().toString());
                        Navigation.findNavController(getView()).navigate(action);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    errorSnackbar = Snackbar.make(binding.getRoot(), apiResponseResource.getMessage(), Snackbar.LENGTH_INDEFINITE);
                    errorSnackbar.setAction("Cerrar", v -> {
                        errorSnackbar.dismiss();
                    });
                    errorSnackbar.show();
                    break;
            }
        });
    }

    public Cookies getCookie(String nombreusuario) throws IOException {
       /* JsonAdapter<Farmacia> adapter=moshi.adapter(Farmacia.class);
        String jsonf=preferences.getString(SharedPreferencesApp.FARMACIA_KEY,"");
        Farmacia farmacia=adapter.fromJson(jsonf);*/

        Cookies cookies=new Cookies();
        cookies.setCookie(Network.getIp(true));
        cookies.setEstado("A");
        cookies.setIpDispositivo(Network.getIp(true));
        cookies.setNombreUsu(nombreusuario);

        return cookies;
    }

    private void validarIp(CharSequence s) {
       viewModel.validateIp(s.toString());
    }
    private void validarUsuario(CharSequence s) {
      viewModel.validateUsuario(s.toString());
    }
    private void validarClave(CharSequence s) {
        viewModel.validateClave(s.toString());
    }

   /* private static final Pattern IP_ADDRESS
            = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))");
    Matcher matcher = IP_ADDRESS.matcher("127.0.0.1");*/
    /*if (matcher.matches()) {
        // ip is correct
    }*/

    private void borrarImagenes(){
        File file=new File(Environment.getExternalStorageDirectory().getPath()+"/Android/data/"+getActivity().getApplicationContext().getPackageName()+"/"+getResources().getString(R.string.app_name)+"/");
        File[] files=file.listFiles();
        if(files!=null){
            for(File f: files){
                f.delete();
            }
        }
    }

    private void crearCarpetaImagenes(){
        File f = new File(getContext().getCacheDir(),File.separator+"ImagenesFarmaScan/");
        if(!f.exists()){
            f.mkdirs();
        }
    }

    private class ValidationTextWatcher implements TextWatcher {
        private final View view;

        public ValidationTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (view.getId()) {
                case R.id.txtip:
                    validarIp(s);
                    break;
                case R.id.txtusuario:
                    validarUsuario(s);
                    break;
                case R.id.txtcontrasenia:
                    validarClave(s);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void login(){
        Retrofit retrofit = new ApiClient("http://" + binding.txtip.getEditText().getText().toString() + "/").retrofitInstance();
        GeneralApiService service = (retrofit.create(GeneralApiService.class));
        String ip = Network.getIp(true);
        viewModel.setService(service);
        viewModel.login(binding.txtusuario.getEditText().getText().toString() + "_" + APP_VERSION,
                binding.txtcontrasenia.getEditText().getText().toString(),
                ip,
                ip);
    }
}

