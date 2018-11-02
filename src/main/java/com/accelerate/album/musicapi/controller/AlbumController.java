package com.accelerate.album.musicapi.controller;

import com.accelerate.album.musicapi.model.Album;
import com.accelerate.album.musicapi.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/create" , method = RequestMethod.POST)
    public ResponseEntity<Object> addAlbum(@RequestBody Album album){
        if (null!=album) {
            album = albumRepository.save(album);

            return new ResponseEntity(album,HttpStatus.OK);
        }
        else {
            return ResponseEntity.badRequest().build();
        }

    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public String deleteAlbum(@PathVariable long id){
        Album album = albumRepository.findAlbumById(id);
        if (null != album){
            albumRepository.delete(album);
        }
        else{
            return "Failure";
        }
        return "Success";
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    public ResponseEntity<Object> updateAlbum(@PathVariable long id , @RequestBody Album album){

        Optional<Album> albumOptional = albumRepository.findById(id);
        if(!albumOptional.isPresent())
            return ResponseEntity.notFound().build();
        album.setId(id);
        albumRepository.save(album);
        return new ResponseEntity(album,HttpStatus.OK);
    }

    @RequestMapping(value = "/find/{id}" , method = RequestMethod.GET)
    public ResponseEntity<Object> fetchAlbum(@PathVariable long id){
        Album album = albumRepository.findAlbumById(id);
        if (null != album)
            return new ResponseEntity(album,HttpStatus.OK);
        else
            return new ResponseEntity("Failure: Unable to locate the album",HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value="/findbyartist/{artist}",method = RequestMethod.GET)
    public ResponseEntity<Object> fetchAlbumByArtist(@PathVariable String artist){
        List<Album> albums = albumRepository.findAlbumByArtist(artist);
        return new ResponseEntity<Object>(albums,HttpStatus.OK);
    }


    @RequestMapping(value="/allartist",method = RequestMethod.GET)
    public ResponseEntity<Object> getAllArtist(){
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
        return new ResponseEntity<Object>(artists,HttpStatus.OK);
    }

    @RequestMapping(value="/stats/{type}",method = RequestMethod.GET)
    public ResponseEntity<Object> getTrends(@PathVariable int type){
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
        return new ResponseEntity<Object>(sorted,HttpStatus.OK);
    }
}
