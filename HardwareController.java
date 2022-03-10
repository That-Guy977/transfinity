package org.firstinspires.ftc.transfinity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    void setPower(double powerLeft, double powerRight) {
      deviceLeft.setPower(powerLeft);
      deviceRight.setPower(powerRight);
    }

    @Override
    public String toString() {
      return String.format(Locale.ENGLISH, "[%.2f, %.2f]", deviceLeft.getPower(), deviceRight.getPower());
    }
  }

  AlphaController(HardwareMap hardwareMap, Gamepad gamepad) {
    super(hardwareMap, gamepad);
    drive = new Drive();
    groups.add(drive);
  }

  void update() {
    double powerLeft = gamepad.left_stick_y == 0 ? 0 : -gamepad.left_stick_y;
    double powerRight = gamepad.right_stick_y == 0 ? 0 : -gamepad.right_stick_y;
    drive.setPower(powerLeft, powerRight);
  }

  @Override
  public String toString() {
    return drive.toString();
  }
}

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
      device.setDirection(DcMotor.Direction.FORWARD);
      device.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    double getPosition() {
      return device.getCurrentPosition() / TICKS_PER_REV;
    }

    void setPosition(double position) {
      device.setTargetPosition((int) Math.round(Math.min(Math.max(position, 0), 1) * TICKS_PER_REV));
    }

    void init() {
      device.setPower(-0.01);
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
    groups.add(armGrab);
    groups.add(armPitch);
    armGrab.disable();
    armPitch.init();
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

class LambdaController extends GroupController {
  final Carousel carousel;

  class Carousel extends HardwareDeviceSingle<DcMotor> {
    Carousel() {
      super(hardwareMap, "carousel", DcMotor.class);
      if (device == null) return;
      device.setDirection(DcMotor.Direction.FORWARD);
      device.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    int getDirection() {
      return (int) device.getPower();
    }

    void setDirection(int direction) {
      device.setPower(direction);
    }

    @Override
    public String toString() {
      return String.format(Locale.ENGLISH, "%d", getDirection());
    }
  }

  LambdaController(HardwareMap hardwareMap, Gamepad gamepad) {
    super(hardwareMap, gamepad);
    carousel = new Carousel();
    groups.add(carousel);
  }

  void update() {
    int direction = Boolean.compare(gamepad.dpad_right, gamepad.dpad_left);
    carousel.setDirection(direction);
  }

  @Override
  public String toString() {
    return carousel.toString();
  }
}

// class ThetaController extends GroupController {}

class GammaController implements Controller {
  private final List<GroupController> controllers = new ArrayList<>();
  protected final AlphaController alpha;
  protected final BetaController beta;
  protected final LambdaController lambda;
  // protected final ThetaController theta;

  GammaController(HardwareMap hardwareMap, Gamepad... gamepads) {
    alpha = new AlphaController(hardwareMap, gamepads[0]);
    beta = new BetaController(hardwareMap, gamepads[1]);
    lambda = new LambdaController(hardwareMap, gamepads[0]);
    // theta = new ThetaController(hardwareMap, gamepads[1]);
    controllers.addAll(Arrays.asList(alpha, beta, lambda/*, theta*/));
  }

  void update() {
    controllers.forEach(GroupController::update);
  }

  @Override
  public String toString() {
    return Arrays.toString(
      controllers.stream()
        .map(GroupController::getClass)
        .map(Class::getSimpleName)
        .map((name) -> name.replace("Controller", ""))
        .toArray()
    );
  }

  @Override
  public boolean hasNull() {
    return controllers.stream().reduce(false, (acc, controller) -> acc || controller.hasNull(), (a, b) -> a || b);
  }
}

abstract class GroupController implements Controller {
  protected final List<Controller> groups = new ArrayList<>();
  protected final HardwareMap hardwareMap;
  protected final Gamepad gamepad;

  GroupController(HardwareMap hardwareMap, Gamepad gamepad) {
    this.hardwareMap = hardwareMap;
    this.gamepad = gamepad;
  }

  abstract void update();
  @Override
  public boolean hasNull() {
    return groups.stream().reduce(false, (acc, group) -> acc || group.hasNull(), (a, b) -> a || b);
  }
}

abstract class HardwareDeviceSingle<T extends HardwareDevice> implements Controller {
  protected final T device;
  protected final String deviceName;

  HardwareDeviceSingle(HardwareMap hardwareMap, String deviceName, Class<T> type) {
    this.deviceName = deviceName;
    this.device = hardwareMap.tryGet(type, deviceName);
  }

  @Override
  public boolean hasNull() {
    return device == null;
  }
}

abstract class HardwareDevicePair<T extends HardwareDevice> implements Controller {
  protected final T deviceLeft, deviceRight;
  protected final String deviceName;

  HardwareDevicePair(HardwareMap hardwareMap, String deviceName, Class<T> type) {
    this.deviceName = deviceName;
    this.deviceLeft = hardwareMap.tryGet(type, deviceName + "Left");
    this.deviceRight = hardwareMap.tryGet(type, deviceName + "Right");
  }

  @Override
  public boolean hasNull() {
    return deviceLeft == null || deviceRight == null;
  }
}

interface Controller {
  @Override
  String toString();
  boolean hasNull();
}
