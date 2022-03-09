package org.firstinspires.ftc.transfinity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.qualcomm.robotcore.hardware.*;

class Drive extends HardwareDeviceGroup<DcMotor> {
  Drive(HardwareMap hardwareMap, String... deviceNames) {
    for (String deviceName: deviceNames) {
      DcMotor motor = hardwareMap.get(DcMotor.class, deviceName);
      devices.put(deviceName, motor);
      motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
      motor.setDirection(deviceName.endsWith("Right") ? DcMotor.Direction.FORWARD : DcMotor.Direction.REVERSE);
    }
  }

  void setPower(Gamepad gamepad) {
    double powerLeft = gamepad.left_stick_y == 0 ? 0 : -gamepad.left_stick_y;
    double powerRight = gamepad.right_stick_y == 0 ? 0 : -gamepad.right_stick_y;
    devices.get("driveLeft").setPower(powerLeft);
    devices.get("driveRight").setPower(powerRight);
  }

  @Override
  public String toString() {
    return String.format(Locale.ENGLISH, "[%.2f, %.2f]", devices.values().stream().map(DcMotor::getPower).toArray());
  }
}

class ArmGrab extends HardwareDeviceGroup<Servo> {
  private double position = 0;

  ArmGrab(HardwareMap hardwareMap, String... deviceNames) {
    for (String deviceName: deviceNames) {
      Servo servo = hardwareMap.get(Servo.class, deviceName);
      devices.put(deviceName, servo);
      servo.setDirection(deviceName.endsWith("Left") ? Servo.Direction.FORWARD : Servo.Direction.REVERSE);
    }
  }

  double getPosition() {
    return position;
  }

  void setPosition(double pos) {
    position = pos;
    for (Servo servo: devices.values())
      servo.setPosition(position);
  }

  @Override
  public String toString() {
    return String.format(Locale.ENGLISH, "%.2f", position);
  }
}

abstract class HardwareDeviceGroup<T extends HardwareDevice> {
  protected final Map<String, T> devices = new HashMap<>();
  @Override
  public abstract String toString();
  boolean hasNull() {
    return devices.containsValue(null);
  }
}
