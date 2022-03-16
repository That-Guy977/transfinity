package org.firstinspires.ftc.transfinity;

import java.util.Locale;

import com.qualcomm.robotcore.hardware.*;

class BetaController extends GroupController {
  final ArmGrab armGrab;
  final ArmPitch armPitch;
  private boolean grabbed = false;
  private boolean grabToggle = false;

  class ArmGrab extends HardwareDevicePair<Servo> {
    private boolean active = false;

    ArmGrab() {
      super(hardwareMap, "armGrab", Servo.class);
      if (hasNull()) return;
      deviceLeft.setDirection(Servo.Direction.FORWARD);
      deviceRight.setDirection(Servo.Direction.FORWARD);
    }

    @Override
    public void init() {
      disable();
    }

    void setGrabbed(boolean grabbed) {
      if (!active) enable();
      deviceLeft.setPosition(grabbed ? 0 : 1);
      deviceRight.setPosition(grabbed ? 1 : 0);
    }

    void enable() {
      active = true;
      deviceLeft.scaleRange(0.5, 0.75);
      deviceRight.scaleRange(0.25, 0.5);
      deviceLeft.setPosition(1);
      deviceRight.setPosition(0);
    }

    void disable() {
      active = false;
      deviceLeft.scaleRange(0, 1);
      deviceRight.scaleRange(0, 1);
      deviceLeft.setPosition(1);
      deviceRight.setPosition(0);
    }

    @Override
    public String toString() {
      return String.valueOf(deviceLeft.getPosition() == 0);
    }
  }

  class ArmPitch extends HardwareDeviceSingle<DcMotor> {
    private static final double TICKS_PER_REV = 1440;

    ArmPitch() {
      super(hardwareMap, "armPitch", DcMotor.class);
      if (device == null) return;
      device.setDirection(DcMotor.Direction.REVERSE);
      device.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void init() {
      device.setPower(-0.1);
    }

    double getPosition() {
      return device.getCurrentPosition() / TICKS_PER_REV;
    }

    void setPosition(double position) {
      device.setTargetPosition((int) Math.round(Math.min(Math.max(position, 0), 1) * TICKS_PER_REV));
    }

    void start() {
      device.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      device.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      device.setPower(0.5);
    }

    @Override
    public String toString() {
      return String.format(Locale.ENGLISH, "%.2f", getPosition());
    }
  }

  BetaController(HardwareMap hardwareMap, Gamepad gamepad) {
    super(hardwareMap, gamepad);
    armGrab = new ArmGrab();
    armPitch = new ArmPitch();
    controllers.add(armGrab);
    controllers.add(armPitch);
  }

  void update() {
    if (grabToggle != gamepad.a) {
      grabToggle = !grabToggle;
      if (!grabToggle)
        grabbed = !grabbed;
    }
    armGrab.setGrabbed(grabbed);
    armPitch.setPosition(armPitch.getPosition() + Boolean.compare(gamepad.dpad_up, gamepad.dpad_down) * 1e-1);
  }

  @Override
  public String toString() {
    return String.format(Locale.ENGLISH, "[%s, %s]", armPitch, armGrab);
  }
}
