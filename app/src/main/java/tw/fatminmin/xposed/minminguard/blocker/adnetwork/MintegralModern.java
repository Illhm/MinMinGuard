package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class MintegralModern extends Blocker {

    private static final String BANNER_PREFIX = "com.mbridge.msdk";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.mbridge.msdk.out.MBBannerView", "load", lpparam);
        result |= ApiBlocking.removeBanner(packageName, "com.mbridge.msdk.out.MBBannerView", "init", lpparam);

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.mbridge.msdk.interstitial.out.InterstitialHandler", "load", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.mbridge.msdk.interstitial.out.InterstitialHandler", "show", lpparam);

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.mbridge.msdk.interstitialvideo.out.MBInterstitialVideoHandler", "load", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.mbridge.msdk.interstitialvideo.out.MBInterstitialVideoHandler", "show", lpparam);

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
