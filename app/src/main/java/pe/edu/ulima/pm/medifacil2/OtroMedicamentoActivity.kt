package pe.edu.ulima.pm.medifacil2

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pe.edu.ulima.pm.medifacil2.models.beans.Medicamentos
import pe.edu.ulima.pm.medifacil2.models.managers.PredefinidasManager

class OtroMedicamentoActivity: AppCompatActivity(){

    var etNombre : EditText? = null
    var etDesc : EditText? = null
    var btAñadir : Button? = null
    var btCancel : Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otromedicamento)

        etNombre = findViewById(R.id.et_om_nombre)
        etDesc = findViewById(R.id.et_om_descripcion)
        btAñadir = findViewById(R.id.bt_om_añadir)
        btCancel = findViewById(R.id.bt_om_cancelar)

        val nom = intent.getStringExtra("nombreMedicamento")
        val desc = intent.getStringExtra("descMedicamento")

        if(nom != null && desc != null){
            etNombre!!.setText(nom)
            etDesc!!.setText(desc)
        }

        btAñadir!!.setOnClickListener{

            if(!TextUtils.isEmpty(etNombre!!.text.toString()) && !TextUtils.isEmpty(etDesc!!.text.toString())){
                val med : ArrayList<Medicamentos> = ArrayList()
                med.add(Medicamentos(etNombre!!.text.toString(), etDesc!!.text.toString(),"a",0 ))
                Log.i("aaa", "aaa")
                PredefinidasManager.getInstance().saveMedicamentos(this, med,{num:Int ->


                    this.finish()
                } )
            }else{
                Toast.makeText(this, "faltan datos", Toast.LENGTH_SHORT).show()
            }

        }
        btCancel!!.setOnClickListener{
            this.finish()
        }
    }

}