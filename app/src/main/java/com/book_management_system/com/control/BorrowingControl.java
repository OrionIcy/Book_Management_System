package com.book_management_system.com.control;

import com.book_management_system.com.model.*;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BorrowingControl
{
    private static BorrowingSet borrowingSet;
    private static DBAdapter borrowingDbAdapter;
    private Context context;

    public BorrowingControl (Context context)
    {
        this.context=context;
        borrowingSet=BorrowingSet.getBorrowingList(context);//读取文件
        borrowingDbAdapter=new DBAdapter(context);
        borrowingDbAdapter.open();
    }

    //借书
    public void borrowBook(String stuno,String bookno)
    {
        if(!borrowingDbAdapter.inDatabase(stuno))
            borrowingDbAdapter.insertBorrowing(stuno);
        borrowingDbAdapter.borrowBook(stuno,bookno);
    }

    //还书
    public void returnBook(String stuno,String bookno)
    {
        borrowingDbAdapter.returnBook(stuno, bookno);
    }

    public Borrowing[] getAllRecords()
    {
        return borrowingDbAdapter.getAllRecords();
    }

    public boolean ifBorrowed(String stuno,String bookno)
    {
        return borrowingDbAdapter.ifBorrowed(stuno,bookno);
    }

    public int borrowingNum(String stuno)
    {
        return borrowingDbAdapter.borrowingNum(stuno);
    }

    public String getBorrowTime(String stuno,String bookno)
    {
        String time="";
        Borrowing[] borr=borrowingDbAdapter.queryBorrowing(stuno);
        List<String> borrbook=borr[0].getBorrBooks();
        for(int i=0;i<borrbook.size();i++)
        {
            if(borrbook.get(i).split("，")[0].equals(bookno)) {
                time = borrbook.get(i).split("，")[1];
                break;
            }
        }
        return time;
    }

    public Borrowing[] queryBorrowing(String stuno)
    {
        return borrowingDbAdapter.queryBorrowing(stuno);
    }
}