package pe.edu.ulima.pm.medifacil2.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.ulima.pm.medifacil2.OtroMedicamentoActivity
import pe.edu.ulima.pm.medifacil2.R
import pe.edu.ulima.pm.medifacil2.adapters.OnPredefinidaItemClickListener
import pe.edu.ulima.pm.medifacil2.adapters.PredefinidasRVAdapter
import pe.edu.ulima.pm.medifacil2.models.beans.Predefinidas
import pe.edu.ulima.pm.medifacil2.models.managers.OnGetPredefinidasDone
import pe.edu.ulima.pm.medifacil2.models.managers.PredefinidasManager
import java.util.ArrayList

class AnadirMedicamentosFragment: Fragment(), OnPredefinidaItemClickListener, OnGetPredefinidasDone{

    private lateinit var rvPredefinidas: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.anadirmedicamentos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPredefinidas = requireView().findViewById(R.id.rv_am_medicamentospredefinidos)
        /*Descomentar esto para utilizar los servicios del api y traer las predefinidass
        PredefinidasManager.getInstance().getPredefinidas(this)
        */
        requireView().findViewById<Button>(R.id.bt_am_otromedicamento).setOnClickListener {
            val intent = Intent(requireContext(), OtroMedicamentoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(Id: String) {
        //Aca iria la lógica de pasar on un intent.putExtra el id para hacer la consulta en Room
        Toast.makeText(requireContext(), Id, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(predefinidas: ArrayList<Predefinidas>) {
        requireActivity().runOnUiThread {
            val rvPredefinidasAdapter = PredefinidasRVAdapter(predefinidas, this, requireContext())
            rvPredefinidas.layoutManager = LinearLayoutManager(requireContext())
            rvPredefinidas.adapter = rvPredefinidasAdapter
        }
    }

    override fun onError(msg: String) {
        TODO("Not yet implemented")
    }

}