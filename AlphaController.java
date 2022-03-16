package org.firstinspires.ftc.transfinity;

import java.util.Locale;

import com.qualcomm.robotcore.hardware.*;

class AlphaController extends GroupController {
  final Drive drive;

  class Drive extends HardwareDevicePair<DcMotor> {
    Drive() {
      super(hardwareMap, "drive", DcMotor.class);
      if (hasNull()) return;
      deviceLeft.setDirection(DcMotor.Direction.REVERSE);
      deviceRight.setDirection(DcMotor.Direction.FORWARD);
    }

    @Override
    public void init() {
      setPower(0, 0);
    }

    void setPower(double powerLeft, double powerRight) {
      deviceLeft.setPower(powerLeft);
      deviceRight.setPower(powerRight);
    }

    double motorPower(double gamepadStick) {
      double gamepadDeviation = Math.abs(gamepadStick);
      if (gamepadDeviation < 0.2) return 0;
      else if (gamepadDeviation > 0.8) return -Math.copySign(1, gamepadStick);
      else return -Math.copySign(0.5, gamepadStick);
    }

    @Override
    public String toString() {
      return String.format(Locale.ENGLISH, "[%.2f, %.2f]", deviceLeft.getPower(), deviceRight.getPower());
    }
  }

  AlphaController(HardwareMap hardwareMap, Gamepad gamepad) {
    super(hardwareMap, gamepad);
    drive = new Drive();
    controllers.add(drive);
  }

  @Override
  void update() {
    double powerLeft = drive.motorPower(gamepad.left_stick_y);
    double powerRight = drive.motorPower(gamepad.right_stick_y);
    drive.setPower(powerLeft, powerRight);
  }

  @Override
  public String toString() {
    return drive.toString();
  }
}
