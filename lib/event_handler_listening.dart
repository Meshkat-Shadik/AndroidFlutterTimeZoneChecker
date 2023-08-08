import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

const eventChannel = EventChannel('com.example.time_test/event');
final StreamController<bool> _timeController =
    StreamController<bool>.broadcast();
Stream<bool> get time => _timeController.stream;
StreamSubscription? subscription;

void listenForTime() async {
  subscription ??=
      eventChannel.receiveBroadcastStream().listen(_onEvent, onError: _onError);
}

void _onEvent(event) {
  // Handle time change event
  debugPrint('Time changed: $event');
  // time.add(event);
  _timeController.add(event);
}

void _onError(error) {
  // Handle error
}

void dispose() {
  _timeController.close();
}
