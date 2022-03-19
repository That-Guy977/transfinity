package org.firstinspires.ftc.transfinity;

import java.util.Arrays;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;

class GammaController extends ControllerGroupController {
  protected final AlphaController alpha;
  protected final BetaController beta;
  protected final LambdaController lambda;

  GammaController(Team team, HardwareMap hardwareMap, Gamepad primary, Gamepad secondary) {
    super(hardwareMap, primary, secondary);
    alpha = new AlphaController(hardwareMap, primary);
    beta = new BetaController(hardwareMap, secondary);
    lambda = new LambdaController(team, hardwareMap, primary);
    controllers.addAll(Arrays.asList(alpha, beta, lambda));
  }
}
