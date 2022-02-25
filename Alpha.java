package org.firstinspires.ftc.transfinity;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;

@Sigma("α")
@TeleOp(name="Alpha", group="α")
public class Alpha extends Zeta {
  protected static final String[] DRIVE_ORDER = { "driveFrontLeft", "driveFrontRight", "driveRearLeft", "driveRearRight" };
  protected Map<String, DcMotor> drive;

  @Override
  public void init() {
    super.init();
    drive = devices
      .entrySet()
      .stream()
      .filter((entry) -> entry.getKey().startsWith("drive") && entry.getValue() instanceof DcMotor)
      .collect(Collectors.<Map.Entry<String, HardwareDevice>, String, DcMotor, LinkedHashMap<String, DcMotor>>toMap(Map.Entry::getKey, (entry) -> (DcMotor) entry.getValue(), (a, b) -> a, LinkedHashMap::new));
    if (drive.containsValue(null)) {
      if (status != Status.FAILED)
        updateTelemetry(Status.FAILED);
      return;
    }
    for (Map.Entry<String, DcMotor> entry: drive.entrySet()) {
      DcMotor motor = entry.getValue();
      motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
      motor.setDirection(entry.getKey().endsWith("Right") ? DcMotor.Direction.FORWARD : DcMotor.Direction.REVERSE);
    }
  }

  protected void updateTelemetry() {
    LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(1);
    telemetryData.put("Motor Power", String.format(Locale.ENGLISH, "[%.2f, %.2f, %.2f, %.2f]", drive.values().stream().map(DcMotor::getPower).toArray()));
    updateTelemetry(telemetryData);
  }
}
