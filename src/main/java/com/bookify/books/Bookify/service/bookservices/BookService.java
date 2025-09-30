package com.bookify.books.Bookify.service.bookservices;

import com.bookify.books.Bookify.exceptions.bookexceptions.BookAlreadyexistsException;
import com.bookify.books.Bookify.exceptions.bookexceptions.BookExistsException;
import com.bookify.books.Bookify.exceptions.userexceptions.UserExistsException;
import com.bookify.books.Bookify.model.dto.bookdto.BookCardDto;
import com.bookify.books.Bookify.model.dto.bookdto.DtoCrearLibro;
import com.bookify.books.Bookify.model.dto.bookdto.DtoLibro;
import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import com.bookify.books.Bookify.model.mappers.book.BookCardDTOMapper;
import com.bookify.books.Bookify.repository.bookrepositories.ScoreRepository;
import com.bookify.books.Bookify.shared.interfaces.bookinterfaces.BookInterface;
import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.repository.bookrepositories.BookRepository;
import com.bookify.books.Bookify.model.mappers.book.BookDTOsMapper;
import com.bookify.books.Bookify.repository.userrepositories.UserRepository;
import com.bookify.books.Bookify.security.JwtUtil;
import com.bookify.books.Bookify.shared.interfaces.scoreinterface.TopRatedBookProjection;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bookify.books.Bookify.model.mappers.book.BookDTOsMapper.convertBookDTOToEntity;
import static com.bookify.books.Bookify.model.mappers.book.BookDTOsMapper.convertBookEntityToDTO;

@Service
@Builder
@RequiredArgsConstructor
public class BookService implements BookInterface{

    private final BookRepository bookRepository;
    private final BookDTOsMapper bookDTOsMapper;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;
    private final UserRepository userRepository;
    private final ScoreRepository scoreRepository;
    private final static Logger LOG = LoggerFactory.getLogger(BookService.class.getName());
    /**
     * Crea un libro a partir de DtoCrearLibro.
     * 1) Verifica que no exista otro activo con el mismo título.
     * 2) Asigna al usuario autenticado como autor (usando el JWT del header).
     * 3) Construye DtoLibro desde DtoCrearLibro con el mapper.
     * 4) Convierte DTO -> Entidad y persiste.
     * 5) Retorna el libro guardado como DtoLibro.
     */

    @Override
    public DtoLibro crearLibro(DtoCrearLibro dtoCrearLibro) {
        Optional<BookEntity> libroExiste = bookRepository.findByTitleAndIsActiveTrue(dtoCrearLibro.getTitle());

        if (libroExiste.isPresent()) {
            throw new BookAlreadyexistsException("Book already exists: " + dtoCrearLibro.getTitle());
        }

        assignUserToBook(dtoCrearLibro);// Asigna el autor desde el token
        DtoLibro dtoLibro = bookDTOsMapper.createBookDto(dtoCrearLibro);

        BookEntity bookEntity = convertBookDTOToEntity(dtoLibro);
        BookEntity guardar = bookRepository.save(bookEntity);

        LOG.info("The book has ben saved: {}", guardar);
        return convertBookEntityToDTO(guardar);
    }

    /**
     * Obtiene el usuario del JWT del header "Authorization" y lo setea en el DtoCrearLibro.
     * - Extrae token "Bearer ..."
     * - Obtiene email con JwtUtil
     * - Busca el User y lo asigna como autor en el DTO
     */
    private void assignUserToBook(DtoCrearLibro dtoCrearLibro) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserExistsException("User not found"));
        dtoCrearLibro.setUser(user);
    }
    /**
     * Lee un libro activo por su bookIdentifierCode.
     * - Busca en repositorio, si no existe lanza excepción.
     * - Devuelve el DTO del libro.
     */

    @Override
    public DtoLibro readBook(String bookIdentifierCode) {
        BookEntity bookSearch = bookRepository
                .findByBookIdentifierCodeAndIsActiveTrue(bookIdentifierCode)
                .orElseThrow(() ->
                        new BookExistsException("Book not found " + bookIdentifierCode));
        return convertBookEntityToDTO(bookSearch);
    }
    /**
     * Libros comprados por un usuario (paginado).
     * - Consulta a través de UserRepository.
     * - Mapea entidades a DtoLibro, luego a BookCardDto (tarjetas ligeras).
     */
    @Override
    public Page<BookCardDto> getPurchasedBooks(Long userId, Pageable pageable) {
        Page<BookEntity> purchasedBooksByUserId = userRepository
                .findPurchasedBooksByUserId(userId, pageable);
        Page<DtoLibro> dtoLibros = purchasedBooksByUserId.map(BookDTOsMapper::convertBookEntityToDTO);

        return dtoLibros.map(BookCardDTOMapper::BookEntityDtoToCard);
    }
    /**
     * Lista todos los libros activos (paginado) como tarjetas.
     * - Convierte cada BookEntity a BookCardDto
     * - Adjunta promedio de score usando el mapa bookId -> averageScore.
     */
    @Override
    public Page<BookCardDto> getAllBooks(Pageable pageable) {
        return bookRepository
                .findAllByIsActiveTrue(pageable)
                .map(book ->
                        BookCardDTOMapper.convertBookToDTO
                                (book, bookIdToScore().get(book.getBookId())));
    }
    /**
     * Lista libros por categoría (like/ignore case) en estado activo (paginado).
     * - Igual que getAllBooks, pero filtrando por categoría.
     */
    @Override
    public Page<BookCardDto> getBooksByCategory(String category, Pageable pageable) {
        return bookRepository.findByCategoryContainingIgnoreCaseAndIsActiveTrue(category, pageable)
                .map(book ->
                        BookCardDTOMapper.convertBookToDTO
                                (book, bookIdToScore().get(book.getBookId())));
    }
    /**
     * Actualiza campos básicos de un libro activo encontrado por su bookIdentifierCode.
     * - Si existe, setea título, descripción y precio; persiste y devuelve DTO.
     * - Si no existe, loggea y retorna DTO vacío.
     */
    @Override
    public DtoLibro updateBook(DtoLibro dtoLibro) {
        Optional<BookEntity> existingBook = bookRepository.findByBookIdentifierCodeAndIsActiveTrue(dtoLibro.getBookIdentifierCode());

        if (existingBook.isPresent()) {
            BookEntity updatedBook = existingBook.get();
            updatedBook.setTitle(dtoLibro.getTitle());
            updatedBook.setDescription(dtoLibro.getDescription());
            updatedBook.setPrice(dtoLibro.getPrice());

            BookEntity bookSaved = bookRepository.save(updatedBook);

            LOG.info("Book updated: {}",bookSaved);
            return convertBookEntityToDTO(bookSaved);
        }

        LOG.error("The book doesn't exists: {}", existingBook);
        return new DtoLibro();
    }
    /**
     * "Elimina" un libro activo por código: hace soft-delete (isActive=false).
     * - Si lo encuentra, marca inactivo y guarda.
     * - Si no, loggea y retorna null.
     */
    @Override
    public String deleteBook(String bookIdentifierCode) {
        Optional<BookEntity> deleteBook = bookRepository.findByBookIdentifierCodeAndIsActiveTrue(bookIdentifierCode);

        if (deleteBook.isPresent()) {
            deleteBook.get().setIsActive(false);
            bookRepository.save(deleteBook.get());
            return "Deleted book";
        } else {
            LOG.error("Book not found {}", deleteBook);
            return null;
        }
    }
    /**
     * Top 5 libros mejor calificados de la semana (tarjetas).
     * - Calcula promedios recientes (últimos 7 días) vía ScoreRepository.
     * - Obtiene BookEntity por ids resultantes y mapea a BookCardDto con score.
     */
    @Override
    public List<BookCardDto> getTopRatedBooksOfWeek() {
        return books().stream()
                .map(book ->
                        BookCardDTOMapper.convertBookToDTO
                                (book, bookIdToScore().get(book.getBookId())))
                .toList();
    }
    // ----------- Helpers internos para Top Rated -----------

    /**
     * Obtiene las entidades de libros para los IDs devueltos por topRatedBooks(),
     * filtrando además por activos.
     */
    private List<BookEntity> books() {
        return bookRepository
                .findAllByBookIdInAndIsActiveTrue(
                        bookIdToScore()
                                .keySet()
                                .stream()
                                .toList());
    }
    /**
     * Construye un mapa {bookId -> averageScore} con el top de la semana.
     */
    private Map<Long, Double> bookIdToScore() {
        return topRatedBooks().stream()
                .collect(Collectors.toMap(
                        TopRatedBookProjection::getBookId,
                        TopRatedBookProjection::getAverageScore
                ));
    }
    /**
     * Consulta al repositorio de puntuaciones el top 5 de los últimos 7 días.
     */
    private List<TopRatedBookProjection> topRatedBooks() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return scoreRepository.findTopFiveBooksRatedInLastSevenDays(sevenDaysAgo);
    }
}
