package com.mobon.perpl_sdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.crossmedia.perpl.view.PerplVideoAdView;
import com.crossmedia.perpl.view.PerplVideoAdView.PerplAdListener;

import org.json.JSONException;
import org.json.JSONObject;

public class PerplAdapter {

    private static final String TAG = "Mobon_PerplAdapter";

    private Context mContext;
    private String PLACEMENT_PARAMETER;
    private int mAdType;
    private PerplVideoAdView mPerplVideoAdView;
    ;
    private Dialog mInterstitialAd;
    private AlertDialog mEndingAd;
    private boolean isInterstitialLoaded;
    private boolean isTestMode;
    private View.OnClickListener mEndingListener;
    private View.OnClickListener mInterstitialListener;
    private boolean isLog;
    private float mDisplayRatio;
    private ContentValues mParams;
    private PerplDialogFragment mPerpleDialog;


    public PerplAdapter(Context context) {
        mContext = context;
    }

    public PerplAdapter(Context context, String _key) {
        mContext = context;
    }

    public void setLog(boolean is) {
        isLog = is;
    }

    public void setTestMode(boolean is) {
        isTestMode = is;
    }

    public void close() {
        if (mInterstitialAd != null && mInterstitialAd.isShowing())
            mInterstitialAd.dismiss();
        if (mEndingAd != null && mEndingAd.isShowing())
            mEndingAd.dismiss();
    }

    public void init(String mediaKey, String key, int adType) {
        destroy();
        this.PLACEMENT_PARAMETER = key;
        this.mAdType = adType;

        if (isLog) {
            System.out.println(TAG + " : init   mediaKey : " + mediaKey + " : init   key : " + key + " : adtype : " + adType);
        }

        mPerplVideoAdView = new PerplVideoAdView(mContext);
        mPerplVideoAdView.setVolume(100);
        mPerplVideoAdView.setTimeout(999);
        mParams = new ContentValues();
        mParams.put("setMediaId", mediaKey);                          // 매체 id
        mParams.put("setSlotId", key);                            // 지면 id
        mParams.put("setGender", "N");                                     // 성별
        mParams.put("setAge", "Y00");                                       // 나이
        mParams.put("setContentTitle", "Mobon,동영상,광고플랫폼");  // 컨텐츠 제목

        // mPerplVideoAdView.initPerpl(mParams, mPerplAdListener);

        switch (adType) {
            case MediationAdSize.INTERSTITIAL_SMALL:
            case MediationAdSize.INTERSTITIAL:
            case MediationAdSize.INTERSTITIAL_POPUP:
            case MediationAdSize.VIDEO:
                mPerpleDialog = PerplDialogFragment.getInstance(mPerplVideoAdView);


//                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                final View view = inflater.inflate(R.layout.perpl_interstitial_layout, null);
//                if (view != null) {
//                    LinearLayout ad_container = view.findViewById(R.id.ad_container);
//                    ad_container.removeAllViews();
//                    ad_container.addView(mPerplVideoAdView);
//
//                    ImageButton cancel = view.findViewById(R.id.cancel_btn);
//                    cancel.setBackgroundColor(Color.TRANSPARENT);
//                    cancel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (mPerplVideoAdView != null)
//                                mPerplVideoAdView.stop();
//
//                            if (mInterstitialAd != null && mInterstitialAd.isShowing())
//                                mInterstitialAd.dismiss();
//
//                            try {
//                                JSONObject obj = new JSONObject();
//                                obj.put("code", MediationAdCode.AD_LISTENER_CODE_AD_CLOSE);
//                                obj.put("msg", "");
//                                v.setTag(obj);
//                                mInterstitialListener.onClick(v);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//
//                }
////                AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.PerplDialogTheme);
////                builder.setView(view);
////                builder.setCustomTitle(null);
////                mInterstitialAd = builder.create();
//
//                mInterstitialAd = new Dialog(mContext, R.style.PerplDialogTheme);
//                mInterstitialAd.setContentView(view);
//
//                mInterstitialAd.setOnKeyListener(new Dialog.OnKeyListener() {
//
//                    @Override
//                    public boolean onKey(DialogInterface arg0, int keyCode,
//                                         KeyEvent event) {
//                        // TODO Auto-generated method stub
//                        if (keyCode == KeyEvent.KEYCODE_BACK) {
//                            mInterstitialAd.dismiss();
//
//                            try {
//                                JSONObject obj = new JSONObject();
//                                obj.put("code", MediationAdCode.AD_LISTENER_CODE_AD_CLOSE);
//                                obj.put("msg", "");
//                                View v = new View(mContext);
//                                v.setTag(obj);
//                                mInterstitialListener.onClick(v);
//                                destroy();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        return true;
//                    }
//                });
                break;

            case MediationAdSize.ENDING:
            case MediationAdSize.NATIVE:

                break;

            default:
                //   setBannerLayoutParams(320,50);
                break;
        }
    }


    public Object getInterstitialView() {
        return mInterstitialAd;
    }


    public Object geEndingView() {
        if (mEndingAd != null) {
            return mEndingAd;
        }
        return null;
    }

    public void setAdListener(final View.OnClickListener _listner) {

        final View v = new View(mContext);

        if (!(mContext instanceof Activity)) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("code", MediationAdCode.AD_LISTENER_CODE_ERROR);
                obj.put("msg", "context is not Activity");
                v.setTag(obj);
                _listner.onClick(v);
                mInterstitialAd = null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }


        mInterstitialListener = _listner;
        mPerpleDialog.setAdListener(_listner);

        //    mPerplVideoAdView.initPerpl(mParams, mPerplAdListener);

        mPerplVideoAdView.initPerpl(mParams, new PerplAdListener() {
            @Override
            public void onAdLoaded(String sessionKey) {
                // 광고 동영상이 play준비가 완료되면 호출됩니다.
                // mPerplVideoAdView.start();

                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! onAdLoaded");

                isInterstitialLoaded = true;
                Log.d(TAG, "onAdLoadSucceeded");

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("code", MediationAdCode.AD_LISTENER_CODE_AD_LOAD);
                    v.setTag(obj);
                    _listner.onClick(v);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onAdStarted(String sessionKey) {
                // 광고 동영상이 start 되면 호출됩니다.

            }

            @Override
            public void onAdFirstQuartile(String sessionKey) {
                // 광고 동영상이 1/4지점을 통과할 때 호출됩니다.

            }

            @Override
            public void onAdMidpoint(String sessionKey) {
                // 광고 동영상이 1/2지점을 통과할 때 호출됩니다.

            }

            @Override
            public void onAdThirdQuartile(String sessionKey) {
                // 광고 동영상이 3/4지점을 통과할 때 호출됩니다.

            }

            @Override
            public void onAdComplete(String sessionKey) {
                // 광고 동영상이 종료되면 호출됩니다.

            }

            @Override
            public void onAdProgress(String sessionKey) {
                // 광고 동영상이 과금시점을 통과할때 호출됩니다.

            }

            @Override
            public void onAdStop(String sessionKey) {
                // Stop 이벤트가 발생되면 호출됩니다.
            }

            @Override
            public void onAdSkiped(String sessionKey) {
                // 광고 화면에서 Skip버튼을 클릭하면 호출됩니다.
            }

            @Override
            public void onAdError(String sessionKey, String errorCode) {
                // 광고 동영상이 play되는 도중에 에러가 발생되면 호출됩니다.
                isInterstitialLoaded = false;
                Log.d(TAG, "Video ad failed to load with error: " +
                        errorCode);

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("code", MediationAdCode.AD_LISTENER_CODE_ERROR);
                    obj.put("msg", errorCode);
                    v.setTag(obj);
                    _listner.onClick(v);
                    mInterstitialAd = null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAdClicked(String sessionKey) {
                // 광고 화면에서 버튼을 클릭하면 호출됩니다.
                Log.d(TAG, "onAdClicked");
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("code", MediationAdCode.AD_LISTENER_CODE_AD_CLICK);
                    v.setTag(obj);
                    _listner.onClick(v);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAdSkipStateChange(String sessionKey) {
                // Skip 카운터에서 Skip버튼으로 전환되는 시점에 호출됩니다.
            }

            @Override
            public void onAdPassBack() {
                isInterstitialLoaded = false;
                // 광고가 없는 경우 호출됩니다.
                // 에러코드의 경우 -1, -8, -9, -10에 해당됩니다.

            }
        });

        //      }


    }

    public void loadAd() {

        if (isLog)
            System.out.println(TAG + "loadAd() call");
        try {
//            if (mPerplVideoAdView != null)
//                mPerplVideoAdView.loadAd();
        } catch (Exception e) {
            System.out.println("Perpl loadAd : " + e.getMessage());
        }

    }

    public boolean isLoaded() {
        if (isLog)
            System.out.println(TAG + "isLoaded() call");
        if ((mAdType == MediationAdSize.INTERSTITIAL_SMALL || mAdType == MediationAdSize.INTERSTITIAL || mAdType == MediationAdSize.INTERSTITIAL_POPUP) && mInterstitialAd != null && isInterstitialLoaded) {
            return true;
        } else if (mAdType == MediationAdSize.ENDING && mEndingAd != null) {
            return true;
        }

        return false;
    }

    public boolean show() {
        if (isLog)
            System.out.println(TAG + "show() call");
        // if ((mAdType == MediationAdSize.INTERSTITIAL_SMALL || mAdType == MediationAdSize.INTERSTITIAL || mAdType == MediationAdSize.INTERSTITIAL_POPUP) && mInterstitialAd != null && isInterstitialLoaded) {

        if (isInterstitialLoaded) {
            mPerpleDialog.show(((Activity) mContext).getFragmentManager(), "");

            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPerplVideoAdView.start();
                }
            }, 1000);
            isInterstitialLoaded = false;

            return true;
        }

//        } else if (mAdType == MediationAdSize.ENDING && mEndingAd != null) {
//            mEndingAd.show();
//            return true;
//        }


        return true;
    }

    public void destroy() {
        if (isLog)
            System.out.println(TAG + "destory() call");
        if (mPerplVideoAdView != null)
            mPerplVideoAdView.destroy();

        if (mInterstitialAd != null)
            mInterstitialAd = null;

        if (mEndingAd != null)
            mEndingAd = null;
    }

    public void getVersion() {
        System.out.println(TAG + ":" + BuildConfig.ADAPTER_LIB_VERSION);
    }


    private void setBannerLayoutParams(int _width, int _height) {
        if (isLog)
            System.out.println(TAG + "setBannerLayoutParams() call");
        int width = toPixelUnits(_width);
        int height = toPixelUnits(_height);
        RelativeLayout.LayoutParams bannerLayoutParams = new RelativeLayout.LayoutParams(width, height);
        bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bannerLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mPerplVideoAdView.setLayoutParams(bannerLayoutParams);
    }

    private int toPixelUnits(int dipUnit) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return Math.round(dipUnit * density);
    }


    private void setPopupDialogSize(int adType) {
        final DisplayMetrics dm = mContext.getResources().getDisplayMetrics();

        while (mInterstitialAd.getWindow() != null) {
            WindowManager.LayoutParams params = mInterstitialAd.getWindow().getAttributes();
            if (adType == MediationAdSize.INTERSTITIAL_POPUP) {
                params.width = dm.widthPixels - convertDpToPx(60);
                params.height = dm.heightPixels - convertDpToPx(120);
                params.dimAmount = 0.6f;
                mInterstitialAd.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mInterstitialAd.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
            }
            break;
        }
    }

    private int convertDpToPx(int pDp) {
        try {
            return ((int) (pDp * mContext.getResources().getDisplayMetrics().density));
        } catch (Exception e) {
            return 0;
        }
    }

}