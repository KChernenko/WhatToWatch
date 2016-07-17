package me.bitfrom.whattowatch.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.core.model.Film;
import me.bitfrom.whattowatch.ui.activities.DetailActivity;
import me.bitfrom.whattowatch.ui.activities.MainActivity;
import me.bitfrom.whattowatch.ui.base.BaseFragment;
import me.bitfrom.whattowatch.ui.fragments.presenters.FavoritesPresenter;
import me.bitfrom.whattowatch.ui.fragments.views.FavoritesMvpView;
import me.bitfrom.whattowatch.ui.recyclerview.FilmsAdapter;
import me.bitfrom.whattowatch.ui.recyclerview.RecyclerItemClickListener;
import me.bitfrom.whattowatch.utils.ConstantsManager;

public class FavoritesFragment extends BaseFragment implements FavoritesMvpView {

    @Inject
    protected FavoritesPresenter presenter;
    @Inject
    protected FilmsAdapter filmsAdapter;

    @BindView(R.id.favorite_root_layout)
    protected RelativeLayout rootLayout;
    @BindView(R.id.favorite_list)
    protected RecyclerView recyclerView;
    @BindView(R.id.favorite_list_empty)
    protected TextView emptyView;
    @BindView(R.id.nothing_found)
    protected TextView nothingFoundView;

    @BindString(R.string.error_list_empty)
    protected String errorUnknownMsg;
    @BindString(R.string.empty_favorite_list)
    protected String errorEmptyListMsg;
    @BindString(R.string.error_nothing_has_found)
    protected String errorNothingFoundMsg;
    @BindString(R.string.search_title)
    protected String searchHintMsg;

    private RecyclerItemClickListener recyclerItemClickListener;
    private SearchView searchView;
    private SearchView.OnQueryTextListener queryListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        getFragmentComponent((MainActivity) getActivity()).inject(this);
        ButterKnife.bind(this, rootView);

        initRecyclerView();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        searchView = (SearchView)menu.findItem(R.id.search_action).getActionView();
        searchView.setQueryHint(searchHintMsg);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        initOnQueryListener();
        searchView.setOnQueryTextListener(queryListener);
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
        presenter.attachView(this);
        presenter.getFavoriteFilms();
        initKeyListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        //noinspection ConstantConditions
        getView().setOnKeyListener(null);
        if (searchView != null) searchView.onActionViewCollapsed();
        if (presenter != null) presenter.detachView();
        if (recyclerView != null) recyclerView.removeOnItemTouchListener(recyclerItemClickListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchView != null) searchView.setOnQueryTextListener(null);
    }

    @Override
    public void showListOfFavorites(@NonNull List<Film> favoriteFilms) {
        nothingFoundView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        filmsAdapter.setFilms(favoriteFilms);
        filmsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showListIsEmpty() {
        nothingFoundView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUnknownError() {
        Snackbar.make(rootLayout,
                errorUnknownMsg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNothingHasFound() {
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        nothingFoundView.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(filmsAdapter);
    }

    private void initOnQueryListener() {
        queryListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.search(newText);
                return false;
            }
        };
    }

    private void initKeyListener() {
        //noinspection ConstantConditions
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                // handle back button's click listener
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                    searchView.onActionViewCollapsed();
                } else {
                    getActivity().onBackPressed();
                }
                return true;
            }
            return false;
        });
    }
}