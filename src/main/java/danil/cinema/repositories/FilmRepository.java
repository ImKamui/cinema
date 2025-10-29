package danil.cinema.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import danil.cinema.models.Film;
@Repository
public interface FilmRepository extends JpaRepository<Film, Integer>{
	Optional<Film> findByFilmName(String filmName);

}
