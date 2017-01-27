package com.pedrocova.popularmoviesapp.Utils;

import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.Gravity;

/**
 * Created by pedro on 26/01/2017.
 */

public class ViewUtils {

    public static class ProgressDrawableManager {

        // Custom method to generate progress bar progress drawable object
        public Drawable generateProgressDrawable(){
            // Create an oval shape
            OvalShape oval = new OvalShape();

            // Create a ShapeDrawable to generate progress bar background
            ShapeDrawable backgroundDrawable = new ShapeDrawable(oval);
            backgroundDrawable.getPaint().setColor(Color.parseColor("#FFB5DDFF"));

            // Initialize a new shape drawable to draw progress bar progress
            ShapeDrawable progressDrawable = new ShapeDrawable(oval);
            //progressDrawable.getPaint().set(paint);
            progressDrawable.getPaint().setColor(Color.parseColor("#FF0A589B"));

            // Another shape drawable to draw secondary progress
            ShapeDrawable secondaryProgressDrawable = new ShapeDrawable(oval);
            secondaryProgressDrawable.getPaint().setColor(Color.parseColor("#350A589B"));

            // Initialize a ClipDrawable to generate progress on progress bar
            ClipDrawable progressClip = new ClipDrawable(progressDrawable, Gravity.BOTTOM,ClipDrawable.VERTICAL);

            // Another clip drawable to draw secondary progress
            ClipDrawable secondaryProgressClip = new ClipDrawable(secondaryProgressDrawable,Gravity.BOTTOM,ClipDrawable.VERTICAL);

            // Initialize a new LayerDrawable to hold progress bar all states
            LayerDrawable layer = new LayerDrawable(new Drawable[]{backgroundDrawable,secondaryProgressClip,progressClip});

            // Set the ids for different layers on layer drawable
            layer.setId(0,android.R.id.background);
            layer.setId(1,android.R.id.secondaryProgress);
            layer.setId(2,android.R.id.progress);

            // Return the LayerDrawable as progress bar progress drawable
            return layer;
        }
    }
}
