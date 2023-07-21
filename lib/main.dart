import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

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
      home: const MyHomePage(),
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

  Future<void> _checkAvailability() async {
    try {
      var available = await methodChannel.invokeMethod("isSensorAvailable");
      setState(() {
        _sensorAvailable = available.toString();
      });
    } catch (error) {
      print("error");
      print(error);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text("Sensor Available Availble: $_sensorAvailable"),
          ElevatedButton(
              onPressed: _checkAvailability,
              child: Text("Check Sensor")
          )
        ],
      ),
    );
  }
}
