package vannes.lamy.ir4grp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
TextView w;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Récuperation de l'intent envoyé de notre activity principale
        Intent i = getIntent();
            //recuperation du login
        String login = i.getStringExtra("msg");
        //recuperation du widget textview
        w= findViewById(R.id.Welcome);
        w.setText("Bienvenue "  + login.toString());
    }
    public void action_esaip(View v){
        //ajouter dans l'androidmanifest
        //<uses-permission android:name="android.permission.INTERNET"/>
   String url= "https://www.esaip.org";
   //declaration d'une intent
        Intent i =new Intent(Intent.ACTION_VIEW);
        //converison dd l'url en URI
        i.setData(Uri.parse(url));
        //lancement du navigateur
        startActivity(i);
    }
    public void action_alarme(View v){
        //Ajouter dans l'androidManifest
        //<uses-permission android:name="com.android.alarm.permission.SET_ALARM"/>
        Intent i =new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_MESSAGE,"Alarme ESAIP");
        i.putExtra(AlarmClock.EXTRA_HOUR,18);
        i.putExtra(AlarmClock.EXTRA_MINUTES,15);
        startActivity(i);
    }
    public void action_video(View v){
Intent i = new Intent(HomeActivity.this, VideoActivity.class);
startActivity(i);

    }
    public void action_callPhone(View v){
        //ajouter permission dans l'androidmanifest
        //<uses-permission android:name="android.permission.CALL_PHONE"/>
        startActivity(new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:0123123456")));
    }
    public void action_games(View v){
        Intent i=new Intent(HomeActivity.this,GamesActivity.class);
        //recuperation du Tag
        String tag= v.getTag().toString();
        i.putExtra("games",tag);
        startActivity(i);
    }

}