package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Og extends Blocker
{

    private static final String BANNER = "com.og.wa.AdWebView";
    private static final String BANNER_PREFIX = "com.og.wa";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        return ApiBlocking.removeBanner(packageName, BANNER, "loadUrl", lpparam);
    }

    @Override
    public String getBannerPrefix()
    {
        return BANNER_PREFIX;
    }

    @Override
    public String getBanner()
    {
        return BANNER;
    }
}
