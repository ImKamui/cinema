package danil.cinema.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import danil.cinema.models.Film;
import danil.cinema.repositories.FilmRepository;

@Service
@Transactional(readOnly = true)
public class FilmService {

	private final FilmRepository filmRepository;
	
	@Autowired
	public FilmService(FilmRepository filmRepository) {
		this.filmRepository = filmRepository;
	}
	
	public List<Film> findAll()
	{
		return filmRepository.findAll();
	}
	
	public Film findOne(int id)
	{
		Optional<Film> foundFilm = filmRepository.findById(id);
		return foundFilm.orElse(null);
	}
	
	public Film findOneByFilmName(String name)
	{
		Optional<Film> foundFilm = filmRepository.findByFilmName(name);
		return foundFilm.orElse(null);
	}
	
	@Transactional
	public void save(Film film)
	{
		filmRepository.save(film);
	}
	
	@Transactional
	public void update(int id, Film updatedFilm)
	{
		updatedFilm.setId(id);
		filmRepository.save(updatedFilm);
	}
	
	@Transactional
	public void delete(int id)
	{
		filmRepository.deleteById(id);
	}
	
}
