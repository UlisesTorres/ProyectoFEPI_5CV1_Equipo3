package com.example.myapplication.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.InfraccionPendiente
import com.example.myapplication.network.RetrofitSecureClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

class SyncWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.getDatabase(applicationContext)
        val dao = database.infraccionDao()
        val pendientes = dao.obtenerTodas()

        if (pendientes.isEmpty()) return Result.success()

        for (infraccion in pendientes) {
            val exito = enviarAlServidor(infraccion)
            if (exito) {
                dao.eliminar(infraccion)
                Log.d("SyncWorker", "Infracción ${infraccion.folio} sincronizada y eliminada localmente")
            }
        }

        return Result.success()
    }

    private suspend fun enviarAlServidor(inf: InfraccionPendiente): Boolean {
        return try {
            // 1. Crear la infracción (JSON)
            val jsonString = """
                {
                  "data": {
                    "folio": "${inf.folio}",
                    "placa_vehiculo": "${inf.placa}",
                    "ubicacion_infraccion": "${inf.ubicacion}",
                    "fecha_infraccion": "${inf.fecha}"
                  }
                }
            """.trimIndent()

            val body = jsonString.toRequestBody("application/json".toMediaTypeOrNull())
            val response = RetrofitSecureClient.infraccionApiService.crearInfraccion(body).execute()

            if (response.isSuccessful) {
                val jsonRes = JSONObject(response.body()!!.string())
                val infraccionId = jsonRes.getJSONObject("data").getInt("id")

                // 2. Subir firma
                val archivoFirma = File(inf.rutaFirma)
                if (archivoFirma.exists()) {
                    subirArchivoSincronizado(infraccionId, archivoFirma, "firma_infractor")
                }

                // 3. Subir fotos
                val rutas = inf.rutasFotos.split(",").filter { it.isNotBlank() }
                for (ruta in rutas) {
                    val archivoFoto = File(ruta)
                    if (archivoFoto.exists()) {
                        subirArchivoSincronizado(infraccionId, archivoFoto, "evidencia_infraccion")
                    }
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error sincronizando: ${e.message}")
            false
        }
    }

    private fun subirArchivoSincronizado(id: Int, archivo: File, field: String) {
        try {
            val filePart = MultipartBody.Part.createFormData(
                "files",
                archivo.name,
                archivo.asRequestBody(
                    if (field == "firma_infractor") "image/png".toMediaTypeOrNull()
                    else "image/jpeg".toMediaTypeOrNull()
                )
            )

            RetrofitSecureClient.uploadApiService.subirArchivo(
                filePart,
                "api::infraccion.infraccion".toRequestBody(),
                id.toString().toRequestBody(),
                field.toRequestBody()
            ).execute() // .execute() es síncrono, ideal para el Worker
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error subiendo archivo $field: ${e.message}")
        }
    }
}
