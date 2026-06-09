package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class IronSourceModern extends Blocker {

    private static final String BANNER_PREFIX = "com.ironsource";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.ironsource.mediationsdk.IronSource", "loadInterstitial", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.ironsource.mediationsdk.IronSource", "showInterstitial", lpparam);

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.ironsource.mediationsdk.IronSource", "loadBanner", lpparam);

        result |= ApiBlocking.removeBanner(packageName, "com.ironsource.mediationsdk.IronSourceBannerLayout", "loadAd", lpparam);

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
