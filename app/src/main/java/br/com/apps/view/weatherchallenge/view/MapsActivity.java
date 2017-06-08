package br.com.apps.view.weatherchallenge.view;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import br.com.apps.view.weatherchallenge.R;
import br.com.apps.view.weatherchallenge.model.Temperature;

import static java.lang.Double.parseDouble;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private ArrayList<Temperature> temperatures;
    private LatLng myLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //------------------------------------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //------------------------------------------------------------------
        //-- Capturar arraylist da primeira intent
        //------------------------------------------------------------------
        temperatures = (ArrayList<Temperature>) getIntent().getSerializableExtra("temperatures");

        //------------------------------------------------------------------
        //-- Cria um objeto LatLng para colocar na centralização + zoom
        //------------------------------------------------------------------
        myLatLng = new LatLng(parseDouble(temperatures.get(0).getLat()),parseDouble(temperatures.get(0).getLon()));
    }
    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;

        //------------------------------------------------------------------
        //-- Centraliza minha localização e zoom nível 10 para cidades
        //------------------------------------------------------------------
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLatLng,10);
        googleMap.animateCamera(cameraUpdate);

        //------------------------------------------------------------------
        //-- Coloca os markers no mapa para cada item do Arraylist
        //------------------------------------------------------------------
        for(int i = 0 ; i < temperatures.size() ; i++ ) {
            createMarker(temperatures.get(i).getLat(),temperatures.get(i).getLon(),temperatures.get(i).getName(),temperatures.get(i).getTemp());
        }
    }

    //------------------------------------------------------------------
    //-- Menu Superior
    //------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }
    //------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itm_ChangeIntent) {

            Intent intent = new Intent(MapsActivity.this,MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //------------------------------------------------------------------
    //-- Método para montar o Marker
    //------------------------------------------------------------------
    protected Marker createMarker(String latitude, String longitude, String title, String snippet) {

        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(parseDouble(latitude), parseDouble(longitude)))
                        .title(title)
                        .anchor(0.5f, 0.5f)
                        .snippet(snippet));


    }
}