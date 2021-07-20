package pe.edu.ulima.pm.medifacil2.models.persistencia.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Medicamento(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name="nombre") val nombre: String,
    @ColumnInfo(name="desc") val desc: String,
    @ColumnInfo(name="imagen") val imagen: String,
    @ColumnInfo(name="periodico") val periodico: String

)
