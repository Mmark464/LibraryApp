package com.libraryManagementSystem.service.impl;

import com.libraryManagementSystem.dto.BookDto;
import com.libraryManagementSystem.entity.Book;
import com.libraryManagementSystem.entity.User;
import com.libraryManagementSystem.exception.NotFoundException;
import com.libraryManagementSystem.mapper.BookMapper;
import com.libraryManagementSystem.repository.BookRepository;
import com.libraryManagementSystem.repository.UserRepository;
import com.libraryManagementSystem.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final UserRepository userRepository;
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;


    @Override
    public void borrowBook(Long bookId, Long userId) {
        findUserById(userId).getBooks().add(findBookById(bookId));
        userRepository.save(findUserById(userId));
    }

    @Override
    public Page<BookDto> getBorrowedBooks(Long userId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getBooks().stream()
                .map(bookMapper::entityToDto)
                .toList()
                .stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> new PageImpl<>(list, pageable, list.size())));
    }

    @Override
    public void returnBook(Long bookId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = user.getBooks().stream()
                        .filter(eachBook -> eachBook.getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Book not found in user's collection"));

        user.getBooks().remove(book);
        userRepository.save(user);
    }

    private Book findBookById(Long id) {
        return bookRepository.findByIdAndIsEnabledTrue(id)
                .orElseThrow(() -> new NumberFormatException("Book not found with id: " + id));
    }

    private User findUserById(Long id) {
        return userRepository.findByIdAndIsEnabledTrue(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }
}
