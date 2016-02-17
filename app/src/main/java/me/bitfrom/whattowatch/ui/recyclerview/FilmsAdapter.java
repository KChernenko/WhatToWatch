package me.bitfrom.whattowatch.ui.recyclerview;

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
import me.bitfrom.whattowatch.data.image.ImageLoader;
import me.bitfrom.whattowatch.data.model.FilmModel;

import static me.bitfrom.whattowatch.data.image.ImageLoaderInteractor.Flag;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.FilmsViewHolder> {

    private List<FilmModel> mFilms;
    @Inject
    protected ImageLoader mImageLoader;

    @Inject
    public FilmsAdapter() {
        mFilms = new ArrayList<>();
    }

    public void  setFilms(List<FilmModel> films) {
        mFilms = films;
    }

    @Override
    public FilmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.film_item, parent, false);
        return new FilmsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FilmsViewHolder holder, int position) {
        FilmModel film = mFilms.get(position);
        mImageLoader.loadImage(Flag.RECYCLER_ITEM, film.urlPoster, holder.poster);
        holder.title.setText(film.title);
        holder.director.setText(film.directors);
        holder.genres.setText(film.genres);
        holder.rating.setText(film.rating);
        holder.year.setText(film.year);
    }

    @Override
    public int getItemCount() {
        return mFilms.size();
    }

    class FilmsViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cv_poster)
        ImageView poster;
        @Bind(R.id.cv_title)
        TextView title;
        @Bind(R.id.cv_director)
        TextView director;
        @Bind(R.id.cv_genre)
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
