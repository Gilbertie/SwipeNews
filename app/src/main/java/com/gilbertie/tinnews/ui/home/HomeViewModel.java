package com.gilbertie.tinnews.ui.home;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.gilbertie.tinnews.model.Article;
import com.gilbertie.tinnews.model.NewsResponse;
import com.gilbertie.tinnews.repositroy.NewsRepository;

public class HomeViewModel extends ViewModel {
    private final NewsRepository repository;
    private final MutableLiveData<String> countryInput = new MutableLiveData<>();
    private final MutableLiveData<Article> favoriteArticleInput = new MutableLiveData<>();

    public HomeViewModel(NewsRepository newsRepository) {
        this.repository = newsRepository;
    }

    public void setCountryInput(String country) {
        countryInput.setValue(country);
    }

    public void setFavoriteArticleInput(Article article) {
        favoriteArticleInput.setValue(article);
    }

    public LiveData<Boolean> onFavorite() {
        return Transformations.switchMap(favoriteArticleInput, repository::favoriteArticle);
    }

    public LiveData<NewsResponse> getTopHeadlines() {
//         return a MediatorLiveData {map[sourceX LiveData<X>: observer{sourceY LiveData<X>, onChanged}]}, observer.onChanged() is called when sourceX
//         1st parameter is a LiveData as sourceX, "countryInput", listened on by observer
//         2nd parameter is a Function, receives X as parameter, returns Y, "repository::getTopHeadlines", or (repository.getTopHeadlines(country)); returns NewsResponse
//         the function is used to override MediatorLiveData's observer's onChanged() {
//              newSource = function(x)
//              sourceY is updated as newSource, which means newSource is now listened on
//         }
//         in onChanged() of the MediatorLiveData, every time source is set, we get a new LiveData from the parameter Function, the new LiveData is set as new source of the MediatorLiveData
        return Transformations.switchMap(countryInput, repository::getTopHeadlines);
    }

    public void onCancel() {
        repository.onCancel();
    }
}
