package org.firstinspires.ftc.transfinity;

import java.util.Arrays;

import com.qualcomm.robotcore.hardware.*;

class KappaController extends ControllerGroupController {
  protected final Position position;
  protected final AlphaController alpha;
  protected final BetaController beta;
  protected final LambdaController lambda;
  protected final ThetaController theta;

  KappaController(Team team, Position position, HardwareMap hardwareMap, Gamepad primary, Gamepad secondary) {
    super(hardwareMap, primary, secondary);
    this.position = position;
    alpha = new AlphaController(hardwareMap, primary);
    beta = new BetaController(hardwareMap, secondary);
    lambda = new LambdaController(team, hardwareMap, primary);
    theta = new ThetaController(hardwareMap, secondary);
    controllers.addAll(Arrays.asList(alpha, beta, lambda/*, theta */));
  }
}
