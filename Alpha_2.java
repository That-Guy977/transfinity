package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Alpha 2 - Rotation", group="α")
public class Alpha_2 extends Alpha {
  @Override
  public void loop() {
    double rot = gamepad1.right_stick_x;
    for (int i = 0; i < drive.size(); i++)
      drive.get(DRIVE_ORDER[i]).setPower(i % 2 == 0 ? rot : -rot);
    updateTelemetry();
  }
}
