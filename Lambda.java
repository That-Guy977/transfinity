package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name="Lambda", group="λ")
public class Lambda extends Zeta<LambdaController> {
  @Override
  public void init() {
    controller = new LambdaController(Team.DEFAULT, hardwareMap, gamepad1);
    super.init();
  }
}
