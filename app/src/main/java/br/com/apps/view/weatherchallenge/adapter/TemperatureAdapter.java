package br.com.apps.view.weatherchallenge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.apps.view.weatherchallenge.R;
import br.com.apps.view.weatherchallenge.model.Temperature;

public class TemperatureAdapter extends RecyclerView.Adapter<TemperatureAdapter.TemperatureHolder>{

    //---------------------------------------------------------------------------
    private ArrayList<Temperature> temperatures;
    private Context context;
    //---------------------------------------------------------------------------
    public TemperatureAdapter(ArrayList<Temperature> temperatures) {
        this.temperatures = temperatures;
    }
    //---------------------------------------------------------------------------
    @Override
    public TemperatureHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.activity_temperature,parent,false);
        TemperatureHolder temperatureHolder = new TemperatureHolder(view);
        return temperatureHolder;
    }
    //---------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(TemperatureHolder holder, int position) {

        Temperature temperature = temperatures.get(position);
        Temperature.TemperatureWeather temperatureWeather = temperature.getTemperatureWeathers().get(0);

        holder.txvTemperatureName.setText(temperature.getName());
        holder.txvTemperatureDescription.setText(temperatureWeather.getDescription());
        holder.txvTemperatureTempFC.setText(temperature.getTemp());
        holder.txvTemperatureTempMin.setText("Min: " + temperature.getTemp_min());
        holder.txvTemperatureTempMax.setText("Max: " + temperature.getTemp_max());

        Picasso.with(context)
                .load(temperatureWeather.getIcon())
                .into(holder.txvTemperatureIcon);
    }
    //---------------------------------------------------------------------------
    @Override
    public int getItemCount() {
        return temperatures.size();
    }
    //---------------------------------------------------------------------------
    class TemperatureHolder extends RecyclerView.ViewHolder {

        private TextView  txvTemperatureName;
        private ImageView txvTemperatureIcon;
        private TextView  txvTemperatureDescription;
        private TextView  txvTemperatureTempFC;
        private TextView  txvTemperatureTempMin;
        private TextView  txvTemperatureTempMax;

        public TemperatureHolder(View itemView) {

            super(itemView);

            txvTemperatureName        = (TextView)  itemView.findViewById(R.id.txvTemperatureName);
            txvTemperatureIcon        = (ImageView) itemView.findViewById(R.id.txvTemperatureIcon);
            txvTemperatureDescription = (TextView)  itemView.findViewById(R.id.txvTemperatureDescription);
            txvTemperatureTempFC      = (TextView)  itemView.findViewById(R.id.txvTemperatureTempFC);
            txvTemperatureTempMin     = (TextView)  itemView.findViewById(R.id.txvTemperatureTempMin);
            txvTemperatureTempMax     = (TextView)  itemView.findViewById(R.id.txvTemperatureTempMax);
        }
    }
    //---------------------------------------------------------------------------
}
