package postaround.tcc.inatel.br.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import postaround.tcc.inatel.br.postaround.R;

/**
 * Created by Carol on 13/11/2015.
 */
public class PostAiTextView extends TextView {

    public PostAiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int style = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "textStyle", Typeface.NORMAL);
        int boldStyle = Typeface.BOLD;

        Log.d("STYLE: ", style+"");

        if (style == boldStyle) {
            Log.d("STYLE BOLD","!");
            this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/WalkwayRounded.ttf"));
        } else {
            this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/WalkwaySemiBold.ttf"));
        }
    }
}
