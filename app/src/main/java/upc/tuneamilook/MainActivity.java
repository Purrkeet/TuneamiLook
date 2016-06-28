package upc.tuneamilook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import upc.tuneamilook.adapters.PrendasAdapter;
import upc.tuneamilook.entities.Color;
import upc.tuneamilook.entities.Etiqueta;
import upc.tuneamilook.entities.Prenda;
import upc.tuneamilook.entities.TipoPrenda;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase = null;
    final String databaseName = "tuneamilookdb234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.setActivityTitle("Prendas");
        this.addListenerToAgregarPrenda();

        this.mostrarListaDePrendas();
    }

    private void setActivityTitle(String title) {
        TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        if (null != activityTitle)
            activityTitle.setText(title);
    }

    private void addListenerToAgregarPrenda() {
        Button buttonAgregarPrenda = (Button) findViewById(R.id.main__buttonAgregarPrenda);
        if (null == buttonAgregarPrenda)
            return;

        buttonAgregarPrenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AgregarPrenda1Activity.class);
                startActivity(intent);
            }
        });
    }

    private void mostrarListaDePrendas() {
        List<Prenda> prendasList = this.obtenerListaDePrendas();
        if (prendasList.size() == 0)
            return;

        PrendasAdapter adapter = new PrendasAdapter(this, (ArrayList<Prenda>) prendasList);
        ListView listView = (ListView) findViewById(R.id.main__listViewPrendas);
        if (listView == null)
            return;

        listView.setAdapter(adapter);
        listView.setVisibility(View.VISIBLE);

        TextView textView = (TextView) findViewById(R.id.main__textViewPrendas);
        if (null != textView)
            textView.setVisibility(View.GONE);
    }

    private List<Prenda> obtenerListaDePrendas() {
        sqLiteDatabase = this.openOrCreateDatabase(databaseName, MODE_PRIVATE, null);

        List<Prenda> prendasList = new ArrayList<>();
        Cursor cursorPrenda = sqLiteDatabase.rawQuery("SELECT id, id_tipo_prenda, foto FROM prendas", null);

        if (cursorPrenda.moveToFirst()) {
            do {
                Prenda prenda = new Prenda();
                prenda.id = Long.parseLong(cursorPrenda.getString(0));
                prenda.foto = cursorPrenda.getString(2);

                Cursor cursorTipoPrenda = sqLiteDatabase.rawQuery("SELECT id, nombre " +
                        " FROM tipos_prenda" +
                        " WHERE id = " + cursorPrenda.getString(1), null);
                if (cursorTipoPrenda.moveToFirst()) {
                    TipoPrenda tipoPrenda = new TipoPrenda();
                    tipoPrenda.id = Long.parseLong(cursorTipoPrenda.getString(0));
                    tipoPrenda.nombre = cursorTipoPrenda.getString(1);

                    prenda.tipoPrenda = tipoPrenda;
                }

                Cursor cursorEtiqueta = sqLiteDatabase.rawQuery("SELECT e.id, e.nombre " +
                        " FROM prenda_etiquetas AS pe" +
                        " INNER JOIN etiquetas AS e ON (e.id = pe.id_etiqueta)" +
                        " WHERE pe.id_prenda = " + prenda.id.toString(), null);
                if (cursorEtiqueta.moveToFirst()) {
                    do {
                        Etiqueta etiqueta = new Etiqueta();
                        etiqueta.id = Long.parseLong(cursorEtiqueta.getString(0));
                        etiqueta.nombre = cursorEtiqueta.getString(1);

                        prenda.etiquetas.add(etiqueta);
                    }
                    while (cursorEtiqueta.moveToNext());
                }

                Cursor cursorColor = sqLiteDatabase.rawQuery("SELECT c.id, c.hexadecimal " +
                        " FROM prenda_colores AS pc" +
                        " INNER JOIN colores AS c ON (c.id = pc.id_color)" +
                        " WHERE pc.id_prenda = " + prenda.id.toString(), null);
                if (cursorColor.moveToFirst()) {
                    do {
                        Color color = new Color();
                        color.id = Long.parseLong(cursorColor.getString(0));
                        color.hexadecimal = cursorColor.getString(1);

                        prenda.colores.add(color);
                    }
                    while (cursorColor.moveToNext());
                }

                prendasList.add(prenda);
            } while (cursorPrenda.moveToNext());
        }

        return prendasList;
    }
}
