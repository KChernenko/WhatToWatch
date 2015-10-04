package me.bitfrom.whattowatch.adapter;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.bitfrom.whattowatch.R;

import static me.bitfrom.whattowatch.data.FilmsContract.*;

/**
 * Created by Constantine with love.
 */
public class FilmsRecyclerAdapter extends RecyclerView.Adapter<FilmsRecyclerAdapter.ViewHolder> {

    private static final String LOG_TAG = FilmsRecyclerAdapter.class.getSimpleName();

    protected boolean mDataValid;
    protected boolean mAutoRequery;
    protected Cursor mCursor;
    protected Context mContext;
    protected int mRowIDColumn;
    protected ChangeObserver mChangeObserver;
    protected DataSetObserver mDataSetObserver;
    protected FilterQueryProvider mFilterQueryProvider;
    public static final int FLAG_AUTO_REQUERY = 0x01;
    public static final int FLAG_REGISTER_CONTENT_OBSERVER = 0x02;

    //Recommended
    public FilmsRecyclerAdapter(Context context, Cursor c, int flags) {
        init(context, c, flags);
    }

    public FilmsRecyclerAdapter(Context context, Cursor c) {
        init(context, c, FLAG_AUTO_REQUERY);
    }

    public FilmsRecyclerAdapter(Context context, Cursor c, boolean autoRequery) {
        init(context, c, autoRequery ? FLAG_AUTO_REQUERY : FLAG_REGISTER_CONTENT_OBSERVER);
    }

    void init(Context context, Cursor c, int flags) {
        if ((flags & FLAG_AUTO_REQUERY) == FLAG_AUTO_REQUERY) {
            flags |= FLAG_REGISTER_CONTENT_OBSERVER;
            mAutoRequery = true;
        } else {
            mAutoRequery = false;
        }
        boolean cursorPresent = c != null;
        mCursor = c;
        mDataValid = cursorPresent;
        mContext = context;
        mRowIDColumn = cursorPresent ? c.getColumnIndexOrThrow("_id") : -1;
        if ((flags & FLAG_REGISTER_CONTENT_OBSERVER) == FLAG_REGISTER_CONTENT_OBSERVER) {
            mChangeObserver = new ChangeObserver();
            mDataSetObserver = new MyDataSetObserver();
        } else {
            mChangeObserver = null;
            mDataSetObserver = null;
        }

        if (cursorPresent) {
            if (mChangeObserver != null) c.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) c.registerDataSetObserver(mDataSetObserver);
        }
    }

    public int getCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    public Cursor getItem(int position) {
        if (mDataValid && mCursor != null) {
            mCursor.moveToPosition(position);
            return mCursor;
        } else {
            return null;
        }
    }



    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                return mCursor.getLong(mRowIDColumn);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        if (oldCursor != null) {
            if (mChangeObserver != null) oldCursor.unregisterContentObserver(mChangeObserver);
            if (mDataSetObserver != null) oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (newCursor != null) {
            if (mChangeObserver != null) newCursor.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) newCursor.registerDataSetObserver(mDataSetObserver);
            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            mRowIDColumn = -1;
            mDataValid = false;
            // notify the observers about the lack of a data set
            notifyDataSetInvalidated();
        }
        return oldCursor;
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public CharSequence convertToString(Cursor cursor) {
        return cursor == null ? "" : cursor.toString();
    }

    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (mFilterQueryProvider != null) {
            return mFilterQueryProvider.runQuery(constraint);
        }
        return mCursor;
    }


    public FilterQueryProvider getFilterQueryProvider() {
        return mFilterQueryProvider;
    }

    public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
        mFilterQueryProvider = filterQueryProvider;
    }

    protected void onContentChanged() {
        if (mAutoRequery && mCursor != null && !mCursor.isClosed()) {
            if (false) Log.v(LOG_TAG, "Auto requerying " + mCursor + " due to update");
            mDataValid = mCursor.requery();
        }
    }

    private class ChangeObserver extends ContentObserver {
        public ChangeObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            onContentChanged();
        }
    }

    private class MyDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            mDataValid = false;
            notifyDataSetInvalidated();
        }
    }


    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Cursor cursor = getItem(position);

        Picasso.with(mContext).load(cursor.getString(cursor.getColumnIndex(FilmsEntry.COLUMN_URL_POSTER)))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.drawable.progress_animation)
                .resize(115, 170)
                .centerCrop()
                .into(holder.poster);

        holder.title.setText(cursor.getString(cursor.getColumnIndex(FilmsEntry.COLUMN_TITLE)));
        holder.director.setText(cursor.getString(cursor.getColumnIndex(FilmsEntry.COLUMN_DIRECTORS)));
        holder.genres.setText(cursor.getString(cursor.getColumnIndex(FilmsEntry.COLUMN_GENRES)));
        holder.rating.setText(cursor.getString(cursor.getColumnIndex(FilmsEntry.COLUMN_RATING)));
        holder.year.setText(cursor.getString(cursor.getColumnIndex(FilmsEntry.COLUMN_YEAR)));

    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView poster;
        public final TextView title;
        public final TextView director;
        public final TextView genres;
        public final TextView rating;
        public final TextView year;

        public ViewHolder(View itemView) {
            super(itemView);

            poster = (ImageView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            director = (TextView) itemView.findViewById(R.id.cv_director);
            genres = (TextView) itemView.findViewById(R.id.cv_genre);
            rating = (TextView) itemView.findViewById(R.id.cv_rating);
            year = (TextView) itemView.findViewById(R.id.cv_year);
        }
    }

}
