package com.collabera.library_application.borrower.mapper;


import com.collabera.library_application.borrower.dto.response.BorrowerResponse;
import com.collabera.library_application.borrower.entity.Borrower;
import com.collabera.library_application.borrowing.entity.BookBorrow;
import org.springframework.stereotype.Component;

@Component
public class BorrowerMapper {


    public static BorrowerResponse toRegistrationResponse(Borrower borrower) {

        return new BorrowerResponse(
                borrower.getId(),
                borrower.getName(),
                borrower.getEmail()
        );
    }
}
