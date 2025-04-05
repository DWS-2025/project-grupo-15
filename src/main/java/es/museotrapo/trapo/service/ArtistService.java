package es.museotrapo.trapo.service;

import es.museotrapo.trapo.dto.ArtistMapper;
import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.repository.ArtistRepository;
import es.museotrapo.trapo.dto.ArtistDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;
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


    public Page <ArtistDTO> getArtists(Pageable pageable) {
		Page<Artist> artistPage = artistRepository.findAll(pageable);
        return convertToDTOPage(artistPage);
		//return artistRepository.findAll(pageable).map(this::toDTO);
	}

	public Collection <ArtistDTO> getArtists() {

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
		ArtistDTO artistDTO = toDTO(artist);
		artistRepository.deleteById(id);
		return artistDTO;
	}

	public Page<ArtistDTO> convertToDTOPage(Page<Artist> artistPage) {
        return new PageImpl<>(
            artistPage.getContent().stream().map(mapper::toDTO).collect(Collectors.toList()),
            artistPage.getPageable(),
            artistPage.getTotalElements()
        );
    }

	public long count(){
		return artistRepository.count();
	}

	public Page<ArtistDTO> searchArtists(String name, String nickname, String birthDate, Pageable pageable) {
        Page<Artist> productPage;

		if (name != null && !name.isEmpty() && nickname != null && !nickname.isEmpty() && birthDate != null) {
            productPage = artistRepository.findByNameContainingIgnoreCaseAndNicknameContainingIgnoreCaseAndBirthDateContainingIgnoreCase(name, nickname, birthDate, pageable);
        } else if (name != null && !name.isEmpty() && nickname != null && !nickname.isEmpty()) {
            productPage = artistRepository.findByNameContainingIgnoreCaseAndNicknameContainingIgnoreCase(name, nickname, pageable);
        } else if (name != null && !name.isEmpty() && birthDate != null) {
            productPage = artistRepository.findByNameContainingIgnoreCaseAndBirthDateContainingIgnoreCase(name, birthDate, pageable);
        } else if (nickname != null && !nickname.isEmpty() && birthDate != null) {
            productPage = artistRepository.findByNicknameContainingIgnoreCaseAndBirthDateContainingIgnoreCase(nickname, birthDate, pageable);
        } else if (name != null && !name.isEmpty()) {
            productPage = artistRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (nickname != null && !nickname.isEmpty()) {
            productPage = artistRepository.findByNicknameContainingIgnoreCase(nickname, pageable);
        } else if (birthDate != null) {
            productPage = artistRepository.findByBirthDateContainingIgnoreCase(birthDate, pageable);
        } else {
            productPage = artistRepository.findAll(pageable);
        }

        return convertToDTOPage(productPage);

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