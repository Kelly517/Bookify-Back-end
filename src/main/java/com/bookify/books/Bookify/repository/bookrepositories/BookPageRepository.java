package com.bookify.books.Bookify.repository.bookrepositories;

import com.bookify.books.Bookify.model.entities.bookentities.BookPageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookPageRepository extends JpaRepository<BookPageEntity, Long> {

    /**
     * (page) es el alias que se asigna para hacer la consulta.
     * page.bookId.bookId: el alias llama al atributo de la entidad page llamado bookId,
     * este id trae la información de la entidad de libro que también tiene el id llamado bookId
     * por eso bookId llama directamente a bookId
     * @param bookId es el id de la page
     * @return retorna un int con la cantidad de páginas actuales
     */
    @Query("SELECT COUNT(page) FROM BookPageEntity page WHERE page.bookId.bookId = :bookId")
    int countPagesByBookId(@Param("bookId") Long bookId);
}
