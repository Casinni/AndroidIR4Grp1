package vannes.lamy.openweathermap1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Response.Listener<Bitmap> {
    private ImageView viewer;
    private TextView ville;
    private TextView description;
    private TextView temp;
    private TextView temp_max;
    private TextView temp_min;
    private TextView vit_vent;
    private TextView pression_atm;
    private TextView humidite;
    private TextView direction;
    private ProgressDialog progress;
    private Weather weather;
    private static String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?id=524901&units=metric&lang=FR&APPID=c0bbff4a824cce23670fa594dfa7e8b1";
    private static String IMG_URL = "https://openweathermap.org/img/w/";
    private SearchManager searchManager;
    private SearchView searchView=null;
    String requeteVille;
    RequestQueue queue;
    public String fileName = "file";
    public SharedPreferences pos;
    public SharedPreferences villeSearch;
    String lat;
    String lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //révupération latitude longitude
        pos = getSharedPreferences(fileName, 0);
        lat = pos.getString("lat", "47.46481");
        lon = pos.getString("long", "-0.49729");


        // Instancie la file de message (cet objet doit être un singleton)

        queue = Volley.newRequestQueue(this);
        MiseAJour();
        this.viewer = (ImageView) findViewById(R.id.imageV);
        this.ville = (TextView) findViewById(R.id.ville);
        this.description = (TextView) findViewById(R.id.desc);
        this.temp = (TextView) findViewById(R.id.temp);
        this.temp_max = (TextView) findViewById(R.id.temp_max);
        this.temp_min = (TextView) findViewById(R.id.temp_min);
        this.vit_vent = (TextView) findViewById(R.id.vitvent);
        this.pression_atm = (TextView) findViewById(R.id.pression);
        this.humidite = (TextView) findViewById(R.id.humidite);
        this.direction = (TextView) findViewById(R.id.direction);

        Button btnRefresh = (Button) findViewById(R.id.refresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View actuelView) {
              MiseAJour();
                       }
        });

    }

    public void MiseAJour(){

        this.progress = new ProgressDialog(this);
        this.progress.setTitle("Veuillez patientez");
        this.progress.setMessage("Récupération de des informations météos en cours...");
        this.progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progress.show();
        String newUrl=BASE_URL;
        if( searchView!=null ){
           newUrl+="&q="+searchView.getQuery().toString();
        }
        else
            newUrl+="&lat="+lat+"&lon="+lon;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, newUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        weather = new Weather();
                        try {
                            Log.e("tab",response.toString());
                            weather = JSONWeatherParser.getWeather(response.toString());

                            ville.setText("Latitude:" +lat.toString()+ " Longitude:"+lon.toString());
                            temp_max.setText("température max: " + weather.temperature.getMaxTemp() + "Degrés Celcius");
                            temp_min.setText("température min: " +  weather.temperature.getMinTemp());
                            temp.setText("température: " + weather.temperature.getTemp());
                            description.setText("Description: " + weather.currentCondition.getDescr());
                            pression_atm.setText("pression Atmospherique: "+ weather.currentCondition.getPressure()+ "HP");
                            vit_vent.setText("Vitesse du vent:" + weather.wind.getSpeed());
                            humidite.setText("Humidité:" +  weather.currentCondition.getHumidity() +"%");
                            direction.setText("Direction du vent:" + weather.wind.getDeg() + " degrés");
                            if (progress.isShowing()) progress.dismiss();
                            lat = pos.getString("lat", "47.46481");
                            lon = pos.getString("long", "-0.49729");

                       downloadImage(weather.currentCondition.getIcon(), queue);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.e("error Volley", error.toString());

                    }
                });
        queue.add(jsObjRequest);



    }

    @Override
    public void onResponse(Bitmap response) { //callback en cas de succès
        //fermeture de la boite de dialogue
        if (this.progress.isShowing()) this.progress.dismiss();

   Bitmap bm = Bitmap.createScaledBitmap(response, 400, 400, true);
        //TODO Affectation de l'image dans l'imageview
       this.viewer.setImageBitmap(bm);


    }

    public void downloadImage(String pathImg, RequestQueue queue) {
        // Requête d'une image à l'URL demandée
        Log.i("Image down path:", pathImg);
        ImageRequest picRequest = new ImageRequest(IMG_URL + pathImg + ".png?APPID=c0bbff4a824cce23670fa594dfa7e8b1", this, 0, 0, null, null);
        // Insère la requête dans la file
        queue.add(picRequest);


    }


}