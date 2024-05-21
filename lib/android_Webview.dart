import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class MyHomePage extends StatefulWidget {
  final  String url ;
  const MyHomePage({super.key, required this.url});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const methodChannel = MethodChannel('com.julo.barometer/meter');

  Future<void> _checkAvailability() async {
    try {
      var available = await methodChannel.invokeMethod("isSensorAvailable");

      print("available method channel");
      print(available);
    } catch (error) {}
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: ElevatedButton(
            onPressed: _checkAvailability, child: const Text("Check Sensor")),
      ),
    );
  }
}
