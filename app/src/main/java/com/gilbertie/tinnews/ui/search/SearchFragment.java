package com.gilbertie.tinnews.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.gilbertie.tinnews.R;
import com.gilbertie.tinnews.databinding.FragmentSearchBinding;
import com.gilbertie.tinnews.model.Article;
import com.gilbertie.tinnews.repositroy.NewsRepository;
import com.gilbertie.tinnews.repositroy.NewsViewModelFactory;

import static android.widget.Toast.LENGTH_SHORT;

public class SearchFragment extends Fragment {

    private SearchViewModel viewModel;
    private FragmentSearchBinding binding;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // create adapter and configure it
        SearchNewsAdapter newsAdapter = new SearchNewsAdapter();
        newsAdapter.setLikeListener(new SearchNewsAdapter.LikeListener() {
            @Override
            public void onLike(Article article) {
                viewModel.setFavoriteArticleInput(article);
            }

            @Override
            public void onClick(Article article) {
                SearchFragmentDirections.ActionTitleSearchToDetail actionTitleSearchToDetail = SearchFragmentDirections.actionTitleSearchToDetail();
                actionTitleSearchToDetail.setArticle(article);
                NavHostFragment.findNavController(SearchFragment.this).navigate(actionTitleSearchToDetail);
            }
        });
        binding.recyclerView.setAdapter(newsAdapter);

        // create layout_manager and configure it
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return position == 0 ? 2 : 1;
                    }
                });
        binding.recyclerView.setLayoutManager((gridLayoutManager));

        // set listener on the search edit_text
        binding.searchView.setOnEditorActionListener(
                (v, actionId, event) -> {
                    String searchText = binding.searchView.getText().toString();
                    if (actionId == EditorInfo.IME_ACTION_DONE && !searchText.isEmpty()) {
                        viewModel.setSearchInput(searchText);
                        return true;
                    } else {
                        return false;
                    }
                });

        // get viewModel
        NewsRepository repository = new NewsRepository(getContext());
        viewModel = new ViewModelProvider(this, new NewsViewModelFactory(repository))
                .get(SearchViewModel.class);

        // observe newsResponse
        viewModel
                .searchNews()
                .observe(
                        getViewLifecycleOwner(),
                        newsResponse -> {
                            if (newsResponse != null) {
                                newsAdapter.setArticles(newsResponse.articles);
                            }
                        });

        // observe "isSuccess", or if an article is observed
        viewModel
                .onFavorite()
                .observe(
                        getViewLifecycleOwner(),
                        isSuccess -> {
                            if (isSuccess) {
                                Toast.makeText(requireActivity(), "Success", LENGTH_SHORT).show();
                                newsAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(requireActivity(), "You might have liked before", LENGTH_SHORT).show();
                            }
                        });

    }
}