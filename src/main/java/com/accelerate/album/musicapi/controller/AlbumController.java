package com.accelerate.album.musicapi.controller;

import com.accelerate.album.musicapi.model.Album;
import com.accelerate.album.musicapi.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;


/**
 * @author Rashmi
 */

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    AlbumService albumService;

    /**
     * This is the HTTP route to create an {@link Album} in the database
     * @param album This is an album object of type {@link Album}
     * @return ResponseEntity<Object> Returns the ResponseEntity
     */
    @RequestMapping(value = "/create" , method = RequestMethod.POST)
    public ResponseEntity<Object> addAlbum(@RequestBody Album album){
        Optional<Album> optionalAlbum = albumService.createAlbum(album);
        if(optionalAlbum.isPresent()){
            return new ResponseEntity((Album)optionalAlbum.get(),HttpStatus.OK);
        }else{
            return new ResponseEntity("Failure",HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This the HTTP route to delete an {@link Album} in the database
     * @param id This is the identifying parameter for the Album to be deleted
     * @return ResponseEntity<Object> Returns the ResponseEntity:
     *         Success for success
     *         Failure for Failure
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteAlbum(@PathVariable long id){
            Optional<Album> optionalAlbum = albumService.deleteAlbum(id);
            if(optionalAlbum.isPresent()){
                return new ResponseEntity("Success",HttpStatus.OK);
            }else{
                return new ResponseEntity("Failure",HttpStatus.BAD_REQUEST);
            }
    }

    /**
     * This the HTTP route to update an {@link Album} in the database
     * @param id This is the identifying parameter for the Album to be deleted
     * @param album These are album properties as JSON object
     * @return ResponseEntity<Object> Returns the ResponseEntity
     */
    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    public ResponseEntity<Object> updateAlbum(@PathVariable long id , @RequestBody Album album){

        Optional<Object> optionalAlbum = albumService.updateAlbum(id,album);
        if (optionalAlbum.isPresent()){
            return new ResponseEntity((Album)optionalAlbum.get(),HttpStatus.OK);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * This Route provides the album as a JSON object
     * @param id This is the identifying parameter for the Album
     * @return ResponseEntity<Object> Returns the ResponseEntity
     */
    @RequestMapping(value = "/find/{id}" , method = RequestMethod.GET)
    public ResponseEntity<Object> fetchAlbum(@PathVariable long id){
        Album album = albumService.fethcAlbum(id);
        if (null != album){
            return new ResponseEntity(album,HttpStatus.OK);
        }else {
            return new ResponseEntity("Failure", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This Route provides the all the Albums produced by {@param artist}
     * @param artist This is the name of the Artist
     * @return List of {@link Album} objects as a ResponseEntity
     */
    @RequestMapping(value="/findbyartist/{artist}",method = RequestMethod.GET)
    public ResponseEntity<Object> fetchAlbumByArtist(@PathVariable String artist){
        List<Album> albums = albumService.fetchAlbumByArtist(artist);
        return new ResponseEntity<Object>(albums,HttpStatus.OK);
    }

    /**
     * This Route provides the list of all the artists in the system
     * @return List of all the artists in the system
     */
    @RequestMapping(value="/allartist",method = RequestMethod.GET)
    public ResponseEntity<Object> getAllArtist(){
        List<String> artists = albumService.getArtists();
        return new ResponseEntity<Object>(artists,HttpStatus.OK);
    }

    /**
     * This Route provides the Statistics about the albums
     * @param type This is the parameter that decides on the statistics
     *             1 provides the ranking for Number of Albums produced by artist
     *             0 provides the ranking for Albums produced by the years
     * @return ResponseEntity
     */
    @RequestMapping(value="/stats/{type}",method = RequestMethod.GET)
    public ResponseEntity<Object> getTrends(@PathVariable int type){
        Map<String, Integer> trends = albumService.getTrends(type);
        return new ResponseEntity<Object>(trends,HttpStatus.OK);
    }
}
