package postaround.tcc.inatel.br.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Carol on 26/11/2015.
 */
public class ScrollingPostAiTextViewBehavior extends CoordinatorLayout.Behavior<PostAiTextView> {

    private int toolbarHeight;

    public ScrollingPostAiTextViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        //this.toolbarHeight = GenericUtils.getActionBarHeight(context);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        this.toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, PostAiTextView fab, View dependency) {
        return dependency instanceof AppBarLayout || dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, PostAiTextView fab, View dependency) {
        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = 12;//(fab.getHeight() + fabBottomMargin) + 10;
            float ratio = dependency.getY() / (float) toolbarHeight;

            fab.setTranslationY(-distanceToScroll * ratio);

            return true;
        }

        if (dependency instanceof Snackbar.SnackbarLayout) {
            float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());

            fab.setTranslationY(translationY);

            return true;
        }

        return false;
    }
}
