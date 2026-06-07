package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class InMobiModern extends Blocker {

    private static final String BANNER_PREFIX = "com.inmobi";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, "com.inmobi.ads.InMobiBanner", "load", lpparam);

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.inmobi.ads.InMobiInterstitial", "load", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.inmobi.ads.InMobiInterstitial", "show", lpparam);

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.inmobi.ads.InMobiNative", "load", lpparam);

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
