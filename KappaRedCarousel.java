package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Kappa (RC)", group="κ", preselectTeleOp="TeleOp (Red)")
public class KappaRedCarousel extends Kappa {


  @Override
  public void init() {
    init(Team.RED, Position.CAROUSEL);
  }

  @Override
  public void loop() {
    gamepad.reset();
    double time = getRuntime();
    if (time < 0.1) idle();
    else if (time < 7.2) preload();
    else if (time < 7.65) forwards();
    else if (time < 7.8) idle();
    else if (time < 8.2) right();
    else if (time < 8.4) idle();
    else if (time < 11.25) backwards();
    else if (time < 11.4) idle();
    else if (time < 13.7) carousel();
    else if (time < 28.0) warehouse();
    super.loop();
  }
}
