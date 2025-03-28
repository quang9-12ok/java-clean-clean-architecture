package com.example.bookmanagement.getListBook;

import java.util.List;

public interface GetListBookOutputBoundary {
    void present(List<GetListBookOutputDTO> bookList);
}
