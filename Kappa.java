package org.firstinspires.ftc.transfinity;

abstract class Kappa extends Zeta<KappaController> {
  @Override
  public abstract void init();

  protected void init(Team team, Position position) {
    controller = new KappaController(team, position, hardwareMap, gamepad1, gamepad2);
    super.init();
  }

  @Override
  public void start() {
    super.start();
    controller.beta.armPitch.start();
  }
}
