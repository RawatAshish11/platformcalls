import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:webview_flutter/webview_flutter.dart';
void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // is not restarted.
        primarySwatch: Colors.blue,
      ),
      home:  MyHomePage(),
    );
  }
}



class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

  static const methodChannel = MethodChannel('com.julo.barometer/meter');
  static const pressureChannel = EventChannel('com.julo.barometer/pressure');

  String _sensorAvailable = "Unknown";
  String batteryData = "Unknown";

  Future<void> _checkAvailability() async {
    try {
      var available = await methodChannel.invokeMethod("isSensorAvailable");

      print("available");
      print(available);
      setState(() {
        _sensorAvailable = available;
      });
    } catch (error) {
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [

            Center(child: Text("Sensor Available Availble: $_sensorAvailable")),

            Center(child: Text("Battery Data: $batteryData")),

            ElevatedButton(
                onPressed: _checkAvailability,
                child: const Text("Check Sensor")
            )
          ],
        ),
      ),
    );
  }
}
