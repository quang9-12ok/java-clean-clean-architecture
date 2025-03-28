package com.example.bookmanagement.themSach;

public interface ThemSachOutputBoundary {
    void prepareSuccessView(ThemSachOutputDTO outputDTO);
    void prepareFailView(ThemSachResponseError error);
   
    
}
