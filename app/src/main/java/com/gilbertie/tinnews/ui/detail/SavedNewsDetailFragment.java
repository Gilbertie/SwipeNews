package com.gilbertie.tinnews.ui.detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gilbertie.tinnews.R;
import com.gilbertie.tinnews.databinding.FragmentSavedNewsDetailBinding;
import com.gilbertie.tinnews.model.Article;
import com.squareup.picasso.Picasso;

public class SavedNewsDetailFragment extends Fragment {

    private FragmentSavedNewsDetailBinding binding;

    public SavedNewsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedNewsDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() == null) {
            return;
        }
        Article article = SavedNewsDetailFragmentArgs.fromBundle(getArguments()).getArticle();
        if (article == null) {
            return;
        }
        binding.title.setText(article.title);
        binding.author.setText(article.author);
        binding.timeStamp.setText(article.publishedAt);
        if (article.urlToImage != null) {
            Picasso.get().load(article.urlToImage).into(binding.image);
        } else {
            binding.image.setImageResource(R.drawable.ic_empty_image);
        }
        binding.description.setText(article.description);
        binding.content.setText(article.content);
    }
}