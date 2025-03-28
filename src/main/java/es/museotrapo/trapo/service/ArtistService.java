package es.museotrapo.trapo.service;

import es.museotrapo.trapo.dto.ArtistMapper;
import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.repository.ArtistRepository;
import es.museotrapo.trapo.dto.ArtistDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Collection;

/**
 * Service class for managing Artist entities.
 * Provides methods to interact with the ArtistRepository and PictureRepository.
 */
@Service
public class ArtistService {

    // Injecting ArtistRepository to manage artist data.
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistMapper mapper;


    public Collection<ArtistDTO> getArtists() {

		return toDTOs(artistRepository.findAll());
	}

    public ArtistDTO getArtist(long id) {

		return toDTO(artistRepository.findById(id).orElseThrow());
	}

    public ArtistDTO createArtist(ArtistDTO artistDTO) {

		Artist artist = toDomain(artistDTO);
		artistRepository.save(artist);
		return toDTO(artist);
	}

    public ArtistDTO replaceArtist(long id, ArtistDTO updatedArtistDTO) {

		if (artistRepository.existsById(id)) {

			Artist updatedArtist = toDomain(updatedArtistDTO);
			updatedArtist.setId(id);

			artistRepository.save(updatedArtist);
			return toDTO(updatedArtist);

		} else {
			throw new NoSuchElementException();
		}
	}

    public ArtistDTO deleteArtist(long id) {

		Artist artist = artistRepository.findById(id).orElseThrow();
		artistRepository.deleteById(id);
		return toDTO(artist);
	}


    public boolean existsById(long id){
        return artistRepository.existsById(id);
    }

    private ArtistDTO toDTO(Artist artist){
		return mapper.toDTO(artist);
	}

	protected Artist toDomain(ArtistDTO artistDTO){
		return mapper.toDomain(artistDTO);
	}

	private Collection<ArtistDTO> toDTOs(Collection<Artist> artists){
		return mapper.toDTOs(artists);
	}
}