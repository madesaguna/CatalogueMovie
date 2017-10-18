package com.omrobbie.cataloguemovie.ui.listmovie;

import android.util.Log;

import com.omrobbie.cataloguemovie.data.DataManager;
import com.omrobbie.cataloguemovie.data.model.search.ResultsItem;
import com.omrobbie.cataloguemovie.data.model.search.SearchModel;
import com.omrobbie.cataloguemovie.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListMoviePresenter extends BasePresenter<ListMovieContract.View>
        implements ListMovieContract.Presenter {

    private static final String TAG = ListMoviePresenter.class.getSimpleName();
    private final DataManager mDataManager;
    private int mPageNumber = 0;
    private int mTotalPage = 0;

    @Inject
    public ListMoviePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getPopularMovie() {
        getMvpView().showLoading(true);

        if (mPageNumber < mTotalPage) {
            mPageNumber++;
        }

        Log.d(TAG, "getPopularMovie: page number " + String.valueOf(mPageNumber));

        Disposable disposable = mDataManager.getPopularMovie(mPageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getMvpView().showLoading(false);
                    }
                })
                .subscribeWith(new DisposableSingleObserver<SearchModel>() {
                    @Override
                    public void onSuccess(@NonNull SearchModel searchModel) {
                        List<ResultsItem> listMovie = searchModel.getResults();
                        mTotalPage = searchModel.getTotalPages();
                        Log.d(TAG, "onSuccess: result size= " + listMovie.size());
                        if (!listMovie.isEmpty()) {
                            getMvpView().showPopularMovie(listMovie, searchModel.getTotalResults());
                        } else {
                            Log.d(TAG, "onSuccess: no movie found");
                            getMvpView().showNoDataFound();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getMvpView().showError(e);
                    }
                });

        mDisposables.add(disposable);
    }

    @Override
    public void onPageRefresh() {
        mPageNumber = 1;
        mTotalPage = 0;
        getPopularMovie();
    }

}