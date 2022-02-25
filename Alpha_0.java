package org.firstinspires.ftc.transfinity;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Alpha 0 - Movement, Binary State", group="α")
public class Alpha_0 extends Alpha {
  @Override
  public void loop() {
    int dirX = Math.round(-gamepad1.left_stick_y);
    int dirY = Math.round(gamepad1.left_stick_x);
    int dir = 0;
    switch (dirX) {
      case 1: dir = dirY == -1 ? 8 : dirY + 1; break;
      case -1: dir = -dirY + 5; break;
      case 0: if (dirY != 0) dir = dirY * -2 + 5; break;
    }
    double[] run = { 0, 0, 0, 0 };
    switch (dir) {
      case 1: run = new double[]{ 1,  1,  1,  1 }; break;
      case 2: run = new double[]{ 1,  0,  0,  1 }; break;
      case 3: run = new double[]{ 1, -1, -1,  1 }; break;
      case 4: run = new double[]{ 0, -1, -1,  0 }; break;
      case 5: run = new double[]{-1, -1, -1, -1 }; break;
      case 6: run = new double[]{-1,  0,  0, -1 }; break;
      case 7: run = new double[]{-1,  1,  1, -1 }; break;
      case 8: run = new double[]{ 0,  1,  1,  0 }; break;
    }
    for (int i = 0; i < drive.size(); i++)
      drive.get(DRIVE_ORDER[i]).setPower(run[i]);
    updateTelemetry();
  }
}