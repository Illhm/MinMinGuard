package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class AppodealModern extends Blocker {

    private static final String BANNER_PREFIX = "com.appodeal";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.appodeal.ads.Appodeal", "show", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.appodeal.ads.Appodeal", "cache", lpparam);

        result |= ApiBlocking.removeBanner(packageName, "com.appodeal.ads.BannerView", "load", lpparam);
        result |= ApiBlocking.removeBanner(packageName, "com.appodeal.ads.NativeAdView", "load", lpparam);

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
