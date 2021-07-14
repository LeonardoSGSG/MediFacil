package pe.edu.ulima.pm.medifacil2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.edu.ulima.pm.medifacil2.R
import pe.edu.ulima.pm.medifacil2.models.beans.Medicamentos
import java.util.ArrayList

interface OnMedicamentoItemClickListener {
    fun onClick(Id : String)
}

class MedicamentosRVAdapter: RecyclerView.Adapter<MedicamentosRVAdapter.MyViewHolder> {

    class MyViewHolder: RecyclerView.ViewHolder{
        var ivImagen: ImageView? = null
        var tvNombre: TextView? = null
        var tvId: TextView? = null

        constructor(view : View) : super(view) {
            ivImagen = view.findViewById(R.id.iv_mcard_imagen)
            tvNombre = view.findViewById(R.id.tv_mcard_nombre)
            tvId = view.findViewById(R.id.tv_mcard_id)
        }
    }

    private var medicamentos: ArrayList<Medicamentos>? = null
    private var listener : OnMedicamentoItemClickListener? = null
    private var context : Context? = null

    constructor(medicamentos : ArrayList<Medicamentos>,
                listener : OnMedicamentoItemClickListener,
                context : Context) : super(){
        this.medicamentos = medicamentos
        this.listener = listener
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medicamentocard, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val medicamento = medicamentos!![position]

        holder.tvNombre!!.text = medicamento.nombre
        holder.tvId!!.text = medicamento.id

        /*Glide.with(context!!).load(medicamento.imagen)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.ivImagen!!)*/

        holder.ivImagen!!.setImageResource(R.mipmap.ic_launcher)

        holder.itemView.setOnClickListener {
            listener!!.onClick(holder.tvId!!.text.toString())
        }
    }

    override fun getItemCount(): Int {
        return medicamentos!!.size
    }

}