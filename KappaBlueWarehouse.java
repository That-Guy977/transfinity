package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Kappa (BW)", group="κ", preselectTeleOp="Gamma (B)")
public class KappaBlueWarehouse extends Kappa {
  @Override
  public void init() {
    init(Team.BLUE, Position.WAREHOUSE);
  }
}
