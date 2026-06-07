package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class FyberModern extends Blocker {

    private static final String BANNER_PREFIX = "com.fyber.inneractive.sdk";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.fyber.inneractive.sdk.external.InneractiveAdSpotManager", "createSpot", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.fyber.inneractive.sdk.external.InneractiveAdSpot", "requestAd", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.fyber.inneractive.sdk.external.InneractiveFullscreenUnitController", "show", lpparam);

        result |= ApiBlocking.removeBanner(packageName, "com.fyber.inneractive.sdk.ui.IAmraidWebViewController", "show", lpparam);
        result |= ApiBlocking.removeBanner(packageName, "com.fyber.inneractive.sdk.external.InneractiveAdViewUnitController", "bindView", lpparam);

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
