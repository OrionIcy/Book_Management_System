package com.book_management_system.com.control;

import com.book_management_system.com.model.*;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookControl
{
    private static BookSet bookSet;
    private static DBAdapter bookDbAdapter;
    private Context context;

    public BookControl (Context context)
    {
        this.context=context;
        bookSet=BookSet.getBookList(context);
        bookDbAdapter=new DBAdapter(context);
        bookDbAdapter.open();
    }

    //添加图书
    public void addBook(Book book)
    {
        bookDbAdapter.insertBook(book);
        //studentSet.insertStudent(student);
    }

    public void saveAll()
    {
        //把文件中读取出的数据保存到数据库中

        //使用事务可以极大地增加插入速度，不再是一条一条地插入，而是暂存在缓存区最后一起插入数据库
        //dbAdapter.deleteAllStudents();
        String temp;
        bookDbAdapter.db.beginTransaction();//开启事务
        for(int i=0;i<bookSet.size();i++)
        {
            temp=bookSet.get(i).getNo();
            if(bookDbAdapter.queryBook(temp)!=null)
                bookDbAdapter.deleteBook(temp);
            bookDbAdapter.insertBook(bookSet.get(i));
        }
        bookDbAdapter.db.setTransactionSuccessful();//设置事务标志为成功，当事务结束时就会提交事务
        bookDbAdapter.db.endTransaction();//事务结束
    }

    public Book[] queryBorrowable()
    {
        return bookDbAdapter.queryBorrowable();
    }
    public Book[] queryUnBorrowable()
    {
        return bookDbAdapter.queryUnborrowable();
    }

    //删除所有图书
    public void deleteAll()
    {
        bookDbAdapter.deleteAllBooks();
        //studentSet.deleteAll();
    }

    //删除一本图书
    public boolean deleteBook(String info)
    {
        //ArrayList<Student> student;
        //student=studentSet.queryStudent(info);
        Book book[]=bookDbAdapter.queryBook(info);
        if(book!=null)
        {
            //studentSet.deleteStudent(info);
            bookDbAdapter.deleteBook(info);
            return true;
        }
        return false;
    }

    //删除一组图书
    public boolean deleteBooks(Student[] s)
    {
        String info;
        int n=0;
        for(int i=0;i<s.length;i++) {
            info=s[i].getNo();
            Book book[] = bookDbAdapter.queryBook(info);
            if (book != null) {
                //studentSet.deleteStudent(info);
                bookDbAdapter.deleteBook(info);
                n++;
            }
        }
        if(n==s.length)
            return true;
        return false;
    }
    //更新
    public void updateBook(Book book)
    {
        String no=book.getNo();
        //ArrayList<Student> stuList=studentSet.queryStudent(no);//把查询结果放到ArrayList中
        Book b[]=bookDbAdapter.queryBook(no);
        /*if(stuList!=null)
        {
            studentSet.deleteStudent(no);
            studentSet.insertStudent(student);
        }*/
        if(b!=null)
        {
            bookDbAdapter.updateBook(no,book);
        }
    }
    //查询
    public Book[] queryBook(String info)
    {
        /*ArrayList<Student> stuList = studentSet.queryStudent(no);//把查询结果放到ArrayList中
        if (stuList != null)
        {
            Student[] students = new Student[stuList.size()];
            for (int i = 0; i < stuList.size(); i++)
            {//将ArrayList中的学生取出来放到学生数组中
                students[i] = stuList.get(i);
            }
            return students;
        } else
            return null;*/
        return bookDbAdapter.queryBook(info);
    }

    public Book[] searchBook(String info)
    {
        Book[] result;
        Book[] result1=bookDbAdapter.searchBookNo(info);
        Book[] result2=bookDbAdapter.searchBookName(info);
        Book[] result3=bookDbAdapter.searchBookAuthor(info);
        Book[] result4=bookDbAdapter.searchBookPublisher(info);
        Book[] result5=bookDbAdapter.searchBookDate(info);
        List<Book> list=new ArrayList<>();
        if(result1!=null)
            Collections.addAll(list,result1);
        if(result2!=null)
            Collections.addAll(list,result2);
        if(result3!=null)
            Collections.addAll(list,result3);
        if(result4!=null)
            Collections.addAll(list,result4);
        if(result5!=null)
            Collections.addAll(list,result5);
        if(list.size()!=0)
        {
            for(int i=0;i<list.size();i++){
                for(int j=i+1;j<list.size();j++){
                    if(list.get(i).getNo().equals(list.get(j).getNo()))
                    {
                        list.remove(j);
                        j--;
                    }
                }
            }
            result=new Book[list.size()];
            list.toArray(result);
            return result;
        }
        return null;
    }


    //获取所有图书信息
    public Book[] getAllBooks()
    {
        /*int size = studentSet.ListLength_S();
        Student students[] = new Student[size];
        for (int i = 0; i < size; i++)
        {
            students[i] = studentSet.get(i);
        }
        return students;*/
        return bookDbAdapter.getAllBook();
    }

    public int getRemaining(String bookno)
    {
        return bookDbAdapter.getRemaining(bookno);
    }
}
