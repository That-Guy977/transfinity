package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Kappa (RW)", group="κ", preselectTeleOp="Gamma (R)")
public class KappaRedWarehouse extends Kappa {
  @Override
  public void init() {
    init(Team.RED, Position.WAREHOUSE);
  }
}
