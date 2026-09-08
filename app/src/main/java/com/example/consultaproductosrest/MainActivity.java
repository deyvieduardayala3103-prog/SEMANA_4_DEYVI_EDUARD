package com.example.consultaproductosrest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerProductos;
    private ProgressBar progressBar;
    private TextView txtError;
    private MaterialButton btnReintentar;
    private MaterialButton btnAgregarProducto;

    private ProductoAdapter adapter;
    private final List<Producto> listaProductos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerProductos = findViewById(R.id.recyclerProductos);
        progressBar = findViewById(R.id.progressBar);
        txtError = findViewById(R.id.txtError);
        btnReintentar = findViewById(R.id.btnReintentar);
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto);

        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProductos.setHasFixedSize(true);

        adapter = new ProductoAdapter(listaProductos);
        recyclerProductos.setAdapter(adapter);

        btnReintentar.setOnClickListener(v -> consultarProductos());
        btnAgregarProducto.setOnClickListener(v -> mostrarFormularioAgregar());

        consultarProductos();
    }

    private ApiService obtenerApiService() {
        return RetrofitClient
                .getClient()
                .create(ApiService.class);
    }

    private void consultarProductos() {

        progressBar.setVisibility(View.VISIBLE);
        txtError.setVisibility(View.GONE);
        btnReintentar.setVisibility(View.GONE);

        obtenerApiService()
                .getProductos()
                .enqueue(new Callback<ProductoResponse>() {

                    @Override
                    public void onResponse(
                            Call<ProductoResponse> call,
                            Response<ProductoResponse> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().getProducts() != null) {

                            listaProductos.clear();
                            listaProductos.addAll(response.body().getProducts());

                            adapter.notifyDataSetChanged();

                            recyclerProductos.setVisibility(View.VISIBLE);
                            txtError.setVisibility(View.GONE);
                            btnReintentar.setVisibility(View.GONE);

                        } else {
                            mostrarError("No se pudieron obtener los productos.");
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ProductoResponse> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        String detalle = t.getMessage() == null
                                ? "Revise su conexión a Internet."
                                : t.getMessage();

                        mostrarError("Error de conexión:\n" + detalle);
                    }
                });
    }

    private void mostrarFormularioAgregar() {

        View vista = LayoutInflater.from(this)
                .inflate(R.layout.dialog_agregar_producto, null);

        EditText edtNombre = vista.findViewById(R.id.edtNombre);
        EditText edtPrecio = vista.findViewById(R.id.edtPrecio);
        EditText edtCategoria = vista.findViewById(R.id.edtCategoria);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Agregar producto")
                .setView(vista)
                .setPositiveButton("AGREGAR", null)
                .setNegativeButton("CANCELAR", (d, which) -> d.dismiss())
                .create();

        dialog.setOnShowListener(d -> {

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {

                        String nombre = edtNombre.getText()
                                .toString()
                                .trim();

                        String precioTexto = edtPrecio.getText()
                                .toString()
                                .trim()
                                .replace(",", ".");

                        String categoria = edtCategoria.getText()
                                .toString()
                                .trim();

                        if (nombre.isEmpty()) {
                            edtNombre.setError("Ingrese el nombre");
                            edtNombre.requestFocus();
                            return;
                        }

                        if (precioTexto.isEmpty()) {
                            edtPrecio.setError("Ingrese el precio");
                            edtPrecio.requestFocus();
                            return;
                        }

                        if (categoria.isEmpty()) {
                            edtCategoria.setError("Ingrese la categoría");
                            edtCategoria.requestFocus();
                            return;
                        }

                        double precio;

                        try {
                            precio = Double.parseDouble(precioTexto);
                        } catch (NumberFormatException e) {
                            edtPrecio.setError("Ingrese un precio válido");
                            edtPrecio.requestFocus();
                            return;
                        }

                        if (precio <= 0) {
                            edtPrecio.setError("El precio debe ser mayor que 0");
                            edtPrecio.requestFocus();
                            return;
                        }

                        Producto nuevoProducto =
                                new Producto(nombre, precio, categoria);

                        agregarProducto(nuevoProducto, dialog);
                    });
        });

        dialog.show();
    }

    private void agregarProducto(
            Producto producto,
            AlertDialog dialog) {

        progressBar.setVisibility(View.VISIBLE);
        btnAgregarProducto.setEnabled(false);

        obtenerApiService()
                .agregarProducto(producto)
                .enqueue(new Callback<Producto>() {

                    @Override
                    public void onResponse(
                            Call<Producto> call,
                            Response<Producto> response) {

                        progressBar.setVisibility(View.GONE);
                        btnAgregarProducto.setEnabled(true);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            Producto productoCreado = response.body();

                            listaProductos.add(0, productoCreado);
                            adapter.notifyItemInserted(0);

                            recyclerProductos.setVisibility(View.VISIBLE);
                            recyclerProductos.scrollToPosition(0);

                            dialog.dismiss();

                            Toast.makeText(
                                    MainActivity.this,
                                    "Producto agregado correctamente",
                                    Toast.LENGTH_SHORT
                            ).show();

                        } else {

                            Toast.makeText(
                                    MainActivity.this,
                                    "No se pudo agregar el producto",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Producto> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);
                        btnAgregarProducto.setEnabled(true);

                        String detalle = t.getMessage() == null
                                ? "Error de conexión"
                                : t.getMessage();

                        Toast.makeText(
                                MainActivity.this,
                                "Error: " + detalle,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void mostrarError(String mensaje) {

        recyclerProductos.setVisibility(View.GONE);

        txtError.setText(mensaje);
        txtError.setVisibility(View.VISIBLE);

        btnReintentar.setVisibility(View.VISIBLE);
    }
}
