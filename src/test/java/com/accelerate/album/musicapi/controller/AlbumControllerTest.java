package com.accelerate.album.musicapi.controller;

import com.accelerate.album.musicapi.model.Album;
import com.accelerate.album.musicapi.service.AlbumService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@WebMvcTest(value = AlbumController.class)
public class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumService albumService;

    @Test
    public void addAlbum() throws Exception {

        Album mockAlbum = new Album( "Slippery When Wet","Bon Jovi","Rock",1986);

        String mockAlbumJson = "{\"name\":\"Slippery When Wet\",\"artist\":\"Bon Jovi\",\"genre\":\"Rock\",\"year\":1986}";

        Mockito.when(
                albumService.createAlbum(
                        Mockito.any(Album.class))).thenReturn(Optional.of(mockAlbum));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/albums/create")
                .accept(MediaType.APPLICATION_JSON).content(mockAlbumJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @Test
    public void deleteAlbum() throws Exception {

        Album mockAlbum = new Album( "Slippery When Wet","Bon Jovi","Rock",1986);


        Mockito.when(
                albumService.deleteAlbum(
                        Mockito.anyLong())).thenReturn(Optional.of(mockAlbum));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/albums/delete/103")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());


    }

    @Test
    public void updateAlbum() throws Exception {
        Album mockAlbum = new Album( "Slippery When Wet","Bon Jovi","Rock",1986);

        String mockAlbumJson = "{\"name\":\"Slippery When Wet\",\"artist\":\"Bon Jovi\",\"genre\":\"Rock/Pop\",\"year\":1986}";

        Mockito.when(
                albumService.updateAlbum(
                        Mockito.anyLong(),Mockito.any(Album.class))).thenReturn(Optional.of(mockAlbum));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/albums/update/103")
                .accept(MediaType.APPLICATION_JSON).content(mockAlbumJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
    }

    @Test
    public void fetchAlbum() throws Exception {
        Album mockAlbum = new Album( "Slippery When Wet","Bon Jovi","Rock",1986);
        mockAlbum.setId(103);

        Mockito.when(albumService.fethcAlbum(Mockito.anyLong())).thenReturn(mockAlbum);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/albums/find/103").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"id\":103,\"name\":\"Slippery When Wet\",\"artist\":\"Bon Jovi\",\"genre\":\"Rock\",\"year\":1986}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }

    @Test
    public void fetchAlbumByArtist() throws Exception{

        Album album1 = new Album( "Any Man Of Mine","Shania Twain","Country",1995);
        Album album2 = new Album("Come on Over","Shania Twain","Country",1997);
        album1.setId(100);
        album2.setId(101);
        List<Album> albums = new ArrayList<>();
        albums.add(album1);
        albums.add(album2);
        Mockito.when(albumService.fetchAlbumByArtist(Mockito.anyString())).thenReturn(albums);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/albums/findbyartist/Shania Twain").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
    }

    @Test
    public void getAllArtist() throws Exception{
        List<String> artists = new ArrayList<>();
        artists.add("Shania Twain");
        artists.add("Bon Jovi");
        artists.add("Led Zeppelin");
        artists.add("Cher");
        artists.add("Bruno Mars");

        Mockito.when(albumService.getArtists()).thenReturn(artists);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/albums/allartist").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());

    }

    @Test
    public void getYearlyMostAlbumRankings() throws Exception {
        Map<String,Integer> albumcountbyyear = new HashMap<>();
        albumcountbyyear.put("1995",5);
        albumcountbyyear.put("1996",4);
        albumcountbyyear.put("1997",3);
        albumcountbyyear.put("1998",2);
        albumcountbyyear.put("1999",1);

        Mockito.when(albumService.getTrends(Mockito.anyInt())).thenReturn(albumcountbyyear);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/albums/stats/1"
        ).accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
    }

    @Test
    public void getArtistMostAlbumRankings() throws Exception{
        Map<String,Integer> albumCountByArtist = new HashMap<>();
        albumCountByArtist.put("John Mayer",5);
        albumCountByArtist.put("Cher",4);
        albumCountByArtist.put("Bon Jovi",3);
        albumCountByArtist.put("Pink Floyd",2);
        albumCountByArtist.put("Bruno Mars",1);

        Mockito.when(albumService.getTrends(Mockito.anyInt())).thenReturn(albumCountByArtist);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/albums/stats/0"
        ).accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
    }
}