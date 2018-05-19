package nize.example.com.checklotto;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CurrentDate {
    private int day;
    private int month;
    private int year;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CurrentDate() {
        setCurrentDate();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setCurrentDate() {
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        Date currentTime = localCalendar.getTime();
        this.day = localCalendar.get(Calendar.DATE);
        this.month = localCalendar.get(Calendar.MONTH) + 1;
        this.year = localCalendar.get(Calendar.YEAR) + 543;
        Log.i("Date : ", currentTime + "    :   " + this.day + " : " + this.month + " : " + this.year);
    }

    public int getDay() {
        return this.day;
    }

    public int getMonth() {
        return this.month;
    }

    public int getYear() {
        return this.year;
    }
}

