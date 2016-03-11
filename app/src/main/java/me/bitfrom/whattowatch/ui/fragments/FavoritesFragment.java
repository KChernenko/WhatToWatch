package me.bitfrom.whattowatch.ui.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import butterknife.Bind;
import butterknife.BindString;
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
    protected FavoritesPresenter mFavoritesPresenter;
    @Inject
    protected FilmsAdapter mFilmsAdapter;

    @Bind(R.id.favorite_root_layout)
    protected RelativeLayout mRootLayout;
    @Bind(R.id.favorite_list)
    protected RecyclerView mRecyclerView;
    @Bind(R.id.favorite_list_empty)
    protected TextView mEmptyView;
    @Bind(R.id.nothing_found)
    protected TextView mNothingFound;

    @BindString(R.string.error_list_empty)
    protected String mErrorUnknown;
    @BindString(R.string.empty_favorite_list)
    protected String mErrorEmptyList;
    @BindString(R.string.error_nothing_has_found)
    protected String mErrorNothingFound;
    @BindString(R.string.search_title)
    protected String mSearchHint;

    private RecyclerItemClickListener mRecyclerItemClickListener;
    private SearchView mSearchView;
    private SearchView.OnQueryTextListener mQueryListener;

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
        mSearchView = (SearchView)menu.findItem(R.id.search_action).getActionView();
        mSearchView.setQueryHint(mSearchHint);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        initOnQueryListener();
        mSearchView.setOnQueryTextListener(mQueryListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerItemClickListener = new RecyclerItemClickListener(getActivity(), (view, position) -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(ConstantsManager.POSITION_ID_KEY, mFilmsAdapter.getImdbIdByPosition(position));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //noinspection unchecked
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(getActivity());
                ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
            } else {
                startActivity(intent);
            }
        });
        mRecyclerView.addOnItemTouchListener(mRecyclerItemClickListener);
        mFavoritesPresenter.attachView(this);
        mFavoritesPresenter.getFavoriteFilms();
        initKeyListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        //noinspection ConstantConditions
        getView().setOnKeyListener(null);
        if (mSearchView != null) mSearchView.onActionViewCollapsed();
        if (mFavoritesPresenter != null) mFavoritesPresenter.detachView();
        if (mRecyclerView != null) mRecyclerView.removeOnItemTouchListener(mRecyclerItemClickListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSearchView != null) mSearchView.setOnQueryTextListener(null);
    }

    @Override
    public void showListOfFavorites(List<Film> favoriteFilms) {
        mNothingFound.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mFilmsAdapter.setFilms(favoriteFilms);
        mFilmsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showListIsEmpty() {
        mNothingFound.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUnknownError() {
        Snackbar.make(mRootLayout,
                mErrorUnknown, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNothingHasFound() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mNothingFound.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFilmsAdapter);
    }

    private void initOnQueryListener() {
        mQueryListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mFavoritesPresenter.search(newText);
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
                if (!mSearchView.isIconified()) {
                    mSearchView.setIconified(true);
                    mSearchView.onActionViewCollapsed();
                } else {
                    getActivity().onBackPressed();
                }
                return true;
            }
            return false;
        });
    }
}
