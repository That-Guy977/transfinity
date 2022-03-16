package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Alpha", group="α")
public class Alpha extends Zeta<AlphaController> {
  @Override
  public void init() {
    controller = new AlphaController(hardwareMap, gamepad1);
    super.init();
  }
}
