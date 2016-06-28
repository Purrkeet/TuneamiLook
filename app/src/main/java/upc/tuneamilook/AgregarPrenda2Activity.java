package upc.tuneamilook;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class AgregarPrenda2Activity extends AppCompatActivity {
    final private String galleryFolderName = "Tunea mi look";
    final private File baseGalleryPath = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), galleryFolderName);
    private String photoPath;
    private Uri photoUri;
    private File photo;

    private String agregarPrenda_tipoPrenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_prenda_2);

        agregarPrenda_tipoPrenda = getIntent().getStringExtra("agregarPrenda_tipoPrenda");

        this.setActivityTitle("Agregar prenda");
        this.addListenerToTomarFoto();
    }

    private void setActivityTitle(String title) {
        TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        if (null != activityTitle)
            activityTitle.setText(title);
    }

    private void addListenerToTomarFoto() {
        Button buttonTomarFoto = (Button) findViewById(R.id.agregarPrenda2__buttonTomarFoto);
        if (null == buttonTomarFoto)
            return;

        buttonTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoPath = "IMG_" + System.currentTimeMillis() + ".jpg";

                photo = new File(baseGalleryPath, photoPath);
                // Asegurarnos de que la carpeta donde guardaremos la imagen exista..
                photo.getParentFile().mkdirs();

                photoUri = Uri.fromFile(photo);

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                startActivityForResult(intent, 88);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 88 && resultCode == RESULT_OK) {
            this.getContentResolver().notifyChange(photoUri, null);

            Bitmap bitmap;
            try {
                // Agregar foto a la galería.
                MediaScannerConnection.scanFile(this,
                        new String[] { photo.getAbsolutePath() }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                // ...
                            }
                        });

                // Verificar que le orientazión de la imagen sea la correcta a la hora de imprimirla.
                bitmap = orientateBitmapCorrectly();

                ImageView imageViewFoto = (ImageView) findViewById(R.id.agregarPrenda2__imageViewFoto);
                if (imageViewFoto != null) {
                    imageViewFoto.setImageBitmap(bitmap);
                    imageViewFoto.setVisibility(View.VISIBLE);
                }

                TextView textViewDetalle = (TextView) findViewById(R.id.agregarPrenda2__textViewDetalle);
                if (textViewDetalle != null) {
                    textViewDetalle.setVisibility(View.GONE);
                }

                Intent intentToContinue = new Intent(AgregarPrenda2Activity.this, AgregarPrenda3Activity.class);
                intentToContinue.putExtra("agregarPrenda_foto", photo.getAbsolutePath());

                // Página anterior.
                intentToContinue.putExtra("agregarPrenda_tipoPrenda", agregarPrenda_tipoPrenda);

                startActivity(intentToContinue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap orientateBitmapCorrectly() throws IOException {
        String file = photo.getAbsolutePath();
        Bitmap rotatedBitmap = null;

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(file, opts);
        ExifInterface exif = new ExifInterface(file);
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

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
