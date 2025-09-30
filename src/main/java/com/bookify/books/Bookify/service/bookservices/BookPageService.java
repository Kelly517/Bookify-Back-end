package com.bookify.books.Bookify.service.bookservices;

import com.bookify.books.Bookify.exceptions.bookexceptions.BookExistsException;
import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import com.bookify.books.Bookify.model.entities.bookentities.BookPageEntity;
import com.bookify.books.Bookify.shared.interfaces.bookinterfaces.BookPageInterface;
import com.bookify.books.Bookify.shared.interfaces.userinterface.ValidatorInterface;
import com.bookify.books.Bookify.model.dto.bookdto.BookPageDTO;
import com.bookify.books.Bookify.repository.bookrepositories.BookPageRepository;
import com.bookify.books.Bookify.repository.bookrepositories.BookRepository;
import com.bookify.books.Bookify.model.mappers.book.BookPageDTOsMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookPageService implements BookPageInterface, ValidatorInterface {

    private final BookPageRepository bookPageRepository;
    private final BookRepository bookRepository;
    private final BookPageDTOsMapper bookPageDTOsMapper;

    private final static Logger LOG = LoggerFactory.getLogger(BookPageService.class.getName());

    @Override
    public BookPageDTO createBookPage(BookPageDTO bookPageDTO, String bookIdentifierCode) {
        Optional<BookEntity> book = bookRepository.findByBookIdentifierCodeAndIsActiveTrue(bookIdentifierCode);

        if (book.isPresent()) {
            BookEntity bookEntity = book.get();
            validators(bookPageDTO, bookEntity);

            bookPageDTO.setPageNumber(countPages(bookPageDTO));
            BookPageEntity newBookPage = bookPageDTOsMapper.convertDtoToEntity(bookPageDTO);
            newBookPage.setBookId(bookEntity);
            bookEntity.getBookPageEntity().add(newBookPage);

            return savePage(bookEntity.getTitle(), newBookPage);
        }

        LOG.error("Selected book doesn't exists {}", book);
        return null;
    }

    private int countPages(BookPageDTO bookPageDTO) {
        int currentPage = bookPageRepository.countPagesByBookId(bookPageDTO.getBookId());
        return currentPage + 1;
    }

    private BookPageDTO savePage(String bookTitle, BookPageEntity bookPageEntity) {
        try {
            exceptionValidators(bookPageEntity);

            String userHome = getUserHome();
            File bookDirectory = createBookDirectory(userHome, bookTitle);
            String filePath = createChapterFile(bookDirectory, bookPageEntity);

            bookPageEntity.setFileRoute(filePath);
            bookPageRepository.save(bookPageEntity);

            LOG.info("Página guardada exitosamente en: {}", filePath);
            return bookPageDTOsMapper.convertEntityToDto(bookPageEntity);

        } catch (IOException ioException) {
            LOG.error("Error al guardar la página: {}", ioException.getMessage());
            throw new BookExistsException("Error al guardar la página");
        } catch (Exception e) {
            LOG.error("Error inesperado: {}", e.getMessage());
            throw new RuntimeException("Error inesperado en savePage", e);
        }
    }

    private String getUserHome() {
        String userHome = System.getProperty("user.home");
        LOG.info("Carpeta de usuario detectada: {}", userHome);
        return userHome;
    }

    private File createBookDirectory(String userHome, String bookTitle) throws IOException {
        LOG.info("Libro procesado: {}", bookTitle);
        File bookDirectory = new File(userHome + "/Libros Bookify/" + bookTitle);

        if (!bookDirectory.exists()) {
            boolean created = bookDirectory.mkdirs();
            if (!created) {
                LOG.error("ERROR: No se pudo crear el directorio: {}", bookDirectory.getAbsolutePath());
                throw new IOException("No se pudo crear el directorio.");
            }
            LOG.info("Directorio del libro creado: {}", bookDirectory.getAbsolutePath());
        }

        return bookDirectory;
    }

    private String createChapterFile(File bookDirectory, BookPageEntity bookPageEntity) throws IOException {
        String sanitizedTitle = bookPageEntity.getPageTitle().replaceAll("[^a-zA-Z0-9_]", "_");
        String filePath = bookDirectory.getAbsolutePath() + "/chapter_" + sanitizedTitle + ".txt";

        LOG.info("Creando archivo en: {}", filePath);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(bookPageEntity.getPageContent());
        }

        return filePath;
    }

    private void exceptionValidators(BookPageEntity bookPageEntity) {
        if (bookPageEntity.getPageTitle() == null || bookPageEntity.getPageTitle().trim().isEmpty()) {
            LOG.error("ERROR: El título de la página es nulo o vacío.");
            throw new IllegalArgumentException("El título de la página no puede estar vacío.");
        }

        if (bookPageEntity.getBookId() == null) {
            LOG.error("ERROR: El libro asociado no tiene un título.");
            throw new IllegalArgumentException("El libro asociado no puede ser nulo.");
        }
    }

    @Override
    @Transactional
    public Set<BookPageEntity> getAllPages(Long bookId) {
        List<BookEntity> findAllChapters = bookRepository
                .findAllByBookIdAndIsActiveTrue(bookId);

        Set<BookPageEntity> allPages = findAllChapters
                .stream()
                .filter(Objects::nonNull)
                .map(BookEntity::getBookPageEntity)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        if (allPages.isEmpty()) {
            throw new BookExistsException("Error: The book doesn't have pages");
        }

        return allPages;
    }

    @Override
    @Transactional
    public BookPageDTO getIndividualPage(Long bookPageId, int pageNumber, Long bookId) {
        BookPageEntity bookPage = bookPageRepository
                .findById(bookPageId)
                .orElseThrow(() ->
                        new RuntimeException("La página no existe"));

        boolean belongsToBook = bookPage.getBookId().getBookId().equals(bookId);
        boolean matchesPageNumber = bookPage.getPageNumber() == pageNumber;

        if (belongsToBook && matchesPageNumber) {
            LOG.info("Página validada correctamente: bookId={}, pageId={}, pageNumber={}", bookId, bookPageId, pageNumber);
            return bookPageDTOsMapper.convertEntityToDto(bookPage);
        }
        throw new BookExistsException("Error: Book page not found");
    }

    @Override
    @Transactional
    public String extractContentPage(Long bookPageId) {
        BookPageEntity page = bookPageRepository
                .findById(bookPageId)
                .orElseThrow(() ->
                        new BookExistsException("Error with the book"));

        try {
            Path path = Paths.get(page.getFileRoute());
            return Files.readString(path);
        } catch (IOException ioException) {
            throw new RuntimeException("Error reading file");
        }
    }

    @Override
    public BookPageDTO updateBookPage(Long bookPageId, BookPageDTO bookPageDTO) throws IOException {
        BookPageEntity bookPage = bookPageRepository.findById(bookPageId)
                .orElseThrow(() -> new RuntimeException("Page doesn't exists"));

        bookPage.setPageTitle(bookPageDTO.getPageTitle());
        bookPage.setPageContent(bookPageDTO.getPageContent());

        String userHome = getUserHome();
        File bookDirectory = createBookDirectory(userHome, bookPageDTO.getPageTitle());
        String filePath = createChapterFile(bookDirectory, bookPage);

        bookPage.setFileRoute(filePath);

        bookPageRepository.save(bookPage);

        LOG.info("Book updated successfully: {}", bookPageDTO.getPageContent());
        return bookPageDTOsMapper.convertEntityToDto(bookPage);
    }

    @Override
    public String deleteBookPage(BookPageDTO bookPageDTO) {
        return "";
    }
}
