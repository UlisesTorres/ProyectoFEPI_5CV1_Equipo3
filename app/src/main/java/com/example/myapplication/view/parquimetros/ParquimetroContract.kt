package com.example.myapplication.view.parquimetros

interface ParquimetroContract {

    /**
     * Define los métodos que la Vista (ParquimetroActivity) debe implementar.
     */
    interface View {
        fun navegarAStatus()
        fun navegarAConfiguracion()
        fun navegarAReporte()
        fun mostrarMensajeError(mensaje: String)
    }

    /**
     * Define los métodos que el Presenter (ParquimetroPresenter) debe implementar.
     */
    interface Presenter {
        fun alHacerClickConsultar()
        fun alHacerClickConfiguracion()

        // --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
        // Añade la definición del método que faltaba.
        fun alHacerClickReporte()

        fun destruir()
    }
}
