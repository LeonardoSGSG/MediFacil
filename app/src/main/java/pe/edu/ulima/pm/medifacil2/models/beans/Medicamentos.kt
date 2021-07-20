package pe.edu.ulima.pm.medifacil2.models.beans

data class Medicamentos(
    //datos del objeto traido del room
    val nombre: String,
    val desc: String,
    val imagen: String,
    val id: Int,
    val periodico: String
)