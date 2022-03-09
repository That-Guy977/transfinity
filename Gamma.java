package org.firstinspires.ftc.transfinity;

import java.util.LinkedHashMap;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Sigma("γ")
@TeleOp(name="Gamma - Alpha & Lambda 0", group="γ")
public class Gamma extends Zeta {
  private static final String[] DRIVE_DEVICE_NAMES = new String[]{ "driveLeft", "driveRight" };
  private Drive drive;
  private DcMotor carousel;

  @Override
  public void init() {
    super.init();
    drive = new Drive(hardwareMap, DRIVE_DEVICE_NAMES);
    carousel = (DcMotor) devices.get("carousel");
    if (drive.hasNull() && status != Status.FAILED)
      updateTelemetry(Status.FAILED, "Reason", "null in drive");
    if (carousel == null && status != Status.FAILED)
      updateTelemetry(Status.FAILED, "Reason", "null in carousel");
    if (status == Status.FAILED) return;
    carousel.setDirection(DcMotor.Direction.FORWARD);
    carousel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  }

  @Override
  public void loop() {
    drive.setPower(gamepad1);
    carousel.setPower(Boolean.compare(gamepad1.dpad_right, gamepad1.dpad_left));
    updateTelemetry();
  }

  private void updateTelemetry() {
    LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(2);
    telemetryData.put("Drive Power", drive);
    telemetryData.put("Carousel Power", carousel.getPower());
    updateTelemetry(telemetryData);
  }
}