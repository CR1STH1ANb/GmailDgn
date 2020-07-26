package com.example.gmaildgn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.gmaildgn.Models.adtMensajes;
import com.example.gmaildgn.Models.mensaje;
import com.example.gmaildgn.WebService.Asynchtask;
import com.example.gmaildgn.WebService.WebService;
import com.google.android.material.navigation.NavigationView;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, Asynchtask  {
    DrawerLayout drawerLayout;
    NavigationView navView;
    RecyclerView recyclerview=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.iconmenu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView = (NavigationView)findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        recyclerview=(RecyclerView)findViewById(R.id.rcvLista);
        recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));


        recyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        Map<String, String> datos = new HashMap<>();

        WebService ws= new WebService("https://recycleviewfbp.firebaseio.com/archivo.json",datos,
                MainActivity.this, (Asynchtask) MainActivity.this);

        ws.execute("GET");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.mnutoolbar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        boolean fragmentTransaction = false;

        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.menu_seccion_1:
                Intent intent = new Intent(this, Principal.class);
                startActivity(intent);
                break;
            case R.id.menu_seccion_2:
                Intent intent2 = new Intent(this, Social.class);
                startActivity(intent2);
                break;
            case R.id.menu_seccion_3:
                Intent intent3 = new Intent(this, Promociones.class);
                startActivity(intent3);
                break;
            case R.id.menu_seccion_4:
                Intent intent4 = new Intent(this, Notificaciones.class);
                startActivity(intent4);
                break;
            case R.id.menu_seccion_5:
                Intent intent5 = new Intent(this, Foros.class);
                startActivity(intent5);
                break;

        }


        if(fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            menuItem.setChecked(true);
            getSupportActionBar().setTitle(menuItem.getTitle());
        }
        drawerLayout.closeDrawers();
        return true;


    }
    @Override
    public void processFinish(String result) throws JSONException {
        JSONArray JSONlistaProductos = new JSONArray(result);
        ArrayList<mensaje> lstMensajes =new ArrayList<mensaje>();

        //Invoco al metodo de la clase productos que es para el parseo de datos-me devuelve un arraylist de producto
        lstMensajes = mensaje.JsonObjectsBuild(JSONlistaProductos);

        //HASTA AQUÍ SÍ RECIBE LOS 11 PRODUCTOS
        //Envío la lista de productos a l
        adtMensajes adapatorHortalizas = new adtMensajes(this, lstMensajes);
        recyclerview.setAdapter(adapatorHortalizas);
    }
}