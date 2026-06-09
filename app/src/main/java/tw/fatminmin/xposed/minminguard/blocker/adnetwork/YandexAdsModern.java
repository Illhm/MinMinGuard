package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class YandexAdsModern extends Blocker {

    private static final String BANNER_PREFIX = "com.yandex.mobile.ads";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, "com.yandex.mobile.ads.banner.BannerAdView", "loadAd", lpparam);

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.yandex.mobile.ads.interstitial.InterstitialAd", "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.yandex.mobile.ads.interstitial.InterstitialAd", "show", lpparam);

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.yandex.mobile.ads.nativeads.NativeAdLoader", "loadAd", lpparam);

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
