package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Kappa (RC)", group="κ", preselectTeleOp="Gamma (R)")
public class KappaRedCarousel extends Kappa {
  @Override
  public void init() {
    init(Team.RED, Position.CAROUSEL);
  }
}
