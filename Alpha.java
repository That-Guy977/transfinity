package org.firstinspires.ftc.transfinity;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;

@Sigma("α")
@TeleOp(name="Alpha", group="α")
public class Alpha extends Zeta {
  private static final List<String> DRIVE_ORDER = Arrays.asList("driveLeft", "driveRight");
  private Map<String, DcMotor> drive;

  @Override
  public void init() {
    super.init();
    drive = devices
      .entrySet()
      .stream()
      .filter((entry) -> entry.getKey().startsWith("drive") && entry.getValue() instanceof DcMotor)
      .sorted(Comparator.comparingInt(entry -> DRIVE_ORDER.indexOf(entry.getKey())))
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

  @Override
  public void loop() {
    drive.get("driveLeft").setPower(-gamepad1.left_stick_y);
    drive.get("driveRight").setPower(-gamepad1.right_stick_y);
    updateTelemetry();
  }

  private void updateTelemetry() {
    LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(1);
    telemetryData.put("Motor Power", String.format(Locale.ENGLISH, "[%.2f, %.2f]", drive.values().stream().map(DcMotor::getPower).toArray()));
    updateTelemetry(telemetryData);
  }
}
