import 'package:flutter/material.dart';
import 'package:time_test/event_handler_listening.dart';

void main() {
  runApp(const MyApp());
  listenForTime();
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: Scaffold(
        body: Center(
            child: StreamBuilder<bool>(
          stream: time,
          builder: (context, snapshot) {
            return Text(
              snapshot.data.toString(),
              style: Theme.of(context).textTheme.headlineMedium,
            );
          },
        )),
      ),
    );
  }
}
