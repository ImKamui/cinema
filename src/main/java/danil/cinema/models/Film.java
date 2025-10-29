package danil.cinema.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "Films")
@Data
public class Film {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "film_name")
	private String filmName;
	
	@Column(name = "author")
	private String author;
	
	@Column(name = "year_of_publication")
	private String yearOfPublication;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "file_path")
	private String filePath;
}
