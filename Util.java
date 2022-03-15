package org.firstinspires.ftc.transfinity;

import java.lang.annotation.*;
import java.util.Locale;

import com.qualcomm.robotcore.hardware.HardwareDevice;

@Sigma("δ")
abstract class Delta<T extends HardwareDevice> extends Zeta {
  protected T device;
  protected Class<T> type;

  protected void init(String deviceName) {
    device = hardwareMap.get(type, deviceName);
    if (device == null && status != Status.FAILED)
      setFailed(String.format(Locale.ENGLISH, "null %s", deviceName));
  }

  @Override
  public abstract void init();
  @Override
  public abstract void loop();
  protected abstract void updateTelemetry();
}

enum Status {
  INITIALIZING,
  READY,
  ACTIVE,
  STOPPED,
  FAILED
}

enum Team {
  RED, BLUE
}

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Sigma { String value(); }
