package upc.tuneamilook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AgregarPrenda3Activity extends AppCompatActivity {
    private Bitmap photoBitmap;
    private List<String> selectedColors = new ArrayList<>();
    private String photoAbsolutePath;

    private String agregarPrenda_tipoPrenda;
    private String agregarPrenda_foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_prenda_3);

        Intent intent = getIntent();
        agregarPrenda_tipoPrenda = intent.getStringExtra("agregarPrenda_tipoPrenda");
        agregarPrenda_foto = intent.getStringExtra("agregarPrenda_foto");

        photoAbsolutePath = intent.getStringExtra("agregarPrenda_foto");

        try {
            // Verificar que le orientazión de la imagen sea la correcta a la hora de imprimirla.
            photoBitmap = orientateBitmapCorrectly();

            ImageView imageViewFoto = (ImageView) findViewById(R.id.agregarPrenda3__imageViewFoto);
            if (imageViewFoto != null) {
                imageViewFoto.setImageBitmap(photoBitmap);
                imageViewFoto.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.addListenerToCapturarColor();
        this.addListenerToContinuar();
    }

    private void addListenerToContinuar() {
        Button buttonContinuar = (Button) findViewById(R.id.agregarPrenda3__buttonContinuar);
        if (null == buttonContinuar)
            return;

        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (selectedColors.size() == 0) {
                        new AlertDialog.Builder(AgregarPrenda3Activity.this)
                                .setTitle("Espera")
                                .setMessage("Debes tocar la imagen para agregar al menos un color.")
                                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continuar.
                                    }
                                })
                                .show();

                        return;
                    }

                    Intent intent = new Intent(AgregarPrenda3Activity.this, AgregarPrenda4Activity.class);
                    intent.putStringArrayListExtra("agregarPrenda_colores", (ArrayList<String>) selectedColors);

                    intent.putExtra("agregarPrenda_foto", agregarPrenda_foto);
                    intent.putExtra("agregarPrenda_tipoPrenda", agregarPrenda_tipoPrenda);

                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addListenerToCapturarColor() {
        ImageView imageViewFoto = (ImageView) findViewById(R.id.agregarPrenda3__imageViewFoto);
        if (null == imageViewFoto)
            return;

        imageViewFoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // Escalamiento del touch en la imagen.
                float eventX = motionEvent.getX();
                float eventY = motionEvent.getY();
                float[] eventXY = new float[]{eventX, eventY};

                Bitmap bitmap = photoBitmap;

                Matrix invertMatrix = new Matrix();
                ((ImageView) view).getImageMatrix().invert(invertMatrix);

                invertMatrix.mapPoints(eventXY);
                int x = (int) eventXY[0];
                int y = (int) eventXY[1];

                // Limit x, y range within bitmap.
                if (x < 0) {
                    x = 0;
                } else if (x > bitmap.getWidth() - 1) {
                    x = bitmap.getWidth() - 1;
                }

                if (y < 0) {
                    y = 0;
                } else if (y > bitmap.getHeight() - 1) {
                    y = bitmap.getHeight() - 1;
                }

                int colorInteger = bitmap.getPixel(x, y);

                int redValue = Color.red(colorInteger);
                int greenValue = Color.green(colorInteger);
                int blueValue = Color.blue(colorInteger);

                String redHex = Integer.toString(redValue, 16);
                if (redHex.length() == 1)
                    redHex = "0" + redHex;

                String greenHex = Integer.toString(greenValue, 16);
                if (greenHex.length() == 1)
                    greenHex = "0" + greenHex;

                String blueHex = Integer.toString(blueValue, 16);
                if (blueHex.length() == 1)
                    blueHex = "0" + blueHex;

                String colorHex = "#" + redHex + greenHex + blueHex;

                if (selectedColors.contains(colorHex))
                    return false;

                selectedColors.add(colorHex);

                View layoutColor = getLayoutInflater().inflate(R.layout.element__agregar_prenda__color, null);
                LinearLayout linearLayoutColor = (LinearLayout) layoutColor.findViewById((R.id.element__agregar_prenda__color__linearLayoutColor));
                linearLayoutColor.setBackgroundColor(colorInteger);
                LinearLayout linearLayoutColores = (LinearLayout) findViewById(R.id.agregarPrenda3__linearLayoutColores);
                linearLayoutColores.addView(layoutColor);

                return false;
            }
        });
    }

    private Bitmap orientateBitmapCorrectly() throws IOException {
        String file = photoAbsolutePath;
        Bitmap rotatedBitmap = null;

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(file, opts);
        ExifInterface exif = new ExifInterface(file);
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);

        return rotatedBitmap;
    }
}
