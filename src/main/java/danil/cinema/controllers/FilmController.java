package danil.cinema.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import danil.cinema.CinemaApplication;
import danil.cinema.dto.FilmDTO;
import danil.cinema.models.Film;
import danil.cinema.repositories.FilmRepository;
import danil.cinema.services.FilmService;

@Controller
@RequestMapping("/films")
public class FilmController {

    private final CinemaApplication cinemaApplication;

	private final FilmRepository filmRepository;
	private final FilmService filmService;
	
	@Autowired
	public FilmController(FilmRepository filmRepository, FilmService filmService, CinemaApplication cinemaApplication) {
		this.filmRepository = filmRepository;
		this.filmService = filmService;
		this.cinemaApplication = cinemaApplication;
	}
	
	@GetMapping()
	public String index(Model model)
	{
		model.addAttribute("films", filmService.findAll());
		return "cinema/index";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable("id")int id, Model model)
	{
		model.addAttribute("film", filmService.findOne(id));
		return "cinema/show";
	}
	
	@GetMapping("/new")
	public String newFilm(@ModelAttribute("film") FilmDTO film)
	{
		return "cinema/new";
	}
	
	@PostMapping
	public String create(@ModelAttribute("film") FilmDTO film, BindingResult bindingResult, @RequestParam("file") MultipartFile file)
	{
		if (bindingResult.hasErrors())
		{
			return "cinem/new";
		}
		Film filmModel = new Film();
		filmModel.setFilmName(film.getFilmName());
		filmModel.setAuthor(film.getAuthor());
		filmModel.setDescription(film.getDescription());
		filmModel.setYearOfPublication(film.getYearOfPublication());
		
		if (!file.isEmpty())
		{
			try
			{
				String fileName = file.getOriginalFilename();
				Path uploadPath = Paths.get("uploads");
				
				if (!Files.exists(uploadPath))
				{
					Files.createDirectories(uploadPath);
				}
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				
				filmModel.setFilePath(filePath.toString());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		filmService.save(filmModel);
		return "redirect:/films";
	}
	
	@GetMapping("/{id}/play")
	public ResponseEntity<Resource> playFilm(@PathVariable("id") int id) throws MalformedURLException
	{
		Film film = filmService.findOne(id);
		if (film == null || film.getFilePath() == null)
		{
			return ResponseEntity.notFound().build();
		}
		
		Path path = Paths.get(film.getFilePath());
		Resource resource = new UrlResource(path.toUri());
		
		if (resource.exists() && resource.isReadable())
		{
			String contentType = "application/octet-stream";
			
			try
			{
				contentType = Files.probeContentType(path);
				if (contentType == null)
				{
					String extension = getFileExtension(path.toString());
					contentType = getMimeTypeByExtension(extension);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName() + "\"")
					.body(resource);
		}
		else
		{
			return ResponseEntity.notFound().build();
		}
	}
	private String getMimeTypeByExtension(String extension) {
	    switch (extension) {
	        case "mp4": return "video/mp4";
	        case "avi": return "video/x-msvideo";
	        case "mov": return "video/quicktime";
	        case "wmv": return "video/x-ms-wmv";
	        case "flv": return "video/x-flv";
	        case "webm": return "video/webm";
	        case "mkv": return "video/x-matroska";
	        case "m4v": return "video/x-m4v";
	        default: return "application/octet-stream";
	    }
	}
	private String getFileExtension(String fileName) {
	    int lastDotIndex = fileName.lastIndexOf('.');
	    return (lastDotIndex > 0) ? fileName.substring(lastDotIndex + 1).toLowerCase() : "";
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id)
	{
		Film film = filmService.findOne(id);
		String filePath = film.getFilePath();
		File file = new File(filePath);
		file.delete();
		filmService.delete(id);
		return "redirect:/films";
	}
	
}
