package me.bitfrom.whattowatch.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.core.image.ImageDownloader;
import me.bitfrom.whattowatch.core.model.Film;

import static me.bitfrom.whattowatch.core.image.ImageLoaderInteractor.Flag;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.FilmsViewHolder> {

    private List<Film> mFilms;
    @Inject
    protected ImageDownloader mImageLoader;

    @Inject
    public FilmsAdapter() {
        mFilms = new ArrayList<>();
    }

    public void setFilms(@NonNull List<Film> films) {
        mFilms = films;
    }

    public String getImdbIdByPosition(int position) {
        Film film = mFilms.get(position);
        return film.idIMDB();
    }

    @Override
    public FilmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.film_item, parent, false);
        return new FilmsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmsViewHolder holder, int position) {
        Film film = mFilms.get(position);
        mImageLoader.loadImage(Flag.RECYCLER_ITEM, film.urlPoster(), holder.poster);
        holder.title.setText(film.title());
        holder.director.setText(film.directors());
        holder.genres.setText(film.genres());
        holder.rating.setText(film.rating());
        holder.year.setText(film.year());
    }

    @Override
    public int getItemCount() {
        return mFilms.size();
    }

    public class FilmsViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cv_poster)
        ImageView poster;
        @Bind(R.id.cv_title)
        TextView title;
        @Bind(R.id.cv_directors)
        TextView director;
        @Bind(R.id.cv_genres)
        TextView genres;
        @Bind(R.id.cv_rating)
        TextView rating;
        @Bind(R.id.cv_year)
        TextView year;

        public FilmsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
