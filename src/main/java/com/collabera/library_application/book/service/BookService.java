package com.collabera.library_application.book.service;

import com.collabera.library_application.book.dto.request.BookRegistrationRequest;
import com.collabera.library_application.book.dto.response.BookResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {

    BookResponse registerBook(BookRegistrationRequest request);

    List<BookResponse> getBooks();

}
