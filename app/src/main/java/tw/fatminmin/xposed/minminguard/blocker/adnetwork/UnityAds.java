package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class UnityAds extends Blocker
{

    private static final String UnityAds = "com.unity3d.ads.UnityAds";

    @Override
    public boolean handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.blockAdFunctionWithSafeDefault(packageName, UnityAds, "show", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, UnityAds, "canShow", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, UnityAds, "canShowAds", lpparam);

        return result;
    }

    @Override
    public String getBanner()
    {
        return null;
    }

    @Override
    public String getBannerPrefix()
    {
        return null;
    }
}

