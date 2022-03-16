package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Gamepad;

@Autonomous(name="Iota", group="ι")
public class Iota extends Zeta<IotaController> {
  @Override
  public void init() {
    controller = new IotaController(hardwareMap, new Gamepad());
    super.init();
  }

  @Override
  public void start() {
    super.start();
    controller.beta.armPitch.start();
  }

  @Override
  public void loop() {
    updateTelemetry();
  }
}
