package upc.tuneamilook;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class AgregarPrenda4Activity extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase = null;
    final String databaseName = "tuneamilookdb234";

    private String agregarPrenda_tipoPrenda;
    private String agregarPrenda_foto;
    private List<String> agregarPrenda_colores;
    private List<String> agregarPrenda_etiquetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_prenda_4);

        Intent intent = getIntent();
        agregarPrenda_tipoPrenda = intent.getStringExtra("agregarPrenda_tipoPrenda");
        agregarPrenda_foto = intent.getStringExtra("agregarPrenda_foto");
        agregarPrenda_colores = intent.getStringArrayListExtra("agregarPrenda_colores");

        this.addListenerToAgregarEtiqueta();

        Button buttonAgregarEtiqueta = (Button) findViewById(R.id.agregarPrenda4__buttonAgregarEtiqueta);
        if (null != buttonAgregarEtiqueta) {
            buttonAgregarEtiqueta.callOnClick();
        }

        this.addListenerToContinuar();
    }

    private void addListenerToContinuar() {
        Button buttonContinuar = (Button) findViewById(R.id.agregarPrenda4__buttonContinuar);
        if (null == buttonContinuar)
            return;

        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LinearLayout etiquetasLinearLayout = (LinearLayout) findViewById(R.id.agregarPrenda4__etiquetas);
                    List<EditText> editTextChilds = getEditTextChilds(etiquetasLinearLayout);

                    List<String> etiquetas = new ArrayList<>();
                    for (int i = 0; i < editTextChilds.size(); i++) {
                        String e = editTextChilds.get(i).getText().toString();
                        if (!e.isEmpty())
                            etiquetas.add(e);
                    }

                    if (etiquetas.size() == 0) {
                        new AlertDialog.Builder(AgregarPrenda4Activity.this)
                                .setTitle("Espera")
                                .setMessage("Debes agregar al menos una etiqueta.")
                                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continuar.
                                    }
                                })
                                .show();

                        return;
                    }

                    agregarPrenda_etiquetas = etiquetas;

                    try {
                        sqLiteDatabase = AgregarPrenda4Activity.this.openOrCreateDatabase(databaseName, MODE_PRIVATE, null);

                        /**
                         * INSERT INTO TABLE_NAME (column1, column2, column3,...columnN)]
                         * VALUES (value1, value2, value3,...valueN);
                         */

                        // 1. Ingresar las etiquetas.
                        List<Long> agregarPrenda_etiquetasIds = new ArrayList<>();
                        for (int i = 0; i < agregarPrenda_etiquetas.size(); i++) {
                            String e = agregarPrenda_etiquetas.get(i);

                            ContentValues values = new ContentValues();
                            values.put("nombre", e);

                            long id = sqLiteDatabase.replace("etiquetas", null, values);
                            agregarPrenda_etiquetasIds.add(id);
                        }

                        // 2. Ingresar los colores.
                        List<Long> agregarPrenda_coloresIds = new ArrayList<>();
                        for (int i = 0; i < agregarPrenda_colores.size(); i++) {
                            String c = agregarPrenda_colores.get(i);

                            ContentValues values = new ContentValues();
                            values.put("hexadecimal", c);

                            long id = sqLiteDatabase.replace("colores", null, values);
                            agregarPrenda_coloresIds.add(id);
                        }

                        // 3. Ingresar tipo de prenda.
                        Long agregarPrenda_tipoPrendaId;
                        {
                            ContentValues values = new ContentValues();
                            values.put("nombre", agregarPrenda_tipoPrenda);

                            agregarPrenda_tipoPrendaId = sqLiteDatabase.replace("tipos_prenda", null, values);
                        }

                        // 4. Ingresar prenda.
                        Long agregarPrenda_id;
                        {
                            ContentValues values = new ContentValues();
                            values.put("id_tipo_prenda", agregarPrenda_tipoPrendaId);
                            values.put("foto", agregarPrenda_foto);

                            agregarPrenda_id = sqLiteDatabase.insert("prendas", null, values);
                        }

                        // 5. Agregar colores a la prenda.
                        for (int i = 0; i < agregarPrenda_coloresIds.size(); i++) {
                            Long id_color = agregarPrenda_coloresIds.get(i);

                            ContentValues values = new ContentValues();
                            values.put("id_prenda", agregarPrenda_id);
                            values.put("id_color", id_color);

                            sqLiteDatabase.insert("prenda_colores", null, values);
                        }

                        // 5. Agregar colores a la prenda.
                        for (int i = 0; i < agregarPrenda_etiquetasIds.size(); i++) {
                            Long id_etiqueta = agregarPrenda_etiquetasIds.get(i);

                            ContentValues values = new ContentValues();
                            values.put("id_prenda", agregarPrenda_id);
                            values.put("id_etiqueta", id_etiqueta);

                            sqLiteDatabase.insert("prenda_etiquetas", null, values);
                        }

                        Intent intent = new Intent(AgregarPrenda4Activity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addListenerToAgregarEtiqueta() {
        Button buttonAgregarEtiqueta = (Button) findViewById(R.id.agregarPrenda4__buttonAgregarEtiqueta);
        if (null == buttonAgregarEtiqueta)
            return;

        buttonAgregarEtiqueta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View layoutEtiqueta = getLayoutInflater().inflate(R.layout.element__agregar_prenda__etiqueta, null);

                LinearLayout etiquetas = (LinearLayout) findViewById((R.id.agregarPrenda4__etiquetas));

                if (etiquetas != null)
                    etiquetas.addView(layoutEtiqueta);
            }
        });
    }

    private List<EditText> getEditTextChilds(View view) {
        List<EditText> editTextChilds = new ArrayList<>();
        List<View> unvisited = new ArrayList<>();
        unvisited.add(view);

        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);

            if (child instanceof EditText) {
                editTextChilds.add((EditText) child);
                continue;
            }

            if (!(child instanceof ViewGroup))
                continue;

            ViewGroup group = (ViewGroup) child;
            final int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++)
                unvisited.add(group.getChildAt(i));
        }

        return editTextChilds;
    }
}
