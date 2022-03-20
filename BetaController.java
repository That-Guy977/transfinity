package org.firstinspires.ftc.transfinity;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

class BetaController extends GroupController<Controller> {
  final ArmGrab armGrab;
  final ArmPitch armPitch;
  private boolean grabbed = false;
  private boolean grabToggle = false;

  class ArmGrab extends HardwareDevicePair<Servo> {
    ArmGrab() {
      super(hardwareMap, "armGrab", Servo.class);
      if (hasNull()) return;
      deviceLeft.setDirection(Servo.Direction.FORWARD);
      deviceRight.setDirection(Servo.Direction.FORWARD);
    }

    @Override
    public void init() {
      deviceLeft.scaleRange(0.5, 0.75);
      deviceRight.scaleRange(0.25, 0.5);
      deviceLeft.setPosition(1);
      deviceRight.setPosition(0);
    }

    void setGrabbed(boolean grabbed) {
      deviceLeft.setPosition(grabbed ? 0 : 1);
      deviceRight.setPosition(grabbed ? 1 : 0);
    }

    @Override
    public String toString() {
      return String.valueOf(deviceLeft.getPosition() == 0);
    }
  }

  class ArmPitch extends HardwareDeviceSingle<DcMotor> {
    private static final int RANGE = 690;
    private static final int TOLERANCE = 25;
    private boolean started = false;
    private int min = 0;
    private int max = 0;

    ArmPitch() {
      super(hardwareMap, "armPitch", DcMotor.class);
      if (hasNull()) return;
      device.setDirection(DcMotor.Direction.REVERSE);
      device.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void init() {
      device.setPower(-0.1);
      Timer timer = new Timer(true);
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          start();
          timer.cancel();
        }
      }, 3000);
    }

    void start() {
      if (!started) {
        started = true;
        device.setPower(0);
        min = device.getCurrentPosition();
        max = min + RANGE;
      }
    }

    double getPosition() {
      if (!started) return 0;
      return (double) (device.getCurrentPosition() - min) / RANGE;
    }

    void updateDir(int dir) {
      int position = device.getCurrentPosition();
      if (dir > 0 && position < max - TOLERANCE) {
        if (position >= max - TOLERANCE * 6) device.setPower(0.4);
        else device.setPower(0.8);
      } else if (dir < 0 && position > min + TOLERANCE * 2) {
        if (position <= min + TOLERANCE * 12) device.setPower(-0.1);
        else device.setPower(-0.4);
      } else device.setPower(0);
    }

    @Override
    public String toString() {
      return String.format(Locale.ENGLISH, "%.1f", device.getPower());
    }
  }

  BetaController(HardwareMap hardwareMap, Gamepad gamepad) {
    super(hardwareMap, gamepad);
    armGrab = new ArmGrab();
    armPitch = new ArmPitch();
    controllers.add(armGrab);
    controllers.add(armPitch);
  }

  @Override
  void update() {
    if (grabToggle != gamepad.a) {
      grabToggle = !grabToggle;
      if (!grabToggle)
        grabbed = !grabbed;
    }
    armGrab.setGrabbed(grabbed);
    armPitch.updateDir(Boolean.compare(gamepad.dpad_up, gamepad.dpad_down));
  }

  @Override
  public String toString() {
    return String.format(Locale.ENGLISH, "[%s, %s]", armGrab, armPitch);
  }
}
