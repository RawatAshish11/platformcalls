package com.example.platformcalls
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
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

    private fun tearDownChannels()
    {
            methodChannel!!.setMethodCallHandler(null)
    }

    private fun setupChannels(context: Context, messenger: BinaryMessenger)
    {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        methodChannel = MethodChannel(messenger, METHOD_CHANNEL_NAME)
        methodChannel!!.setMethodCallHandler{
            call,result -> if(call.method == "isSensorAvailable")
        {
                result.success(sensorManager!!.getSensorList(Sensor.TYPE_PRESSURE).isNotEmpty())
        }
            else{
                result.notImplemented()
        }
        }

        pressureChannel = EventChannel(messenger, EVENT_CHANNEL_NAME)
        pressureStreamHandler = StreamHandler(sensorManager!!, )
    }
}
