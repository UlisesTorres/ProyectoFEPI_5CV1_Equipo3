import com.example.myapplication.view.transito.TransitoContract

class TransitoPresenter(private val view: TransitoContract.View) : TransitoContract.Presenter {
    override fun clickGenerarInfraccion() = view.navegarAGenerarInfraccion()
    override fun clickConfiguracion() = view.navegarAConfiguracion()
    override fun clickGenerarArrastre() = view.navegarAGenerarArrastre()
    override fun clickHistorial() = view.navegarAHistorial()
    override fun clickParquimetros() = view.navegarAParquimetros()

}