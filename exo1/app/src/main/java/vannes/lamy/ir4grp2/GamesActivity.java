package vannes.lamy.ir4grp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class GamesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        //recuperation intent
        Intent i=getIntent();
        String games= i.getStringExtra("games");
        //recuperation la webview
        WebView jeux=findViewById(R.id.webView);
        //activation du JS
        WebSettings webSettings=jeux.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if(games.equals("dj")){
            jeux.loadUrl("file:///android_asset/doodleJump/index.html");
        }
        else  if(games.equals("fb")){
            jeux.loadUrl("file:///android_asset/flappyBird/index.html");
        }
        else  if(games.equals("p4")){
            jeux.loadUrl("file:///android_asset/puissance4/index.html");
        }
        else {
            Toast.makeText(getApplicationContext(), " Erreur", Toast.LENGTH_LONG).show();

        }
    }
}