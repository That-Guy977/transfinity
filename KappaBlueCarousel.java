package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Kappa (BC)", group="κ", preselectTeleOp="Gamma (B)")
public class KappaBlueCarousel extends Kappa {
  @Override
  public void init() {
    init(Team.BLUE, Position.CAROUSEL);
  }
}
