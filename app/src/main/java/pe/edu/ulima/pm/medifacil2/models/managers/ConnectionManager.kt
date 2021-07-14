package pe.edu.ulima.pm.medifacil2.models.managers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConnectionManager {

    //https://60ed9eada78dc700178ae028.mockapi.io

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

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("https://60ed9eada78dc700178ae028.mockapi.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRetrofit() : Retrofit{
        return retrofit!!
    }

}