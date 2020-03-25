package cn.surine.lazyandroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Intro：定闹钟
 * @author sunliwei
 * @date 2019-10-15 20:42
 */
public class AlarmClockManager {

    /**
     * 设置一个某个时间点的闹钟
     *
     * @param id   闹钟id
     * @param time 闹钟时间
     *             格式 yyyy-MM-dd HH:mm
     * @param context 上下文
     * @param intent 事件
     * @param repeatTime 重复时间（仅适用于Android4.4以下）
     */
    public static void setAlarmClock(int id, String time, Context context,Intent intent,long repeatTime) {
        PendingIntent pendingIntent = PendingIntent.getService(context, id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long times = 0L;
        try {
            Date date = simpleDateFormat.parse(time);
            times = date.getTime();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, times, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, times, pendingIntent);
            } else {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, times,repeatTime, pendingIntent);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
