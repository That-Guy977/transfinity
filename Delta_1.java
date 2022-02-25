package org.firstinspires.ftc.transfinity;

import java.util.LinkedHashMap;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Sigma("δ-1")
@TeleOp(name="Delta 1 - Servo", group="δ")
public class Delta_1 extends Zeta {
  private Servo servo;
  private boolean absolute = true;
  private boolean modePressed = false;

  @Override
  public void init() {
    super.init();
    servo = (Servo) devices.get("servo");
    if (servo == null) {
      if (status != Status.FAILED)
        updateTelemetry(Status.FAILED);
      return;
    }
    servo.setDirection(Servo.Direction.FORWARD);
    servo.setPosition(0);
  }

  @Override
  public void loop() {
    if (!modePressed && gamepad1.a) modePressed = true;
    else if (modePressed && !gamepad1.a) {
      modePressed = false;
      absolute = !absolute;
    }
    double pos = Math.min(1, Math.max(0,
      absolute
        ? gamepad1.right_trigger
        : servo.getPosition() - gamepad1.right_stick_y / 100
    ));
    servo.setPosition(pos);
    updateTelemetry();
  }

  private void updateTelemetry() {
    LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(2);
    telemetryData.put("Servo Position", servo.getPosition());
    telemetryData.put("Mode", absolute ? "Absolute" : "Relative");
    updateTelemetry(telemetryData);
  }
}
