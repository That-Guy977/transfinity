package org.firstinspires.ftc.transfinity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.concurrent.TimeUnit.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Sigma("ζ")
@TeleOp(name="Zeta", group="ζ")
public class Zeta extends OpMode {
  protected Status status = Status.INITIALIZING;

  @Override
  public void init() {}

  @Override
  public void init_loop() {
    if (status != Status.FAILED) {
      if (status != Status.READY) updateTelemetry(Status.READY);
      Sigma sigma = getClass().getAnnotation(Sigma.class);
      updateTelemetry("Sigma", sigma == null ? "null" : sigma.value());
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
    for (Map.Entry<String, Object> entry: telemetryData.entrySet())
      telemetry.addData(entry.getKey(), entry.getValue());
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

  protected void updateTelemetry(Status status) {
    updateTelemetry(status, new LinkedHashMap<>(0));
  }

  protected void updateTime() {
    updateTelemetry(new LinkedHashMap<>(0));
  }

  protected void setFailed(Object value) {
    this.status = Status.FAILED;
    updateTelemetry("Reason", value);
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
