package pe.edu.ulima.pm.medifacil2.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.ulima.pm.medifacil2.InfoMedicamentoActivity
import pe.edu.ulima.pm.medifacil2.OtroMedicamentoActivity
import pe.edu.ulima.pm.medifacil2.R
import pe.edu.ulima.pm.medifacil2.adapters.MedicamentosRVAdapter
import pe.edu.ulima.pm.medifacil2.adapters.OnMedicamentoItemClickListener
import pe.edu.ulima.pm.medifacil2.models.beans.Medicamentos
import java.util.ArrayList

class MedicamentosFragment: Fragment(), OnMedicamentoItemClickListener {

    private lateinit var rvMedicamentos: RecyclerView
    val listaMedicamentosPRUEBA: ArrayList<Medicamentos> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.medicamentos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMedicamentos = requireView().findViewById(R.id.rv_m_medicamentos)

        //PRUEBAAAAAAAAAA, borrarlo e implementar lo real
        listaMedicamentosPRUEBA.add(Medicamentos("Mitrozol", "El mitrozol es mitrozol", "a", "1"))
        listaMedicamentosPRUEBA.add(Medicamentos("Estazona", "El Estazona es Estazona", "a", "2"))
        listaMedicamentosPRUEBA.add(Medicamentos("Estepalo", "El Estepalo es Estepalo", "a", "3"))
        onSuccessPRUEBA(listaMedicamentosPRUEBA)
        /////////////////
    }

    //FUNCION DE PRUEBA, Borrar toda la funcion o lo que creas conveniente
    private fun onSuccessPRUEBA(listaPRUEBA: ArrayList<Medicamentos>){
        requireActivity().runOnUiThread {
            val rvMedicamentosAdapter = MedicamentosRVAdapter(listaPRUEBA, this, requireContext())
            rvMedicamentos.layoutManager = LinearLayoutManager(requireContext())
            rvMedicamentos.adapter = rvMedicamentosAdapter
        }
    }

    override fun onClick(Id: String) {
        //En la otra actividad se hara la consulta a room y se mostrara la info de medicamento con este Id que estamos
        //pasando
        val intent = Intent(requireContext(), InfoMedicamentoActivity::class.java)
        intent.putExtra("idMedicamento", Id)
        startActivity(intent)
    }

}