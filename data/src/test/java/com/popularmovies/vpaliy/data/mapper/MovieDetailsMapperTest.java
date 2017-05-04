package com.popularmovies.vpaliy.data.mapper;

import com.popularmovies.vpaliy.data.entity.ActorEntity;
import com.popularmovies.vpaliy.data.entity.Movie;
import com.popularmovies.vpaliy.data.entity.MovieDetailEntity;
import com.popularmovies.vpaliy.data.source.DataSourceTestUtils;
import com.popularmovies.vpaliy.data.source.local.MoviesContract;
import com.popularmovies.vpaliy.domain.model.ActorCover;
import com.popularmovies.vpaliy.domain.model.MovieCover;
import com.popularmovies.vpaliy.domain.model.MovieDetails;
import com.popularmovies.vpaliy.domain.model.MovieInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MovieDetailsMapperTest {

    private Mapper<MovieDetails,MovieDetailEntity> mapper;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private ActorMapper actorMapper;

    @Mock
    private MovieInfoMapper infoMapper;


    @Before
    public void setUp(){
        mapper=new MovieDetailsMapper(movieMapper,actorMapper,infoMapper);

    }
    @Test
    public void testMappingToMovieDetails(){
        mapper.map(provideFakeDetailEntity());

        verify(movieMapper).map(any(Movie.class));
        verify(movieMapper).map(anyList());
        verify(infoMapper).map(any(Movie.class));
        verify(actorMapper).map(anyList());

    }


    @Test
    public void testReverseMapping(){
        when(actorMapper.map(anyList())).thenReturn(Collections.singletonList(new ActorCover(0,0)));
        when(movieMapper.map(anyList())).thenReturn(Collections.singletonList(new MovieCover()));
        when(movieMapper.map(any(Movie.class))).thenReturn(new MovieCover());
        when(infoMapper.map(any(Movie.class))).thenReturn(new MovieInfo(0,null));
        MovieDetailEntity detailEntity=provideFakeDetailEntity();
        List<?> entityList=detailEntity.getSimilarMovies();
        int movieMapperCalls=entityList!=null?entityList.size():0;
        entityList=detailEntity.getCast();
        int castMapperCalls=entityList!=null?entityList.size():0;

        MovieDetails movieDetails=mapper.map(detailEntity);
        mapper.reverseMap(movieDetails);


        verify(movieMapper,times(movieMapperCalls)).reverseMap(any(MovieCover.class));
        verify(infoMapper).reverseMap(any(MovieInfo.class));
        verify(actorMapper,times(castMapperCalls)).reverseMap(any(ActorCover.class));

    }

    private MovieDetailEntity provideFakeDetailEntity(){
        Movie movie=DataSourceTestUtils.provideFakeMovie();
        MovieDetailEntity entity=new MovieDetailEntity();
        entity.setMovie(DataSourceTestUtils.provideFakeMovie());
        entity.setSimilarMovies(Collections.singletonList(movie));
        entity.setCast(Collections.singletonList(new ActorEntity()));
        return entity;
    }
}
