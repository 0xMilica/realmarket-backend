package io.realmarket.propeler.service.helper;

import java.util.Calendar;
import java.util.Date;

public class DateTimeHelper {

  public static Date getPointInTime(int timeUnit, int quantity) {
    Calendar cal = Calendar.getInstance();
    cal.add(timeUnit, quantity);
    return cal.getTime();
  }
}
