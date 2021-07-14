package pe.edu.ulima.pm.medifacil2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InfoMedicamentoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.infomedicamento)

        val prueba = intent.getStringExtra("idMedicamento")
        Toast.makeText(this, prueba, Toast.LENGTH_SHORT).show()

    }

}