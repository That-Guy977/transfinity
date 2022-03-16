package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Beta", group="β")
public class Beta extends Zeta<BetaController> {
  @Override
  public void init() {
    controller = new BetaController(hardwareMap, gamepad1);
    super.init();
  }

  @Override
  public void start() {
    super.start();
    controller.armPitch.start();
  }
}
