package tw.fatminmin.xposed.minminguard.blocker.custom_mod;

import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.Util;

public final class Train
{
    private static String pkg = "idv.nightgospel.TWRailScheduleLookUp";

    private Train() throws InstantiationException
    {
        throw new InstantiationException("This class is not for instantiation");
    }

    public static void handleLoadPackage(LoadPackageParam lpparam)
    {
        if (!lpparam.packageName.equals(pkg))
        {
            return;
        }

        XposedHelpers.findAndHookMethod("com.waystorm.ads.WSAdBanner", lpparam.classLoader, "setWSAdListener", "com.waystorm.ads.WSAdListener", new XC_MethodHook()
        {
            @Override
            protected void beforeHookedMethod(MethodHookParam param)
            {
                String debugMsg = String.format("Prevent WSAdBanner setWSAdListener %s", pkg);
                Util.log(pkg, debugMsg);
                param.setResult(new Object());
            }
        });
    }

    public static void handleInitPackageResources(InitPackageResourcesParam resparam)
    {
        if (!resparam.packageName.equals(pkg))
        {
            return;
        }

        resparam.res.hookLayout(pkg, "layout", "adlayout", new XC_LayoutInflated()
        {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam)
            {
                String debugMsg = String.format("Handle train ad layout %s", pkg);
                Util.log(pkg, debugMsg);
                View ad = liparam.view.findViewById(liparam.res.getIdentifier("adLayout", "id", pkg));
                if (ad != null)
                {
                    ad.setVisibility(View.GONE);
                }
            }
        });
    }
}
