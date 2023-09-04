package com.farma.appfarmaenlace.ui.main.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.farma.appfarmaenlace.R;
import com.farma.appfarmaenlace.data.api.ApiClient;
import com.farma.appfarmaenlace.data.api.Cookies;
import com.farma.appfarmaenlace.data.api.GeneralApiService;
import com.farma.appfarmaenlace.ui.home.model.Farmacia;
import com.farma.appfarmaenlace.utils.Network;
import com.farma.appfarmaenlace.utils.SharedPreferencesApp;
import com.google.android.material.navigation.NavigationView;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private CompositeDisposable compositeDisposable;
    private static final int CODIGO_PERMISOS_CAMARA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //NavHostFragment navHostFragment=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        //navController=navHostFragment.getNavController();
        navController = Navigation.findNavController(this, R.id.fragment);
        drawerLayout = findViewById(R.id.drawerLayout);
        appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.homeFragment)
                //.setFallbackOnNavigateUpListener(super::onSupportNavigateUp)
                //.setOpenableLayout(drawerLayout)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        String datos=getIntent().getStringExtra("datos");
        /*if(true){
            Navigation.findNavController(this,R.id.fragment).navigate(R.id.imagenesFragment);
        }*/

        compositeDisposable=new CompositeDisposable();
        comprobarpermiso();

    }



    @Override
    public boolean onSupportNavigateUp() {
        //se sobre escribe este mètodo para que funcione el botòn para abrir el drawer
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.i("Menú",item.getItemId()+"");
        Log.i("Menú 1",R.id.cerrar_sesion+"");
        final NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavOptions.Builder builder=new NavOptions.Builder();
        builder.setLaunchSingleTop(true);
        if (item.getItemId() == R.id.cerrar_sesion) {
            try {
                Log.i("Menú 1", item.getItemId() + "");
                logout();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);

        //return super.onOptionsItemSelected(item);
    }

    public void logout() throws IOException {
        SharedPreferences preferences= SharedPreferencesApp.getPreferences(getApplicationContext());

        Moshi moshi=new Moshi.Builder().build();
        JsonAdapter<Farmacia> adapter=moshi.adapter(Farmacia.class);
        String jsonf=preferences.getString(SharedPreferencesApp.FARMACIA_KEY,"");
        Farmacia farmacia=adapter.fromJson(jsonf);

        Cookies cookies=new Cookies();
        cookies.setCookie(Network.getIp(true));
        //cookies.setCookie("192.168.237.34");
        cookies.setEstado("X");
        cookies.setIpDispositivo(Network.getIp(true));
        //cookies.setIpDispositivo("192.168.237.34");
        cookies.setNombreUsu(farmacia.getNombreCorto());

        Log.i("cookies",cookies.toString());

        Retrofit retrofit2 = new ApiClient("http://"+preferences.getString(SharedPreferencesApp.IP_FARMACIA_KEY,"")+"/").retrofitInstance();
        GeneralApiService service = (retrofit2.create(GeneralApiService.class));
        Disposable disposable=service.CerrarSesion(cookies)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResponseResponse -> {
                    String mensaje=(String) apiResponseResponse.body().getMensaje();
                    if(mensaje.equalsIgnoreCase("ok")){
                        navController.navigate(R.id.action_global_loginFragment);
                    }else {
                        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                    }
                },throwable -> {
                    Log.i("Main logout",throwable.getMessage());
                    Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void comprobarpermiso(){
        // Recuerda: MainActivity es el nombre de tu actividad
        int estadoDePermiso = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // Aquí el usuario dio permisos para acceder a la cámara
            Log.i("Tiene Permisos", estadoDePermiso+"");
        } else {
           pedirPermisosCamara();
        }
    }

    public void pedirPermisosCamara(){
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA},
                CODIGO_PERMISOS_CAMARA);
        // Si no, entonces pedimos permisos...;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == CODIGO_PERMISOS_CAMARA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permisoDeCamaraConcedido();
                Log.i("Concedido", "Concedido");
            } else {
                //permisoDeCamaraDenegado();
                pedirPermisosCamara();
                Log.i("Denegado", "Denegado");
            }
            // Aquí más casos dependiendo de los permisos
            // case OTRO_CODIGO_DE_PERMISOS...
        }
    }
}