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
    else if (time < 6.8) preload();
    else if (time < 13.0) idle();
    else if (time < 28.0) warehouse();
    super.loop();
  }
}
