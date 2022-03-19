package org.firstinspires.ftc.transfinity;

import java.util.Arrays;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;

class KappaController extends ControllerGroupController {
  protected final Position position;
  protected final AlphaController alpha;
  protected final BetaController beta;
  protected final LambdaController lambda;

  KappaController(Team team, Position position, HardwareMap hardwareMap, Gamepad gamepad) {
    super(hardwareMap, gamepad);
    this.position = position;
    alpha = new AlphaController(hardwareMap, gamepad);
    beta = new BetaController(hardwareMap, gamepad);
    lambda = new LambdaController(team, hardwareMap, gamepad);
    controllers.addAll(Arrays.asList(alpha, beta, lambda));
  }
}
