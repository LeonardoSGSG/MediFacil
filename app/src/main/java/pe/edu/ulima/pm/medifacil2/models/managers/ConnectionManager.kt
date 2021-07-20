package pe.edu.ulima.pm.medifacil2.models.managers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConnectionManager {

    //https://60ed9eada78dc700178ae028.mockapi.io

    //Singleton que contiene la instancia de retrofit para la conexion
    companion object {
        private var instance : ConnectionManager? = null

        fun getInstance() : ConnectionManager {
            if (instance == null) {
                instance = ConnectionManager()
            }
            return instance!!
        }
    }

    private var retrofit : Retrofit? = null

    //Al crearse el singleton se construye la conexion al API
    init {
        retrofit = Retrofit.Builder()
            .baseUrl("https://60ed9eada78dc700178ae028.mockapi.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Función para retornar la instancia
    fun getRetrofit() : Retrofit{
        return retrofit!!
    }

}