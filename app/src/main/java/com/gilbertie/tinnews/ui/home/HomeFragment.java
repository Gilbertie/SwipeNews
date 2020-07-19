package com.gilbertie.tinnews.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gilbertie.tinnews.R;
import com.gilbertie.tinnews.databinding.FragmentHomeBinding;
import com.gilbertie.tinnews.model.Article;
import com.gilbertie.tinnews.repositroy.NewsRepository;
import com.gilbertie.tinnews.repositroy.NewsViewModelFactory;
import com.mindorks.placeholderview.SwipeDecor;

import static android.widget.Toast.LENGTH_SHORT;

public class HomeFragment extends Fragment implements TinNewsCard.OnSwipeListener {

    private HomeViewModel viewModel;
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // bind view
        binding
                .swipeView
                .getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(
                        new SwipeDecor()
                                .setPaddingTop(20)
                                .setRelativeScale(0.01f));

        binding.rejectBtn.setOnClickListener(v -> binding.swipeView.doSwipe(false));
        binding.acceptBtn.setOnClickListener(v -> binding.swipeView.doSwipe(true));

        // get repository
        NewsRepository repository = new NewsRepository(getContext());

        // get ViewModelProvider (VMP) to create viewModel
        // VMP stores context, factory, and existing viewModel
        // repository is passed from Factory to Provider to ViewModel
        ViewModelProvider vmp = new ViewModelProvider(this, new NewsViewModelFactory(repository));

        // return existing one if there is a satisfactory viewModel stored
        // use factory to create one with context if there is not
        viewModel = vmp.get(HomeViewModel.class);

        // listen on input and NewsResponse
        viewModel.getTopHeadlines() // here returns a MediatorLiveData
                .observe(getViewLifecycleOwner(), // only when the owner is in "started" and "resumed" states, observer receive events
                        newsResponse -> { // this is an observer, listening on LiveData<NewsResponse>, sourceY within the inner observer of MediatorLiveData
                            if (newsResponse != null) {
                                for (Article article : newsResponse.articles) {
                                    TinNewsCard tinNewsCard = new TinNewsCard(article, this);
                                    binding.swipeView.addView(tinNewsCard);
                                }
                            }
                        });

        // set input
        viewModel.setCountryInput("us");

        // handle like
        viewModel
                .onFavorite()
                .observe(
                        getViewLifecycleOwner(),
                        isSuccess -> {
                            if (isSuccess) {
                                Toast.makeText(getContext(), "Success", LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "You might have liked before", LENGTH_SHORT).show();
                            }
                        });
    }

    @Override
    public void onLike(Article news) {
        viewModel.setFavoriteArticleInput(news);
    }

    @Override
    public void onDisLike(Article news) {
        if (binding.swipeView.getChildCount() < 3) {
            viewModel.setCountryInput("us");
        }
    }

    @Override
    public void onDestroyView() { // prevent memory leak, remove references (such as requests' references to fragment) to release resources
        super.onDestroyView();
        viewModel.onCancel();
    }
}