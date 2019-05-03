package com.book_management_system.com.model;

import java.util.ArrayList;
import java.util.List;

public class Borrowing {
    private String studentNo;
    private List<String> borrBooks=new ArrayList<>();
    public Borrowing(String studentNo,List<String> borrBooks)
    {
        this.studentNo=studentNo;
        this.borrBooks=borrBooks;
    }

    public Borrowing(){}

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public List<String> getBorrBooks() {
        return borrBooks;
    }

    public void setBorrBooks(List<String> borrBooks) {
        this.borrBooks = borrBooks;
    }
}
