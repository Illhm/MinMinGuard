package tw.fatminmin.xposed.minminguard.blocker;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import tw.fatminmin.xposed.minminguard.Main;

public class ViewBlocking
{
    public static void removeAdView(String packageName, View view)
    {
        if (view == null || view.getContext() == null) return;
        Util.notifyRemoveAdView(view.getContext(), packageName, 1);
        removeAdView(packageName, view, true, 51);
    }

    private static void removeAdView(final String packageName, final View view, final boolean first, final float heightLimit)
    {
        if (view == null) return;

        float adHeight = convertPixelsToDp(view.getHeight());

        if (first || (adHeight > 0 && adHeight <= heightLimit))
        {
            ViewGroup.LayoutParams params = view.getLayoutParams();

            if (params == null)
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            else if (params.height != 0)
                params.height = 0;

            if (view.getLayoutParams() != params || params.height == 0) {
                view.setLayoutParams(params);
            }
        }

        if (view.isAttachedToWindow()) {
            view.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (view == null || !view.isAttachedToWindow()) return;

                    float adHeight = convertPixelsToDp(view.getHeight());

                    if (first || (adHeight > 0 && adHeight <= heightLimit))
                    {
                        ViewGroup.LayoutParams params = view.getLayoutParams();

                        if (params == null)
                            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                        else if (params.height != 0)
                            params.height = 0;

                        if (view.getLayoutParams() != params || params.height == 0) {
                            view.setLayoutParams(params);
                        }
                    }
                }
            });
        }

        if (view.getParent() != null && view.getParent() instanceof ViewGroup)
        {
            ViewGroup parent = (ViewGroup) view.getParent();

            // Check if we can safely collapse parent
            if (parent.getChildCount() <= 1) {
                removeAdView(packageName, parent, false, heightLimit);
            }
        }
    }

    private static DisplayMetrics metrics = new DisplayMetrics();;

    private static float convertPixelsToDp(float px)
    {
        if(Main.resources != null)
            metrics = Main.resources.getDisplayMetrics();

        if (metrics.densityDpi == 0) return 0;
        return px / (metrics.densityDpi / 160f);
    }
}
