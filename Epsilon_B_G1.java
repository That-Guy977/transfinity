package org.firstinspires.ftc.transfinity;

import java.util.LinkedHashMap;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Sigma("β")
@TeleOp(name="Epsilon β - Arm Grab 1S", group="δ")
public class Epsilon_B_G1 extends Zeta {
  private static final String[] MODES = { "Adjust", "Toggle", "Hold" };
  private Servo armGrab;
  private int mode = 0;
  private boolean modePressed = false;
  private boolean toggle = false;
  private boolean togglePressed = false;


  @Override
  public void init() {
    super.init();
    armGrab = (Servo) devices.get("armGrab");
    if (armGrab == null) {
      if (status != Status.FAILED)
        updateTelemetry(Status.FAILED);
      return;
    }
    armGrab.setDirection(Servo.Direction.FORWARD);
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
      case 0: pos = Math.min(Math.max(armGrab.getPosition() - gamepad1.right_stick_y / 100, 0), 1); break;
      case 2: pos = gamepad1.right_trigger; break;
      default: mode = 0; return;
    }
    armGrab.setPosition(pos);
    updateTelemetry();
  }

  private void updateTelemetry() {
    LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(2);
    telemetryData.put("Servo Position", armGrab.getPosition());
    telemetryData.put("Mode", MODES[mode]);
    updateTelemetry(telemetryData);
  }
}
