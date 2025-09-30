package com.bookify.books.Bookify.repository.bookrepositories;

import com.bookify.books.Bookify.model.entities.bookentities.BookRating;
import com.bookify.books.Bookify.shared.interfaces.scoreinterface.TopRatedBookProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<BookRating, Long> {

    @Query("""
                SELECT br.book.id AS bookId, AVG(br.score) AS averageScore
                FROM BookRating br
                WHERE br.createdAt >= :sevenDaysAgo
                GROUP BY br.book.id
                ORDER BY AVG(br.score) DESC
                LIMIT 5
            """)
    List<TopRatedBookProjection> findTopFiveBooksRatedInLastSevenDays(
            @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo
    );
}
