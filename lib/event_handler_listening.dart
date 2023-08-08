import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:rxdart/subjects.dart';

// const platform = MethodChannel('your.package.name/time');
const eventChannel = EventChannel('com.example.time_test/event');

BehaviorSubject<bool> time = BehaviorSubject<bool>();
StreamSubscription? _subscription;

void listenForTime() async {
  _subscription ??=
      eventChannel.receiveBroadcastStream().listen(_onEvent, onError: _onError);
}

void _onEvent(event) {
  // Handle time change event
  debugPrint('Time changed: $event');
  time.add(event);
}

void _onError(error) {
  // Handle error
}
