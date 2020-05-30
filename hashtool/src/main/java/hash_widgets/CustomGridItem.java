package hash_widgets;

import android.content.Context;
import android.util.AttributeSet;

import androidx.cardview.widget.CardView;

public class CustomGridItem extends CardView {
    public CustomGridItem(Context context) {
        super(context);
    }

    public CustomGridItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGridItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // This is the key that will make the height equivalent to its width
    }
}
