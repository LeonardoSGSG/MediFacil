package pe.edu.ulima.pm.medifacil2

import android.os.Bundle
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

    var fragments : ArrayList<Fragment> = ArrayList()
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragments.add(MedicamentosFragment())
        fragments.add(AnadirMedicamentosFragment())
        bottomNavView = findViewById(R.id.bn_amain_bottomNav)
        bottomNavView.setOnItemSelectedListener {
            val ft = supportFragmentManager.beginTransaction()
            when(it.itemId){
                R.id.navigation_medi -> {
                    ft.replace(R.id.fl_amain_frameLayout, fragments[0])
                    ft.commit()
                    true
                }
                R.id.navigation_nuevo -> {
                    ft.replace(R.id.fl_amain_frameLayout, fragments[1])
                    ft.commit()
                    true
                }
                else -> false
            }
        }
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.fl_amain_frameLayout, fragments[0])
        ft.commit()
    }

}