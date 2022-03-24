package org.firstinspires.ftc.transfinity;

import java.util.Timer;
import java.util.TimerTask;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.TouchSensor;

abstract class Kappa extends Zeta<KappaController> {
  protected Team team;
  protected Position position;
  protected TouchSensor touch;
  protected Gamepad gamepad = new Gamepad();
  protected int preloadPosition = 0;
  protected boolean preloadPlaced = false;
  protected double preloadStartTime = -1;
  protected double warehouseStartTime = -1;

  @Override
  public abstract void init();

  protected void init(Team team, Position position) {
    controller = new KappaController(team, hardwareMap, gamepad);
    this.team = team;
    this.position = position;
    touch = hardwareMap.get(TouchSensor.class, "touch");
    super.init();
    gamepad.a = true;
    controller.update();
    Timer timer = new Timer(true);
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        gamepad.a = false;
        controller.update();
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
    if (preloadPosition == 0) preloadPosition = 3;
  }

  private int readPreLoadPosition() {
    return 0;
  }

  protected void preload() {
    boolean dirRight = (team == Team.RED) == (position == Position.CAROUSEL);
    if (preloadStartTime == -1) preloadStartTime = getRuntime();
    double time = getRuntime() - preloadStartTime;
    if (time < 0.3) idle();
    else if (time < 1.8) forwards();
    else if (time < 1.9) idle();
    else if (time < 2.3) raiseArm();
    else if (time < 3.2) { if (dirRight) weakRight(); else weakLeft(); raiseArm(); }
    else if (time < 3.4) raiseArm();
    else if (time < 3.5) {
      if (!preloadPlaced) {
        preloadPlaced = true;
        gamepad.a = true;
      }
    } else if (time < 3.7) raiseArm();
    else if (time < 4.5) { if (dirRight) weakBackLeft(); else weakBackRight(); raiseArm(); }
    else if (time < 4.7) idle();
    else if (time < 4.9) down();
    else if (time < 5.0) idle();
    else if (time < 6.6) backwards();
  }

  protected void warehouse() {
    if (warehouseStartTime == -1) warehouseStartTime = getRuntime();
    double time = getRuntime() - warehouseStartTime;
    switch (position) {
      case WAREHOUSE:
        boolean red = team == Team.RED;
        if (time < 0.3) idle();
        else if (time < 0.75) { if (red) right(); else left(); }
        else if (time < 0.8) idle();
        else if (time < 3.4) forwards();
        else if (time < 3.5) idle();
        else if (time < 3.9) { if (red) left(); else right(); }
        else if (time < 4.0) idle();
        else if (time < 5.2) forwards();
        else if (time < 5.4) idle();
        else if (time < 5.7) { if (red) right(); else left(); }
        else if (time < 5.9) idle();
        else if (time < 7.0) forwards();
        else if (time < 7.2) idle();
        else if (time < 7.5) { if (red) right(); else left(); }
        break;
      case CAROUSEL:
        switch (team) {
          case RED:
            if (time < 0.1) idle();
            else if (time < 0.6) weakRight();
            else if (time < 1.0) weakLeft();
            else if (time < 1.1) weakRight();
            break;
          case BLUE:
            if (time < 0.1) idle();
            else if (time < 0.4) left();
            else if (time < 0.5) weakRight();
            else if (time < 0.7) weakLeft();
            else if (time < 0.9) idle();
            break;
        }
        if (time < 7.0) idle();
        else if (time < 10.0) forwards();
        else if (time < 11.0) weakRight();
        else if (time < 14.3) forwards();
        break;
    }
  }

  protected void idle() {}

  protected void forwards() {
    gamepad.left_stick_y = -0.5f;
    gamepad.right_stick_y = -0.5f;
  }

  protected void backwards() {
    gamepad.left_stick_y = 0.5f;
    gamepad.right_stick_y = 0.5f;
  }

  protected void right() {
    gamepad.left_stick_y = -1;
    gamepad.right_stick_y = 1;
  }

  protected void left() {
    gamepad.left_stick_y = 1;
    gamepad.right_stick_y = -1;
  }

  protected void weakRight() {
    gamepad.left_stick_y = -1;
    gamepad.right_stick_y = -0.5f;
  }

  protected void weakLeft() {
    gamepad.left_stick_y = -0.5f;
    gamepad.right_stick_y = -1;
  }

  protected void weakBackLeft() {
    gamepad.left_stick_y = 1;
    gamepad.right_stick_y = 0.5f;
  }

  protected void weakBackRight() {
    gamepad.left_stick_y = 0.5f;
    gamepad.right_stick_y = 1;
  }

  protected void up() {
    gamepad.dpad_up = true;
  }

  protected void down() {
    gamepad.dpad_down = true;
  }

  protected void carousel() {
    gamepad.right_bumper = true;
  }

  protected void raiseArm() {
    double targetPosition;
    switch (preloadPosition) {
      case 1:
        targetPosition = 0.18;
        break;
      case 2:
        targetPosition = 0.58;
        break;
      default:
      case 3:
        targetPosition = 0.87;
        break;
    }
    if (targetPosition > controller.beta.armPitch.getPosition()) up();
  }
}
