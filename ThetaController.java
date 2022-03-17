package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;

class ThetaController extends GroupController<Controller> {
  ThetaController(HardwareMap hardwareMap, Gamepad gamepad) {
    super(hardwareMap, gamepad);
  }

  void update() {}

  @Override
  public String toString() {
    return "";
  }
}
