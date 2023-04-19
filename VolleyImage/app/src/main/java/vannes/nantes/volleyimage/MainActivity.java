package vannes.nantes.volleyimage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity implements Response.Listener<Bitmap> {
    private ImageView viewer;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.viewer = (ImageView) findViewById(R.id.imageView);
    }
    @Override
    protected void onStart() {
        super.onStart();
        this.progress = new ProgressDialog(this);
        this.progress.setTitle("Veuillez patientez");
        this.progress.setMessage("Récupération de l'image en cours...");
        this.progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progress.show();

        //Instancie la file de message (cet objet doit être un singleton)
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://img.phonandroid.com/2018/12/top-10-android-applis.jpg";
        // Requête d'une image à l'URL demandé
        ImageRequest picRequest = new ImageRequest(    url, this, 0,
              0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Erreur download:"+error,Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        //Insère la requête dans la file
        queue.add(picRequest);
    }
    @Override
    public void onResponse(Bitmap response) { //callback en cas de succès
        //fermeture de la boite de dialogue
        if(this.progress.isShowing()) this.progress.dismiss();
        //Affectation de l'image dans l'imageview
        this.viewer.setImageBitmap(response);
    }


}