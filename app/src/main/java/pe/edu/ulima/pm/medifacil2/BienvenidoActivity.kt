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

        if(!checkIfFirstTime()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else{
            findViewById<Button>(R.id.bt_b_continuar).setOnClickListener {
                val sp = getSharedPreferences("AppData", Context.MODE_PRIVATE).edit()
                sp.putBoolean("FirstTime", false); sp.apply()
                startActivity(Intent(this, MainActivity::class.java)); finish()
            }
        }

    }

    private fun checkIfFirstTime(): Boolean{
        return getSharedPreferences("AppData", Context.MODE_PRIVATE).getBoolean("FirstTime", true)
    }

}