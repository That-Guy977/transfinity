package org.firstinspires.ftc.transfinity;

import java.util.LinkedHashMap;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
@Sigma("β")
@TeleOp(name="Beta", group="β")
public class Beta extends Zeta {
  private DcMotor armPitch;
  private Servo armGrab;

  @Override
  public void init() {
    super.init();
    armPitch = (DcMotor) devices.get("armPitch");
    armGrab = (Servo) devices.get("armGrab");
    if (armPitch == null || armGrab == null) {
      if (status != Status.FAILED)
        updateTelemetry(Status.FAILED);
      return;
    }
    armPitch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  }

  @Override
  public void loop() {
    updateTelemetry();
  }

  private void updateTelemetry() {
    LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(2);
    telemetryData.put("Arm Position", armPitch.getCurrentPosition());
    telemetryData.put("Grabber Position", armGrab.getPosition());
    updateTelemetry(telemetryData);
  }
}