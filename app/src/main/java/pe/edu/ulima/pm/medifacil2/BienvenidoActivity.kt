package pe.edu.ulima.pm.medifacil2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class BienvenidoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bienvenido)
        //se chequea si es la primera vez entrando a la app
        if(!checkIfFirstTime()) {
            //si es que no es primera vez, se pinta el main activity de frente
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else{
            //si es primera vez, se quedará en este activity bienvenido hasta presionar el boton
            //definimos el listener del boton
            findViewById<Button>(R.id.bt_b_continuar).setOnClickListener {
                //cambia el valor del shared preferences para saltarse esta activity e inicia el main activity
                val sp = getSharedPreferences("AppData", Context.MODE_PRIVATE).edit()
                sp.putBoolean("FirstTime", false); sp.apply()
                startActivity(Intent(this, MainActivity::class.java)); finish()
            }
        }

    }

    private fun checkIfFirstTime(): Boolean{
        //retornamos el valor del shared preferences
        return getSharedPreferences("AppData", Context.MODE_PRIVATE).getBoolean("FirstTime", true)
    }

}