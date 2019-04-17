package com.AKS.chooser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class FragmentSetting extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.setting_fragment,container,false);
        ImageView imageViewVK = fragment.findViewById(R.id.imageViewVKSetting);
        imageViewVK.setOnClickListener(this);
        ImageView imageViewInst = fragment.findViewById(R.id.imageViewInstagramSetting);
        imageViewInst.setOnClickListener(this);
        Button b = fragment.findViewById(R.id.buttonRateAppSetting);
        b.setOnClickListener(this);

        final Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.gear_setting_anim);
        ImageView imageViewGear = fragment.findViewById(R.id.imageView_gear_setting);
        imageViewGear.startAnimation(anim);
        Toolbar toolbar = fragment.findViewById(R.id.toolbarSetting);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backspace);
        toolbar.setTitle(R.string.setting_setting);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonRateAppSetting:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
                break;
            case R.id.imageViewInstagramSetting:
                Intent browserIntentInst = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/kair32.ru/"));
                startActivity(browserIntentInst);
                break;
            case R.id.imageViewVKSetting:
                Intent browserIntentVK = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/id77986339"));
                startActivity(browserIntentVK);
                break;
        }
    }
}
