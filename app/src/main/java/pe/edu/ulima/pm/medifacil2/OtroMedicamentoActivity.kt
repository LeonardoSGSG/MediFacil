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

        createNotificationChannel()
        calendar = Calendar.getInstance()
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        etNombre = findViewById(R.id.et_om_nombre)
        etDesc = findViewById(R.id.et_om_descripcion)
        etPeriodico = findViewById(R.id.et_om_periodico)
        ivMedicamento = findViewById(R.id.iv_om_imagenMedicamento)
        btAnadir = findViewById(R.id.bt_om_añadir)
        btCancel = findViewById(R.id.bt_om_cancelar)

        val nom = intent.getStringExtra("nombreMedicamento")
        val desc = intent.getStringExtra("descMedicamento")
        val imageUrl = intent.getStringExtra("imagenUrlMedicamento")

        if(nom != null && desc != null && imageUrl != null){
            etNombre!!.setText(nom)
            etDesc!!.setText(desc)
            Glide.with(this).load(imageUrl)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(ivMedicamento!!)
        }

        ivMedicamento!!.setOnClickListener {
            //Toast.makeText(this, "iamgen", Toast.LENGTH_SHORT).show()
            capturarImagen()
        }

        btAnadir!!.setOnClickListener{
            if(!TextUtils.isEmpty(etNombre!!.text.toString()) && !TextUtils.isEmpty(etDesc!!.text.toString())){
                val med : ArrayList<Medicamentos> = ArrayList()
                val imagePath = bringImageFile()
                val periodico = crearAlarmas(etNombre!!.text.toString())
                med.add(Medicamentos(etNombre!!.text.toString(), etDesc!!.text.toString(), imagePath, 0, periodico))
                PredefinidasManager.getInstance().saveMedicamentos(this, med,{ num:Int ->
                    this.finish()
                })
            }else{
                Toast.makeText(this, "faltan datos", Toast.LENGTH_SHORT).show()
            }
        }

        btCancel!!.setOnClickListener{
            this.finish()
        }

    }

    private fun crearAlarmas(nombre: String): String{
        if (!TextUtils.isEmpty(etPeriodico!!.text.toString())){
            val horasLista = separarElementos(etPeriodico!!.text.toString())
            for(hora in horasLista){
                val compo = separarPorDosPuntos(hora)
                calendar!![Calendar.HOUR_OF_DAY] = compo[0].toInt()
                calendar!![Calendar.MINUTE] = compo[1].toInt()
                calendar!![Calendar.SECOND] = 0
                calendar!![Calendar.MILLISECOND] = 0
                //hacemos intent al AlarmReciever
                val intent = Intent(this, AlarmReceiver::class.java)
                //pasamos el dato del nombre de la medicina
                intent.putExtra("nombre", nombre)
                pendingIntent = PendingIntent.getBroadcast(this, System.currentTimeMillis().toInt(), intent, 0)
                alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, calendar!!.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            }
            return etPeriodico!!.text.toString()
        }else{
            return "No hay"
        }
    }

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

    private fun separarPorDosPuntos(hora: String): ArrayList<String>{
        val a = hora.split(":")
        return a as ArrayList<String>
    }

    private fun capturarImagen() {
        photoFile = getImageFile()
        val fileProvider = FileProvider.getUriForFile(this, "pe.edu.ulima.pm.medifacil2.fileprovider", photoFile!!)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if(cameraIntent.resolveActivity(packageManager) != null){
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(cameraIntent, 1000)
            }else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 200)
            }
        }
    }

    private fun getImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(Date())
        val imageFilename = "JPEG_$timeStamp"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFilename, ".jpg", storageDir)
    }

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 200){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(cameraIntent, 1000)
            }else {
                Toast.makeText(this, "No se dieron los permisos para cámara", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            ivMedicamento!!.setImageBitmap(takenImage)
            fotoPorDefecto = false
        }
    }

}