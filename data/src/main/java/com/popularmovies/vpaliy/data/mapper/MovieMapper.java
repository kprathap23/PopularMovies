package com.popularmovies.vpaliy.data.mapper;

import android.util.Log;

import com.popularmovies.vpaliy.data.configuration.ImageQualityConfiguration;
import com.popularmovies.vpaliy.data.entity.Movie;
import com.popularmovies.vpaliy.domain.model.MediaCover;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import static com.popularmovies.vpaliy.data.utils.MapperUtils.convert;
import static com.popularmovies.vpaliy.data.utils.MapperUtils.convertToBackdrops;
import static com.popularmovies.vpaliy.data.utils.MapperUtils.convertToDuration;
import static com.popularmovies.vpaliy.data.utils.MapperUtils.convertToGenres;
import static com.popularmovies.vpaliy.data.utils.MapperUtils.convertToRuntime;
import static com.popularmovies.vpaliy.data.utils.MapperUtils.convertToYear;


@Singleton
public class MovieMapper extends Mapper<MediaCover,Movie> {

    private final ImageQualityConfiguration qualityConfiguration;

    @Inject
    public MovieMapper(ImageQualityConfiguration qualityConfiguration){
        this.qualityConfiguration=qualityConfiguration;
    }

    @Override
    public MediaCover map(Movie movieEntity) {
        MediaCover cover=new MediaCover();
        cover.setMediaId(movieEntity.getMovieId());
        cover.setPosterPath(qualityConfiguration.convertCover(movieEntity.getPosterPath()));
        cover.setGenres(convert(movieEntity.getGenres()));
        cover.setMovieTitle(movieEntity.getTitle());
        cover.setAverageRate(movieEntity.getVoteAverage());
        cover.setFavorite(movieEntity.isFavorite());
        cover.setBackdrops(convert(movieEntity.getBackdropImages(),qualityConfiguration));
        cover.setReleaseDate(movieEntity.getReleaseDate());
        cover.setDuration(convertToDuration(movieEntity.getRuntime()));
        cover.setReleaseDate(cover.getReleaseDate());
        cover.setReleaseYear(convertToYear(cover.getReleaseDate()));
        cover.setMustWatch(movieEntity.isMustWatch());
        cover.setWatched(movieEntity.isWatched());
        cover.setMainBackdrop(qualityConfiguration.convertBackdrop(movieEntity.getBackdrop_path()));
        return cover;
    }

    @Override
    public Movie reverseMap(MediaCover movieCover) {
        Movie result=new Movie();
        result.setMovieId(movieCover.getMediaId());
        result.setPosterPath(qualityConfiguration.extractPath(movieCover.getPosterPath()));
        result.setGenres(convertToGenres(movieCover.getGenres()));
        result.setTitle(movieCover.getMovieTitle());
        result.setVoteAverage(movieCover.getAverageVote());
        result.setBackdropPath(qualityConfiguration.extractPath(movieCover.getMainBackdrop()));
        result.setFavorite(movieCover.isFavorite());
        result.setRuntime(convertToRuntime(movieCover.getDuration()));
        result.setBackdropImages(convertToBackdrops(movieCover.getBackdrops(), qualityConfiguration));
        result.setReleaseDate(movieCover.getReleaseDate());
        result.setMustWatch(movieCover.isMustWatch());
        result.setWatched(movieCover.isWatched());
        return result;
    }
}
