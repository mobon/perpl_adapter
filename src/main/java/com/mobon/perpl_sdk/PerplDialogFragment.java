package com.mobon.perpl_sdk;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.crossmedia.perpl.view.PerplVideoAdView;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class PerplDialogFragment extends DialogFragment {

    private static PerplVideoAdView mPerplVideoAdView;
    private View.OnClickListener mInterstitialListener;


    public PerplDialogFragment() {

    }

    public static PerplDialogFragment getInstance(PerplVideoAdView _view) {

        mPerplVideoAdView = _view;
        PerplDialogFragment p = new PerplDialogFragment();
        return p;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.perpl_interstitial_layout, null);
        LinearLayout ad_container = view.findViewById(R.id.ad_container);
        ad_container.addView(mPerplVideoAdView);


        ImageButton cancel = view.findViewById(R.id.cancel_btn);
        cancel.setBackgroundColor(Color.TRANSPARENT);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPerplVideoAdView != null)
                    mPerplVideoAdView.stop();
                dismiss();
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("code", MediationAdCode.AD_LISTENER_CODE_AD_CLOSE);
                    obj.put("msg", "");
                    v.setTag(obj);
                    mInterstitialListener.onClick(v);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode, android.view.KeyEvent event) {

                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("code", MediationAdCode.AD_LISTENER_CODE_AD_CLOSE);
                        obj.put("msg", "");
                        View v = new View(getActivity());
                        v.setTag(obj);
                        mInterstitialListener.onClick(v);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return false; // pretend we've processed it

            }
        });


        //    mPerplVideoAdView.start();
        return view;
    }

    public void setAdListener(final View.OnClickListener _listener) {
        mInterstitialListener = _listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPerplVideoAdView != null) {
            //  onResume 되는 시점에 호출
            mPerplVideoAdView.stop();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPerplVideoAdView != null) {
            //  onPause 되는 시점에 호출
            mPerplVideoAdView.onPause();
        }
     //   dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPerplVideoAdView != null) {
            mPerplVideoAdView.onDestroy();
            mPerplVideoAdView = null;
        }
    }
}
