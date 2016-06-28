package upc.tuneamilook.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import upc.tuneamilook.R;
import upc.tuneamilook.entities.Prenda;

public class PrendasAdapter extends ArrayAdapter<Prenda> {
    public PrendasAdapter(Context context, ArrayList<Prenda> prendas) {
        super(context, 0, prendas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Prenda prenda = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.element__main__prenda, parent, false);
        }

        ImageView imageViewFoto = (ImageView) convertView.findViewById(R.id.element__main__prenda__imageViewFoto);
        TextView textViewTipoPrenda = (TextView) convertView.findViewById(R.id.element__main__prenda__textViewTipoPrenda);

        File fileFoto = new  File(prenda.foto);
        if(fileFoto.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(fileFoto.getAbsolutePath());
            imageViewFoto.setImageBitmap(myBitmap);
        }

        textViewTipoPrenda.setText(prenda.tipoPrenda.nombre);

        return convertView;
    }
}