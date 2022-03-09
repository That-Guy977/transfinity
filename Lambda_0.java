package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Sigma("λ-0")
@TeleOp(name="Lambda 0 - Carousel", group="ε")
public class Lambda_0 extends Zeta {
  private DcMotor motor;

  @Override
  public void init() {
    super.init();
    motor = (DcMotor) devices.get("carousel");
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
    motor.setPower(Boolean.compare(gamepad1.dpad_right, gamepad1.dpad_left));
    updateTelemetry();
  }

  private void updateTelemetry() {
    updateTelemetry("Motor Power", motor.getPower());
  }
}