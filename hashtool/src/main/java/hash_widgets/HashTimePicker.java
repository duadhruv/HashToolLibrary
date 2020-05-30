package hash_widgets;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;

import com.hex.hashtool.R;

import java.util.Calendar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class HashTimePicker extends AppCompatActivity {

    private TimePicker timePicker1;
    private Calendar calendar;
    private String format = "";
    String time="";
    Button select;
    String label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_time_picker);

        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        timePicker1.setIs24HourView(false);
        label = getIntent().getStringExtra("label");

        calendar = Calendar.getInstance();
        select  =findViewById(R.id.set_button);
        //select.setBackground(getResources().getDrawable(R.drawable.submit_btn_ripple));
        select.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("data", time);
                resultIntent.putExtra("type","time");
                resultIntent.putExtra("label",label);
                setResult(Activity.RESULT_OK, resultIntent);
                finishAfterTransition();
                //finish();
            }
        });

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        showTime(hour, min);


        if(getIntent().getStringExtra("Time")!=null)
        {
            String time = getIntent().getStringExtra("Time");
            boolean hasAM = time.toLowerCase().contains("AM".toLowerCase());
            if(hasAM)
            {
                time = time.replaceAll("(?i)AM","");
            }
            else
            {
                time = time.replaceAll("(?i)PM","");
            }
            time=time.trim();
            String[] vals = time.split(":");
            int h = Integer.parseInt(vals[0]);
            int m = Integer.parseInt(vals[1]);
            if(!hasAM)
            {
                h+=12;
            }
            timePicker1.setCurrentHour(h);
            timePicker1.setCurrentMinute(m);
            showTime(h,m);
        }

        timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                int hour = timePicker1.getCurrentHour();
                int min = timePicker1.getCurrentMinute();
                showTime(hour, min);
            }
        });


    }

    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        String h = String.format("%02d", hour);
        String m = String.format("%02d", min);
        time = String.valueOf(new StringBuilder().append(h).append(":").append(m).append(" "+format));//.append("").append(format));
    }
}
