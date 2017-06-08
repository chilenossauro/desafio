package br.com.apps.view.weatherchallenge.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import br.com.apps.view.weatherchallenge.BuildConfig;
import br.com.apps.view.weatherchallenge.R;
import br.com.apps.view.weatherchallenge.adapter.TemperatureAdapter;
import br.com.apps.view.weatherchallenge.controller.Service;
import br.com.apps.view.weatherchallenge.controller.ServiceGenerator;
import br.com.apps.view.weatherchallenge.model.Temperature;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private RecyclerView rcwTemperature;

    private LocationManager locationManager;
    private Location location;
    private String latitude;
    private String longitude;

    private Service service;

    private ProgressDialog progressDialog;

    private TemperatureAdapter temperatureAdapter;
    private ArrayList<Temperature> temperatures;

    private String tempType;

    private Menu menu;
    private MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //------------------------------------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //------------------------------------------------------------------
        temperatures = new ArrayList<>();
        //------------------------------------------------------------------
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        latitude = String.format(Locale.US,"%.1f",location.getLatitude());
        longitude = String.format(Locale.US,"%.1f",location.getLongitude());
        //------------------------------------------------------------------
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Weather Data");
        progressDialog.setMessage("Receiving data. Please wait.\nLatitude: " + latitude + "\nLongitude:" + longitude);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        //------------------------------------------------------------------
        rcwTemperature = (RecyclerView) findViewById(R.id.rcwTemperature);

        service = ServiceGenerator.createService(Service.class);

        tempType = tempTypeb;

        Intent intent = new Intent();
        //------------------------------------------------------------------
        //-- RxAndroid -- criar objetos observaveis e tomar ação baseado em eventos
        //------------------------------------------------------------------
        Observable<ResponseBody> callTemperature =
                service.getWeather
                        (latitude, longitude, BuildConfig.LANGUAGE,tempType,
                                BuildConfig.CITIES, BuildConfig.API_KEY)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());

        callTemperature.subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(ResponseBody value) {

                JSONObject jsonObject;

                try {
                    jsonObject = new JSONObject(value.string());
                    int count = jsonObject.getInt("count");

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for(int position=0;position<count;position++){
                        temperatures.add(new Temperature(jsonArray.getJSONObject(position)));
                    }

                } catch (Exception e) {
                    Log.e("Temperature Error",e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("Temperature onError",e.getMessage());
            }

            @Override
            public void onComplete() {
                temperatureAdapter = new TemperatureAdapter(temperatures);

                int number_columns = 1;
                RecyclerView.LayoutManager recyce = new GridLayoutManager(MainActivity.this,number_columns);
                rcwTemperature.addItemDecoration(new GridSpacingdecoration(2, dpToPx(10), true));
                rcwTemperature.setLayoutManager(recyce);
                rcwTemperature.setItemAnimator( new DefaultItemAnimator());
                rcwTemperature.setAdapter(temperatureAdapter);

                progressDialog.dismiss();


            }
        });
    }
    //------------------------------------------------------------------
    //-- Gerenciamento da localização - mudança de localização, provedor de localização
    //------------------------------------------------------------------
    @Override
    public void onLocationChanged(Location location) {
    }
    //------------------------------------------------------------------
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    //------------------------------------------------------------------
    @Override
    public void onProviderEnabled(String provider) {
    }
    //------------------------------------------------------------------
    @Override
    public void onProviderDisabled(String provider) {
    }

    //------------------------------------------------------------------
    //-- Menu Superior
    //------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;

        MenuItem menuItem = menu.getItem(0);

        if(tempType == "metric"){
            menuItem.setIcon(R.drawable.fahrenheit);
        } else {
            menuItem.setIcon(R.drawable.celsius);
        }

        return true;
    }
    //------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itm_ChangeIntent) {
            Intent intent = new Intent(MainActivity.this,MapsActivity.class);
            intent.putExtra("temperatures",temperatures);
            startActivity(intent);
            return true;
        }

        if (id == R.id.itm_ChangeTemp){
            menuItem = menu.getItem(0);

            if(tempType == "metric"){
                tempTypeb = "imperial";
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
            } else {
                tempTypeb = "metric";
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //------------------------------------------------------------------
    //-- Classe customizada para decorar o Recycler View
    //------------------------------------------------------------------
    public class GridSpacingdecoration extends RecyclerView.ItemDecoration {

        private int span;
        private int space;
        private boolean include;

        public GridSpacingdecoration(int span, int space, boolean include) {
            this.span = span;
            this.space = space;
            this.include = include;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % span;

            if (include) {
                outRect.left = space - column * space / span;
                outRect.right = (column + 1) * space / span;

                if (position < span) {
                    outRect.top = space;
                }
                outRect.bottom = space;
            } else {
                outRect.left = column * space / span;
                outRect.right = space - (column + 1) * space / span;
                if (position >= span) {
                    outRect.top = space;
                }
            }
        }

    }

    //------------------------------------------------------------------
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    //------------------------------------------------------------------
    public static String tempTypeb = "metric";
}