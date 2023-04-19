package vannes.lamy.ir4grp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
EditText l,p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get widgets  login and password
        l=findViewById(R.id.login);
        p=findViewById(R.id.pwd);

    }
    public void clicValidate(View v){
        //authentification
        if(getResources().getText(R.string.login).equals(l.getText().toString()) &&
        getResources().getText(R.string.pwd).equals(p.getText().toString())) {
            //declaration intent
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            //insertion message dans intent correpondant au login
            i.putExtra("msg", l.getText().toString());
            //on lance l'activity HomeActivity
            startActivity(i);
        }
        else
            Toast.makeText(MainActivity.this," Error Authentification : login: "+
                            l.getText() + " Pwd: "+p.getText() ,
                    Toast.LENGTH_SHORT).show();
            this.clicClear(v);
    }
    public void clicClose(View v){
        finish();
    }
    public void clicClear(View v){
        l.setText("");
        p.setText("");
    }
}