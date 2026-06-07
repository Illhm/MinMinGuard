package tw.fatminmin.xposed.minminguard.blocker;

import android.net.Uri;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.Main;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static de.robv.android.xposed.XposedHelpers.findClass;

public final class UrlFiltering
{
    private static boolean adExist = false;

    static public boolean removeWebViewAds(final String packageName, LoadPackageParam lpparam)
    {
        try
        {
            Class<?> adView = findClass("android.webkit.WebView", lpparam.classLoader);

            XposedBridge.hookAllMethods(adView, "loadUrl", new XC_MethodHook()
            {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                {
                    if (param.args == null || param.args.length == 0 || !(param.args[0] instanceof String)) return;
                    String url = (String) param.args[0];
                    adExist = urlFiltering(url, "", param, packageName);
                    if (adExist)
                    {
                        param.setResult(null);
                    }
                }
            });

            XposedBridge.hookAllMethods(adView, "loadData", new XC_MethodHook()
            {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                {
                    if (param.args == null || param.args.length == 0 || !(param.args[0] instanceof String)) return;
                    String data = (String) param.args[0];
                    adExist = urlFiltering("", data, param, packageName);
                    if (adExist)
                    {
                        param.setResult(null);
                    }
                }
            });

            XposedBridge.hookAllMethods(adView, "loadDataWithBaseURL", new XC_MethodHook()
            {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                {
                    if (param.args == null || param.args.length < 2) return;

                    String url = param.args[0] instanceof String ? (String) param.args[0] : "";
                    String data = param.args[1] instanceof String ? (String) param.args[1] : "";

                    adExist = urlFiltering(url, data, param, packageName);
                    if (adExist)
                    {
                        param.setResult(null);
                    }
                }
            });
        }
        catch (ClassNotFoundError e)
        {
            return false;
        }
        return adExist;
    }

    static private boolean urlFiltering(String url, String data, MethodHookParam param, String packageName)
    {
        if (url == null) url = "";
        if (data == null) data = "";

        try
        {
            url = URLDecoder.decode(url, "UTF-8");
        }
        catch (Exception e)
        {
            // Ignore encoding exceptions
        }

        try {
            Uri uri = Uri.parse(url);
            String host = uri.getHost();
            if (host != null) {
                for (String adUrl : Main.patterns) {
                    if (host.contains(adUrl) || url.contains(adUrl)) {
                        if (param.thisObject instanceof View) {
                            ViewBlocking.removeAdView(packageName, (View) param.thisObject);
                        }
                        return true;
                    }
                }
            } else {
                for (String adUrl : Main.patterns) {
                    if (url.contains(adUrl)) {
                        if (param.thisObject instanceof View) {
                            ViewBlocking.removeAdView(packageName, (View) param.thisObject);
                        }
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // Ignore URI parsing exceptions
        }

        for (String adUrl : Main.patterns)
        {
            if (url.contains(adUrl))
            {
                if (param.thisObject instanceof View) {
                    ViewBlocking.removeAdView(packageName, (View) param.thisObject);
                }
                return true;
            }
        }

        try
        {
            data = URLDecoder.decode(data, "UTF-8");
        }
        catch (Exception e)
        {
            // Ignore encoding exceptions
        }

        for (String adUrl : Main.patterns)
        {
            if (data.contains(adUrl))
            {
                if (param.thisObject instanceof View) {
                    ViewBlocking.removeAdView(packageName, (View) param.thisObject);
                }
                return true;
            }
        }

        return false;
    }
}
