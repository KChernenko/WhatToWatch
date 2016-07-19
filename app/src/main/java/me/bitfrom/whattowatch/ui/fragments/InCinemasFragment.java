package me.bitfrom.whattowatch.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.core.storage.entities.FilmEntity;
import me.bitfrom.whattowatch.ui.activities.DetailActivity;
import me.bitfrom.whattowatch.ui.activities.MainActivity;
import me.bitfrom.whattowatch.ui.base.BaseFragment;
import me.bitfrom.whattowatch.ui.fragments.presenters.InCinemasPresenter;
import me.bitfrom.whattowatch.ui.fragments.views.InCinemasMvpView;
import me.bitfrom.whattowatch.ui.recyclerview.FilmsAdapter;
import me.bitfrom.whattowatch.ui.recyclerview.RecyclerItemClickListener;
import me.bitfrom.whattowatch.utils.ConstantsManager;

public class InCinemasFragment extends BaseFragment implements InCinemasMvpView,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    protected InCinemasPresenter presenter;
    @Inject
    protected FilmsAdapter filmsAdapter;

    @BindView(R.id.films_root_layout)
    protected RelativeLayout rootLayout;
    @BindView(R.id.list_of_films)
    protected RecyclerView recyclerView;
    @BindView(R.id.films_list_empty)
    protected TextView emptyView;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;

    @BindString(R.string.error_list_empty)
    protected String errorUnknownMsg;

    private RecyclerItemClickListener recyclerItemClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.films_list_fragment, container, false);

        getFragmentComponent((MainActivity) getActivity()).inject(this);
        ButterKnife.bind(this, rootView);

        initRecyclerView();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerItemClickListener = new RecyclerItemClickListener(getActivity(), (view, position) -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(ConstantsManager.POSITION_ID_KEY, filmsAdapter.getImdbIdByPosition(position));
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeCustomAnimation(getActivity(), R.anim.enter_pull_in, R.anim.exit_fade_out);
            ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
        });
        recyclerView.addOnItemTouchListener(recyclerItemClickListener);
        swipeRefreshLayout.setOnRefreshListener(this);
        presenter.attachView(this);
        presenter.getFilms();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) presenter.detachView();
        if (swipeRefreshLayout != null) swipeRefreshLayout.setOnRefreshListener(null);
        if (recyclerView != null) recyclerView.removeOnItemTouchListener(recyclerItemClickListener);
    }

    @Override
    public void onRefresh() {
        loadNewFilms(true);
    }

    @Override
    public void showFilmsList(@NonNull List<FilmEntity> films) {
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        filmsAdapter.setFilms(films);
        filmsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showListIsEmpty() {
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showServerError() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.app_name)
                .setMessage(errorUnknownMsg)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                }).show();
    }

    @Override
    public void showInternetUnavailableError() {
        Snackbar.make(rootLayout,
                getString(R.string.error_connection_unavailable),
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void loadNewFilms(boolean pullToRefresh) {
        presenter.loadFilms(pullToRefresh);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        swipeRefreshLayout.setRefreshing(pullToRefresh);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(filmsAdapter);
    }
}