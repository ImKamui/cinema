package danil.cinema.dto;

import java.util.List;

public class FilmSearchDTO {

	private List<FilmRestDTO> films;

    public List<FilmRestDTO> getFilms() { return films; }
    public void setFilms(List<FilmRestDTO> films) { this.films = films; }
	
}
