package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotor;

class LambdaController extends GroupController<Controller> {
  final Carousel carousel;

  class Carousel extends HardwareDeviceSingle<DcMotor> {
    Carousel(Team team) {
      super(hardwareMap, "carousel", DcMotor.class);
      if (hasNull()) return;
      device.setDirection(team == Team.RED ? DcMotor.Direction.FORWARD : DcMotor.Direction.REVERSE);
      device.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void init() {
      setPowered(false);
    }

    boolean getPowered() {
      return device.getPower() != 0;
    }

    void setPowered(boolean active) {
      device.setPower(active ? 1 : 0);
    }

    @Override
    public String toString() {
      return String.valueOf(getPowered());
    }
  }

  LambdaController(Team team, HardwareMap hardwareMap, Gamepad gamepad) {
    super(hardwareMap, gamepad);
    carousel = new Carousel(team);
    controllers.add(carousel);
  }

  void update() {
    carousel.setPowered(gamepad.left_bumper);
  }

  @Override
  public String toString() {
    return carousel.toString();
  }
}
