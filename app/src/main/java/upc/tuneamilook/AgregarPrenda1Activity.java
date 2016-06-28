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
import android.widget.Checkable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AgregarPrenda1Activity extends AppCompatActivity {
    private RadioGroup radioGroupTipoPrenda;
    private List<RadioButton> radioButtonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_prenda_1);

        this.setActivityTitle("Agregar prenda");
        this.addListenerToContinuar();

        this.addRadioButtonListenerToTipoDePrendas();
    }

    private void addRadioButtonListenerToTipoDePrendas() {
        radioGroupTipoPrenda = (RadioGroup) findViewById(R.id.agregarPrenda1__radioGroupTipoPrenda);
        radioButtonList = getRadioButtonChilds(radioGroupTipoPrenda);

        try {
            for (int i = 0; i < radioButtonList.size(); i++) {
                RadioButton radioButton = radioButtonList.get(i);
                radioButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        for (int j = 0; j < radioButtonList.size(); j++) {
                            RadioButton r = radioButtonList.get(j);
                            r.setChecked(false);
                        }

                        ((Checkable) view).setChecked(true);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<RadioButton> getRadioButtonChilds(View view) {
        List<RadioButton> radioButtonChilds = new ArrayList<>();
        List<View> unvisited = new ArrayList<>();
        unvisited.add(view);

        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);

            if (child instanceof RadioButton) {
                radioButtonChilds.add((RadioButton) child);
                continue;
            }

            if (!(child instanceof ViewGroup))
                continue;

            ViewGroup group = (ViewGroup) child;
            final int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++)
                unvisited.add(group.getChildAt(i));
        }

        return radioButtonChilds;
    }

    private void setActivityTitle(String title) {
        TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        if (null != activityTitle)
            activityTitle.setText(title);
    }

    private void addListenerToContinuar() {
        Button buttonContinuar = (Button) findViewById(R.id.agregarPrenda1__buttonContinuar);
        if (null == buttonContinuar)
            return;

        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    RadioButton radioButtonTipoPrenda = null;

                    for (int j = 0; j < radioButtonList.size(); j++) {
                        RadioButton r = radioButtonList.get(j);
                        if (r.isChecked()) {
                            radioButtonTipoPrenda = r;
                            break;
                        }
                    }

                    if (null == radioButtonTipoPrenda) {
                        new AlertDialog.Builder(AgregarPrenda1Activity.this)
                                .setTitle("Espera")
                                .setMessage("Debes seleccionar un tipo de prenda.")
                                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continuar.
                                    }
                                })
                                .show();

                        return;
                    }

                    String tipoPrenda = radioButtonTipoPrenda.getText().toString();

                    Intent intent = new Intent(AgregarPrenda1Activity.this, AgregarPrenda2Activity.class);
                    intent.putExtra("agregarPrenda_tipoPrenda", tipoPrenda);

                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
