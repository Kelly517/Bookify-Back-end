package com.bookify.books.Bookify.service.searchbarservice;

import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import com.bookify.books.Bookify.shared.interfaces.bookinterfaces.SearchBar;
import com.bookify.books.Bookify.model.dto.bookdto.BookCardDto;
import com.bookify.books.Bookify.model.dto.bookdto.DtoLibro;
import com.bookify.books.Bookify.model.dto.userdto.UserDTO;
import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.model.mappers.user.UserDTOsMapper;
import com.bookify.books.Bookify.model.mappers.book.BookCardDTOMapper;
import com.bookify.books.Bookify.model.mappers.book.BookDTOsMapper;
import com.bookify.books.Bookify.repository.searchbarrepositories.SearchBarRepository;
import com.bookify.books.Bookify.repository.searchbarrepositories.SearchBarUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchBarImpl implements SearchBar {

    private final SearchBarUserRepository searchBarUserRepository;
    private final SearchBarRepository searchBarRepository;
    private final BookCardDTOMapper bookCardDTOMapper;
    private final BookDTOsMapper bookDTOsMapper;
    private final UserDTOsMapper userDTOsMapper;
    private static final Logger LOG = LoggerFactory.getLogger(SearchBarImpl.class);

    @Override
    public Page<BookCardDto> searchBook(String title, Pageable pageable) {
        Page<BookEntity> bookEntities = searchBarRepository.findByTitleContainingIgnoreCaseAndIsActiveTrue(title, pageable);

        return bookEntities.map(bookEntity -> {

            DtoLibro dtoLibro = convertBookEntityToDto(bookEntity);
            BookCardDto bookCardDto = convertBookEntityToCard(dtoLibro);

            LOG.info("Book found: {}", bookCardDto);
            return convertBookEntityToCard(dtoLibro);
        });
    }

    private DtoLibro convertBookEntityToDto(BookEntity bookEntity) {
        return BookDTOsMapper.convertBookEntityToDTO(bookEntity);
    }

    private BookCardDto convertBookEntityToCard(DtoLibro dtoLibro) {
        return BookCardDTOMapper.BookEntityDtoToCard(dtoLibro);
    }

    @Override
    public Page<UserDTO> searchBookByAuthor(String username, Pageable pageable) {
        Page<User> userEntityPage = searchBarUserRepository.findByUserNameContainingIgnoreCase(username, pageable);

        return userEntityPage.map(userEntity -> {
            UserDTO userDTO = convertUserEntityToDto( userEntity);
            LOG.info("User found: {}", userDTO);
            return userDTO;
        });
    }

    private UserDTO convertUserEntityToDto(User user) {
        return userDTOsMapper.convertEntityToDTO(user);
    }
}