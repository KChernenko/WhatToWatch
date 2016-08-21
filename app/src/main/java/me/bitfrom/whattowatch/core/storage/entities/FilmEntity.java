package me.bitfrom.whattowatch.core.storage.entities;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

@AutoValue
public abstract class FilmEntity implements FilmTableModel, Parcelable {

    public static final Factory<FilmEntity> FACTORY = new Factory<>((FilmTableModel.Creator<FilmEntity>)
            AutoValue_FilmEntity::new);

    public static final RowMapper<FilmEntity> MAPPER_SELECT_BY_CATEGORY_ID = FACTORY.select_by_category_idMapper();

    public static final RowMapper<FilmEntity> MAPPER_SELECT_BY_ID = FACTORY.select_by_idMapper();

    public static final RowMapper<FilmEntity> MAPPER_SELECT_FAVORITES = FACTORY.select_favoritesMapper();

    public static final RowMapper<FilmEntity> MAPPER_SELECT_LIKE = FACTORY.select_likeMapper();
}