package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class StartIoModern extends Blocker {

    private static final String BANNER_PREFIX = "com.startapp.sdk";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, "com.startapp.sdk.ads.banner.Banner", "loadAd", lpparam);

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.startapp.sdk.adsbase.StartAppAd", "showAd", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.startapp.sdk.adsbase.StartAppAd", "loadAd", lpparam);

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
