package com.accelerate.album.musicapi.service;

import com.accelerate.album.musicapi.model.Album;
import com.accelerate.album.musicapi.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Rashmi
 */

@Component
public class AlbumService {


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    AlbumRepository albumRepository;


    /**
     *
     * @param album
     * @return
     */
    public Optional<Album> createAlbum(Album album){
        Optional<Album> optionalAlbum = Optional.of(albumRepository.save(album));
        return optionalAlbum;
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<Album> deleteAlbum(long id){
        Optional<Album> optionalAlbum = Optional.of(albumRepository.findAlbumById(id));
        if (optionalAlbum.isPresent()){
            albumRepository.delete(optionalAlbum.get());
        }
        return optionalAlbum;
    }


    /**
     *
     * @param id
     * @param album
     * @return
     */
    public Optional<Object> updateAlbum(long id, Album album){
        Optional<Album> albumOptional = albumRepository.findById(id);
        if(!albumOptional.isPresent())
            return Optional.empty();
        album.setId(id);
        albumRepository.save(album);
        return Optional.of(album);
    }


    /**
     *
     * @param id
     * @return
     */
    public Album fethcAlbum(long id){
        Album album = albumRepository.findAlbumById(id);
        return album;
    }

    /**
     *
     * @param artist
     * @return
     */
    public List<Album> fetchAlbumByArtist(String artist){
        List<Album> albums = albumRepository.findAlbumByArtist(artist);
        return albums;
    }

    /**
     *
     * @return
     */
    public List<String> getArtists(){
        List<String> artists = new ArrayList<String>();
        jdbcTemplate.query("SELECT distinct artist FROM ALBUM", new ResultSetExtractor<Object>() {
            @Override
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()){
                    String name = rs.getString("artist");
                    artists.add(name);
                }
                return artists;
            }
        });
        Collections.sort(artists);
        return artists;
    }


    /**
     *
     * @param type
     * @return
     */
    public Map<String,Integer> getTrends(int type){

        Map<String,Integer> stats = new HashMap();

        StringBuilder sql = new StringBuilder("SELECT COUNT(*),");
        sql.append(type == 1 ? " genre":" year");
        sql.append(" from album group by");
        sql.append(type == 1 ? " genre":" year");
        sql.append(" order by 1 desc");

        jdbcTemplate.query(sql.toString(), new ResultSetExtractor<Object>() {
            @Override
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()){
                    Integer occurrence = rs.getInt(1);
                    String key = rs.getString(type == 1 ? "genre":"year");
                    stats.put(key,occurrence);
                }
                return stats;
            }
        });

        Map<String,Integer> sorted = stats.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        return sorted;
    }
}
