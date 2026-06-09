package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class SmaatoModern extends Blocker {

    private static final String BANNER_PREFIX = "com.smaato.sdk";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, "com.smaato.sdk.banner.widget.BannerView", "loadAd", lpparam);

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.smaato.sdk.interstitial.InterstitialAd", "showAd", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.smaato.sdk.interstitial.SmaatoInterstitial", "loadAd", lpparam);

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
