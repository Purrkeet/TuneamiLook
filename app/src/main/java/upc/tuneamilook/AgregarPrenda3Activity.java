package upc.tuneamilook;

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
import android.widget.ImageView;

import java.io.IOException;

public class AgregarPrenda3Activity extends AppCompatActivity {
    private String photoAbsolutePath;
    Bitmap photoBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_prenda_3);

        Intent intent = getIntent();
        photoAbsolutePath = intent.getStringExtra("agregarPrenda2_photoAbsolutePath");

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

                Log.e("COLOR", colorHex);

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
