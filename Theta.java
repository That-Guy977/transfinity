package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name="Theta", group="λ")
public class Theta extends Zeta<ThetaController> {
  @Override
  public void init() {
    controller = new ThetaController(hardwareMap, gamepad1);
    super.init();
  }
}
