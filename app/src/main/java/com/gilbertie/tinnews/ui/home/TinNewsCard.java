package com.gilbertie.tinnews.ui.home;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.gilbertie.tinnews.R;
import com.gilbertie.tinnews.model.Article;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.squareup.picasso.Picasso;


@Layout(R.layout.tin_news_card)
public class TinNewsCard {

    @View(R.id.news_image)
    private ImageView image;

    @View(R.id.news_title)
    private TextView newsTitle;

    @View(R.id.news_description)
    private TextView newsDescription;

    private final Article article;
    private final OnSwipeListener onSwipeListener;

    public TinNewsCard(Article news, OnSwipeListener onSwipeListener) {
        this.article = news;
        this.onSwipeListener = onSwipeListener;
    }

    @Resolve
    private void onResolved() {
        if (article.urlToImage == null || article.urlToImage.isEmpty()) {
            image.setImageResource(R.drawable.ic_empty_image);
        } else {
            Picasso.get().load(article.urlToImage).into(image);
        }
        newsTitle.setText(article.title);
        newsDescription.setText(article.description);
    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeOut
    private void onSwipedOut() {
        onSwipeListener.onDisLike(article);
        Log.d("EVENT", "onSwipedOut");
    }

    @SwipeIn
    private void onSwipeIn() {
        onSwipeListener.onLike(article);
        article.favorite = true;
        Log.d("EVENT", "onSwipedIn");
    }

    interface OnSwipeListener {
        void onLike(Article article);
        void onDisLike(Article article);
    }
}
