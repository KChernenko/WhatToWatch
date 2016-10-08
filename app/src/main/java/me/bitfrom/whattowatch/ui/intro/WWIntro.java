package me.bitfrom.whattowatch.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.ui.main.MainActivity;

public class WWIntro extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showStatusBar(false);
        showSkipButton(false);

        addSlide(SliderFragment.newInstance(R.layout.intro_slide_1));
        addSlide(SliderFragment.newInstance(R.layout.intro_slide_2));
        addSlide(SliderFragment.newInstance(R.layout.intro_slide_3));
        addSlide(SliderFragment.newInstance(R.layout.intro_slide_4));
        addSlide(SliderFragment.newInstance(R.layout.intro_slide_5));
        addSlide(SliderFragment.newInstance(R.layout.intro_slide_6));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}