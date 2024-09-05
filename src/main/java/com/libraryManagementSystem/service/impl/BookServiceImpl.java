package com.libraryManagementSystem.service.impl;

import com.libraryManagementSystem.specification.BookSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import com.libraryManagementSystem.repository.*;
import org.springframework.cache.annotation.*;
import com.libraryManagementSystem.service.*;
import com.libraryManagementSystem.mapper.*;
import com.libraryManagementSystem.entity.*;
import com.libraryManagementSystem.dto.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    @Autowired
    BookMapper bookMapper;

    @Autowired
    BookRepository bookRepository;

    @Override
    public BookDto saveBook(BookRequest bookRequest) {
        return bookMapper.entityToDto(
                bookRepository.save(
                        bookMapper.requestToEntity(
                                bookRequest
                        )));
    }

    @Override
    @Cacheable(value = "books", key = "'allBooks'")
    public Page<BookDto> getAllBooks(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return bookRepository.findAllByIsEnabledTrue(pageable)
                             .map(bookMapper::entityToDto);
    }

    @Override
    @Cacheable(value = "books", key = "'bookById_' + #id")
    public BookDto getBookById(Long id) {
        return bookMapper.entityToDto(findById(id));
    }

    @Override
    public Page<BookDto> bookFilter(Long publishingHouseId,
                                    Set<Long> authorIds,
                                    Set<Long> genreIds,
                                    String title,
                                    int pageNumber, int pageSize) {

        Specification<Book> specification = BookSpecification.findBookByCriteria(
                publishingHouseId, authorIds, genreIds, title);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return  bookRepository.findAllByIsEnabledTrue(specification, pageable)
                              .map(bookMapper::entityToDto);
    }


    @Override
    @CachePut(value = "books", key = "'bookById_' + #bookDto.id")
    public BookDto updateBook(BookDto bookDto) {

        bookMapper.updateEntityFromDto(bookDto, findById(bookDto.getId()));

        return bookMapper.entityToDto(
                bookRepository.save(
                        findById(
                                bookDto.getId()
                        )));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "books", key = "'allBooks'", allEntries = true),
            @CacheEvict(value = "books", key = "'bookById_' + #id")
    })
    public void deleteBook(Long id) {
        findById(id).setIsEnabled(false);
        bookRepository.save(
                findById(id)
        );
    }

    @Override
    public void updateAverageRating(Long id) {
        var book = findById(id);

        BigDecimal sum = book.getReviews().stream()
                .map(Review::getRating)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if(book.getReviews().isEmpty()){
            book.setAverageRating(BigDecimal.ZERO);
        }else{
            BigDecimal avg = sum.divide(
                    new BigDecimal(book.getReviews().size()), RoundingMode.HALF_UP);
            book.setAverageRating(avg);
        }
        bookRepository.save(book);
    }

    private Book findById(Long id) {
        return bookRepository.findByIdAndIsEnabledTrue(id)
                .orElseThrow(() -> new NumberFormatException("Book not found with id: " + id));
    }
}
