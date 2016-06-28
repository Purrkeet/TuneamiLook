package upc.tuneamilook.entities;

import java.util.ArrayList;
import java.util.List;

public class Prenda {
    public Long id;
    public String foto;

    public TipoPrenda tipoPrenda;
    public List<Color> colores = new ArrayList<>();
    public List<Etiqueta> etiquetas = new ArrayList<>();
}
