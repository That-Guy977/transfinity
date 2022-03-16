package org.firstinspires.ftc.transfinity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.concurrent.TimeUnit.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

abstract class Zeta<T extends GroupController> extends OpMode {
  protected Status status = Status.INITIALIZING;
  protected T controller;

  @Override
  public void init() {
    if (checkNull()) return;
    controller.init();
  }

  @Override
  public void init_loop() {
    if (status != Status.FAILED)
      updateTelemetry(Status.READY);
  }

  @Override
  public void start() {
    if (status != Status.FAILED)
      updateTelemetry(Status.ACTIVE);
    else requestOpModeStop();
  }

  @Override
  public void loop() {
    controller.update();
    updateTelemetry("State", controller);
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

  protected void updateTelemetry(String caption, Object value) {
    LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(1);
    telemetryData.put(caption, value);
    updateTelemetry(telemetryData);
  }

  protected void updateTelemetry(Status status) {
    this.status = status;
    updateTelemetry(new LinkedHashMap<>(0));
  }

  protected void updateTelemetry() {
    LinkedHashMap<String, Object> telemetryData = new LinkedHashMap<>(controller.controllers.size() + 1);
    telemetryData.put("State", controller);
    controller.controllers.forEach(
      (controller) -> telemetryData.put(
        controller.getClass()
          .getSimpleName()
          .replace("Controller", ""),
        controller
      )
    );
    updateTelemetry(telemetryData);
  }

  protected boolean checkNull() {
    if (controller.hasNull()) {
      updateTelemetry(Status.FAILED);
      updateTelemetry("Reason", "null in controller");
      return true;
    } else return false;
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
