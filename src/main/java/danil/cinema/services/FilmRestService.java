package danil.cinema.services;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import danil.cinema.dto.FilmRestDTO;
import danil.cinema.dto.FilmSearchDTO;

@Service
public class FilmRestService {

	 private final String API_URL = "https://kinopoiskapiunofficial.tech/api/v2.2/films/";
	    private final String SEARCH_URL = "https://kinopoiskapiunofficial.tech/api/v2.1/films/search-by-keyword";

	    private final String API_KEY = "be927361-5fa5-4e3b-a988-5d2c81ba0619";

	    private final RestTemplate restTemplate = new RestTemplate();

	    public FilmRestDTO searchMovieByTitle(String title) {
	        try {
	            String url = SEARCH_URL + "?keyword=" + title;

	            HttpHeaders headers = new HttpHeaders();
	            headers.set("X-API-KEY", API_KEY);

	            HttpEntity<String> entity = new HttpEntity<>(headers);

	            ResponseEntity<FilmSearchDTO> response = restTemplate.exchange(
	                url, HttpMethod.GET, entity, FilmSearchDTO.class
	            );

	            List<FilmRestDTO> movies = response.getBody().getFilms();
	            if (movies != null && !movies.isEmpty()) {
	                return movies.get(0); 
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	
}
