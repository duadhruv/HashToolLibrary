package hash_widgets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.CalendarView;

import com.hex.hashtool.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class HashDatePicker extends AppCompatActivity {
String date;
String dayid;
String MY_PREFS_NAME = "mypref";
String label;

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.activity_date_picker);
        label = getIntent().getStringExtra("label");
        final CalendarView c = findViewById(R.id.calendarView);
        c.setMaxDate(System.currentTimeMillis());
        Bundle extras = getIntent().getExtras();
        final String day = extras.getString("dayid");
        DateFormat timeformat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        Log.w("DatePicker",day);
        Date  d = null;
        try {
            d = timeformat.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        c.setDate(d.getTime());
        c.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month+=1;
                date = dayOfMonth + "." +month + "." +year;
                //year = year-2000;
                dayid = String.valueOf(year);
                if(month<10)
                {
                   dayid = dayid+"0"+ String.valueOf(month);
                }
                else
                {
                    dayid = dayid+ String.valueOf(month);
                }
                if(dayOfMonth<10)
                {
                    dayid = dayid+"0"+ String.valueOf(dayOfMonth);
                }
                else
                {
                    dayid = dayid+ String.valueOf(dayOfMonth);
                }

                date=String.format("%02d", dayOfMonth)+"."+String.format("%02d", month)+"."+String.format("%02d",year);

                Log.w("dayid" , dayid);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("data", date);
                resultIntent.putExtra("dayid",dayid);
                resultIntent.putExtra("type","date");
                resultIntent.putExtra("label",label);
                setResult(Activity.RESULT_OK, resultIntent);
                //finish();
                supportFinishAfterTransition();
            }
        });

    }
}
