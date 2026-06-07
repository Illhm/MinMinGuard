package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class VungleModern extends Blocker {

    private static final String BANNER_PREFIX = "com.vungle";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.vungle.warren.Vungle", "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.vungle.warren.Vungle", "playAd", lpparam);

        result |= ApiBlocking.removeBanner(packageName, "com.vungle.warren.ui.view.VungleBannerView", "loadAd", lpparam);
        result |= ApiBlocking.removeBanner(packageName, "com.vungle.warren.ui.view.VungleNativeView", "loadAd", lpparam);

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
