package upc.tuneamilook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class AgregarPrenda4Activity extends AppCompatActivity {

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

                    // TODO: Agregar la prenda a la base de datos.


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
