package org.firstinspires.ftc.transfinity;

interface Controller {
  @Override
  String toString();
  void init();
  boolean hasNull();
}

enum Status {
  INITIALIZING,
  READY,
  ACTIVE,
  STOPPED,
  FAILED
}

enum Team {
  RED, BLUE;
  static Team DEFAULT = RED;
}

enum Position {
  CAROUSEL, WAREHOUSE
}
