package org.firstinspires.ftc.transfinity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    updateTime();
    setDevices();
  }

  @Override
  public void init_loop() {
    if (status != Status.FAILED) {
      Sigma sigma = getClass().getAnnotation(Sigma.class);
      updateTelemetry(Status.READY, "Sigma", sigma == null ? "null" : sigma.value());
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
    updateTime();
  }

  @Override
  public void stop() {
    if (status != Status.FAILED)
      updateTelemetry(Status.STOPPED);
  }

  protected void updateTelemetry(LinkedHashMap<String, Object> telemetryData) {
    telemetry.addData("Status", status);
    telemetry.addData("Time", getTime());
    for (Map.Entry<String, Object> entry: telemetryData.entrySet()) {
      telemetry.addData(entry.getKey(), entry.getValue());
    }
    telemetry.update();
  }

  protected void updateTelemetry(Status status, LinkedHashMap<String, Object> telemetryData) {
    this.status = status;
    updateTelemetry(telemetryData);
  }

  protected void updateTelemetry(String caption, Object value) {
    LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(1);
    telemetryData.put(caption, value);
    updateTelemetry(telemetryData);
  }

  protected void updateTelemetry(Status status, String caption, Object value) {
    this.status = status;
    updateTelemetry(caption, value);
  }

  protected void updateTelemetry(Status status) {
    updateTelemetry(status, new LinkedHashMap<>(0));
  }

  protected void updateTime() {
    updateTelemetry(new LinkedHashMap<>(0));
  }

  private void setDevices() {
    Sigma sigmaAnnotation = getClass().getAnnotation(Sigma.class);
    String sigma = sigmaAnnotation == null ? "null" : sigmaAnnotation.value();
    try {
      HashMap<String, Class<? extends HardwareDevice>> deviceMap = new HashMap<>();
      switch (sigma) {
        case "ζ": break;
        case "α":
          deviceMap.put("driveLeft", DcMotor.class);
          deviceMap.put("driveRight", DcMotor.class);
          break;
        case "β":
          deviceMap.put("armPitch", DcMotor.class);
          deviceMap.put("armGrabLeft", Servo.class);
          deviceMap.put("armGrabRight", Servo.class);
          break;
        case "γ":
          deviceMap.put("driveLeft", DcMotor.class);
          deviceMap.put("driveRight", DcMotor.class);
          deviceMap.put("carousel", DcMotor.class);
          break;
        case "λ-0": deviceMap.put("carousel", DcMotor.class); break;
        default: {
         if (sigma.matches("δ-\\d+")) {
           int delta = Integer.parseInt(sigma.substring(2));
           Class<? extends HardwareDevice> device;
           switch (delta) {
             case 0: device = DcMotor.class; break;
             case 1: device = Servo.class; break;
             default: throw new IllegalArgumentException();
           }
           deviceMap.put("device", device);
         } else throw new IllegalArgumentException();
        }
      }
      for (Map.Entry<String, Class<? extends HardwareDevice>> entry: deviceMap.entrySet()) {
        String deviceName = entry.getKey();
        Class<? extends HardwareDevice> deviceType = entry.getValue();
        devices.put(deviceName, hardwareMap.get(deviceType, deviceName));
      }
    } catch (IllegalArgumentException err) {
      updateTelemetry(Status.FAILED, "Reason", "Sigma: " + sigma);
    }
  }

  private String getTime() {
    long seconds = Math.round(getRuntime());
    List<Long> time = new ArrayList<>(4);
    StringBuilder format = new StringBuilder("%02d:%02d");
    time.add(SECONDS.toMinutes(seconds % HOURS.toSeconds(1)));
    time.add(seconds % MINUTES.toSeconds(1));
    long hours = SECONDS.toHours(seconds);
    if (hours != 0) {
      time.add(0, hours);
      format.insert(0, "%d:");
    }
    return String.format(Locale.ENGLISH, format.toString(), time.toArray());
  }
}
