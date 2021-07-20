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
import pe.edu.ulima.pm.medifacil2.InfoMedicamentoActivity
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
        //identificamos el recyclerView del fragment
        rvPredefinidas = requireView().findViewById(R.id.rv_am_medicamentospredefinidos)
        //solicitamos la instancia del manager
        PredefinidasManager.getInstance().getPredefinidas(this)
        //asignamos la funcionalidad del boton otro medicamento para crear un medicamento personalizado
        requireView().findViewById<Button>(R.id.bt_am_otromedicamento).setOnClickListener {
            //llamamos al otro activity donde se crea un nuevo medicamento
            val intent = Intent(requireContext(), OtroMedicamentoActivity::class.java)
            startActivity(intent)
        }
    }
    //funcion onClick de cada elemento del recyclerView traidos de la api
    override fun onClick(nombre: String, desc: String, imagen: String) {
        //pasamos con un intent.putExtra el id, nombre e imagen al siguiente activity
        val intent = Intent(requireContext(), OtroMedicamentoActivity::class.java)
        intent.putExtra("nombreMedicamento", nombre)
        intent.putExtra("descMedicamento", desc)
        intent.putExtra("imagenUrlMedicamento", imagen)
        startActivity(intent)
    }
    //funcion a realizar cuando es exitosa la respuesta del api
    override fun onSuccess(predefinidas: ArrayList<Predefinidas>) {
        //corremos en un uiThread la generación del recyclerview con los datos de la api
        requireActivity().runOnUiThread {
            val rvPredefinidasAdapter = PredefinidasRVAdapter(predefinidas, this, requireContext())
            rvPredefinidas.layoutManager = LinearLayoutManager(requireContext())
            rvPredefinidas.adapter = rvPredefinidasAdapter
        }
    }
    //funcion en caso no resulta exitosa la peticion de internet
    override fun onError(msg: String) {
        Toast.makeText(context, "sin conexion a internet", Toast.LENGTH_SHORT).show()
    }

}