package me.bitfrom.whattowatch.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.ui.fragments.SliderFragment;

public class WWIntro extends AppIntro{

    @Override
    public void init(Bundle savedInstanceState) {
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
    public void onSkipPressed() {

    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    @Override
    public void onSlideChanged() {

    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
