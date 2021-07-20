package pe.edu.ulima.pm.medifacil2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text
import pe.edu.ulima.pm.medifacil2.R
import pe.edu.ulima.pm.medifacil2.models.beans.Predefinidas

interface OnPredefinidaItemClickListener {
    fun onClick(nombre: String, desc: String, imagen: String)
}

class PredefinidasRVAdapter: RecyclerView.Adapter<PredefinidasRVAdapter.MyViewHolder> {

    class MyViewHolder: RecyclerView.ViewHolder{
        var ivImagen: ImageView? = null
        var tvNombre: TextView? = null
        var tvId: TextView? = null
        var tvDesc: TextView? = null
        var tvImagenUrl: TextView? = null
        //Constructor que se usará para el view de cada medicamento predefinido,
        //siendo sus elementos una imagen, un nombre y un id
        constructor(view : View) : super(view) {
            ivImagen = view.findViewById(R.id.iv_mcard_imagen)
            tvNombre = view.findViewById(R.id.tv_mcard_nombre)
            tvId = view.findViewById(R.id.tv_mcard_id)
            tvDesc = view.findViewById(R.id.tv_mcard_desc)
            tvImagenUrl = view.findViewById(R.id.tv_mcard_imagenUrl)
        }
    }

    private var predefinidas : ArrayList<Predefinidas>? = null
    private var listener : OnPredefinidaItemClickListener? = null
    private var context : Context? = null

    //Constructor de la clase PredefinidasRVAdapter, el cual recibe como parámetro la lista de
    //medicamentos que queremos mostrar, así como sus listener y context
    constructor(predefinidas : ArrayList<Predefinidas>,
                listener : OnPredefinidaItemClickListener,
                context : Context) : super(){
        this.predefinidas = predefinidas
        this.listener = listener
        this.context = context
    }
    //Se encarga de crear la vista por cada elemento en la lista de medicamentos
    //solo se llama una vez
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medicamentocard, parent, false)
        return MyViewHolder(view)
    }
    //Para enlazar las vistas con los valores que queremos mostrar para cada medicamento
    //en determinada posicion
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //obtengo un medicamento predefinido en cierta posicion
        val predef = predefinidas!![position]
        //Llena los textviews con los valores del medicamento en la posicion correspondiente
        holder.tvNombre!!.text = predef.nombre
        holder.tvDesc!!.text = predef.desc
        holder.tvImagenUrl!!.text = predef.imagen

        //Se obtiene la imagen del medicamento obtenido mediante la API
        Glide.with(context!!).load(predef.imagen)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.ivImagen!!)

        holder.itemView.setOnClickListener {
            listener!!.onClick(holder.tvNombre!!.text.toString(),holder.tvDesc!!.text.toString(), holder.tvImagenUrl!!.text.toString())
        }
    }
    //Retorna la cantidad de medicamentos disponibles para agregar que mostraremos en pantalla
    override fun getItemCount(): Int {
        return predefinidas!!.size
    }

}