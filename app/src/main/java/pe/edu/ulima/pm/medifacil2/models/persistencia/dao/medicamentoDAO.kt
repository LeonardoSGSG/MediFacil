package pe.edu.ulima.pm.medifacil2.models.persistencia.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pe.edu.ulima.pm.medifacil2.models.persistencia.entities.Medicamento

//DAO que contiene las distintas querys al SQLite de medicamentos
@Dao
interface medicamentoDAO {
    @Query("SELECT * FROM Medicamento")
    fun findAll() : List<Medicamento>
    @Query("SELECT * FROM Medicamento where id = :id")
    fun findByMed(id: Int) : List<Medicamento>
    @Insert
    fun insert(med: Medicamento)
    @Query("DELETE FROM Medicamento where id = :id")
    fun delete(id: Int)
}