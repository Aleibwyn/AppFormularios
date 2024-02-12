package pe.edu.idat.appformularios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Toast
import pe.edu.idat.appformularios.comunes.AppMensaje
import pe.edu.idat.appformularios.comunes.MiApp
import pe.edu.idat.appformularios.comunes.TipoMensaje
import pe.edu.idat.appformularios.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity(), OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityRegistroBinding
    private var estadoCivil = ""
    private val listarPreferencias = ArrayList<String>()
    private val listarPersonas = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrarUsuario.setOnClickListener(this)
        binding.btnListarUsuarios.setOnClickListener(this)

        //Integración de la lista
        ArrayAdapter.createFromResource(
            MiApp.instance,
            R.array.estado_civil,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnEstadoCivil.adapter = adapter
        }
        binding.spnEstadoCivil.onItemSelectedListener = this

    }

    override fun onClick(vista: View) {
        if (vista is CheckBox) {
            agregarQuitarPreferenciaSeleccionada(vista)
        } else {
            when (vista.id) {
                R.id.btnRegistrarUsuario -> {
                    registrarPersona()
                }

                R.id.btnListarUsuarios -> {
                    startActivity(
                        Intent(
                            applicationContext,
                            ListadoActivity::class.java
                        ).apply { putExtra("listarPersonas", listarPersonas) })

                }
            }
        }
    }

    private fun agregarQuitarPreferenciaSeleccionada(vista: CheckBox) {

        if (vista.isChecked) {
            when (vista.id) {
                R.id.cbDeportes -> listarPreferencias.add(vista.text.toString())
                R.id.cbMusica -> listarPreferencias.add(vista.text.toString())
                R.id.cbOtros -> listarPreferencias.add(vista.text.toString())
            }
        } else {
            when (vista.id) {
                R.id.cbDeportes -> listarPreferencias.remove(vista.text.toString())
                R.id.cbMusica -> listarPreferencias.remove(vista.text.toString())
                R.id.cbOtros -> listarPreferencias.remove(vista.text.toString())
            }
        }
    }


    fun registrarPersona() {
        if (validarFormulario()) {
            val infoPersona = binding.etNombre.text.toString() + " " +
                    binding.etApellido.text.toString() + " " +
                    obtenerGeneroSeleccionado() + " " +
                    listarPreferencias.toString() + " " +
                    binding.swNotificar.isChecked
            listarPersonas.add(infoPersona)
            AppMensaje.enviarMensaje(
                binding.root,
                getString(R.string.mensajeRegistroCorrecto),
                TipoMensaje.SUCCESSFULL
            )
            setearControles()

        }
        //Toast.makeText(applicationContext, "Click en Registro de persona", Toast.LENGTH_LONG).show()
    }

    fun setearControles() {
        listarPreferencias.clear()
        binding.etNombre.setText("")
        binding.etApellido.setText("")
        binding.swNotificar.isChecked = false
        binding.cbDeportes.isChecked = false
        binding.cbMusica.isChecked = false
        binding.cbOtros.isChecked = false
        binding.rgGenero.clearCheck()
        binding.spnEstadoCivil.setSelection(0)
    }

    fun validarNombreApellido(): Boolean {
        var respuesta = true
        if (binding.etNombre.text.toString().trim().isEmpty()) {
            binding.etNombre.isFocusableInTouchMode = true
            binding.etNombre.requestFocus()
            respuesta = false
        } else if (binding.etApellido.text.toString().trim().isEmpty()) {
            binding.etApellido.isFocusableInTouchMode = true
            binding.etApellido.requestFocus()
            respuesta = false
        }
        return respuesta
    }

    fun validarGenero(): Boolean {
        var respuesta = true
        if (binding.rgGenero.checkedRadioButtonId == -1) {
            respuesta = false
        }
        return respuesta
    }


    fun validarPreferencias(): Boolean {
        var respuesta = false
        if (binding.cbDeportes.isChecked
            || binding.cbMusica.isChecked || binding.cbOtros.isChecked
        ) {
            respuesta = true
        }
        return respuesta
    }

    fun validarEstadoCivil(): Boolean {
        return estadoCivil != ""

    }

    fun validarFormulario(): Boolean {
        var respuesta = false
        if (!validarNombreApellido()) {
            AppMensaje.enviarMensaje(binding.root, "Ingrese nombre y apellido", TipoMensaje.ERROR)
        } else if (!validarGenero()) {
            AppMensaje.enviarMensaje(binding.root, "Seleccione su género", TipoMensaje.ERROR)
        } else if (!validarPreferencias()) {
            AppMensaje.enviarMensaje(
                binding.root,
                "Seleccione al menos una preferencia",
                TipoMensaje.ERROR
            )
        } else if (!validarEstadoCivil()) {
            AppMensaje.enviarMensaje(binding.root, "Seleccione su estado civil", TipoMensaje.ERROR)
        } else {
            respuesta = true
        }
        return respuesta
    }

    fun obtenerGeneroSeleccionado(): String {
        var genero = ""
        when (binding.rgGenero.checkedRadioButtonId) {
            R.id.rbMasculino -> {
                genero = binding.rbMasculino.text.toString()
            }

            R.id.rbFemenino -> {
                genero = binding.rbFemenino.text.toString()
            }
        }
        return genero
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        estadoCivil = if (position > 0) {
            parent!!.getItemAtPosition(position).toString()
        } else ""
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}