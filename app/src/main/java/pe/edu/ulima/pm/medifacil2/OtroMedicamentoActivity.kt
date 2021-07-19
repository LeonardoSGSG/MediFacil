package pe.edu.ulima.pm.medifacil2

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
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
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OtroMedicamentoActivity: AppCompatActivity(){

    private var etNombre : EditText? = null
    private var etDesc : EditText? = null
    private var ivMedicamento: ImageView? = null
    private var btAnadir : Button? = null
    private var btCancel : Button? = null
    private var photoFile: File? = null
    private var fotoPorDefecto = true
    private val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otromedicamento)

        etNombre = findViewById(R.id.et_om_nombre)
        etDesc = findViewById(R.id.et_om_descripcion)
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
                val imagePath: String = if(fotoPorDefecto){
                    if(ivMedicamento!!.drawable == null){
                        "a"
                    }else{
                        saveImageFromView().absolutePath
                    }
                }else{
                    photoFile!!.absolutePath
                }
                med.add(Medicamentos(etNombre!!.text.toString(), etDesc!!.text.toString(), imagePath,0 ))
                PredefinidasManager.getInstance().saveMedicamentos(this, med,{num:Int ->
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