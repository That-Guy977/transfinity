package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Kappa (BW)", group="κ", preselectTeleOp="TeleOp (Blue)")
public class KappaBlueWarehouse extends Kappa {
  @Override
  public void init() {
    init(Team.BLUE, Position.WAREHOUSE);
  }

  @Override
  public void loop() {
    gamepad.reset();
    double time = getRuntime();
    if (time < 0.1) idle();
    else if (time < 6.8) preload();
    else if (time < 14.3) warehouse();
    super.loop();
  }
}
