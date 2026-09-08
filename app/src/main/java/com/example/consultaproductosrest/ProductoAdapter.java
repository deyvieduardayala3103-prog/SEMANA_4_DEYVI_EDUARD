package com.example.consultaproductosrest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.Locale;

public class ProductoAdapter
        extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private final List<Producto> listaProductos;

    public ProductoAdapter(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);

        return new ProductoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ProductoViewHolder holder,
            int position) {

        Producto producto = listaProductos.get(position);

        String nombre = producto.getTitle() == null
                ? "Producto sin nombre"
                : producto.getTitle();

        String categoriaTraducida =
                traducirCategoria(producto.getCategory());

        holder.txtNombreProducto.setText(nombre);
        holder.chipCategoria.setText(categoriaTraducida);

        holder.txtPrecio.setText(
                String.format(
                        Locale.getDefault(),
                        "S/ %.2f",
                        producto.getPrice()
                )
        );

        holder.txtIdProducto.setText(
                "ID: " + producto.getId()
        );

        View.OnClickListener detalleListener = v ->
                mostrarDetalle(
                        holder,
                        producto,
                        categoriaTraducida
                );

        holder.cardProducto.setOnClickListener(detalleListener);
        holder.btnVerDetalle.setOnClickListener(detalleListener);
    }

    private void mostrarDetalle(
            ProductoViewHolder holder,
            Producto producto,
            String categoria) {

        String nombre = producto.getTitle() == null
                ? "Producto sin nombre"
                : producto.getTitle();

        String mensaje =
                "ID: " + producto.getId()
                        + "\n\nProducto: " + nombre
                        + "\n\nCategoría: " + categoria
                        + "\n\nPrecio: "
                        + String.format(
                        Locale.getDefault(),
                        "S/ %.2f",
                        producto.getPrice()
                );

        new AlertDialog.Builder(holder.itemView.getContext())
                .setTitle("Detalle del producto")
                .setMessage(mensaje)
                .setPositiveButton("ACEPTAR", null)
                .show();
    }

    private String traducirCategoria(String categoria) {

        if (categoria == null || categoria.trim().isEmpty()) {
            return "Sin categoría";
        }

        switch (categoria.toLowerCase(Locale.ROOT).trim()) {

            case "beauty":
                return "Belleza";

            case "fragrances":
                return "Perfumes";

            case "furniture":
                return "Muebles";

            case "groceries":
                return "Abarrotes";

            case "home-decoration":
                return "Decoración";

            case "kitchen-accessories":
                return "Accesorios de cocina";

            case "laptops":
                return "Laptops";

            case "mens-shirts":
                return "Camisas para hombre";

            case "mens-shoes":
                return "Calzado para hombre";

            case "mens-watches":
                return "Relojes para hombre";

            case "mobile-accessories":
                return "Accesorios para celulares";

            case "motorcycle":
                return "Motocicletas";

            case "skin-care":
                return "Cuidado de la piel";

            case "smartphones":
                return "Celulares";

            case "sports-accessories":
                return "Accesorios deportivos";

            case "sunglasses":
                return "Lentes de sol";

            case "tablets":
                return "Tablets";

            case "tops":
                return "Ropa";

            case "vehicle":
                return "Vehículos";

            case "womens-bags":
                return "Bolsos para mujer";

            case "womens-dresses":
                return "Vestidos para mujer";

            case "womens-jewellery":
                return "Joyería";

            case "womens-shoes":
                return "Calzado para mujer";

            case "womens-watches":
                return "Relojes para mujer";

            default:
                return categoria;
        }
    }

    @Override
    public int getItemCount() {
        return listaProductos == null ? 0 : listaProductos.size();
    }

    public static class ProductoViewHolder
            extends RecyclerView.ViewHolder {

        MaterialCardView cardProducto;
        TextView txtNombreProducto;
        TextView txtPrecio;
        TextView txtIdProducto;
        Chip chipCategoria;
        MaterialButton btnVerDetalle;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);

            cardProducto = itemView.findViewById(R.id.cardProducto);
            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            chipCategoria = itemView.findViewById(R.id.chipCategoria);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            txtIdProducto = itemView.findViewById(R.id.txtIdProducto);
            btnVerDetalle = itemView.findViewById(R.id.btnVerDetalle);
        }
    }
}
