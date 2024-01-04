import UIKit
import Flutter
import CoreMotion

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
//      let controller: FlutterViewController
      
      let METHOD_CHANNEL_NAME = "com.julo.barometer/meter";
      let EVENT_CHANNEL_NAME = "com.julo.barometer/pressure";
      let pressureStreamHandler = PressureStreamHandler()
      
      let controller: FlutterViewController  = window?.rootViewController as! FlutterViewController
      
      let methodChannel = FlutterMethodChannel(name: METHOD_CHANNEL_NAME, binaryMessenger: controller.binaryMessenger)
      
      methodChannel.setMethodCallHandler({
          (call:FlutterMethodCall , result: @escaping FlutterResult)-> Void in
          switch call.method
          {
          case "isSensorAvailable" :
              result(CMAltimeter.isRelativeAltitudeAvailable())
              
          default: result(FlutterMethodNotImplemented)
          }
      })
      
      let  pressureChannel = FlutterEventChannel(name: EVENT_CHANNEL_NAME, binaryMessenger:controller.binaryMessenger)
      pressureChannel.setStreamHandler(pressureStreamHandler)
      
      GeneratedPluginRegistrant.register(with: self)
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
}
