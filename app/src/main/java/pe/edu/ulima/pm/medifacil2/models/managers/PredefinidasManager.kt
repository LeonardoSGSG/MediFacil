package pe.edu.ulima.pm.medifacil2.models.managers

import pe.edu.ulima.pm.medifacil2.models.beans.Predefinidas
import pe.edu.ulima.pm.medifacil2.models.dao.PredefinidasService
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

}