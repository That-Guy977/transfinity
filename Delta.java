package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareDevice;

@TeleOp(name="Delta", group="δ")
public class Delta extends Zeta {
  private HardwareDevice device;

  @Override
  public void init() {
    super.init();
    device = devices.get("device");
    if (device == null) {
      if (status != Status.FAILED)
        updateTelemetry(Status.FAILED);
      return;
    }
    device.resetDeviceConfigurationForOpMode();
  }

  @Override
  public void loop() {
    updateTelemetry();
  }

  private void updateTelemetry() {
    updateTelemetry("Device", device.getDeviceName());
  }
}