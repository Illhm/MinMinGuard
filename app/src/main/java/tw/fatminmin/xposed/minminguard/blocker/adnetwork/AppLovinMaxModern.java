package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class AppLovinMaxModern extends Blocker {

    private static final String BANNER_PREFIX = "com.applovin";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        String[] banners = {
            "com.applovin.mediation.ads.MaxAdView",
            "com.applovin.mediation.nativeAds.MaxNativeAdView",
            "com.applovin.sdk.AppLovinAdView"
        };
        for (String b : banners) {
            result |= ApiBlocking.removeBanner(packageName, b, "loadAd", lpparam);
            result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, b, "render", lpparam);
        }

        String[] inters = {
            "com.applovin.mediation.ads.MaxInterstitialAd",
            "com.applovin.adview.AppLovinInterstitialAd",
            "com.applovin.adview.AppLovinInterstitialAdDialog"
        };
        for (String i : inters) {
            result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, i, "loadAd", lpparam);
            result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, i, "showAd", lpparam);
            result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, i, "show", lpparam);
        }

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.applovin.mediation.nativeAds.MaxNativeAdLoader", "loadAd", lpparam);

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
