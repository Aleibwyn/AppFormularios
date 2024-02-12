package pe.edu.idat.appformularios.comunes

import android.R.attr.textColor
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import pe.edu.idat.appformularios.R


object AppMensaje {
    fun enviarMensaje(vista: View, mensaje: String, tipoMensaje: TipoMensaje) {
        val snackBar = Snackbar.make(vista, mensaje, Snackbar.LENGTH_LONG)
        val snackBarView: View = snackBar.view
        if (tipoMensaje == TipoMensaje.ERROR) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(MiApp.instance, R.color.errorColor )
            )
        } else if (tipoMensaje == TipoMensaje.SUCCESSFULL) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(MiApp.instance, R.color.exitoColor )
            )
        }  else if (tipoMensaje == TipoMensaje.INFO) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(MiApp.instance, R.color.infoColor)
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(MiApp.instance, R.color.advertenciaColor)
            )
        }

        snackBar.setTextColor(Color.BLACK)
        snackBar.show()
    }
}