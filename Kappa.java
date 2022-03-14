package org.firstinspires.ftc.transfinity;

import java.util.LinkedHashMap;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Sigma("κ")
@TeleOp(name="Kappa", group="κ")
public class Kappa extends Zeta {
  private KappaController controller;

  @Override
  public void init() {
    controller = new KappaController(hardwareMap);
    if (controller.hasNull() && status != Status.FAILED)
      setFailed("null in controller");
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