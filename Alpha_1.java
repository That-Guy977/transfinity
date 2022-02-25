package org.firstinspires.ftc.transfinity;

import java.util.LinkedHashMap;
import java.util.Locale;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Alpha 1 - Movement", group="α")
public class Alpha_1 extends Alpha {
  @Override
  public void loop() {
    int magX = Math.round(-gamepad1.left_stick_y * 100);
    int magY = Math.round(gamepad1.left_stick_x * 100);
    boolean stopped = magX == 0 && magY == 0;
    int mag = Math.min(Math.round((float) Math.hypot(magX, magY)), 100);
    int quad =
      magX <= 0 && magY > 0 ? 1
        : magX < 0 ? 2
        : magY < 0 ? 3
        : 0;
    int dir = stopped ? 0 : (Math.round((float) Math.toDegrees(Math.atan2(magY, magX))) + 180) % 90;

    if (!stopped) {
      double[] run = { 0, 0, 0, 0 };
      switch (quad) {
        case 0: run = new double[]{ 1d, -0d, -0d,  1d }; break;
        case 1: run = new double[]{-0d, -1d, -1d, -0d }; break;
        case 2: run = new double[]{-1d,  0d,  0d, -1d }; break;
        case 3: run = new double[]{ 0d,  1d,  1d,  0d }; break;
      }
      for (int i = 0; i < drive.size(); i++)
        drive.get(DRIVE_ORDER[i]).setPower((run[i] != 0 ? run[i] : (dir - 45) / 45d * Math.copySign(1, run[i])) * mag);
    } else for (DcMotor motor: drive.values()) motor.setPower(0);

    updateTelemetry(mag, quad, dir);
  }

  protected void updateTelemetry(int mag, int quad, int dir) {
    LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(3);
    telemetryData.put("Motor Power", String.format(Locale.ENGLISH, "[%.2f, %.2f, %.2f, %.2f]", drive.values().stream().map(DcMotor::getPower).toArray()));
    telemetryData.put("Magnitude", mag);
    telemetryData.put("Direction", String.format(Locale.ENGLISH, "[%d:%d°]", quad, dir));
    updateTelemetry(telemetryData);
  }
}