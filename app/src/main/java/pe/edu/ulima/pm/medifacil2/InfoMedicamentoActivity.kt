package pe.edu.ulima.pm.medifacil2

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pe.edu.ulima.pm.medifacil2.models.beans.Medicamentos
import pe.edu.ulima.pm.medifacil2.models.managers.PredefinidasManager
import java.util.ArrayList

class InfoMedicamentoActivity: AppCompatActivity() {

    var tvNombre: TextView? = null
    var tvDesc: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.infomedicamento)

        tvNombre = findViewById(R.id.tv_im_nombreMedicamento)
        tvDesc = findViewById(R.id.tv_im_descripcion)

        val idMed = intent.getStringExtra("idMedicamento")
        //Toast.makeText(this, prueba, Toast.LENGTH_SHORT).show()
        PredefinidasManager.getInstance().getMedicamentosRoomPorId(this, idMed!!.toInt(), { med: Medicamentos ->
            tvNombre!!.text = med.nombre
            tvDesc!!.text = med.desc
        })
    }

}