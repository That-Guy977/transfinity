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
    else if (time < 10.2) weakBackRight();
    else if (time < 10.4) idle();
    else if (time < 11.4) weakBackLeft();
    else if (time < 13.9) carousel();
    else if (time < 14.2) weakRight();
    else if (time < 14.6) forwards();
    // else if (time < 28.0) warehouse();
    super.loop();
  }
}
