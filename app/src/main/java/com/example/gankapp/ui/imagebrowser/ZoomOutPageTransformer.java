package com.example.gankapp.ui.imagebrowser;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by chunchun.hu on 2018/3/13.
 */

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(View page, float position) {
         int pageWidth = page.getWidth();
         int pageHeight = page.getHeight();

         if (position < -1){
             // [-Infinity,-1)
             // This page is way off-screen to the left.
             page.setAlpha(0);
         }else if (position <= 1){  //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
             // [-1,1]
             // Modify the default slide transition to shrink the page as well
             float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
             float vertMargin = pageHeight * (1 - scaleFactor) / 2;
             float horzMargin = pageWidth * (1 - scaleFactor) / 2;
             if (position < 0){
                 page.setTranslationX(horzMargin - vertMargin / 2);
             }else{
                 page.setTranslationX(-horzMargin + vertMargin / 2);
             }
             // Scale the page down (between MIN_SCALE and 1)
             page.setScaleX(scaleFactor);
             page.setScaleY(scaleFactor);
             // Fade the page relative to its size.
             page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_ALPHA) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
         }else{
             // (1,+Infinity]
             // This page is way off-screen to the right.
             page.setAlpha(0);
         }
    }
}
