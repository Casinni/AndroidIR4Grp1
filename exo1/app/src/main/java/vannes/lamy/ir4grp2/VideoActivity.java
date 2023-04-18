package vannes.lamy.ir4grp2;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        //Uri is the path to access file
        Uri raw_uri= Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bigbunny);
        //get widget
        VideoView v=findViewById(R.id.videoView);
        //define ressource to play
        v.setVideoURI(raw_uri);
        //define controller to start and stop de video
        v.setMediaController(new MediaController(this));
        v.start();
    }
}