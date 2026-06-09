package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class UnityAdsModern extends Blocker {

    private static final String BANNER_PREFIX = "com.unity3d.services.ads";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.unity3d.ads.UnityAds", "load", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.unity3d.ads.UnityAds", "show", lpparam);

        result |= ApiBlocking.removeBanner(packageName, "com.unity3d.services.banners.BannerView", "load", lpparam);

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
