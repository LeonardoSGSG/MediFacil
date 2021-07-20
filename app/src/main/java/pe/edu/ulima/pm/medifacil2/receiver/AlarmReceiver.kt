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

    override fun onReceive(context: Context?, intent: Intent?) {

        //recogemos el valor del nombre recibido desde el intent.putStringExtra
        val nombre = intent!!.getStringExtra("nombre")

        val id = System.currentTimeMillis()
        val i = Intent(context, DestinationActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)

        val builder = NotificationCompat.Builder(context!!, "MediFacil")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Notificación de medicina")
            .setContentText("Hora de tomar " + nombre)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)){
            notify(id.toInt(), builder.build())
        }
        /*val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(id.toInt(), builder.build())*/
    }

}