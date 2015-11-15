package postaround.tcc.inatel.br.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Carol on 13/11/2015.
 */
public class PostAiTextView extends TextView {

    public PostAiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/WalkwaySemiBold.ttf"));
    }
}
