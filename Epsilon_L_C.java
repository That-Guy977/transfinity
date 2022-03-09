package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Sigma("δ-0")
@TeleOp(name="Epsilon λ - Carousel", group="ε")
public class Epsilon_L_C extends Zeta {
  private DcMotor motor;

  @Override
  public void init() {
    super.init();
    motor = (DcMotor) devices.get("device");
    if (motor == null) {
      if (status != Status.FAILED)
        updateTelemetry(Status.FAILED);
      return;
    }
    motor.setDirection(DcMotor.Direction.FORWARD);
    motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  }

  @Override
  public void loop() {
    motor.setPower(gamepad1.right_stick_y == 0 ? 0 : -gamepad1.right_stick_y);
    updateTelemetry();
  }

  private void updateTelemetry() {
    updateTelemetry("Motor Power", motor.getPower());
  }
}