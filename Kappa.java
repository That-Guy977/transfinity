package org.firstinspires.ftc.transfinity;

import java.util.Timer;
import java.util.TimerTask;

import com.qualcomm.robotcore.hardware.Gamepad;

abstract class Kappa extends Zeta<KappaController> {
  protected Gamepad gamepad = new Gamepad();
  protected AutoCycle cycle = AutoCycle.IDLE;
  protected int preloadPosition = 0;
  protected boolean preloadPlaced = false;

  @Override
  public abstract void init();

  protected void init(Team team, Position position) {
    controller = new KappaController(team, position, hardwareMap, gamepad);
    super.init();
    cycle = AutoCycle.PRELOAD;
    gamepad.a = true;
    controller.update();
    Timer timer = new Timer(true);
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        gamepad.a = false;
        controller.update();
        cycle = AutoCycle.IDLE;
        timer.cancel();
      }
    }, 500);
  }

  @Override
  public void start() {
    super.start();
    resetStartTime();
    controller.beta.armPitch.start();
    preloadPosition = readPreLoadPosition();
    if (preloadPosition == 0) preloadPosition = 2;
  }

  private int readPreLoadPosition() {
    return 0;
  }
}
