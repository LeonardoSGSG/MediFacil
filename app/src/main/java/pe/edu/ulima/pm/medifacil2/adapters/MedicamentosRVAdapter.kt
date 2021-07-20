package pe.edu.ulima.pm.medifacil2.adapters

import android.content.Context
import android.graphics.BitmapFactory
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

        //Constructor que se usará para el view de cada medicamento, siendo sus elementos
        //una imagen, un nombre y un id
        constructor(view : View) : super(view) {
            ivImagen = view.findViewById(R.id.iv_mcard_imagen)
            tvNombre = view.findViewById(R.id.tv_mcard_nombre)
            tvId = view.findViewById(R.id.tv_mcard_id)
        }
    }

    private var medicamentos: ArrayList<Medicamentos>? = null
    private var listener : OnMedicamentoItemClickListener? = null
    private var context : Context? = null

    //Constructor de la clase MedicamentosRVAdapter, el cual recibe como parámetro la lista de
    //medicamentos que queremos mostrar, así como sus listener y context
    constructor(medicamentos : ArrayList<Medicamentos>,
                listener : OnMedicamentoItemClickListener,
                context : Context) : super(){
        this.medicamentos = medicamentos
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
        //obtengo un medicamento en cierta posicion
        val medicamento = medicamentos!![position]

        //Llena los textviews con los valores del medicamento en la posicion correspondiente
        holder.tvNombre!!.text = medicamento.nombre
        holder.tvId!!.text = medicamento.id.toString()

        //En caso no se haya cargado una imagen (imagen=a), se pinta con mipmap una imagen por
        //defecto
        if(medicamento.imagen == "a"){
            holder.ivImagen!!.setImageResource(R.mipmap.ic_launcher)
        }
        //En caso se haya cargado una imagen desde la camara, esta se pinta en el ivImagen
        //del holder del medicamento en cierta posicion
        else{
            val takenImage = BitmapFactory.decodeFile(medicamento.imagen)
            holder.ivImagen!!.setImageBitmap(takenImage)
        }
        /*Glide.with(context!!).load(medicamento.imagen)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.ivImagen!!)*/

        //Establecemos que información se va a pasar por la interface cuando se haga click
        holder.itemView.setOnClickListener {
            listener!!.onClick(holder.tvId!!.text.toString())
        }
    }
    //Retorna la cantidad de medicamentos que mostraremos en pantalla
    override fun getItemCount(): Int {
        return medicamentos!!.size
    }

}