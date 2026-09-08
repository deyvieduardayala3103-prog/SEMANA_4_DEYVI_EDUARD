package com.example.consultaproductosrest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("products")
    Call<ProductoResponse> getProductos();

    @POST("products/add")
    Call<Producto> agregarProducto(@Body Producto producto);
}
