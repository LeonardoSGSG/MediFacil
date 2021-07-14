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
import pe.edu.ulima.pm.medifacil2.models.beans.Predefinidas

interface OnPredefinidaItemClickListener {
    fun onClick(Id : String)
}

class PredefinidasRVAdapter: RecyclerView.Adapter<PredefinidasRVAdapter.MyViewHolder> {

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

    private var predefinidas : ArrayList<Predefinidas>? = null
    private var listener : OnPredefinidaItemClickListener? = null
    private var context : Context? = null

    constructor(predefinidas : ArrayList<Predefinidas>,
                listener : OnPredefinidaItemClickListener,
                context : Context) : super(){
        this.predefinidas = predefinidas
        this.listener = listener
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medicamentocard, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val predef = predefinidas!![position]

        holder.tvNombre!!.text = predef.nombre
        holder.tvId!!.text = predef.id

        Glide.with(context!!).load(predef.imagen)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.ivImagen!!)

        holder.itemView.setOnClickListener {
            listener!!.onClick(holder.tvId!!.text.toString())
        }
    }

    override fun getItemCount(): Int {
        return predefinidas!!.size
    }

}