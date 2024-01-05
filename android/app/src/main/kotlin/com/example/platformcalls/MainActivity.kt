package com.example.platformcalls
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.Network
import android.os.BatteryManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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

    companion object{
        private val TAG= "HOME_SCREEN_ARCH_d"
        private val WEBVIEW_REQUEST_CODE = 101
        val INTENT_WEBVIEW_KEY = "INTENT_WEBVIEW_KEY"
        val INTENT_EXTRAS_KEY = "INTENT_EXTRAS_KEY"

    }
//    val openHomeWebView = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val quizzReturnData = result.data?.getStringExtra(INTENT_WEBVIEW_KEY)
//            Log.d(TAG, "quizzReturnData:$quizzReturnData ")
//        }
//    }
    private var methodChannel: MethodChannel? = null
    private var pressureChannel: EventChannel? = null
    private lateinit var sensorManager: SensorManager
    private var pressureStreamHandler: StreamHandler? = null
    private var pendingResult:  MethodChannel.Result? = null
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


            call,result -> if(call.method == "isSensorAvailable")
        {

            val intent = Intent(this, WebView::class.java)
            intent.putExtra("appBarTitle", "Ebooks")
            intent.putExtra("appBarUrl", "https://www.geeksforgeeks.org/singleton-class-java/")
//            openHomeWebViewView.launch(intent)
            startActivityForResult(intent,101)
            pendingResult = result
        }

            else{
                result.notImplemented()
        }
        }

        pressureChannel = EventChannel(messenger, EVENT_CHANNEL_NAME)
        pressureStreamHandler = StreamHandler(sensorManager, Sensor.TYPE_PRESSURE)
        pressureChannel!!.setStreamHandler(pressureStreamHandler)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    if(requestCode==101 && resultCode ==Activity.RESULT_OK){
        val quizzReturnData = data?.getStringExtra(INTENT_WEBVIEW_KEY)
        Log.d(TAG, "quizzReturnData:$quizzReturnData ")
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        pendingResult?.success(quizzReturnData)
    }


    }


    private fun tearDownChannels()
    {
        methodChannel!!.setMethodCallHandler(null)
        pressureChannel!!.setStreamHandler(null)
    }


}
