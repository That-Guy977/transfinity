package org.firstinspires.ftc.transfinity;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Sigma {
  String value();
}
