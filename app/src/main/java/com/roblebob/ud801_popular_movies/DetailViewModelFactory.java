package com.roblebob.ud801_popular_movies;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application application;
    private final int movieID;

    public DetailViewModelFactory(Application application, int movieID) {
        super(application);
        this.application = application;
        this.movieID = movieID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailViewModel( application, movieID);
    }
}