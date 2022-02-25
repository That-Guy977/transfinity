package org.firstinspires.ftc.transfinity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.Map.Entry;
import static java.util.concurrent.TimeUnit.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.*;

@Sigma("ζ")
@TeleOp(name="Zeta", group="ζ")
public class Zeta extends OpMode {
  protected Status status = Status.INITIALIZING;
  protected Map<String, HardwareDevice> devices = new HashMap<>();

  @Override
  public void init() {
    updateTelemetry(new LinkedHashMap<>(0));
    setDevices();
  }

  @Override
  public void init_loop() {
    if (status != Status.FAILED) {
      Sigma sigma = getClass().getAnnotation(Sigma.class);
      LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(1);
      telemetryData.put("Sigma", sigma == null ? "null" : sigma.value());
      updateTelemetry(Status.READY, telemetryData);
    }
  }

  @Override
  public void start() {
    if (status != Status.FAILED)
      updateTelemetry(Status.ACTIVE);
    else requestOpModeStop();
  }

  @Override
  public void loop() {
    updateTelemetry(new LinkedHashMap<>(0));
  }

  @Override
  public void stop() {
    if (status != Status.FAILED)
      updateTelemetry(Status.STOPPED);
  }

  protected void updateTelemetry(LinkedHashMap<String, Object> telemetryData) {
    telemetry.addData("Status", status);
    telemetry.addData("Time", getTime());
    for (Entry<String, Object> entry: telemetryData.entrySet()) {
      telemetry.addData(entry.getKey(), entry.getValue());
    }
    telemetry.update();
  }

  protected void updateTelemetry(Status status, LinkedHashMap<String, Object> telemetryData) {
    this.status = status;
    updateTelemetry(telemetryData);
  }

  protected void updateTelemetry(Status status) {
    updateTelemetry(status, new LinkedHashMap<>(0));
  }

  private void setDevices() {
    Sigma sigma = getClass().getAnnotation(Sigma.class);
    try {
      if (sigma == null) throw new IllegalArgumentException();
      HashMap<String, Class<? extends HardwareDevice>> deviceMap = new HashMap<>();
      switch (sigma.value()) {
        case "ζ": break;
        case "α":
          deviceMap.put("driveFrontLeft", DcMotor.class);
          deviceMap.put("driveFrontRight", DcMotor.class);
          deviceMap.put("driveRearLeft", DcMotor.class);
          deviceMap.put("driveRearRight", DcMotor.class);
          break;
        case "δ-0": deviceMap.put("motor", DcMotor.class); break;
        case "δ-1": deviceMap.put("servo", Servo.class); break;
        case "λ-0":
          deviceMap.put("driveLeft", DcMotor.class);
          deviceMap.put("driveRight", DcMotor.class);
          break;
        case "ε":
          deviceMap.put("servo0", Servo.class);
          deviceMap.put("servo1", Servo.class);
          break;
        default: throw new IllegalArgumentException();
      }
      for (Entry<String, Class<? extends HardwareDevice>> entry: deviceMap.entrySet()) {
        String deviceName = entry.getKey();
        Class<? extends HardwareDevice> deviceType = entry.getValue();
        devices.put(deviceName, hardwareMap.get(deviceType, deviceName));
      }
    } catch (IllegalArgumentException e) {
      LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(1);
      telemetryData.put("Sigma", sigma == null ? "null" : sigma.value());
      updateTelemetry(Status.FAILED, telemetryData);
    }
  }

  private String getTime() {
    long ms = Math.round(getRuntime() * 1000);
    List<Long> time = new ArrayList<>(4);
    StringBuilder format = new StringBuilder("%02d:%02d.%03d");
    time.add(MILLISECONDS.toMinutes(ms % HOURS.toMillis(1)));
    time.add(MILLISECONDS.toSeconds(ms % MINUTES.toMillis(1)));
    time.add(ms % SECONDS.toMillis(1));
    long hours = MILLISECONDS.toHours(ms);
    if (hours != 0) {
      time.add(0, hours);
      format.insert(0, "%d:");
    }
    return String.format(Locale.ENGLISH, format.toString(), time.toArray());
  }
}
