package org.firstinspires.ftc.transfinity;

import java.util.Arrays;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;

class IotaController extends ControllerGroupController {
  protected final AlphaController alpha;
  protected final BetaController beta;
  protected final LambdaController lambda;

  IotaController(HardwareMap hardwareMap, Gamepad gamepad) {
    super(hardwareMap, gamepad);
    alpha = new AlphaController(hardwareMap, gamepad);
    beta = new BetaController(hardwareMap, gamepad);
    lambda = new LambdaController(Team.DEFAULT, hardwareMap, gamepad);
    controllers.addAll(Arrays.asList(alpha, beta, lambda));
  }
}
