package pe.edu.ulima.pm.medifacil2.models.managers

import android.content.Context
import androidx.room.Room
import pe.edu.ulima.pm.medifacil2.models.beans.Medicamentos
import pe.edu.ulima.pm.medifacil2.models.beans.Predefinidas
import pe.edu.ulima.pm.medifacil2.models.dao.PredefinidasService
import pe.edu.ulima.pm.medifacil2.models.persistencia.AppDatabase
import pe.edu.ulima.pm.medifacil2.models.persistencia.entities.Medicamento
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.ArrayList

interface OnGetPredefinidasDone{
    fun onSuccess(predefinidas: ArrayList<Predefinidas>)
    fun onError(msg: String)
}

class PredefinidasManager {

    companion object{
        private var instance : PredefinidasManager? = null

        fun getInstance(): PredefinidasManager{
            if(instance == null){
                instance = PredefinidasManager()
            }
            return instance!!
        }
    }
    //funcion para traer los elementos del api
    fun getPredefinidas(callback: OnGetPredefinidasDone) {
        val retrofit = ConnectionManager.getInstance().getRetrofit()
        val devicesService = retrofit.create<PredefinidasService>()
        devicesService.getPredefinidas().enqueue(object : Callback<ArrayList<Predefinidas>> {
            override fun onResponse(call: Call<ArrayList<Predefinidas>>, response: Response<ArrayList<Predefinidas>>) {
                if (response.code() == 200 && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError("Arraylist nulo")
                }
            }
            override fun onFailure(call: Call<ArrayList<Predefinidas>>, t: Throwable) {
                callback.onError(t.message!!)
            }
        })
    }
    //funcion para guardar medicamentos en el room
    fun saveMedicamentos(context: Context, medicamentos: ArrayList<Medicamentos>, callback: (Int) -> Unit){
        //conexion al room
        val db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,"mediFacil").fallbackToDestructiveMigration().build()
        Thread{
            val medDao = db.MedicamentoDAO()
            //Se guardan los medicamentos de una lista de medicamentos en el room
            medicamentos.forEach{ m : Medicamentos ->
                medDao.insert(
                    Medicamento(
                        0,
                        m.nombre,
                        m.desc,
                        m.imagen,
                        m.periodico
                    )
                )
            }
            db.close()
            //elemento a devolver en callback
            callback(1)
        }.start()
    }
    //funcion para traer todos los medicamentos del room
    fun getMedicamentosRoom(context: Context, id: Int, callback: (ArrayList<Medicamentos>) -> Unit){
        //conexion al room
        val db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "mediFacil").fallbackToDestructiveMigration().build()
        Thread{
            val medDao = db.MedicamentoDAO()
            val medList = ArrayList<Medicamentos>()
            //se traen todos los medicamentos del room y se trabaja uno por uno
            medDao.findAll().forEach{ m : Medicamento ->
                medList.add(
                    Medicamentos(
                        m.nombre,
                        m.desc,
                        m.imagen,
                        m.id,
                        m.periodico
                    )
                )

            }
            //elemento a devolver en callback
            callback(medList)
        }.start()
    }
    //funcion para traer un medicamento por id
    fun getMedicamentosRoomPorId(context: Context, id: Int, callback: (Medicamentos) -> Unit){
        //conexion al room
        val db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "mediFacil").fallbackToDestructiveMigration().build()
        Thread{
            val medDao = db.MedicamentoDAO()
            val medList = ArrayList<Medicamentos>()
            //recuperamos el array de respuesta del room y trabajamos por los elementos traidos(solo 1)
            medDao.findByMed(id).forEach{ m : Medicamento ->
                medList.add(
                    Medicamentos(
                        m.nombre,
                        m.desc,
                        m.imagen,
                        m.id,
                        m.periodico
                    )
                )
            }
            //elemento a devolver en callback
            callback(medList[0])
        }.start()
    }
    //funcion para eliminar medicamento por ID
    fun deleteMed(context: Context, id: Int, callback: (Int) -> Unit){
        //conexion al room
        val db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "mediFacil").fallbackToDestructiveMigration().build()
        Thread{
            //funcion de eliminacion del DAO
            val medDao = db.MedicamentoDAO()
            medDao.delete(id)
            db.close()
            //elemento a devolver en callback
            callback(1)
        }.start()
    }

}