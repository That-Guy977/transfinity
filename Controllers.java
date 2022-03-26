package org.firstinspires.ftc.transfinity;

import java.util.Arrays;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

class GammaController extends ControllerGroupController {
  protected final AlphaController alpha;
  protected final BetaController beta;
  protected final LambdaController lambda;

  GammaController(Team team, HardwareMap hardwareMap, Gamepad primary, Gamepad secondary) {
    super(hardwareMap, primary, secondary);
    alpha = new AlphaController(hardwareMap, primary);
    beta = new BetaController(hardwareMap, secondary);
    lambda = new LambdaController(team, hardwareMap, primary);
    controllers.addAll(Arrays.asList(alpha, beta, lambda));
  }
}

class KappaController extends ControllerGroupController {
  protected final AlphaController alpha;
  protected final BetaController beta;
  protected final LambdaController lambda;

  KappaController(Team team, HardwareMap hardwareMap, Gamepad gamepad) {
    super(hardwareMap, gamepad);
    alpha = new AlphaController(hardwareMap, gamepad);
    beta = new BetaController(hardwareMap, gamepad);
    lambda = new LambdaController(team, hardwareMap, gamepad);
    controllers.addAll(Arrays.asList(alpha, beta, lambda));
  }
}

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

class BetaController extends GroupController<Controller> {
  final ArmGrab armGrab;
  final ArmPitch armPitch;
  private boolean grabbed = false;
  private boolean grabToggle = false;
  private boolean locked = false;

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

    void setGrabbed() {
      deviceLeft.setPosition(grabbed ? 0 : 1);
      deviceRight.setPosition(grabbed ? 1 : 0);
    }

    @Override
    public String toString() {
      return String.valueOf(grabbed);
    }
  }

  class ArmPitch extends HardwareDeviceSingle<DcMotor> {
    private static final int RANGE = 720;
    private static final int TOLERANCE = 25;
    private boolean started = false;
    private int min = 0;
    private int max = 0;
    private double lockPos = -1;

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
      if (!locked && lockPos != -1) lockPos = -1;
      int position = device.getCurrentPosition();
      if (dir > 0 && position < max - TOLERANCE) {
        if (position >= max - TOLERANCE * 6) device.setPower(0.4);
        else device.setPower(0.8);
      } else if (dir < 0 && position > min + TOLERANCE * 2) {
        if (position <= min + TOLERANCE * 12) device.setPower(-0.1);
        else device.setPower(-0.4);
      } else device.setPower(0);
    }

    void lock() {
      if (lockPos == -1) lockPos = getPosition();
      if (getPosition() < lockPos) updateDir(1);
      else updateDir(0);
    }

    @Override
    public String toString() {
      String format = "%.1f (%s$)".replace("$", locked ? " %.2f" : "");
      return String.format(Locale.ENGLISH, format, device.getPower(), locked, lockPos);
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
    armGrab.setGrabbed();
    int dir = Boolean.compare(gamepad.dpad_up, gamepad.dpad_down);
    locked = gamepad.left_bumper && dir == 0;
    if (locked) armPitch.lock();
    else armPitch.updateDir(dir);
  }

  @Override
  public String toString() {
    return String.format(Locale.ENGLISH, "[%s, %s]", armGrab, armPitch);
  }
}

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
    carousel.setPowered(gamepad.right_bumper);
  }

  @Override
  public String toString() {
    return carousel.toString();
  }
}
