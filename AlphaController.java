package org.firstinspires.ftc.transfinity;

import java.util.Locale;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotor;

class AlphaController extends GroupController<Controller> {
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
      if (gamepadDeviation < 0.1) return 0;
      else if (gamepadDeviation > 0.9) return -Math.copySign(1, gamepadStick);
      else return -Math.copySign(0.5, gamepadStick);
    }

    @Override
    public String toString() {
      return String.format(Locale.ENGLISH, "[%.1f, %.1f]", deviceLeft.getPower(), deviceRight.getPower());
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
