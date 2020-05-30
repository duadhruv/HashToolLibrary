package hash_widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.TextView;

import com.hex.hashtool.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import app.futured.donut.DonutDataset;
import app.futured.donut.DonutProgressView;

public class PercentageGauge extends ConstraintLayout {
    View view;
    DonutProgressView donutProgressView;
    int colorbg,colorprogress,gapangle,gapwidth,rotation,progress;
    int width,textsize;
    TextView txt;
    float currProgress;
    AnticipateOvershootInterpolator anticipateOvershootInterpolator;
    ProgressTextFormatter progressTextFormatter;
    ValueAnimator valueAnimator ;
    public PercentageGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.percentage_gauge, this, true);
        donutProgressView=view.findViewById(R.id.donut_view);
        txt=view.findViewById(R.id.percentage);




        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PercentageGauge,
                0, 0);
        initAttributes(a);

        currProgress=progress;


        progressTextFormatter=new ProgressTextFormatter() {
            @NonNull
            @Override
            public CharSequence provideFormattedText(float progress) {
                return (int)progress+"%";
            }
        };
        donutProgressView.setBgLineColor(colorbg);
        donutProgressView.setStrokeWidth(width);
        donutProgressView.setGapAngleDegrees(gapangle);
        donutProgressView.setGapWidthDegrees(gapwidth);
        if(rotation==1)
        {
            donutProgressView.setScaleX(-1);
            donutProgressView.setScaleY(1);
            donutProgressView.setTranslationX(1);
        }

        if(progress!=-1)
        {
            DonutDataset donutDataset = new DonutDataset("d", colorprogress, progress);
            ArrayList<DonutDataset> donutDatasets = new ArrayList<>();
            donutDatasets.add(donutDataset);
            donutProgressView.setAnimateChanges(false);
            donutProgressView.submitData(donutDatasets);

        }

        txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,textsize);
    }
    public void setHideGauge(boolean hideGauge)
    {
        if(hideGauge)
        {
            donutProgressView.setVisibility(GONE);
        }
        else
        {
            donutProgressView.setVisibility(VISIBLE);
        }
    }

    public void setProgressTextFormatter(ProgressTextFormatter progressTextFormatter)
    {
        this.progressTextFormatter=progressTextFormatter;
    }


    public void setProgress(final float progress, final boolean animate) {

        if(progress!=currProgress)
        {
            int time=0;
            if(animate)
            {
                time=200;
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(currProgress==-1f) {
                        DonutDataset donutDataset = new DonutDataset("d", colorprogress, progress);
                        ArrayList<DonutDataset> donutDatasets = new ArrayList<>();
                        donutDatasets.add(donutDataset);
                        donutProgressView.setAnimateChanges(animate);
                        donutProgressView.submitData(donutDatasets);
                    }
                    else if(currProgress<progress)
                    {
                        donutProgressView.setAnimateChanges(animate);
                        donutProgressView.addAmount("d",progress-currProgress,colorprogress);
                    }
                    else if(currProgress>progress)
                    {
                        donutProgressView.setAnimateChanges(animate);
                        donutProgressView.removeAmount("d",currProgress-progress);
                    }
                    //donutProgressView.addAmount();

                    if(animate) {
                        valueAnimator = new ValueAnimator().ofFloat(currProgress, progress);
                        valueAnimator.setInterpolator(donutProgressView.getAnimationInterpolator());
                        valueAnimator.setDuration(donutProgressView.getAnimationDurationMs());
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                //Log.w("Animator", String.valueOf(animation.getAnimatedValue()));
                                float p = (float) animation.getAnimatedValue();

                                if (p < 0) {
                                    p = 0f;
                                } else if (p > 100) {
                                    p = 100f;
                                }
                                if ((int) currProgress != (int) p) {
                                    txt.setText(progressTextFormatter.provideFormattedText(p));
                                }

                                currProgress = p;
                            }
                        });


                        valueAnimator.start();
                    }
                    else
                    {
                        currProgress=progress;
                        txt.setText(progressTextFormatter.provideFormattedText(progress));
                    }



                }
            }, time);


        }
    }
    private void initAttributes(TypedArray a)
    {
        try {
            colorbg = a.getColor(R.styleable.PercentageGauge_ProgressBgColor,getResources().getColor(android.R.color.holo_green_dark));
            colorprogress=a.getColor(R.styleable.PercentageGauge_ProgressColor,getResources().getColor(android.R.color.white));
            width=a.getDimensionPixelSize(R.styleable.PercentageGauge_StrokeWidth,10);
            textsize=a.getDimensionPixelSize(R.styleable.PercentageGauge_TextSize,5);
            gapangle=a.getInteger(R.styleable.PercentageGauge_GapAngle,90);
            gapwidth=a.getInteger(R.styleable.PercentageGauge_GapWidth,0);
            rotation=a.getInteger(R.styleable.PercentageGauge_Rotation,0);
            progress=a.getInteger(R.styleable.PercentageGauge_Progress,-1);

        }catch (Exception e){}



    }

    public interface ProgressTextFormatter {

        @NonNull
        CharSequence provideFormattedText(float progress);

    }
}
