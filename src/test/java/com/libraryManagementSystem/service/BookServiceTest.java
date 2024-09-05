package com.libraryManagementSystem.service;

import com.libraryManagementSystem.dto.BookDto;
import com.libraryManagementSystem.dto.BookRequest;
import com.libraryManagementSystem.entity.Book;
import com.libraryManagementSystem.entity.Review;
import com.libraryManagementSystem.mapper.BookMapper;
import com.libraryManagementSystem.repository.BookRepository;
import com.libraryManagementSystem.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookMapper bookMapper;

    private final Long id = 1L;
    private final int pageSize = 10;
    private final int pageNumber = 0;
    private final String[] pageSort = {"id", "asc"};
    private BookRequest bookRequest;
    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void init(){
        bookRequest = new BookRequest();
        bookRequest.setTitle("Book Title");

        book = new Book();
        book.setId(id);
        book.setTitle(bookRequest.getTitle());

        bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setTitle(bookRequest.getTitle());
    }


    @Test
    public void saveBook() {
        when(bookMapper.requestToEntity(bookRequest)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.entityToDto(book)).thenReturn(bookDto);

        BookDto result = bookService.saveBook(bookRequest);

        assertNotNull(result);
        assertEquals(bookDto.getTitle(), result.getTitle());
        assertEquals(bookDto.getPrice(), result.getPrice());
        assertEquals(bookDto.getId(), result.getId());
    }

    @Test
    public void getAllBooks() {

        Book book2 = new Book();
        book2.setId(id + 1);
        book2.setTitle(bookRequest.getTitle() + "2");

        List<Book> books = new ArrayList<>();
        books.add(book);
        books.add(book2);

        BookDto bookDto2 = new BookDto();
        bookDto2.setId(id + 1);
        bookDto2.setTitle(bookRequest.getTitle() + "2");

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(pageSort));
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAllByIsEnabledTrue(pageable)).thenReturn(bookPage);
        when(bookMapper.entityToDto(book)).thenReturn(bookDto);
        when(bookMapper.entityToDto(book2)).thenReturn(bookDto2);

        Page<BookDto> result = bookService.getAllBooks(pageSize, pageNumber, pageSort);

        assertNotNull(result);
        assertEquals(bookDto, result.getContent().getFirst());
        assertEquals(bookDto2, result.getContent().getLast());
    }

    @Test
    public void getBookById() {
        when(bookRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(book));
        when(bookMapper.entityToDto(book)).thenReturn(bookDto);

        BookDto result = bookService.getBookById(id);

        assertNotNull(result);
        assertEquals(bookDto, result);
    }

    @Test
    public void bookFilter() {
        Set<Long> authorIds = Set.of(id, (id + 1));
        Set<Long> genreIds = Set.of(id);
        String title = "Test Book";
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Book book2 = new Book();
        book2.setId(id + 1);
        book2.setTitle(bookRequest.getTitle() + "2");

        List<Book> books = new ArrayList<>();
        books.add(book);
        books.add(book2);

        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(bookPage);
        when(bookMapper.entityToDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.bookFilter(id, authorIds, genreIds, title, pageSize, pageNumber, pageSort);

        assertEquals(bookDto, result.getContent().getFirst());
    }

    @Test
    public void updateBook() {
        Book updatedBook = new Book();
        updatedBook.setId(id);
        updatedBook.setTitle("New " + bookRequest.getTitle());

        bookDto.setId(id);
        bookDto.setTitle("New " + bookRequest.getTitle());

        doNothing().when(bookMapper).updateEntityFromDto(bookDto, book);
        when(bookRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(updatedBook);
        when(bookMapper.entityToDto(updatedBook)).thenReturn(bookDto);

        BookDto result = bookService.updateBook(bookDto);

        assertNotNull(result);
        assertEquals(bookDto, result);
    }

    @Test
    public void deleteBook() {

        when(bookRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(book));
        bookService.deleteBook(id);

        assertFalse(book.getIsEnabled());
    }

    @Test
    public void updateAverageRating() {

        BigDecimal rating1 = BigDecimal.valueOf(4.0);
        BigDecimal rating2 = BigDecimal.valueOf(5.0);

        Review review1 = new Review();
        review1.setRating(rating1);

        Review review2 = new Review();
        review2.setRating(rating2);

        book.setReviews(Set.of(review1, review2));

        when(bookRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        bookService.updateAverageRating(id);


        assertEquals(
                (rating1.add(rating2))
                        .divide(BigDecimal.valueOf(
                                book.getReviews().size()),
                                RoundingMode.HALF_UP)
                , book.getAverageRating());
    }
}
