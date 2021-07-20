package pe.edu.ulima.pm.medifacil2.models.dao

import pe.edu.ulima.pm.medifacil2.models.beans.Predefinidas
import retrofit2.Call
import retrofit2.http.GET
import java.util.ArrayList

interface PredefinidasService {

    //Llamada del api y definimos que objeto traemos que será un ArrayList
    @GET("/api/predefinidas")
    fun getPredefinidas(): Call<ArrayList<Predefinidas>>

}