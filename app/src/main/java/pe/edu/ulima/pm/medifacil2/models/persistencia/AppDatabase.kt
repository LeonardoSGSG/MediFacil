package pe.edu.ulima.pm.medifacil2.models.persistencia

import androidx.room.Database
import androidx.room.RoomDatabase
import pe.edu.ulima.pm.medifacil2.models.persistencia.dao.medicamentoDAO
import pe.edu.ulima.pm.medifacil2.models.persistencia.entities.Medicamento

//Definición de la base de datos Room de medicamentos
@Database(entities = arrayOf(Medicamento :: class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun MedicamentoDAO() : medicamentoDAO
}