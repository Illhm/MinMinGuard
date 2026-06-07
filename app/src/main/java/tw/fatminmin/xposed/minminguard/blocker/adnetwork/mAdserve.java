package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;

public class mAdserve extends Blocker
{

    private static final String BANNER = "com.adsdk.sdk.banner.InAppWebView";
    private static final String BANNER_PREFIX = "com.adsdk.sdk.banner";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        if (ApiBlocking.blockConstructor(packageName, BANNER, lpparam)) {
            Util.log(packageName, packageName + " uses mAdserve");
            return true;
        }
        return false;
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
