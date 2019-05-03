package com.book_management_system.com.model;

public class Book
{
    private String no;
    private String name;
    private String author;
    private String publisher;
    private int totalnum;
    private int borrownum;
    private int remaining;
    private String year,month,day;
    //构造函数
    public Book(String no,String name,String author,String publisher,int totalnum,int borrownum,String year,String month,String day)
    {
        super();//注意到class Student后面并没有跟extends XXX，这里super();是所有类的基类object的构造方法。也可以不写。
        this.no=no;
        this.name=name;
        this.author=author;
        this.publisher=publisher;
        this.totalnum=totalnum;
        this.borrownum=borrownum;
        this.year=year;
        this.month=month;
        this.day=day;
    }

    public Book(){}

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(int totalnum) {
        this.totalnum = totalnum;
    }

    public int getBorrownum() {
        return borrownum;
    }

    public void setBorrownum(int borrownum) {
        this.borrownum = borrownum;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getRemaining() {
        return this.totalnum-this.borrownum;
    }
//直接右键Generate，选Getter and Setter
}