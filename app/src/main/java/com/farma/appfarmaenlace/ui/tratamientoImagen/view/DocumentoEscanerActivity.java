package com.farma.appfarmaenlace.ui.tratamientoImagen.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.farma.appfarmaenlace.R;
import com.farma.appfarmaenlace.data.api.ApiAnalisisImagen;
import com.farma.appfarmaenlace.data.api.ApiClient;
import com.farma.appfarmaenlace.data.api.ApiResponseEnvioInformacion;
import com.farma.appfarmaenlace.data.api.BodyOCR;
import com.farma.appfarmaenlace.data.api.EstrucOrdenamiento;
import com.farma.appfarmaenlace.ui.main.view.MainActivity;
import com.farma.appfarmaenlace.utils.Constantes;
import com.farma.appfarmaenlace.utils.SharedPreferencesApp;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import net.kuama.documentscanner.presentation.BaseScannerActivity;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class DocumentoEscanerActivity extends BaseScannerActivity {
    private CompositeDisposable disposable;
    private FrameLayout frameLayout;
    private Moshi moshi;
    private SharedPreferences preferences;
    private ApiResponseEnvioInformacion estructuraDocumento;
    private List<String> images;

    @Override
    public void onClose() {

    }

    @Override
    public void onDocumentAccepted(@NotNull Bitmap bitmap) {

        try {
            getEstructuraDocumento();
            getImagenes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        frameLayout.setVisibility(View.VISIBLE);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();


        // int keydoc=preferences.getInt(SharedPreferencesApp.CODIGO_ESCANER_KEY,0);
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        //TODO añadir atributo bitmap tipo Bitmap a BodyOCR

        BodyOCR body = new BodyOCR();
        body.setBase64(encoded);
        body.setBitmap(bitmap);

        try {
            validarImagen(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(@NotNull Throwable throwable) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup layout = findViewById(R.id.root_view);
        ViewGroup layoutpg = (ViewGroup) getLayoutInflater().inflate(R.layout.progressbar_layout, layout);

        frameLayout = findViewById(R.id.progressBar);
        moshi = new Moshi.Builder().build();
        preferences = SharedPreferencesApp.getPreferences(getApplicationContext());
        disposable = new CompositeDisposable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void getEstructuraDocumento() throws IOException {
        String estructura = preferences.getString(SharedPreferencesApp.ESTRUCTURA_DOC_KEY, "");
        JsonAdapter<ApiResponseEnvioInformacion> adapter1 = moshi.adapter(ApiResponseEnvioInformacion.class);
        estructuraDocumento = adapter1.fromJson(estructura);
    }

    private void getImagenes() throws IOException {
        String jsonstring = preferences.getString(SharedPreferencesApp.IMAGENES_ESC_KEY, "[]");

        Type listMyData = Types.newParameterizedType(List.class, String.class);
        JsonAdapter<List<String>> adapter = moshi.adapter(listMyData);
        images = adapter.fromJson(jsonstring);

    }

    private void addImagen(Bitmap bitmap) throws IOException {

        File dir=new File(getBaseContext().getCacheDir()+File.separator+"ImagenesFarmaScan/");

        String nombreImagen =  (System.currentTimeMillis()+"");
        nombreImagen = nombreImagen.substring((nombreImagen.length()-8));

        //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), result);
        File imagen=new File(dir,nombreImagen+".jpg");

        try {
            FileOutputStream out = new FileOutputStream(imagen);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* images.add(base64);

        Type listMyData = Types.newParameterizedType(List.class, String.class);
        JsonAdapter<List<String>> adapter = moshi.adapter(listMyData);

        String jsonstring = adapter.toJson(images);

        preferences
                .edit()
                .putString(SharedPreferencesApp.IMAGENES_ESC_KEY, jsonstring)
                .apply(); */

        cerrarActivity();
    }

    private void predecir(BodyOCR body, String validarOCR) {
        ViewGroup layout = findViewById(R.id.root_view);
        Retrofit retrofit = new ApiClient(Constantes.URL_PREDECIR).retrofitInstance();
        ApiAnalisisImagen servicio = retrofit.create(ApiAnalisisImagen.class);

        Disposable disposables = servicio.PredecirDocumento(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResponseResponse -> {
                    frameLayout.setVisibility(View.GONE);

                    String mensaje = (String) apiResponseResponse.body().getMensaje();
                    //Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                    if (mensaje.contains("Vuelva a escanear") && validarOCR.equals("S")) {
                        //Snackbar.make(layout.getRootView(), "Vuelva a escanear.", Snackbar.LENGTH_LONG).show();
                        Snackbar.make(layout.getRootView(), "Vuelva a escanear", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                    addImagen(body.getBitmap());

                    Toast.makeText(this, "Imagen Correcta",
                            Toast.LENGTH_LONG).show();
                }, throwable -> {
                    Toast.makeText(this, "MENSAJE" + throwable.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
        disposable.add(disposables);
    }

    /*private void validarImagen(BodyOCR body) throws IOException {
        List<EstrucOrdenamiento> farmascanML = new ArrayList<>();
        farmascanML = estructuraDocumento.getFarmascanML();

        if(farmascanML==null){
            addImagen(body.getBitmap());
            return;
        }
        if (farmascanML.size() == 0) {
            addImagen(body.getBitmap());
            return;
        }

        if (farmascanML.size() == images.size()) {
            Toast.makeText(getApplicationContext(), "El número de documentos esta completo", Toast.LENGTH_LONG).show();
            //cerrarActivity();
            return;
        }
        EstrucOrdenamiento est = farmascanML.get(images.size());
        body.setCodigoDocumento(est.getFml_documento_codigo());

        if (est.getFml_validar_modelo_prediccion().equals("S")) {
            predecir(body, est.getFml_validar_reglas_ocr());
        } else {
            addImagen(body.getBitmap());
        }
    }*/
    private void validarImagen(BodyOCR body) throws IOException {
        File dir=new File(getBaseContext().getCacheDir()+File.separator+"ImagenesFarmaScan/");
        int nroimg=dir.list().length;

        List<EstrucOrdenamiento> farmascanML = new ArrayList<>();
        farmascanML = estructuraDocumento.getFarmascanML();

        if(farmascanML==null){
            addImagen(body.getBitmap());
            return;
        }
        if (farmascanML.size() == 0) {
            addImagen(body.getBitmap());
            return;
        }

        if (farmascanML.size() == nroimg) {
            Toast.makeText(getApplicationContext(), "El número de documentos esta completo", Toast.LENGTH_LONG).show();
            //cerrarActivity();
            return;
        }
        EstrucOrdenamiento est = farmascanML.get(nroimg);
        body.setCodigoDocumento(est.getFml_documento_codigo());

        if (est.getFml_validar_modelo_prediccion().equals("S")) {
            predecir(body, est.getFml_validar_reglas_ocr());
        } else {
            addImagen(body.getBitmap());
        }
    }

    public void cerrarActivity() {
        Intent intent = new Intent(DocumentoEscanerActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}