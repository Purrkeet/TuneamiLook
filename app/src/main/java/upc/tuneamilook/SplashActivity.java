package upc.tuneamilook;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase = null;
    final String databaseName = "tuneamilook";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        try {
            sqLiteDatabase = this.openOrCreateDatabase(databaseName, MODE_PRIVATE, null);

            /**
             * Crear tablas en caso no existan.
             */

            // Tipo de prenda
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS" +
                    " tipo_prenda" +
                    " (id INTEGER PRIMARY KEY, nombre VARCHAR);");

            // Color
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS" +
                    " color" +
                    " (id INTEGER PRIMARY KEY, hexadecimal VARCHAR);");

            // Etiqueta
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS" +
                    " etiqueta" +
                    " (id INTEGER PRIMARY KEY, hexadecimal VARCHAR);");

            // Prenda
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS" +
                    " prenda" +
                    " (id INTEGER PRIMARY KEY, id_tipo_prenda INTEGER, foto VARCHAR);");

            // Etiquetas de cada prenda
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS" +
                    " prenda_etiquetas" +
                    " (id INTEGER PRIMARY KEY, id_prenda INTEGER, id_etiqueta INTEGER);");

            // Colores de cada prenda
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS" +
                    " prenda_colores" +
                    " (id INTEGER PRIMARY KEY, id_prenda INTEGER, id_color INTEGER);");

            // Combinación
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS" +
                    " combinacion" +
                    " (id INTEGER PRIMARY KEY, numero_prendas INTEGER);");

            // Prendas en cada combinación
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS" +
                    " combinacion_prendas" +
                    " (id INTEGER PRIMARY KEY, id_combinacion INTEGER, id_prenda INTEGER);");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
