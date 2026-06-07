package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class MetaAudienceNetworkModern extends Blocker {

    private static final String BANNER_PREFIX = "com.facebook.ads";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        String[] banners = {
            "com.facebook.ads.AdView",
            "com.facebook.ads.NativeAdLayout",
            "com.facebook.ads.MediaView"
        };
        for (String b : banners) {
            result |= ApiBlocking.removeBanner(packageName, b, "loadAd", lpparam);
            result |= ApiBlocking.removeBanner(packageName, b, "loadAdFromBid", lpparam);
        }

        String[] inters = {
            "com.facebook.ads.InterstitialAd",
            "com.facebook.ads.NativeAd",
            "com.facebook.ads.NativeBannerAd"
        };
        for (String i : inters) {
            result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, i, "loadAd", lpparam);
            result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, i, "loadAdFromBid", lpparam);
            result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, i, "show", lpparam);
        }

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
