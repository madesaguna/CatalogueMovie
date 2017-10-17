package com.omrobbie.cataloguemovie.data;

import com.omrobbie.cataloguemovie.data.model.search.SearchModel;
import com.omrobbie.cataloguemovie.data.remote.ApiService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class DataManager {

    private final ApiService mApiService;

    @Inject
    public DataManager(ApiService mApiService) {
        this.mApiService = mApiService;
    }

    public Single<SearchModel> getPopularMovie(int page) {
        return mApiService.getPopularMovie(page);
    }

    public Single<SearchModel> searchMovie(int page, String movieTitle) {
        return mApiService.getSearchMovie(page, movieTitle);
    }

}
