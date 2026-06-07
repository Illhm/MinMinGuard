package tw.fatminmin.xposed.minminguard.blocker;

import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import java.io.ByteArrayInputStream;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.Main;

public class WebViewRequestBlocking {

    public static void handle(final String packageName, final LoadPackageParam lpparam) {
        if (lpparam == null || lpparam.classLoader == null || packageName == null) return;

        try {
            Class<?> webViewClientClass = XposedHelpers.findClassIfExists("android.webkit.WebViewClient", lpparam.classLoader);
            if (webViewClientClass == null) return;

            // Hook for older APIs (API < 21)
            XposedBridge.hookAllMethods(webViewClientClass, "shouldInterceptRequest", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    try {
                        if (param.args == null || param.args.length < 2) return;

                        // the signature is shouldInterceptRequest(WebView view, String url)
                        if (param.args[1] instanceof String) {
                            String url = (String) param.args[1];
                            if (shouldBlock(url)) {
                                param.setResult(createEmptyResponse());
                            }
                        }
                    } catch (Throwable t) {
                        // ignore
                    }
                }
            });

            // Hook for newer APIs (API >= 21)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                XposedBridge.hookAllMethods(webViewClientClass, "shouldInterceptRequest", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        try {
                            if (param.args == null || param.args.length < 2) return;

                            // the signature is shouldInterceptRequest(WebView view, WebResourceRequest request)
                            if (param.args[1] instanceof WebResourceRequest) {
                                WebResourceRequest request = (WebResourceRequest) param.args[1];
                                Uri uri = request.getUrl();
                                if (uri != null) {
                                    String url = uri.toString();
                                    if (shouldBlock(url)) {
                                        param.setResult(createEmptyResponse());
                                    }
                                }
                            }
                        } catch (Throwable t) {
                            // ignore
                        }
                    }
                });
            }

            Util.log("WebViewRequestBlocking", "Enabled WebView request interception for " + packageName);

        } catch (Throwable t) {
            Util.log("WebViewRequestBlocking", "Failed to hook WebViewClient in " + packageName + ": " + t.getMessage());
        }
    }

    private static boolean shouldBlock(String url) {
        if (url == null) return false;

        try {
            Uri uri = Uri.parse(url);
            String host = uri.getHost();
            if (host != null) {
                for (String adUrl : Main.patterns) {
                    if (host.contains(adUrl) || url.contains(adUrl)) {
                        return true;
                    }
                }
            } else {
                for (String adUrl : Main.patterns) {
                    if (url.contains(adUrl)) {
                        return true;
                    }
                }
            }
        } catch (Throwable t) {
            // fallback if URI parse fails
            for (String adUrl : Main.patterns) {
                if (url.contains(adUrl)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static WebResourceResponse createEmptyResponse() {
        return new WebResourceResponse("text/plain", "UTF-8", new ByteArrayInputStream("".getBytes()));
    }
}
