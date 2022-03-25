package org.firstinspires.ftc.transfinity;

abstract class Gamma extends Zeta<GammaController> {
  @Override
  public abstract void init();

  protected void init(Team team) {
    controller = new GammaController(team, hardwareMap, gamepad1, gamepad2);
    super.init();
    gamepad1.reset();
    gamepad2.reset();
  }

  @Override
  public void start() {
    super.start();
    controller.beta.armPitch.start();
  }
}
