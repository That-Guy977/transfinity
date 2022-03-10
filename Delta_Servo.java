package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Delta - Servo", group="σ")
public class Delta_Servo extends Delta<Servo> {
  private boolean active = false;
  private boolean toggle = false;

  @Override
  public void init() {
    init("servo");
  }

  @Override
  public void loop() {
    if (toggle != gamepad1.a) {
      toggle = !toggle;
      if (!toggle)
        active = !active;
    }
    device.setPosition(active ? 0 : 1);
    updateTelemetry();
  }

  protected void updateTelemetry() {
    updateTelemetry("Servo Position", device.getPosition());
  }
}
