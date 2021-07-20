package pe.edu.ulima.pm.medifacil2

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pe.edu.ulima.pm.medifacil2.models.beans.Medicamentos
import pe.edu.ulima.pm.medifacil2.models.managers.PredefinidasManager
import java.util.ArrayList

class InfoMedicamentoActivity: AppCompatActivity() {

    /*
     *
     * Integrantes:
     * Diego Antonio Esquivel Patiño -> 20170532
     * Leonardo Sipion Guillen -> 20171484
     * Fabricio Sotelo Parra -> 20171497
     *
     *
     */

    var tvNombre: TextView? = null
    var tvDesc: TextView? = null
    var ivMedicamento: ImageView? = null
    var btEliminar: Button? = null
    var tvPeriodico: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.infomedicamento)
        //recogemos elementos del xml
        tvNombre = findViewById(R.id.tv_im_nombreMedicamento)
        tvDesc = findViewById(R.id.tv_im_descripcion)
        tvDesc!!.movementMethod = ScrollingMovementMethod()
        ivMedicamento = findViewById(R.id.iv_im_imagenMedicamento)
        btEliminar = findViewById(R.id.bt_im_eliminar)
        tvPeriodico = findViewById(R.id.tv_im_periodico)
        //recogemos el dato del id
        val idMed = intent.getStringExtra("idMedicamento")

        //Toast.makeText(this, idMed, Toast.LENGTH_SHORT).show()
        //conseguimos la instancia del manager y traemos un medicamento especifico por id
        PredefinidasManager.getInstance().getMedicamentosRoomPorId(this, idMed!!.toInt(), { med: Medicamentos ->
            //llenamos los campos del xml con los datos guardados del medicamento por id
            tvNombre!!.text = med.nombre
            tvDesc!!.text = med.desc
            tvPeriodico!!.text = "Periodico (24h.): " + med.periodico
            //cargamos la imagen guardada por medicamento
            runOnUiThread {
                if(med.imagen == "a"){
                    ivMedicamento!!.setImageResource(R.mipmap.ic_launcher)
                }else {
                    val takenImage = BitmapFactory.decodeFile(med.imagen)
                    ivMedicamento!!.setImageBitmap(takenImage)
                }
            }
        })
        //boton para eliminar el medicamento de la lista
        btEliminar!!.setOnClickListener{
            //conseguimos la instancia del manager y eliminamos un medicamento por id
            PredefinidasManager.getInstance().deleteMed(this, idMed.toInt(), {num:Int ->
                //avisamos con un shares preference que se hizo un cambio en la tabla del room
                var ed = getSharedPreferences("AppData", Context.MODE_PRIVATE).edit()
                ed.putInt("cambio", 1); ed.apply()
                this.finish()
            })
        }
    }

}