package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Kappa (BC)", group="κ", preselectTeleOp="TeleOp (Blue)")
public class KappaBlueCarousel extends Kappa {
  @Override
  public void init() {
    init(Team.BLUE, Position.CAROUSEL);
  }

  @Override
  public void loop() {
    gamepad.reset();
    double time = getRuntime();
    if (time < 0.1) idle();
    else if (time < 7.2) preload();
    else if (time < 8.9) forwards();
    else if (time < 9.2) idle();
    else if (time < 10.4) weakBackRight();
    else if (time < 10.6) backwards();
    else if (time < 10.7) idle();
    else if (time < 11.42) weakBackLeft();
    else if (time < 11.5) idle();
    else if (time < 13.9) carousel();
    else if (time < 14.1) idle();
    else if (time < 28.0) warehouse();
    super.loop();
  }
}
