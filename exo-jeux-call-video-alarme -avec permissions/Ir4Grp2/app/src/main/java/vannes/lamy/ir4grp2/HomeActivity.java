package vannes.lamy.ir4grp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        //TODO definir les runtimes permissions
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.CALL_PHONE},1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "permission acceptée", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Vous devez accepter les permissions pour pouvoir téléphoner!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    public void action_esaip(View v){
        String url = "https://www.esaip.org/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void action_alarme(View v){
        //TODO ajouter les permissions dans l'androidmanifest
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_MESSAGE, "Alarme ESAIP");
        i.putExtra(AlarmClock.EXTRA_HOUR, 18);
        i.putExtra(AlarmClock.EXTRA_MINUTES, 15);
        startActivity(i);
    }
    public void action_video(View v){
        Intent i=new Intent(HomeActivity.this, VideoActivity.class);
        startActivity(i);

    }
    public void action_callPhone(View v){
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