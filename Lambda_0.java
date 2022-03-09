package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Sigma("λ-0")
@TeleOp(name="Lambda 0 - Carousel", group="λ")
public class Lambda_0 extends Zeta {
  private DcMotor carousel;

  @Override
  public void init() {
    super.init();
    carousel = (DcMotor) devices.get("carousel");
    if (carousel == null) {
      if (status != Status.FAILED)
        updateTelemetry(Status.FAILED);
      return;
    }
    carousel.setDirection(DcMotor.Direction.FORWARD);
    carousel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  }

  @Override
  public void loop() {
    carousel.setPower(Boolean.compare(gamepad1.dpad_right, gamepad1.dpad_left));
    updateTelemetry();
  }

  private void updateTelemetry() {
    updateTelemetry("Carousel Power", carousel.getPower());
  }
}