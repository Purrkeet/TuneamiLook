package upc.tuneamilook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.setActivityTitle("Prendas");
        this.addListenerToAgregarPrenda();
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
}
