package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Appnext extends Blocker
{
    private static final String INTER_ADS = "com.appnext.ads.interstitial.Interstitial";

    private static final String VIDEO_ADS = "com.appnext.ads.fullscreen.Video";
    private static final String FULLSCREEN_ADS = "com.appnext.ads.fullscreen.FullscreenAd";

    @Override
    public boolean handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.blockAdFunctionWithSafeDefault(packageName, INTER_ADS, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, INTER_ADS, "showAd", lpparam);

        // Do not block Video ads blindly to protect rewarded video flows.
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, FULLSCREEN_ADS, "showAd", lpparam);

        return result;
    }

    @Override
    public String getBanner()
    {
        return null;
    }

    @Override
    public String getBannerPrefix()
    {
        return null;
    }
}
