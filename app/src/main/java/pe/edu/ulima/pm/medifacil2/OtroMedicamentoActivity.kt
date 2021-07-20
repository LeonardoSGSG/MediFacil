package pe.edu.ulima.pm.medifacil2

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import pe.edu.ulima.pm.medifacil2.models.beans.Medicamentos
import pe.edu.ulima.pm.medifacil2.models.managers.PredefinidasManager
import pe.edu.ulima.pm.medifacil2.receiver.AlarmReceiver
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.*

class OtroMedicamentoActivity: AppCompatActivity(){

    /*
     *
     * Integrantes:
     * Diego Antonio Esquivel Patiño -> 20170532
     * Leonardo Sipion Guillen -> 20171484
     * Fabricio Sotelo Parra -> 20171497
     *
     *
     */

    //Variables a ser utilizadas en distintas partes de esta actividad
    private var etNombre : EditText? = null
    private var etDesc : EditText? = null
    private var etPeriodico: EditText? = null
    private var ivMedicamento: ImageView? = null
    private var btAnadir : Button? = null
    private var btCancel : Button? = null
    private var photoFile: File? = null
    private var fotoPorDefecto = true
    private var alarmManager: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null
    private var calendar: Calendar? = null
    private val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otromedicamento)
        //Creacion del notification channel
        createNotificationChannel()
        //Instanciamos las variables
        calendar = Calendar.getInstance()
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        etNombre = findViewById(R.id.et_om_nombre)
        etDesc = findViewById(R.id.et_om_descripcion)
        etPeriodico = findViewById(R.id.et_om_periodico)
        ivMedicamento = findViewById(R.id.iv_om_imagenMedicamento)
        btAnadir = findViewById(R.id.bt_om_añadir)
        btCancel = findViewById(R.id.bt_om_cancelar)

        //traemos la info extra del intent si es que es seleccionado de los del API
        val nom = intent.getStringExtra("nombreMedicamento")
        val desc = intent.getStringExtra("descMedicamento")
        val imageUrl = intent.getStringExtra("imagenUrlMedicamento")

        //Comprobamos si es que el medicamento es traido del API para llenar los campos respectivos
        if(nom != null && desc != null && imageUrl != null){
            etNombre!!.setText(nom)
            etDesc!!.setText(desc)
            Glide.with(this).load(imageUrl)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(ivMedicamento!!)
        }

        //Definimos el onclicklistener del imageview para que el usuario pueda cambiar la imagen a gusto
        ivMedicamento!!.setOnClickListener {
            //Toast.makeText(this, "iamgen", Toast.LENGTH_SHORT).show()
            //Funcion para capturar una nueva imagen
            capturarImagen()
        }
        //onClicklistener para guardar el medicamento en Room
        btAnadir!!.setOnClickListener{
            //Verifica si el nombre y la descripcion no están vacios puesto que son obligatorios
            if(!TextUtils.isEmpty(etNombre!!.text.toString()) && !TextUtils.isEmpty(etDesc!!.text.toString())){
                //definimos un medicamento nuevo para guardar los datos
                val med : ArrayList<Medicamentos> = ArrayList()
                //Traemos el path de la imagen nueva, si es que el usuario ha tomado una nueva.
                val imagePath = bringImageFile()
                //Se procede a crear las alarmas del medicamento con la funcion crearAlarmas
                val periodico = crearAlarmas(etNombre!!.text.toString())
                //Se guarda el medicamento con la informacion en Room
                med.add(Medicamentos(etNombre!!.text.toString(), etDesc!!.text.toString(), imagePath, 0, periodico))
                PredefinidasManager.getInstance().saveMedicamentos(this, med,{ num:Int ->
                    this.finish()
                })
            }else{
                Toast.makeText(this, "faltan datos", Toast.LENGTH_SHORT).show()
            }
        }

        //Click listener que finaliza la actividad al presionar "cancelar"
        btCancel!!.setOnClickListener{
            this.finish()
        }

    }

    //Funcion que verifica si es que hay algo en el campo "periodico" para que, en caso haya, se creen las
    // notificaciones respectivas, y si es que no hay en nada en el campo devuelve el string "No Hay"
    private fun crearAlarmas(nombre: String): String{
        //Comprobación si es que no esta vacio
        if (!TextUtils.isEmpty(etPeriodico!!.text.toString())){
            //Se procede a crear las alarmas en funcion de las horas brindadas en el campo, separarElementos() es para
            //obtener las horas del string del campo
            val horasLista = separarElementos(etPeriodico!!.text.toString())
            for(hora in horasLista){
                //por cada una de esas horas brindada, se obtienen la hora y los minutos en una lista con la funcion
                    //separarPorDosPuntos()
                val compo = separarPorDosPuntos(hora)
                calendar!![Calendar.HOUR_OF_DAY] = compo[0].toInt()
                calendar!![Calendar.MINUTE] = compo[1].toInt()
                calendar!![Calendar.SECOND] = 0
                calendar!![Calendar.MILLISECOND] = 0
                //hacemos intent al AlarmReciever
                val intent = Intent(this, AlarmReceiver::class.java)
                //pasamos el dato del nombre de la medicina
                intent.putExtra("nombre", nombre)
                //Obtenemos el broadcast para poner el intent pendiente
                pendingIntent = PendingIntent.getBroadcast(this, System.currentTimeMillis().toInt(), intent, 0)
                //Establecemos la notificacion a la hora dada mediante el alarmManager
                alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, calendar!!.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            }
            return etPeriodico!!.text.toString()
        }else{
            return "No hay"
        }
    }

    //Funcion para separar elementos por coma.
    private fun separarElementos(cadena: String): ArrayList<String> {
        if(cadena.contains(",")){
            val a = cadena.split(",")
            return a as ArrayList<String>
        }else{
            val a = ArrayList<String>()
            a.add(cadena)
            return a
        }
    }

    //Funcion para separar por dos puntos ":"
    private fun separarPorDosPuntos(hora: String): ArrayList<String>{
        val a = hora.split(":")
        return a as ArrayList<String>
    }

    //Funcion para capturar la imagen cuando el usuario de click en la imageView
    private fun capturarImagen() {
        //Se crea el archivo para la imagen
        photoFile = getImageFile()
        //pasamos el url mediante el file provider
        val fileProvider = FileProvider.getUriForFile(this, "pe.edu.ulima.pm.medifacil2.fileprovider", photoFile!!)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        //Pedimos permisos si es que no se tienen a la camara
        if(cameraIntent.resolveActivity(packageManager) != null){
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(cameraIntent, 1000)
            }else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 200)
            }
        }
    }

    /// Funcion para crear el archivo para la imagen
    private fun getImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(Date())
        val imageFilename = "JPEG_$timeStamp"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFilename, ".jpg", storageDir)
    }

    //Funcion para guardar una imagen disponible en el image view
    private fun saveImageFromView(): File {
        val bitmapDrawable = ivMedicamento!!.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        var outStream: FileOutputStream? = null
        val imageFilename = "JPEG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(Date())}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val newPhotoFile = File.createTempFile(imageFilename, ".jpg", storageDir)
        outStream = FileOutputStream(newPhotoFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream)
        outStream.flush()
        outStream.close()
        return newPhotoFile
    }

    //Funcion encargada de discernir si es que la foto es la traida del API, o si es una puesta por el usuario, o
    // si no hay imagen establecida.
    private fun bringImageFile(): String {
        return if (fotoPorDefecto) {
            if (ivMedicamento!!.drawable == null) {
                "a"
            } else {
                saveImageFromView().absolutePath
            }
        } else {
            photoFile!!.absolutePath
        }
    }

    //Funcion para crea el notificationChannel donde se mostraran las notificaciones
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "MediFacilReminderChannel"
            val description = "Channel For Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("MediFacil", name, importance)
            channel.description = description
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    //Funcion donde se indica qué se hará una vez se obtengan los permisos de camara
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //Si se obteienen los permisos de camara, se muestra intent de camara, sino, se muestra un toast indicando
        //que no se dieron los permisos.
        if(requestCode == 200){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(cameraIntent, 1000)
            }else {
                Toast.makeText(this, "No se dieron los permisos para cámara", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    //Cuando se termine de tomar la foto se procede a guardarla en el almacenamiento externo y se pone en el
    //image view
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            ivMedicamento!!.setImageBitmap(takenImage)
            fotoPorDefecto = false
        }
    }

}