package org.firstinspires.ftc.transfinity;

import java.util.LinkedHashMap;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Sigma("ε")
@TeleOp(name="Epsilon - Arm Pitch", group="δ")
public class Epsilon extends Zeta {
  private static final String[] MODES = { "Adjust", "Toggle", "Hold" };
  private final Servo[] servos = new Servo[2];
  private int mode = 0;
  private boolean modePressed = false;
  private boolean toggle = false;
  private boolean togglePressed = false;

  @Override
  public void init() {
    super.init();
    servos[0] = (Servo) devices.get("servo0");
    servos[1] = (Servo) devices.get("servo1");
    if (servos[0] == null || servos[1] == null) {
      if (status != Status.FAILED)
        updateTelemetry(Status.FAILED);
      return;
    }
    for (Servo servo: servos) {
      servo.setDirection(Servo.Direction.REVERSE);
      servo.setPosition(0);
    }
  }

  @Override
  public void loop() {
    if (!modePressed && gamepad1.a) modePressed = true;
    else if (modePressed && !gamepad1.a) {
      modePressed = false;
      mode++; mode %= 3;
      toggle = togglePressed = false;
    }
    double pos;
    if (mode == 1) {
      if (!togglePressed && gamepad1.b) togglePressed = true;
      else if (togglePressed && !gamepad1.b) {
        togglePressed = false;
        toggle = !toggle;
      }
      pos = toggle ? 1 : 0;
    } else switch (mode) {
      case 0: pos = Math.min(Math.max(servos[0].getPosition() - gamepad1.right_stick_y / 100, 0), 1); break;
      case 2: pos = gamepad1.right_trigger; break;
      default: mode = 0; return;
    }
    for (Servo servo: servos) servo.setPosition(pos);
    updateTelemetry();
  }

  private void updateTelemetry() {
    LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(2);
    telemetryData.put("Servo Position", servos[0].getPosition());
    telemetryData.put("Mode", MODES[mode]);
    updateTelemetry(telemetryData);
  }
}