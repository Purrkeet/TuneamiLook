package pe.edu.upc.tuneamilook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import layout.seleccionColor.seleccionColor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void AbrirFRMobtenerColores(View view)
    {
        Intent intent = new Intent(MainActivity.this, seleccionColor.class);
        startActivity(intent);
    }
}
