package hash_widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hex.hashtool.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import sql_classes.ColumnWiseResultHashMap;
import sql_classes.ResultColumn;

public class HashList extends AppCompatActivity {

    ListAdapter listAdapter;
    LinearLayout listView;


    ColumnWiseResultHashMap data;
    EditText searchtxt;
    boolean[] show ;
    TextView norectxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hash_list);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        listView=findViewById(R.id.listview);
        searchtxt=findViewById(R.id.search);
        norectxt = findViewById(R.id.norecords);

        if(getIntent().getSerializableExtra("data")!=null)
        {
            data = (ColumnWiseResultHashMap) getIntent().getSerializableExtra("data");
            setData();

            searchtxt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(final CharSequence s, int start, int before, int count) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean norecords = true;
                            for(int i=0;i<data.getRowCount();i++)
                            {
                                boolean contains = false;
                                for(ResultColumn datacolumn:data.getValues())
                                {
                                    if(datacolumn.getValues().get(i).toLowerCase().contains(s.toString().toLowerCase()))
                                    {
                                        contains=true;
                                        norecords=false;
                                        break;
                                    }
                                }
                                show[i] = contains;
                            }
                            timer.cancel();
                            timer = new Timer();
                            final boolean finalNorecords = norecords;
                            timer.schedule(
                                    new TimerTask() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    NotifyDataChanged();
                                                    if(finalNorecords)
                                                    {
                                                        norectxt.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        norectxt.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                        }
                                    },
                                    DELAY
                            );
                        }
                    }).start();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        }
    }
    private Timer timer=new Timer();
    private final long DELAY = 100;

    public void setData()
    {
        //this.data=columnWiseResultHashMap;
        HashMap<String,Integer> maxLengths = new HashMap<>();
        show = new boolean[data.getRowCount()];
        int rowcount = data.getRowCount();
        View child = LayoutInflater.from(HashList.this).inflate(R.layout.hash_list_v2_cell,null);
        TextView textview = child.findViewById(R.id.textview);
        for (ResultColumn column:data.getValues()) {
            String maxlength="";
            for (int i=0;i<rowcount;i++)
            {
                show[i] = true;
                String id = column.getColumnID();
                if(i==0)
                {
                    maxLengths.put(id,-1);
                }
                //int length = column.getValues().get(i).length();
                textview.setText(column.getValues().get(i));
                textview.measure(0,0);
                if(textview.getMeasuredWidth()>maxLengths.get(id))
                {
                    maxLengths.put(id,textview.getMeasuredWidth());
                    maxlength = column.getValues().get(i);
                    Log.w("hashlistv2",maxlength+" string");
                }
            }


            //textview.setText(maxlength);
            //textview.measure(0,0);
            //maxLengths.put(column.getColumnID(),textview.getMeasuredWidth());
            Log.w("hashlistv2",maxLengths.get(column.getColumnID())+" width");
        }
        listAdapter = new ListAdapter(HashList.this,data,maxLengths);
        for(int i=0;i<data.getRowCount();i++)
        {

            listView.addView(listAdapter.getView(i,null,listView));
        }






    }

    public void NotifyDataChanged()
    {
        listView.removeAllViews();
        for(int i=0;i<data.getRowCount();i++)
        {
            listView.addView(listAdapter.changeGetView(i));
        }
    }

    public void onClick(int position)
    {



        Intent resultIntent = new Intent();
        resultIntent.putExtra("data",data.copyRowToBlankRemoveDollar(position));
        resultIntent.putExtra("type", "list2");
        resultIntent.putExtra("label", getIntent().getStringExtra("label"));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }


//    public int getWidth(String str,TextView textView)
//    {
////        //String str ="Hiren Patel";
////        //View textview = LayoutInflater.from(HashList.this).inflate(R.layout.hash_list_v2_cell,null);
////        Paint paint = new Paint();
////
////
//////        float scaledSizeInPixels = TypedValue.applyDimension(
//////                TypedValue.COMPLEX_UNIT_PX,
//////                ((TextView)textview).getTextSize(),
//////                HashList.this.getResources().getDisplayMetrics());
////
////        float scaledSizeInPixels = TypedValue.applyDimension(
////                TypedValue.COMPLEX_UNIT_SP,
////                textsizesp,
////                HashList.this.getResources().getDisplayMetrics());
////        paint.setTextSize(scaledSizeInPixels);
////
////
////        //paint.setTextSize(textsizepx);
////        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/"+fontasset);
////        paint.setTypeface(typeface);
////        //paint.setColor(Color.BLACK);
////        //paint.setStyle(Paint.Style.FILL);
//        Rect result = new Rect();
////        paint.getTextBounds(str, 0, str.length(), result);
////        return result.width();
//
//        Paint paint = new Paint();
//        paint.setTypeface(textView.getTypeface());
//        paint.setTextSize(textView.getTextSize());
//
//
//        float scaledSizeInPixels = TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_PX,
//                textView.getTextSize(),
//                HashList.this.getResources().getDisplayMetrics());
//
//        //Log.w("hashlistv2",sca)+" txt size");
//        paint.getTextBounds(str, 0, str.length(), result);
//
//
//        //tsttextview.setText(str);// = text;
//        return result.width();
//}

    private class ListAdapter extends BaseAdapter
    {

        HashMap<String,Integer> maxLength;
        ColumnWiseResultHashMap columnWiseResultHashMap;
        Context context;
        ArrayList<View> views ;
        public ListAdapter(Context context, ColumnWiseResultHashMap columnWiseResultHashMap, HashMap<String,Integer> maxLengths)
        {
            this.maxLength=maxLengths;
            this.columnWiseResultHashMap=columnWiseResultHashMap;
            this.context=context;
            views=new ArrayList<>(columnWiseResultHashMap.getRowCount());
        }


        public View changeGetView(int position)
        {
            View view = views.get(position);
            if(show[position])
            {
                view.setVisibility(View.VISIBLE);
                view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
            }
            else
            {

                //view.setVisibility(View.GONE);
                view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT,1));
            }
            return view;
        }
        @Override
        public int getCount() {
            return columnWiseResultHashMap.getRowCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.hash_list_v2_item,parent,false);
            LinearLayout linearLayout = (LinearLayout)view;

                for (ResultColumn column : columnWiseResultHashMap.getValues()) {

                    if(!column.getColumnID().substring(column.getColumnID().length()-1).equalsIgnoreCase("$")) {
                        View child = LayoutInflater.from(context).inflate(R.layout.hash_list_v2_cell, null);
                        TextView textview = child.findViewById(R.id.textview);
                        //((TextView)textview).setEms(maxLength.get(column.getColumnID()));
                        //((TextView)textview).setMinEms(maxLength.get(column.getColumnID()));
                        ((TextView) textview).setWidth(maxLength.get(column.getColumnID()));
                        //((TextView)textview).setTextSize(textsizesp);
                        ((TextView) textview).setText(column.getValues().get(position));
                        linearLayout.addView(child);
                    }
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashList.this.onClick(position);
                    }
                });
            views.add(view);
            return view;
        }
    }
}
