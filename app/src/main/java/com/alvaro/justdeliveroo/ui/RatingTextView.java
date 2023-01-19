package com.alvaro.justdeliveroo.ui;

import static android.view.Gravity.CENTER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.alvaro.justdeliveroo.R;
/**
 * Permite dar un color al rating en función de este
 * */
public class RatingTextView extends AppCompatTextView {

    private Float rating = 0.0f;

    public RatingTextView(Context context) {
        super(context);
        setUpRateStar();
        drawRating();
    }

    public RatingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpRateStar();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RatingTextView);
        rating = ta.getFloat(R.styleable.RatingTextView_rating,0.0f);
        ta.recycle();
        drawRating();
    }

    public RatingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpRateStar();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RatingTextView);
        rating = ta.getFloat(R.styleable.RatingTextView_rating,0.0f);
        ta.recycle();
        drawRating();
    }

    private void setUpRateStar() {
        Drawable starImage = ContextCompat.getDrawable(getContext(), R.drawable.ic_star);
        setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, starImage,null);
    }

    @SuppressLint("SetTextI18n")
    private void drawRating() {
        //Cambio de color
        setTextColor(getResources().getColor(R.color.white));
        setText(" "+ rating);
        setGravity(CENTER);
        setPadding(8,8,8,8);
        if(rating>=8.5){
            setBackgroundColor(getResources().getColor(R.color.ratingHigh));
        }else if(rating>=5)
            setBackgroundColor(getResources().getColor(R.color.ratingMedium));
        else
            setBackgroundColor(getResources().getColor(R.color.ratingLow));

    }

    public void setRating(Float rate){
        rating = rate;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRating();
    }
}
