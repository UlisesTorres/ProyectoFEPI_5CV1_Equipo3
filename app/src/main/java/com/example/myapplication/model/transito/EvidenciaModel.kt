package com.example.myapplication.model.transito

import android.content.Context
import android.location.Geocoder
import com.example.myapplication.network.RetrofitSecureClient
import org.maplibre.android.geometry.LatLng
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import com.example.myapplication.model.transito.TipoInfraccionDTO
import com.example.myapplication.model.transito.TipoInfraccionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EvidenciaModel(private val context: Context) {

    // Mover aquí la lógica de getMapFilePath
    fun prepararMapaLocal(): String {
        val fileName = "mexico-city.mbtiles"
        val file = File(context.filesDir, fileName)
        if (!file.exists() || file.length() == 0L) {
            context.assets.open(fileName).use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                    output.flush()
                }
            }
        }
        return file.absolutePath
    }

    // Lógica del Geocoder
    fun obtenerDireccion(latLng: LatLng): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val direcciones = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!direcciones.isNullOrEmpty()) {
                val d = direcciones[0]
                "${d.thoroughfare ?: "Calle desconocida"} ${d.subThoroughfare ?: ""}, ${d.subLocality ?: ""}"
            } else "Coordenadas: ${latLng.latitude}, ${latLng.longitude}"
        } catch (e: Exception) {
            "Sin conexión: ${latLng.latitude}, ${latLng.longitude}"
        }
    }


    fun obtenerCatalogoInfracciones(
        // Cambia el onSuccess para que devuelva la lista completa de objetos
        onSuccess: (List<TipoInfraccionDTO>) -> Unit,
        // Cambia el onError para que devuelva un mensaje de error detallado
        onError: (String) -> Unit
    ) {
        RetrofitSecureClient.infraccionApiService
            .obtenerTiposInfraccion()
            .enqueue(object : Callback<TipoInfraccionResponse> {
                override fun onResponse(
                    call: Call<TipoInfraccionResponse>,
                    response: Response<TipoInfraccionResponse>
                ) {
                    if (response.isSuccessful) {
                        // Pasamos la lista de DTOs directamente al presenter
                        onSuccess(response.body()?.data ?: emptyList())
                    } else {
                        // Si hay un error (403, 404, etc.), pasamos el código de error
                        onError("Error del servidor: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<TipoInfraccionResponse>, t: Throwable) {
                    // Si falla la conexión, pasamos el mensaje de la excepción
                    onError("Fallo de red: ${t.message}")
                }
            })
    }

}