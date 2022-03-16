package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Gamma (R)", group="γ")
public class GammaRed extends Gamma {
  @Override
  public void init() {
    init(Team.RED);
  }
}
