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

    }
    public void action_alarme(View v){

    }
    public void action_video(View v){


    }
    public void action_callPhone(View v){

    }









    public void action_games(View v){
        Intent i=new Intent(HomeActivity.this,GamesActivity.class);
        //recuperation du Tag
        String tag= v.getTag().toString();
        i.putExtra("games",tag);
        startActivity(i);
    }

}