package com.accelerate.album.musicapi.repository;

import com.accelerate.album.musicapi.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


import java.util.List;

/**
 * @author Rashmi
 */

@RepositoryRestResource(collectionResourceRel = "album", path = "album")
public interface AlbumRepository extends PagingAndSortingRepository<Album,Long> {

    Page<Album> findAll(Pageable pageable);
    List<Album> findAlbumByArtist(String artist);
    Album findAlbumById(long id);
}
