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
     * This function takes an album object and creates an entry in the database
     * In this case in H2 Database
     * @param album This is the parameter the function takes {@link Album}
     * @return Optional<Album> object of Album type
     */
    public Optional<Album> createAlbum(Album album){
        Optional<Album> optionalAlbum = Optional.ofNullable(albumRepository.save(album));
        return optionalAlbum;
    }

    /**
     * This function deletes the album from the H2 Database
     * @param id this is a long type Id that uniquely identifies an Album
     * @return Optional<Album> object of Album type
     */
    public Optional<Album> deleteAlbum(long id){
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        if (optionalAlbum.isPresent()){
            albumRepository.delete(optionalAlbum.get());
            return optionalAlbum;
        }else{
            return Optional.empty();
        }

    }


    /**
     * This function updates the album entry in the H2 Database
     * @param id this is a long type Id that uniquely identifies an Album
     * @param album the JSON formated properties to be updated in the album
     * @return Optional<Album> object of Album type
     */
    public Optional<Album> updateAlbum(long id, Album album){
        Optional<Album> albumOptional = albumRepository.findById(id);
        if(!albumOptional.isPresent())
            return Optional.empty();
        album.setId(id);
        albumRepository.save(album);
        return Optional.of(album);
    }


    /**
     * This function fetches the album entry from the H2 Database
     * @param id this is a long type Id that uniquely identifies an Album
     * @return Album object
     */
    public Optional<Album> fethcAlbum(long id){
        Album album = albumRepository.findAlbumById(id);
        if(null!=album){
            return Optional.of(album);
        }else{
            return Optional.empty();
        }
    }

    /**
     * This function fetches the albums entry from the H2 Database for a particular Artist
     * @param artist this is a name of the artist
     * @return List of all the albums the artist has produced
     */
    public List<Album> fetchAlbumByArtist(String artist){
        List<Album> albums = albumRepository.findAlbumByArtist(artist);
        return albums;
    }

    /**
     * This function fetches all the artists entry from the H2 Database
     * @return List of all the artists sorted in alphabetical order
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
     * This function provides some trends on albums from the H2 Database
     * @param type This signifies if the trend shought is by genre or by year for the albums
     * @return Map of Genre as key and Count as value of if type is 1 else Year as key and
     * Count as value if type is otherwise
     */
    public Map<String,Integer> getTrends(int type){

        Map<String,Integer> stats = new HashMap();

        StringBuilder sql = new StringBuilder("SELECT COUNT(*),");
        sql.append(type == 1 ? " genre":" year");
        sql.append(" from album group by");
        sql.append(type == 1 ? " genre":" year");

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
