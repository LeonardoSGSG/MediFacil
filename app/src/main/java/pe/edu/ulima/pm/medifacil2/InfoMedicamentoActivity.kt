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

    var tvNombre: TextView? = null
    var tvDesc: TextView? = null
    var ivMedicamento: ImageView? = null
    var btEliminar: Button? = null
    var tvPeriodico: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.infomedicamento)

        tvNombre = findViewById(R.id.tv_im_nombreMedicamento)
        tvDesc = findViewById(R.id.tv_im_descripcion)
        tvDesc!!.movementMethod = ScrollingMovementMethod()
        ivMedicamento = findViewById(R.id.iv_im_imagenMedicamento)
        btEliminar = findViewById(R.id.bt_im_eliminar)
        tvPeriodico = findViewById(R.id.tv_im_periodico)

        val idMed = intent.getStringExtra("idMedicamento")
        Toast.makeText(this, idMed, Toast.LENGTH_SHORT).show()
        PredefinidasManager.getInstance().getMedicamentosRoomPorId(this, idMed!!.toInt(), { med: Medicamentos ->
            tvNombre!!.text = med.nombre
            tvDesc!!.text = med.desc
            tvPeriodico!!.text = "Periodico (24h.): " + med.periodico
            runOnUiThread {
                if(med.imagen == "a"){
                    ivMedicamento!!.setImageResource(R.mipmap.ic_launcher)
                }else {
                    val takenImage = BitmapFactory.decodeFile(med.imagen)
                    ivMedicamento!!.setImageBitmap(takenImage)
                }
            }
        })
        btEliminar!!.setOnClickListener{
            PredefinidasManager.getInstance().deleteMed(this, idMed.toInt(), {num:Int ->
                var ed = getSharedPreferences("AppData", Context.MODE_PRIVATE).edit()
                ed.putInt("cambio", 1); ed.apply()
                this.finish()
            })
        }
    }

}