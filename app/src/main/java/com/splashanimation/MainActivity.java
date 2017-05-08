package com.splashanimation;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    ImageView logoOuterIv;
    ImageView logoInnerIv;
    TextView appName;
    boolean isShowingRubberEffect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getIds();
        initAnimation();
    }

    private void getIds()
    {
        logoOuterIv=(ImageView)findViewById(R.id.logo_outer_iv);
        logoInnerIv=(ImageView)findViewById(R.id.logo_inner_iv);
        appName=(TextView)findViewById(R.id.appName);
    }

    private void initAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_top_in);
        logoInnerIv.startAnimation(animation);

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                if (fraction >= 0.8 && !isShowingRubberEffect) {
                    isShowingRubberEffect = true;
                    startLogoOuter();
                    startShowAppName();
                    finishActivity();
                } else if (fraction >= 0.95) {
                    valueAnimator.cancel();
                    startLogoInner2();
                }

            }
        });
        valueAnimator.start();

    }

    private void startLogoOuter() {
        YoYo.with(Techniques.RubberBand).duration(1000).playOn(logoOuterIv);
    }

    private void startShowAppName() {
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(appName);
    }

    private void startLogoInner2() {
        YoYo.with(Techniques.Bounce).duration(1000).playOn(logoInnerIv);
    }

    private void finishActivity() {
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {

                    }
                });
    }

}
