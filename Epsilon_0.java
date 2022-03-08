package org.firstinspires.ftc.transfinity;

import java.util.LinkedHashMap;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Sigma("β")
@TeleOp(name="Epsilon - Arm Pitch", group="δ")
public class Epsilon_0 extends Zeta {
  private static final int TICKS_PER_REV = 1440;
  private static final String[] MODES = { "Adjust", "Toggle", "Hold" };
  private DcMotor armPitch;
  private int mode = 0;
  private boolean modePressed = false;
  private boolean toggle = false;
  private boolean togglePressed = false;

  @Override
  public void init() {
    super.init();
    armPitch = (DcMotor) devices.get("armPitch");
    if (armPitch == null) {
      if (status != Status.FAILED)
        updateTelemetry(Status.FAILED);
      return;
    }
    armPitch.setDirection(DcMotor.Direction.FORWARD);
    armPitch.setPower(-0.05);
  }

  @Override
  public void start() {
    super.start();
    if (status != Status.FAILED) {
      armPitch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      armPitch.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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
    int pos;
    if (mode == 1) {
      if (!togglePressed && gamepad1.b) togglePressed = true;
      else if (togglePressed && !gamepad1.b) {
        togglePressed = false;
        toggle = !toggle;
      }
      pos = toggle ? TICKS_PER_REV / 2 : 0;
    } else switch (mode) {
      case 0: pos = Math.min(Math.max(armPitch.getCurrentPosition() - Math.round(gamepad1.right_stick_y * TICKS_PER_REV / 10), 0), TICKS_PER_REV / 2); break;
      case 2: pos = Math.round(gamepad1.right_trigger * TICKS_PER_REV / 2); break;
      default: mode = 0; return;
    }
    armPitch.setTargetPosition(pos);
    updateTelemetry();
  }

  private void updateTelemetry() {
    LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(2);
    telemetryData.put("Motor Position", armPitch.getCurrentPosition());
    telemetryData.put("Mode", MODES[mode]);
    updateTelemetry(telemetryData);
  }
}
