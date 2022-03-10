package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Delta - Motor", group="σ")
public class Delta_Motor extends Delta<DcMotor> {
  @Override
  public void init() {
    init("motor");
  }

  @Override
  public void loop() {
    device.setPower(gamepad1.left_stick_y == 0 ? 0 : -gamepad1.left_stick_y);
    updateTelemetry();
  }

  protected void updateTelemetry() {
    updateTelemetry("Motor Power", device.getPower());
  }
}
