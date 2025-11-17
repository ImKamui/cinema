package danil.cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FilmRestDTO {

	private int kinopoiskId;
	@JsonProperty("posterUrlPreview")
    private String posterUrl;
    private String nameRu;
    private String nameOriginal;
    private String year;
    private String description;
    @JsonProperty("rating")
    private String ratingKinopoisk;
    @JsonProperty("ratingImdb")
    private String ratingImdb;
	
}
