package com.example.myapplication.model.transito

import android.content.Context
import android.location.Geocoder
import com.example.myapplication.model.licencia.LicenciaDTO
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
import com.example.myapplication.model.licencia.LicenciaResponse
import com.example.myapplication.model.vehiculo.VehiculoDTO
import com.example.myapplication.model.vehiculo.VehiculoResponse

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
        onSuccess: (List<TipoInfraccionDTO>) -> Unit,
        onError: (String) -> Unit
    ) {
        RetrofitSecureClient.tipoInfraccionApiService
            .obtenerTiposInfraccion()
            .enqueue(object : Callback<TipoInfraccionResponse> {

                override fun onResponse(
                    call: Call<TipoInfraccionResponse>,
                    response: Response<TipoInfraccionResponse>
                ) {
                    if (!response.isSuccessful) {
                        onError("Error ${response.code()} al cargar infracciones")
                        return
                    }

                    val lista = response.body()?.data

                    if (lista.isNullOrEmpty()) {
                        onError("No hay infracciones disponibles")
                        return
                    }

                    onSuccess(lista) // ya son TipoInfraccionDTO directos
                }

                override fun onFailure(call: Call<TipoInfraccionResponse>, t: Throwable) {
                    onError("Sin conexión al servidor")
                }
            })
    }

    fun consultarVehiculo(
        placa: String,
        onSuccess: (VehiculoDTO) -> Unit,
        onError: (String) -> Unit
    ) {
        RetrofitSecureClient.consultaApiService.consultarVehiculoPorPlaca(placa)
            .enqueue(object : Callback<VehiculoResponse> {
                override fun onResponse(call: Call<VehiculoResponse>, response: Response<VehiculoResponse>) {
                    if (response.isSuccessful) {
                        val vehiculoDTO = response.body()?.data?.firstOrNull()

                        if (vehiculoDTO != null) {
                            onSuccess(vehiculoDTO)
                        } else {
                            onError("Placa no encontrada.")
                        }
                    } else {
                        onError("Error del servidor: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<VehiculoResponse>, t: Throwable) {
                    onError("Fallo de red: ${t.message}")
                }
            })
    }

    fun consultarLicencia(
        numeroLicencia: String,
        onSuccess: (LicenciaDTO) -> Unit,
        onError: (String) -> Unit
    ){
        RetrofitSecureClient.consultaApiService.consultarLicencia(numeroLicencia)
            .enqueue(object : Callback<LicenciaResponse> {
                override fun onResponse(call: Call<LicenciaResponse>, response: Response<LicenciaResponse>) {
                    if (response.isSuccessful) {
                        val licenciaDTO = response.body()?.data?.firstOrNull()

                        if (licenciaDTO != null) {
                            onSuccess(licenciaDTO)
                        } else {
                            onError("Licencia no encontrada.")
                        }
                    } else {
                        onError("Error del servidor: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<LicenciaResponse>, t: Throwable) {
                    onError("Fallo de red: ${t.message}")
                }
            })
    }

}