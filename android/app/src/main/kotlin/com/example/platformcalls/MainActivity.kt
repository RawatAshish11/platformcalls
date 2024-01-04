package com.example.platformcalls
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.Network
import android.os.BatteryManager
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterFragmentActivity() {
    private val METHOD_CHANNEL_NAME = "com.julo.barometer/meter";
    private val EVENT_CHANNEL_NAME = "com.julo.barometer/pressure";



    private var methodChannel: MethodChannel? = null
    private var pressureChannel: EventChannel? = null
    private lateinit var sensorManager: SensorManager
    private var pressureStreamHandler: StreamHandler? = null

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

    //SETUP CHANNELS
    setupChannels(this,flutterEngine.dartExecutor.binaryMessenger);

    }

    override fun onDestroy() {
        tearDownChannels()
        super.onDestroy()
    }



    private fun setupChannels(context: Context, messenger: BinaryMessenger)
    {

        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        methodChannel = MethodChannel(messenger, METHOD_CHANNEL_NAME)
        methodChannel!!.setMethodCallHandler{
            call,result -> if(call.method == "``````isSensorAvailable``````")
        {

            val intent = Intent(this, WebView::class.java)
            intent.putExtra("appBarTitle", "Ebooks")
            startActivity(intent)

            result.success(true)
        }
            if(call.method == "deviceInfo")
            {
              var service  =  context.getSystemService(Context.BATTERY_SERVICE)  as BatteryManager
                val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
                val network: Network = connectivityManager.activeNetwork!!
                val capabilities = connectivityManager.getNetworkCapabilities(network)



                result.success(hashMapOf(
                        "build" to android.os.Build.MANUFACTURER.toString(),
                        "name" to android.os.Build.BOARD,
                        "bootloader" to android.os.Build.BOOTLOADER,
                        "finger" to android.os.Build.HARDWARE,
                        "hardware" to android.os.Build.HARDWARE,
                        "product" to android.os.Build.MODEL,

                        ));
            }
            else{
                result.notImplemented()
        }
        }

        pressureChannel = EventChannel(messenger, EVENT_CHANNEL_NAME)
        pressureStreamHandler = StreamHandler(sensorManager, Sensor.TYPE_PRESSURE)
        pressureChannel!!.setStreamHandler(pressureStreamHandler)
    }

    private fun tearDownChannels()
    {
        methodChannel!!.setMethodCallHandler(null)
        pressureChannel!!.setStreamHandler(null)
    }
}
