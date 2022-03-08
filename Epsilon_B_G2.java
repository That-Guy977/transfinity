package org.firstinspires.ftc.transfinity;

import java.util.LinkedHashMap;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Sigma("ε-β-2S")
@TeleOp(name="Epsilon β - Arm Grab 2S-O", group="δ")
public class Epsilon_B_G2 extends Zeta {
  private static final String[] MODES = { "Adjust", "Toggle", "Hold" };
  private ServoGroup armGrab;
  private int mode = 0;
  private boolean modePressed = false;
  private boolean toggle = false;
  private boolean togglePressed = false;

  private static class ServoGroup {
    private final Servo[] servos;
    private double position = 0;
    ServoGroup(Servo ...servos) {
      this.servos = servos;
    }

    private void setDirection() {
      for (int i = 0; i < servos.length; i++) {
        servos[i].setDirection(Servo.Direction.values()[i % 2]);
      }
    }

    private double getPosition() {
      return position;
    }

    private void setPosition(double pos) {
      position = pos;
      for (Servo servo: servos)
        servo.setPosition(position);
    }

    private boolean hasNull() {
      for (Servo servo: servos) {
        if (servo == null) return true;
      }
      return false;
    }
  }

  @Override
  public void init() {
    super.init();
    armGrab = new ServoGroup((Servo) devices.get("armGrabLeft"), (Servo) devices.get("armGrabRight"));
    if (armGrab.hasNull()) {
      if (status != Status.FAILED)
        updateTelemetry(Status.FAILED);
      return;
    }
    armGrab.setDirection();
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
