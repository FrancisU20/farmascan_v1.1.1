package com.farma.appfarmaenlace.ui.tratamientoImagen.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.farma.appfarmaenlace.R;
import com.farma.appfarmaenlace.data.api.ApiClient;
import com.farma.appfarmaenlace.data.api.ApiDigitalizacionService;
import com.farma.appfarmaenlace.data.api.ApiResponseEnvioInformacion;
import com.farma.appfarmaenlace.data.api.Imagen;
import com.farma.appfarmaenlace.data.api.Propiedades;
import com.farma.appfarmaenlace.databinding.FragmentImagenesBinding;
import com.farma.appfarmaenlace.ui.dialogos.AlertDialogo;
import com.farma.appfarmaenlace.ui.dialogos.AlertasDialog;
import com.farma.appfarmaenlace.ui.dialogos.EnvioDialogo;
import com.farma.appfarmaenlace.ui.home.model.Farmacia;
import com.farma.appfarmaenlace.ui.tratamientoImagen.adapter.ListaImagenesAdapter;
import com.farma.appfarmaenlace.utils.Constantes;
import com.farma.appfarmaenlace.utils.SharedPreferencesApp;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImagenesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImagenesFragment extends Fragment {
    private FragmentImagenesBinding binding;
    private ActivityResultLauncher<Intent> mGetContent;
    private ActivityResultLauncher<String> mGetContentGaleria;
    private ActivityResultLauncher<Uri> mGetContentCamara;
    private Disposable disposable;
    private FrameLayout progressBar;
    private Snackbar errorSnackbar;
    private EnvioDialogo envioDialogo;
    private SharedPreferences preferences;
    private Moshi moshi;
    private ApiResponseEnvioInformacion estructuraDocumento;
    //private ListaImagenesAdapter rvadapter;
    private boolean isManual;
    private ExecutorService executorService;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ImagenesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImagenesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImagenesFragment newInstance(String param1, String param2) {
        ImagenesFragment fragment = new ImagenesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    //Método drag and drog tablero de imágenes.
    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.START|ItemTouchHelper.END,0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            ListaImagenesAdapter adapter=(ListaImagenesAdapter) recyclerView.getAdapter();
            List<String> imagenes=adapter.getImagenes();
            Collections.swap(imagenes, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            recyclerView.getAdapter().notifyDataSetChanged();
            adapter.setImagenes(imagenes);
            guardarPreferencesListaImagenesTablero();
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                try {
                    irHome();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentImagenesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
        //return inflater.inflate(R.layout.fragment_imagenes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = SharedPreferencesApp.getPreferences(getContext());
        moshi = new Moshi.Builder().build();
        isManual = ImagenesFragmentArgs.fromBundle(getArguments()).getManual();

        try {
            setupUI();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                try {
                    getImagenes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mGetContentGaleria = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), result -> {
            progressBar.setVisibility(View.VISIBLE);
            binding.fab2.setEnabled(false);

            List<Uri> selectedUris = new ArrayList<>(result);

            executorService.execute(() -> {
                try {
                    for (Uri uri : selectedUris) {
                        guardarImagenDesdeGaleria(uri);
                    }

                    getActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        try {
                            getImagenes();
                            progressBar.setVisibility(View.GONE);
                            binding.fab2.setEnabled(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });

        mGetContentCamara= registerForActivityResult(new ActivityResultContracts.TakePicture(),result -> {
            if(result){
                try {
                    getImagenes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            try {
                irHome();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (errorSnackbar != null) {
            errorSnackbar.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        shutdownExcecutroService();
    }

    private void setupUI() throws IOException {
        executorService= Executors.newSingleThreadExecutor();
        ViewGroup layout = binding.getRoot();
        ViewGroup layoutpg = (ViewGroup) getLayoutInflater().inflate(R.layout.progressbar_layout, layout);
        progressBar = layoutpg.findViewById(R.id.progressBar);

        List<String> imagenest=recuperarImagenesTableroPreferences();
        if(imagenest.isEmpty()){
            getImagenes();
        }else{
           setUpRecyclerView(imagenest);
        }
        getEstructuraDocumento();
        if (isManual) {
            binding.fab2.setVisibility(View.VISIBLE);
        } else {
            mostrarDialogoInfo();
        }

        envioDialogo = new EnvioDialogo("¿Desea enviar la información?");
        envioDialogo.setSiDI((dialog, which) -> {
            enviar();
        });
        envioDialogo.setNoDI((dialog, which) -> {
        });

        binding.fab.setOnClickListener(v -> {
            if(isManual){
                File dir = new File(getContext().getCacheDir() + File.separator + "ImagenesFarmaScan/");
                String nombreImagen =  (System.currentTimeMillis()+"");
                nombreImagen = nombreImagen.substring((nombreImagen.length()-8));
                File imagen = new File(dir, nombreImagen + ".jpg");
                Uri uri= FileProvider.getUriForFile(getContext(),getContext().getPackageName() + ".utils.FarmaScanFileProvider",imagen);
                mGetContentCamara.launch(uri);
            }else {
                Intent intent = new Intent(getActivity(), DocumentoEscanerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                mGetContent.launch(intent);
            }
        });

        binding.btnLimpiar.setOnClickListener(v -> {
            limpiarLista();
        });

        binding.btnEnviar.setOnClickListener(v -> {
            envioDialogo.show(getParentFragmentManager(), "DIALOGO_ENVIO");
        });

        binding.fab2.setOnClickListener(v -> {
            mGetContentGaleria.launch("image/*");
        });
    }

    private void setUpRecyclerView(List<String> limages){
        ListaImagenesAdapter rvadapter = new ListaImagenesAdapter (limages);
        rvadapter.setOnDeleteListener(new ListaImagenesAdapter.OnDeleteListener() {
            @Override
            public void onDelete(int position) {

                try {
                    String pathimg=((ListaImagenesAdapter)binding.recyclerImagenes.getAdapter()).getItem(position);
                    borrarImagen(pathimg);
                    rvadapter.removeItem(position);
                    guardarPreferencesListaImagenesTablero();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
        binding.recyclerImagenes.setHasFixedSize(true);
        binding.recyclerImagenes.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.recyclerImagenes.setAdapter(rvadapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerImagenes);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void irHome() throws IOException {
        String ipfarmacia = preferences.getString(SharedPreferencesApp.IP_FARMACIA_KEY, "");
        String jsonfarmacia = preferences.getString(SharedPreferencesApp.FARMACIA_KEY, "");

        JsonAdapter<Farmacia> adapter = moshi.adapter(Farmacia.class);
        Farmacia farmacia = adapter.fromJson(jsonfarmacia);

        Navigation.findNavController(getView()).navigate((NavDirections) ImagenesFragmentDirections.actionGlobalHomeFragment(farmacia, ipfarmacia));
    }

    public void getImagenes() throws IOException {
        binding.btnEnviar.setEnabled(true);

        List<File> imgs = obtenerPathImagenes();
        if (imgs.size() == 0) {
            binding.btnEnviar.setEnabled(false);
        }

        List<String> limages = new ArrayList<>();

        final File[] sortedFileName = imgs.toArray(new File[0]);

        if (imgs != null && sortedFileName.length > 1) {
            Arrays.sort(sortedFileName, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    try {
                        int a = Integer.parseInt(o1.getName().split("\\.")[0]);
                        int b = Integer.parseInt(o2.getName().split("\\.")[0]);
                        return a - b;
                    } catch (Exception exx) {
                        return 0;
                    }
                }
            });
        }

        for (File file : sortedFileName) {
            limages.add(file.getAbsolutePath());
        }

        List<String> imagenesTablero=recuperarImagenesTableroPreferences();
        for(String path:limages){
            if(!imagenesTablero.contains(path)){
                imagenesTablero.add(path);
            }
        }
        setUpRecyclerView(imagenesTablero);
        guardarPreferencesListaImagenesTablero();
    }

    private void getEstructuraDocumento() throws IOException {
        String estructura = preferences.getString(SharedPreferencesApp.ESTRUCTURA_DOC_KEY, "");
        String propiedades = preferences.getString(SharedPreferencesApp.PROPIEDADES_DOC_KEY, "");
        String usuario = preferences.getString(SharedPreferencesApp.PROPIEDADES_USUARIO_KEY, "");
        String ip = preferences.getString(SharedPreferencesApp.PROPIEDADES_IP_KEY, "");
        String cedula = preferences.getString(SharedPreferencesApp.PROPIETARIO_CEDULA_KEY, "");


        JsonAdapter<ApiResponseEnvioInformacion> adapter1 = moshi.adapter(ApiResponseEnvioInformacion.class);
        estructuraDocumento = adapter1.fromJson(estructura);

        if (!isManual) {
            Type listMyData = Types.newParameterizedType(List.class, Propiedades.class);
            JsonAdapter<List<Propiedades>> adapter2 = moshi.adapter(listMyData);
            List<Propiedades> propiedadesList = adapter2.fromJson(propiedades);
            estructuraDocumento.setPropiedades(propiedadesList);
        }

        estructuraDocumento.setCentroCosto(preferences.getString(SharedPreferencesApp.PROPIEDADES_CENTROC_KEY, ""));
        estructuraDocumento.setUsuarioRegistra(usuario);
        estructuraDocumento.setIpRegistra(ip);
        estructuraDocumento.getPropietario().setCiPasRuc(cedula);
        estructuraDocumento.getPropietario().setTipoPropietario(Constantes.TIPO_PROPIETARIO);
        estructuraDocumento.setOrigen(Constantes.ORIGEN);
        estructuraDocumento.setDevolverPropietario(Constantes.DEVOLVER_PROPIETARIO);


    }

    public List<Imagen> generarListaImagenes() throws IOException {
        ListaImagenesAdapter rvadapter=(ListaImagenesAdapter)binding.recyclerImagenes.getAdapter();
        List<Imagen> imagenList = new ArrayList<>(rvadapter.getItemCount());
        for (String file : rvadapter.getImagenes()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArrayImg = stream.toByteArray();
            String encoded = Base64.encodeToString(byteArrayImg, Base64.DEFAULT);
            Imagen img = new Imagen(file, "JPG", encoded);
            imagenList.add(img);
        }

        return imagenList;
    }

    public List<File> obtenerPathImagenes() {
        File dir = new File(getContext().getCacheDir() + File.separator + "ImagenesFarmaScan/");
        File[] files = dir.listFiles();

        return Arrays.stream(files).filter(el->el!=null).collect(Collectors.toList());
    }

    public void limpiarLista() {
        borrarImagenes();
        //preferences.edit().remove(SharedPreferencesApp.IMAGENES_ESC_KEY).apply();
        ((ListaImagenesAdapter) binding.recyclerImagenes.getAdapter()).clear();
        binding.btnEnviar.setEnabled(false);
    }

    private void showErrorSnackbar(String msg) {
        errorSnackbar = Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_INDEFINITE);
        errorSnackbar.setAction("Cerrar", v -> {
            errorSnackbar.dismiss();
        });
        errorSnackbar.show();
    }


    private void guardarImagenDesdeGaleria(Uri uri) throws IOException {
        File dir = new File(getContext().getCacheDir() + File.separator + "ImagenesFarmaScan/");
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);

        String nombreImagen = (System.currentTimeMillis() + "");
        nombreImagen = nombreImagen.substring((nombreImagen.length() - 8));
        File imagen = new File(dir, nombreImagen + ".jpg");

        if(bitmap.getWidth() > bitmap.getHeight()){
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        // Calcular nuevas dimensiones manteniendo la proporción
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth = 1080;  // Ancho deseado
        int newHeight = (int) ((float) originalHeight / originalWidth * newWidth);

        // Redimensionar la imagen
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        try {
            FileOutputStream out = new FileOutputStream(imagen);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void enviar() {

        if (errorSnackbar != null) {
            errorSnackbar.dismiss();
        }
        progressBar.setVisibility(View.VISIBLE);

        try {

            estructuraDocumento.setImagenes(generarListaImagenes());
            String urldigit = preferences.getString(SharedPreferencesApp.URL_DIGITALIZACION_KEY, "");
            Retrofit retrofit2 = new ApiClient(urldigit + "/").retrofitInstance();
            ApiDigitalizacionService service2 = (retrofit2.create(ApiDigitalizacionService.class));

            int codigo = preferences.getInt(SharedPreferencesApp.CODIGO_ESCANER_KEY, 0);
            if (isManual) {
                codigo = estructuraDocumento.getCodigo();
            }
            String token = "bearer " + preferences
                    .getString(SharedPreferencesApp.TOKEN_APP_KEY, "");
            disposable = service2.SubirInformacion(codigo, estructuraDocumento, token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(apiResponseEnvioInformacionResponse -> {
                        Map<String, Object> map = (Map<String, Object>) apiResponseEnvioInformacionResponse.body();
                        try {
                            if (apiResponseEnvioInformacionResponse.isSuccessful()) {
                                if ((Double) map.get("status_code") == 200) {
                                    Toast.makeText(getContext(), "Información enviada correctamente al servidor de ITScan, código  " + map.get("mensaje"), Toast.LENGTH_LONG).show();
                                    limpiarLista();
                                    irHome();
                                } else {
                                    AlertDialogo alertDialogo1 = new AlertDialogo((String) map.get("mensaje"));
                                    alertDialogo1.show(getParentFragmentManager(), "DIALOGO_ERR");

                                }
                            } else {
                                showErrorSnackbar(apiResponseEnvioInformacionResponse.message());
                            }
                        } catch (IOException a) {
                            Toast.makeText(getContext(), "MENSAJE 1" + a.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }, throwable -> {
                        showErrorSnackbar(throwable.getMessage());
                        progressBar.setVisibility(View.GONE);
                    });
        } catch (IOException e) {
            Toast.makeText(getContext(), "MENSAJE 2" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void mostrarDialogoInfo() {
        String msg = "";
        if (estructuraDocumento.getFarmascanML() != null) {
            for (int i = 0; i < estructuraDocumento.getFarmascanML().size(); i++) {
                int nro = i + 1;
                msg += nro + ". " + estructuraDocumento.getFarmascanML().get(i).getFml_mensaje() + "\n";
            }
        }
        AlertasDialog dialog=AlertasDialog.newInstance("Los documentos a digitalizar deben estar en forma vertical. Recuerde enviar lo siguiente:\n",""+
                msg);
        dialog.show(getParentFragmentManager(),"DIALOGO_INFO");
    }

    private void borrarImagenes() {
        File dir = new File(getContext().getCacheDir() + File.separator + "ImagenesFarmaScan/");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
        preferences.edit().remove(SharedPreferencesApp.IMAGENES_TABLERO).apply();
    }

    private void borrarImagen(String filename) {
        //File img = new File(getContext().getCacheDir() + File.separator + "ImagenesFarmaScan" + File.separator + filename);
        File img=new File(filename);
        if (img.exists()) {
            img.delete();
        }
    }

    private void guardarPreferencesListaImagenesTablero(){
        List<String> imagenest=((ListaImagenesAdapter)binding.recyclerImagenes.getAdapter()).getImagenes();
        String imagenesstrign="";
        for(int i=0;i<imagenest.size();i++){
            if(i==imagenest.size()-1){
                imagenesstrign+=imagenest.get(i);
            }else {
                imagenesstrign+=imagenest.get(i)+",";
            }
        }
        preferences.edit().putString(SharedPreferencesApp.IMAGENES_TABLERO,imagenesstrign).apply();
    }
    private List<String> recuperarImagenesTableroPreferences(){
        String imagenesstring=preferences.getString(SharedPreferencesApp.IMAGENES_TABLERO,"");
        List<String> imagenest= Arrays.stream(imagenesstring.split(",")).filter(el->!el.isEmpty()).collect(Collectors.toList());
        return imagenest;
    }

    private void shutdownExcecutroService(){
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

}