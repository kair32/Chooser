package com.example.chooser;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class FragmentLoading extends Fragment {
    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    private MyTimerTaskTwo MyTimerTaskTwo;
    private View fragment;
    private int textnumber = 0;
    private TextView loadText;
    private int timer = 1500;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.loading_layout,container,false);
        loadText = fragment.findViewById(R.id.loading_textView);
        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        MyTimerTaskTwo = new MyTimerTaskTwo();
        mTimer.schedule(mMyTimerTask, 0,timer);
        mTimer.schedule(MyTimerTaskTwo, 0,timer/3);
        final Animation[] anim = {AnimationUtils.loadAnimation(getContext(), R.anim.gear_loading_anim)};
        final ImageView imageViewGear = fragment.findViewById(R.id.loading_imageView);
        imageViewGear.startAnimation(anim[0]);
        return fragment;
    }
    class MyTimerTaskTwo extends TimerTask {
        @Override
        public void run() {
            Handler handler = new Handler(getContext().getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loadText.setText(loadText.getText() + ".");
                }
            });
        }
    }
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            switch (textnumber){
                case 0:
                    setTextPost(getString(R.string.loading_text_0));
                    textnumber = 1;
                    return;
                case 1:
                    setTextPost(getString(R.string.loading_text_1));
                    textnumber = 2;
                    return;
                case 2:
                    setTextPost(getString(R.string.loading_text_2));
                    textnumber = 3;
                    return;
                case 3:
                    setTextPost(getString(R.string.loading_text_3));
                    textnumber = 4;
                    return;
                case 4:
                    setTextPost(getString(R.string.loading_text_4));
                    textnumber = 5;
                    return;
                case 5:
                    mTimer.cancel();
                    getActivity().getSupportFragmentManager().popBackStack();
                    return;
            }
        }
    }

    @Override
    public void onDestroyView() {
        mTimer.cancel();
        super.onDestroyView();
    }

    private void setTextPost(final String text) {
        Handler handler = new Handler(getContext().getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                loadText.setText(text);
            }
        });
    }
}
