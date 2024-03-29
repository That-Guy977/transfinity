package org.firstinspires.ftc.transfinity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

abstract class ControllerGroupController extends GroupController<GroupController<? extends Controller>> {
  protected ControllerGroupController(HardwareMap hardwareMap, Gamepad gamepad) {
    this(hardwareMap, gamepad, null);
  }

  protected ControllerGroupController(HardwareMap hardwareMap, Gamepad primary, Gamepad secondary) {
    super(hardwareMap, primary, secondary);

  }

  @Override
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
}

abstract class GroupController<T extends Controller> implements Controller {
  protected final List<T> controllers = new ArrayList<>();
  protected final HardwareMap hardwareMap;
  protected final Gamepad gamepad, secondary;

  protected GroupController(HardwareMap hardwareMap, Gamepad gamepad) {
    this(hardwareMap, gamepad, null);
  }

  protected GroupController(HardwareMap hardwareMap, Gamepad primary, Gamepad secondary) {
    this.hardwareMap = hardwareMap;
    this.gamepad = primary;
    this.secondary = secondary;
  }

  abstract void update();

  @Override
  public void init() {
    controllers.forEach(Controller::init);
  }

  @Override
  public boolean hasNull() {
    return controllers.stream().reduce(false, (acc, controllers) -> acc || controllers.hasNull(), (a, b) -> a || b);
  }

  @Override
  public abstract String toString();
}

abstract class HardwareDeviceSingle<T extends HardwareDevice> implements Controller {
  protected final T device;
  protected final String deviceName;

  protected HardwareDeviceSingle(HardwareMap hardwareMap, String deviceName, Class<T> type) {
    this.deviceName = deviceName;
    this.device = hardwareMap.tryGet(type, deviceName);
  }

  @Override
  public boolean hasNull() {
    return device == null;
  }

  @Override
  public abstract String toString();
}

abstract class HardwareDevicePair<T extends HardwareDevice> implements Controller {
  protected final T deviceLeft, deviceRight;
  protected final String deviceName;

  protected HardwareDevicePair(HardwareMap hardwareMap, String deviceName, Class<T> type) {
    this.deviceName = deviceName;
    this.deviceLeft = hardwareMap.tryGet(type, deviceName + "Left");
    this.deviceRight = hardwareMap.tryGet(type, deviceName + "Right");
  }

  @Override
  public boolean hasNull() {
    return deviceLeft == null || deviceRight == null;
  }

  @Override
  public abstract String toString();
}
