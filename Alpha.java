package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Sigma("α")
@TeleOp(name="Alpha", group="α")
public class Alpha extends Zeta {
  private AlphaController controller;

  @Override
  public void init() {
    controller = new AlphaController(hardwareMap, gamepad1);
    if (controller.hasNull())
      setFailed("null in controller");
  }

  @Override
  public void loop() {
    controller.update();
    updateTelemetry();
  }

  private void updateTelemetry() {
    updateTelemetry("State", controller);
  }
}
