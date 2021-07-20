package pe.edu.ulima.pm.medifacil2.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import pe.edu.ulima.pm.medifacil2.DestinationActivity
import pe.edu.ulima.pm.medifacil2.R

class AlarmReceiver: BroadcastReceiver() {

    //Se llegará a este broadcast receiver "AlarmReceiver" cada vez que llegue la hora de una notificacion de algun
    //medicamento.
    override fun onReceive(context: Context?, intent: Intent?) {

        //recogemos el valor del nombre del medicamento recibido desde el intent.putStringExtra
        val nombre = intent!!.getStringExtra("nombre")
        //Definimos un id que irá cambiando cada vez que se crea una notificacion
        val id = System.currentTimeMillis()
        //Definimos el intent al activity al que se llegará si el usuario hace click en la notificacion
        val i = Intent(context, DestinationActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)

        //Construimos como se verá la notificación y que informacion contendrá
        val builder = NotificationCompat.Builder(context!!, "MediFacil")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Notificación de medicina")
            .setContentText("Hora de tomar " + nombre)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        //Mostramos la notificacion con los parametros anteriores y con el id generado cada vez
        with(NotificationManagerCompat.from(context)){
            notify(id.toInt(), builder.build())
        }
        /*val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(id.toInt(), builder.build())*/
    }

}