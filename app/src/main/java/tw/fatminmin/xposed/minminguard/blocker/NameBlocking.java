package tw.fatminmin.xposed.minminguard.blocker;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.Main;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fatminmin on 2015/10/27.
 */
public final class NameBlocking
{
    private static Map<String, Boolean> cache = new HashMap<>();

    private static boolean matchBannerName(String clazzName, String banner, String bannerPrefix)
    {
        if (banner != null && banner.equals(clazzName))
        {
            return true;
        }

        return bannerPrefix != null && clazzName.startsWith(bannerPrefix);
    }

    // return adnetwork name
    private static Boolean isAdView(Context context, String pkgName, String clazzName)
    {
        if (clazzName == null) return false;

        if (cache.containsKey(clazzName)) {
            return cache.get(clazzName);
        }

        if (clazzName.startsWith("android.") || clazzName.startsWith("androidx.") || clazzName.startsWith("com.google.android.material.")) {
            cache.put(clazzName, false);
            return false;
        }

        if (clazzName.equals("android.widget.FrameLayout") || clazzName.equals("android.widget.RelativeLayout") || clazzName.equals("android.widget.LinearLayout")) {
            cache.put(clazzName, false);
            return false;
        }

        if (clazzName.startsWith("com.google.ads")) {
            cache.put(clazzName, true);
            return true;
        }

        for (Blocker blocker : Main.blockers)
        {
            String name = blocker.getClass().getSimpleName();
            String banner = blocker.getBanner();
            String bannerPrefix = blocker.getBannerPrefix();

            if (matchBannerName(clazzName, banner, bannerPrefix))
            {
                if (context != null) {
                    Util.notifyAdNetwork(context, pkgName, name);
                }
                cache.put(clazzName, true);
                return true;
            }
        }

        cache.put(clazzName, false);
        return false;
    }

    private static Boolean isAdView(Context context, String pkgName, View view)
    {
        if (view == null) return false;

        Class<?> clazz = view.getClass();
        int maxDepth = 5;

        for (int i = 0; i < maxDepth && clazz != null && clazz != Object.class; i++)
        {
            String clazzName = clazz.getName();

            if (isAdView(context, pkgName, clazzName))
                return true;

            clazz = clazz.getSuperclass();
        }

        return false;
    }

    private static void clearAdViewInLayout(final String packageName, final View view)
    {
        if (view == null) return;

        if (isAdView(view.getContext(), packageName, view))
        {
            ViewBlocking.removeAdView(packageName, view);
        }

        if (view instanceof ViewGroup)
        {
            ViewGroup vg = (ViewGroup) view;

            for (int i = 0; i < vg.getChildCount(); i++)
            {
                clearAdViewInLayout(packageName, vg.getChildAt(i));
            }
        }
    }

    public static void nameBasedBlocking(final String pkgName, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        Util.hookAllMethods("android.view.ViewGroup", lpparam.classLoader, "addView", new XC_MethodHook()
        {
            @Override
            protected void beforeHookedMethod(MethodHookParam param)
            {
                if (param.args == null || param.args.length == 0) return;
                View view = (View) param.args[0];

                if (view != null && isAdView(view.getContext(), pkgName, view))
                {
                    ViewBlocking.removeAdView(pkgName, view);
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param)
            {
                if (param.args == null || param.args.length == 0) return;
                View view = (View) param.args[0];

                if (view != null && isAdView(view.getContext(), pkgName, view))
                {
                    ViewBlocking.removeAdView(pkgName, view);
                }
            }
        });

        Util.hookAllMethods("android.app.Activity", lpparam.classLoader, "setContentView", new XC_MethodHook()
        {
            @Override
            protected void afterHookedMethod(MethodHookParam param)
            {
                if (param.thisObject == null) return;
                Activity ac = (Activity) (param.thisObject);
                if (ac.getWindow() == null || ac.getWindow().getDecorView() == null) return;

                ViewGroup root = ac.getWindow().getDecorView().findViewById(android.R.id.content);

                clearAdViewInLayout(pkgName, root);
            }
        });

        Util.hookAllMethods("android.view.LayoutInflater", lpparam.classLoader, "inflate", new XC_MethodHook()
        {
            @Override
            protected void afterHookedMethod(MethodHookParam param)
            {
                if (param.getResult() == null) return;
                View root = (View) param.getResult();

                if (root != null)
                    clearAdViewInLayout(pkgName, root);
            }
        });
    }
}
