package com.book_management_system.com.model;

public class Student
{
    private String no;
    private String name;
    private String major;
    private String classes;
    private String phone;
    //构造函数
    public Student(String no,String name,String major,String classes,String phone)
    {
        super();//注意到class Student后面并没有跟extends XXX，这里super();是所有类的基类object的构造方法。也可以不写。
        this.no=no;
        this.name=name;
        this.major=major;
        this.classes=classes;
        this.phone=phone;
    }
    public Student(){}

    public String getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public String getMajor() {
        return major;
    }

    public String getClasses() {
        return classes;
    }

    public String getPhone() {
        return phone;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    //直接右键Generate，选Getter and Setter
}