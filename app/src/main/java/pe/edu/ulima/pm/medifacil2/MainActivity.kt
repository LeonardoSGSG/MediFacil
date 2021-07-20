package pe.edu.ulima.pm.medifacil2

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import pe.edu.ulima.pm.medifacil2.databinding.ActivityMainBinding
import pe.edu.ulima.pm.medifacil2.fragments.AnadirMedicamentosFragment
import pe.edu.ulima.pm.medifacil2.fragments.MedicamentosFragment

class MainActivity : AppCompatActivity() {

    /*
     *
     * Integrantes:
     * Diego Antonio Esquivel Patiño -> 20170532
     * Leonardo Sipion Guillen -> 20171484
     * Fabricio Sotelo Parra -> 20171497
     *
     *
     */

    //iniciamos las variables del activity
    var fragments : ArrayList<Fragment> = ArrayList()
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Agregamos los fragments al array de fragments del activity
        fragments.add(MedicamentosFragment())
        fragments.add(AnadirMedicamentosFragment())
        //encontramos el boton de navegacion
        bottomNavView = findViewById(R.id.bn_amain_bottomNav)
        //asignamos el listener al boton de navegacion
        bottomNavView.setOnItemSelectedListener {
            //iniciamos la transaccion del fragment manager
            val ft = supportFragmentManager.beginTransaction()
            when(it.itemId){
                //cuando se seleccione el medicinas, se carga el fragment de medicamentos guardados
                R.id.navigation_medi -> {
                    ft.replace(R.id.fl_amain_frameLayout, fragments[0])
                    ft.commit()
                    true
                }
                //cuando se seleccione el nuevo, se carga el fragment de añadir medicamentos
                R.id.navigation_nuevo -> {
                    ft.replace(R.id.fl_amain_frameLayout, fragments[1])
                    ft.commit()
                    true
                }
                else -> false
            }
        }
        //definimos el fragment de medicamentos como el default
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.fl_amain_frameLayout, fragments[0])
        ft.commit()
    }

    // funcion cuando se resuma el activity
    override fun onResume() {
        super.onResume()
        //verificar si hubo un cambio en la tabla del room con shared preferences
        var sp = getSharedPreferences("AppData", Context.MODE_PRIVATE).getInt("cambio",0)
        //si hubo cambio, se retorna a la normalidad el shared preferences y se recrea el fragment
        if(sp != 0){
            var ed = getSharedPreferences("AppData", Context.MODE_PRIVATE).edit()
            ed.putInt("cambio", 0); ed.apply()
            recreate()
        }
    }

}