package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Sigma("β")
@TeleOp(name="Beta", group="β")
public class Beta extends Zeta {
  private BetaController controller;

  @Override
  public void init() {
    controller = new BetaController(hardwareMap, gamepad1);
    if (controller.hasNull())
      setFailed("null in controller");
  }

  @Override
  public void start() {
    super.start();
    controller.armPitch.start();
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
