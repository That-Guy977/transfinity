package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Sigma("λ")
@TeleOp(name="Lambda", group="λ")
public class Lambda extends Zeta {
  private LambdaController controller;

  @Override
  public void init() {
    controller = new LambdaController(hardwareMap, gamepad1);
    if (controller.hasNull())
      setFailed("null in controller");  }

  @Override
  public void loop() {
    controller.update();
    updateTelemetry();
  }

  private void updateTelemetry() {
    updateTelemetry("State", controller);
  }
}