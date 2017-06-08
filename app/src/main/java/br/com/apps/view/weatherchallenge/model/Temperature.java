package br.com.apps.view.weatherchallenge.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Temperature implements Serializable {

    //-----------------------------------------------------------------------------
    private String name;
    private String lat;
    private String lon;
    private Double temp;
    private Double temp_min;
    private Double temp_max;
    private ArrayList<TemperatureWeather> temperatureWeathers;
    //-----------------------------------------------------------------------------
    public Temperature() {}
    //-----------------------------------------------------------------------------
    public Temperature(JSONObject jsonObject) throws JSONException {

        //-- Capturando o name
        setName(jsonObject.getString("name"));

        //-- Capturando as coordenadas
        JSONObject jsonCoord = jsonObject.getJSONObject("coord");
        setLat(jsonCoord.getString("lat"));
        setLon(jsonCoord.getString("lon"));

        //-- Capturando as temperaturas
        JSONObject jsonMain = jsonObject.getJSONObject("main");
        setTemp(Double.parseDouble(jsonMain.getString("temp")));
        setTemp_min(Double.parseDouble(jsonMain.getString("temp_min")));
        setTemp_max(Double.parseDouble(jsonMain.getString("temp_max")));

        //-- Capturando os climas
        JSONArray jsonWeather = jsonObject.getJSONArray("weather");
        ArrayList<TemperatureWeather> temperatureWeatherArrayList = new ArrayList<>();

        for(int position=0;position<jsonWeather.length();position++) {

            JSONObject jsonWeatherDetail = jsonWeather.getJSONObject(position);
            TemperatureWeather climaWeather = new TemperatureWeather(jsonWeatherDetail.getString("description"),jsonWeatherDetail.getString("icon"));
            temperatureWeatherArrayList.add(climaWeather);
        }

        setTemperatureWeathers(temperatureWeatherArrayList);
    }
    //-----------------------------------------------------------------------------
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTemp() {
        return parseTemp(temp);
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getTemp_min() {
        return parseTemp(temp_min);
    }

    public void setTemp_min(Double temp_min) {
        this.temp_min = temp_min;
    }

    public String getTemp_max() {
        return parseTemp(temp_max);
    }

    public void setTemp_max(Double temp_max) {
        this.temp_max = temp_max;
    }

    public ArrayList<TemperatureWeather> getTemperatureWeathers() {
        return temperatureWeathers;
    }

    public void setTemperatureWeathers(ArrayList<TemperatureWeather> temperatureWeathers) {
        this.temperatureWeathers = temperatureWeathers;
    }
    //-----------------------------------------------------------------------------
    @Override
    public String toString() {
        return getName();
    }

    private String parseTemp(Double temp) {
        // ºC ºF
        return String.format("%.2fº", temp);
    }
    //-----------------------------------------------------------------------------
    public class TemperatureWeather implements Serializable {

        private String description;
        private String icon;

        public TemperatureWeather(String description,String icon) {
            this.description = description;
            this.icon = "http://openweathermap.org/img/w/" + icon + ".png";
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }
    //-----------------------------------------------------------------------------
}