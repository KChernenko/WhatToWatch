/*
 * Copyright (C) 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.bitfrom.whattowatch.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.HashMap;

import javax.inject.Inject;

public class ScrollManager extends RecyclerView.OnScrollListener {

    private static final int MIN_SCROLL_TO_HIDE = 10;
    private boolean hidden;
    private int accummulatedDy;
    private int totalDy;
    private int initialOffset;
    private HashMap<View, Direction> viewsToHide = new HashMap<>();

    public enum Direction {UP, DOWN}

    @Inject
    public ScrollManager() {
    }

    public void attach(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(this);
    }

    public void addView(View view, Direction direction) {
        viewsToHide.put(view, direction);
    }

    public void setInitialOffset(int initialOffset) {
        this.initialOffset = initialOffset;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        totalDy += dy;

        if (totalDy < initialOffset) {
            return;
        }

        if (dy > 0) {
            accummulatedDy = accummulatedDy > 0 ? accummulatedDy + dy : dy;
            if (accummulatedDy > MIN_SCROLL_TO_HIDE) {
                hideViews();
            }
        } else if (dy < 0) {
            accummulatedDy = accummulatedDy < 0 ? accummulatedDy + dy : dy;
            if (accummulatedDy < -MIN_SCROLL_TO_HIDE) {
                showViews();
            }
        }
    }

    public void hideViews() {
        if (!hidden) {
            hidden = true;
            for (View view : viewsToHide.keySet()) {
                hideView(view, viewsToHide.get(view));
            }
        }
    }

    /**
     * Listens ScrollView's onScrollChanged() and hides fab with animation.
     * @param scrollView - root scrollview
     * @param view - view, that we want to hide
     * @param direction - UP or DOWN direction
     **/
    public void hideViewInScrollView(final NestedScrollView scrollView, final View view, final Direction direction) {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int height = scrollView.getHeight();
            int scrollY = scrollView.getScrollY();
            if (scrollY >= height/7) {
                hideView(view, direction);
            } else {
                showView(view);
            }
        });
    }

    private void showViews() {
        if (hidden) {
            hidden = false;
            for (View view : viewsToHide.keySet()) {
                showView(view);
            }
        }
    }

    private void hideView(View view, Direction direction) {
        int height = calculateTranslation(view);
        int translateY = direction == Direction.UP ? -height : height;
        runTranslateAnimation(view, translateY, new AccelerateInterpolator(3));
    }

    /**
     * Takes height + margins
     * @param view View to translate
     * @return translation in pixels
     **/
    private int calculateTranslation(View view) {
        int height = view.getHeight();

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int margins = params.topMargin + params.bottomMargin;

        return height + margins;
    }

    private void showView(View view) {
        runTranslateAnimation(view, 0, new DecelerateInterpolator(3));
    }

    private void runTranslateAnimation(View view, int translateY, Interpolator interpolator) {
        Animator slideInAnimation = ObjectAnimator.ofFloat(view, "translationY", translateY);
        slideInAnimation.setDuration(view.getContext().getResources().getInteger(android.R.integer.config_mediumAnimTime));
        slideInAnimation.setInterpolator(interpolator);
        slideInAnimation.start();
    }
}
