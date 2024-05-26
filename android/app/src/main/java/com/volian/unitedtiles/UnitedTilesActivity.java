package com.volian.unitedtiles;

import org.libsdl.app.SDLActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.appopen.AppOpenAd;
import android.content.Context;
import android.os.Bundle;
import android.app.Application;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback;
import com.google.android.gms.ads.LoadAdError;
import android.app.Activity;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.AdError;
import android.app.Application.ActivityLifecycleCallbacks;
//import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.graphics.Color;
import android.view.ViewGroup; // Make sure to import ViewGroup
import android.view.SurfaceView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.OnUserEarnedRewardListener;

public class UnitedTilesActivity extends SDLActivity {
    public AdView adViewB = null;
    public AdSize adSizeB = null;
    public boolean resizeOnce = false;
    public boolean showBigOnce = false;
    public boolean adLoaded = false;
    //private static MinesweeperPillarActivity activity;

    public InterstitialAd mInterstitialAd;



    public void resizeWindowAd() {
        if (!resizeOnce) {
            ViewGroup.LayoutParams gameLayoutParams = ((View)mSurface).getLayoutParams();
            gameLayoutParams.height = ((View)mSurface).getHeight() - adSizeB.getHeightInPixels(this);
 
            ((View)mSurface).setLayoutParams(gameLayoutParams);
            resizeOnce = true;
        }
    }

    public void loadBigAd() {
        if (adLoaded) {
            return;
        }
        adLoaded = true;
        mInterstitialAd = null;

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
            new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd;

                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdClicked() {
                            // Called when a click is recorded for an ad.
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            mInterstitialAd = null;
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when ad fails to show.
                            mInterstitialAd = null;
                        }

                        @Override
                        public void onAdImpression() {
                            // Called when an impression is recorded for an ad.
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                        }
                    });

                    if (!showBigOnce) {
                        showBigOnce = true;
                        //showBigAd();
                    }

                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    mInterstitialAd = null;
                }
            });
    }

    public void showBigAd() {
        adLoaded = false;
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        }
    }

    public void showAd() {
        adViewB = new AdView(this);
        adViewB.setAdUnitId("ca-app-pub-3940256099942544/9214589741");
        adViewB.setAdSize(AdSize.BANNER);

        AdRequest adRequest = new AdRequest.Builder().build();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);

        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        adViewB.loadAd(adRequest);

        adViewB.setBackgroundColor(Color.TRANSPARENT);

        mLayout.addView(adViewB, params);

        adViewB.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                // Get the height of the ad in pixels
                int adHeight = adViewB.getHeight();
                adSizeB = adViewB.getAdSize();

                resizeWindowAd();
            }
        });

    }

    public void testLoadAd() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Your UI update logic goes here
                loadBigAd();
            }
        });
    }

    public void testShowAd() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Your UI update logic goes here
                showBigAd();
            }
        });
    }

    public void testShowBanner() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Your UI update logic goes here
                showAd();
            }
        });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
    }
}