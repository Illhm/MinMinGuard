package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class GoogleMobileAdsModern extends Blocker {

    private static final String BANNER_PREFIX = "com.google.android.gms.ads";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        String[] banners = {
            "com.google.android.gms.ads.AdView",
            "com.google.android.gms.ads.BaseAdView",
            "com.google.android.gms.ads.search.SearchAdView"
        };
        for (String b : banners) {
            result |= ApiBlocking.removeBanner(packageName, b, "loadAd", lpparam);
            result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, b, "show", lpparam);
        }

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.google.android.gms.ads.interstitial.InterstitialAd", "load", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.google.android.gms.ads.interstitial.InterstitialAd", "show", lpparam);

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.google.android.gms.ads.AdLoader", "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.google.android.gms.ads.AdLoader", "loadAds", lpparam);

        String[] natives = {
            "com.google.android.gms.ads.nativead.NativeAdView",
            "com.google.android.gms.ads.nativead.MediaView"
        };
        for (String n : natives) {
            result |= ApiBlocking.removeBanner(packageName, n, "setNativeAd", lpparam);
            result |= ApiBlocking.removeBanner(packageName, n, "setMediaContent", lpparam);
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
