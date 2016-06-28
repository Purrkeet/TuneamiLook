package upc.tuneamilook.entities;

import java.util.ArrayList;
import java.util.List;

public class Prenda {
    public Long id;
    public String foto;

    public TipoPrenda tipoPrenda;
    public List<Color> colores = new ArrayList<>();
    public List<Etiqueta> etiquetas = new ArrayList<>();

    public List<String> colores() {
        List<String> c = new ArrayList<>();

        for (int i = 0; i < colores.size(); i++)
            c.add(colores.get(i).hexadecimal);

        return c;
    }

    public List<String> etiquetas() {
        List<String> e = new ArrayList<>();

        for (int i = 0; i < etiquetas.size(); i++)
            e.add(etiquetas.get(i).nombre);

        return e;
    }
}
