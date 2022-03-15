package org.firstinspires.ftc.transfinity;

import java.util.LinkedHashMap;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Sigma("γ")
@TeleOp(name="Gamma", group="γ")
public class Gamma extends Zeta {
  private GammaController controller;

  @Override
  public void init() {
    controller = new GammaController(Team.RED, hardwareMap, gamepad1, gamepad2);
    if (controller.hasNull() && status != Status.FAILED) {
      setFailed("null in controller");
      return;
    }
    controller.beta.armPitch.init();
  }

  @Override
  public void start() {
    super.start();
    controller.beta.armPitch.start();
  }

  @Override
  public void loop() {
    controller.update();
    updateTelemetry();
  }

  private void updateTelemetry() {
    LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(3 /* 4 */);
    telemetryData.put("Alpha", controller.alpha);
    telemetryData.put("Beta", controller.beta);
    telemetryData.put("Lambda", controller.lambda);
    // telemetryData.put("Theta", controller.theta);
    updateTelemetry(telemetryData);
  }
}
