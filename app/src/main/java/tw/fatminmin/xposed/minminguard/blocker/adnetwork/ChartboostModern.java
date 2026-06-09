package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class ChartboostModern extends Blocker {

    private static final String BANNER_PREFIX = "com.chartboost";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.chartboost.sdk.Chartboost", "showInterstitial", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.chartboost.sdk.Chartboost", "cacheInterstitial", lpparam);

        result |= ApiBlocking.removeBanner(packageName, "com.chartboost.sdk.ads.Banner", "show", lpparam);
        result |= ApiBlocking.removeBanner(packageName, "com.chartboost.sdk.ads.Banner", "cache", lpparam);

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.chartboost.sdk.ads.Interstitial", "show", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.chartboost.sdk.ads.Interstitial", "cache", lpparam);

        return result;
    }

    @Override
    public String getBannerPrefix() {
        return BANNER_PREFIX;
    }

    @Override
    public String getBanner() {
        return null;
    }
}
