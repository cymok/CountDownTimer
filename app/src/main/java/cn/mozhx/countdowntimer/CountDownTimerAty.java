package cn.mozhx.countdowntimer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Harry
 */
@SuppressLint("SetTextI18n")
public class CountDownTimerAty extends Activity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private TextView mTextView;
    private String mPickTime;
    private HuaShuiCountDownTimer mCountDownTimer;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onClick(View v) {
        setDate();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mPickTime = String.format(Locale.getDefault(), "%04d", year)
                + String.format(Locale.getDefault(), "%02d", (month + 1))
                + String.format(Locale.getDefault(), "%02d", dayOfMonth);
        setTime();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mPickTime += String.format(Locale.getDefault(), "%02d", hourOfDay)
                + String.format(Locale.getDefault(), "%02d", minute);
        mSharedPreferences.edit().putString(getClass().getName(), mPickTime).apply();
        startCountDown();
    }

    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.show();
    }

    private void setTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog =
                new TimePickerDialog(this, this, hour, minute, true);
        timePickerDialog.show();
    }

    private void startCountDown() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault());
        try {
            Date date = format.parse(mPickTime);
            Date nowData = new Date();
            long dL = date.getTime() - nowData.getTime();
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
            }
            mCountDownTimer = new HuaShuiCountDownTimer(dL, 1);
            mCountDownTimer.start();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        mTextView = new TextView(this);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextSize(20);
        frameLayout.addView(mTextView);
        setContentView(frameLayout);

        mTextView.setText("点击我选择日期时间开始倒计时");
        mTextView.setOnClickListener(this);

        mSharedPreferences = getSharedPreferences(getClass().getName(), Context.MODE_PRIVATE);
        mPickTime = mSharedPreferences.getString(getClass().getName(), mPickTime);
        if (mPickTime != null) {
            startCountDown();
        }
    }

    private class HuaShuiCountDownTimer extends CountDownTimer {
        HuaShuiCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int millisecond = (int) (millisUntilFinished % 1000);
            int second = (int) (millisUntilFinished / 1000 % 60);
            int minute = (int) (millisUntilFinished / 1000 / 60 % 60);
            int hour = (int) (millisUntilFinished / 1000 / 60 / 60 % 24);
            int day = (int) (millisUntilFinished / 1000 / 60 / 60 / 24);
            String d = day + "天"
                    + String.format(Locale.getDefault(), "%02d", hour) + "小时"
                    + String.format(Locale.getDefault(), "%02d", minute) + "分钟\n"
                    + String.format(Locale.getDefault(), "%02d", second) + "秒"
                    + String.format(Locale.getDefault(), "%03d", millisecond) + "毫秒";
            mTextView.setText("倒计时:\n" + d);
        }

        @Override
        public void onFinish() {
            mTextView.setText("老子解放啦");
        }
    }

}
