package org.firstinspires.ftc.transfinity;

import java.util.Locale;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

@TeleOp(name="Epsilon", group="ε")
public class Epsilon extends OpMode {
  private static final String VUFORIA_KEY = "Aaahf67/////AAABmXoyVpXjgk/fj9mpPUqqWD4CbL4yMxRH5uxjy9yyTbRgVIrDppyghWs6yMZbzvXYJ9WATIYDZjAM/aXZZjG0+n4TtS8YSX033yZtu0bbCejSkY0RAjOkVFU2k6P9tiQET+Z1LhSnAiPjV1ngBgwrEoDhxXTjE8G0LLcxrzCmgrDRogWHm0/wJZjFi3SCQFZBCYDI0KK4r2e+X+ai5dhkG+v5sVoXNfBlzgQVU8Hu3+ob13VFepGcy6DZzNiXRchVhb5bOxZ6uTo23aJYcxs9hddJ6NR0HQ4W6OZGp2voH4siHTcsC2zD/+gsl4l1At88Syx5EY8Xb1f0/boiHI48SQP+jrJS1I3shzTDQYRcv3jl";
  private static final String TFOD_MODEL_ASSET = "FreightFrenzy_DM.tflite";
  private static final String[] TFOD_LABELS = { "Duck", "Marker" };
  private TFObjectDetector tfod;

  @Override
  public void init() {
    VuforiaLocalizer.Parameters vuforiaParameters = new VuforiaLocalizer.Parameters();
    vuforiaParameters.vuforiaLicenseKey = VUFORIA_KEY;
    vuforiaParameters.cameraName = hardwareMap.tryGet(WebcamName.class, "webcam");

    TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(
      hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName())
    );
    tfodParameters.minResultConfidence = 0.5f;
    tfodParameters.isModelTensorFlow2 = true;

    VuforiaLocalizer vuforia = ClassFactory.getInstance().createVuforia(vuforiaParameters);
    tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
    tfod.loadModelFromAsset(TFOD_MODEL_ASSET, TFOD_LABELS);

    tfod.activate();
    // tfod.setZoom(1, 16.0/9.0);
  }

  @Override
  public void loop() {
    Marker[] markers = tfod.getRecognitions().stream()
    .filter(recognition -> recognition.getLabel().equals("Marker"))
    .map(Marker::new)
    .collect(() -> new Marker[3], (markerAcc, marker) -> {
      int level = marker.getPosition();
      if (level != 0) {
        Marker existingMarker = markerAcc[level - 1];
        if (existingMarker == null || existingMarker.getConfidence() < marker.getConfidence()) markerAcc[level - 1] = marker;
      }
    }, (accA, accB) -> {
      for (int i = 0; i < 3; i++)
        if (accA[i] == null || accB[i] != null && accB[i].getConfidence() > accA[i].getConfidence())
          accA[i] = accB[i];
    });
    for (Marker marker: markers)
      if (marker != null)
        telemetry.addData(marker.getLabel(), marker);
    int lowestConfPos = 1;
    for (int i = 0; i < 3; i++) {
      if (markers[i] == null || markers[i].getConfidence() < markers[lowestConfPos].getConfidence()) lowestConfPos = i;
    }
    telemetry.addData("Position", lowestConfPos);
    telemetry.update();
  }

  @Override
  public void stop() {
    tfod.shutdown();
  }
}

class Marker {
  private final Recognition recognition;

  Marker(Recognition recognition) {
    this.recognition = recognition;
  }

  int getPosition() {
    double center = getCenter();
    if (center < 0.3) return 1;
    else if (center > 0.35 && center < 0.65) return 2;
    else if (center > 0.7) return 3;
    return 0;
  }

  String getLabel() {
    return recognition.getLabel();
  }

  double getConfidence() {
    return recognition.getConfidence();
  }

  double getCenter() {
    return (recognition.getLeft() + recognition.getRight()) / 2;
  }

  @Override
  public String toString() {
    return String.format(Locale.ENGLISH, "[%.2f, { %.2f, %.2f, %.2f, %.2f }]",
      getConfidence(), recognition.getTop(), recognition.getBottom(), recognition.getLeft(), recognition.getRight());
  }
}
