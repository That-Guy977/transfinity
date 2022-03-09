package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Sigma("α")
@TeleOp(name="Alpha", group="α")
public class Alpha extends Zeta {
  private static final String[] DRIVE_DEVICE_NAMES = { "driveLeft", "driveRight" };
  private Drive drive;

  @Override
  public void init() {
    super.init();
    drive = new Drive(hardwareMap, DRIVE_DEVICE_NAMES);
    if (drive.hasNull() && status != Status.FAILED)
      updateTelemetry(Status.FAILED, "Reason", "null in drive");
  }

  @Override
  public void loop() {
    drive.setPower(gamepad1);
    updateTelemetry();
  }

  private void updateTelemetry() {
    updateTelemetry("Motor Power", drive);
  }
}
