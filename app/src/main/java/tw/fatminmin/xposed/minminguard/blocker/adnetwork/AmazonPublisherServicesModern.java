package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class AmazonPublisherServicesModern extends Blocker {

    private static final String BANNER_PREFIX = "com.amazon.device.ads";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, "com.amazon.device.ads.DTBAdView", "fetchAd", lpparam);

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.amazon.device.ads.DTBAdInterstitial", "fetchAd", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.amazon.device.ads.DTBAdInterstitial", "show", lpparam);

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.amazon.device.ads.DTBAdRequest", "loadAd", lpparam);

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
