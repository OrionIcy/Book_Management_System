package com.book_management_system.com.AndroidUI.book;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.book_management_system.R;
import com.book_management_system.com.control.BookControl;
import com.book_management_system.com.model.Book;
import com.book_management_system.com.AndroidUI.book.*;

import java.util.ArrayList;
import java.util.HashMap;


public class BookInsert extends AppCompatActivity implements View.OnClickListener
{
    //定义控件对象
    private EditText name;
    private EditText no;
    private EditText author;
    private EditText publisher;
    private EditText totalnum;
    private EditText year;
    private EditText month;
    private EditText day;
    Bundle bundle2;
    String SOURCE;
    Book[] b;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_insert);
        //绑定控件
        no=(EditText)findViewById(R.id.etNo);
        name=(EditText)findViewById(R.id.etName);
        author=(EditText)findViewById(R.id.etAuthor);
        publisher=(EditText)findViewById(R.id.etPublisher);
        totalnum=(EditText)findViewById(R.id.etTotalnum);
        year=(EditText)findViewById(R.id.etYear);
        month=(EditText)findViewById(R.id.etMonth);
        day =(EditText)findViewById(R.id.etDay);
        bundle2=this.getIntent().getExtras();
        SOURCE=bundle2.getString("source");
    }

    @Override
    public void onClick(View v)
    {
        Book book,books[];
        BookControl bookCtrl=new BookControl(BookInsert.this);
        if(v.getId()==R.id.insert)
        {
            String bookNo = no.getText().toString().trim();
            String bookName = name.getText().toString().trim();
            String bookAuthor = author.getText().toString().trim();
            String bookPublisher = publisher.getText().toString().trim();
            String bookTotal = totalnum.getText().toString().trim();
            int bookTotalnum = Integer.parseInt(bookTotal);
            String bookYear = year.getText().toString().trim();
            String bookMonth = month.getText().toString().trim();
            String bookDay = day.getText().toString().trim();

            if (bookNo.equals("") || bookName.equals("") ||bookAuthor.equals("") || bookPublisher.equals("") || bookTotal.equals("")|| bookYear.equals("")|| bookMonth.equals("")|| bookDay.equals("") ||bookTotalnum<0)
            {
                Toast.makeText(BookInsert.this, "请填写完整且合法的数据", Toast.LENGTH_SHORT).show();
            }
            else {
                if ((books=bookCtrl.queryBook(bookNo)) != null)
                {
                    //Toast.makeText(BookInsert.this, "该学生已经存在", Toast.LENGTH_SHORT).show();
                    if(books[0].getName().equals(bookName))
                    {
                        book=new Book(books[0].getNo(),books[0].getName(),books[0].getAuthor(),books[0].getPublisher(),books[0].getTotalnum()+bookTotalnum,
                                books[0].getBorrownum(),books[0].getYear(),books[0].getMonth(),books[0].getDay());
                        bookCtrl.updateBook(book);
                        myDialog();
                    }
                    else
                        Toast.makeText(BookInsert.this, "该书号已存在", Toast.LENGTH_SHORT).show();
                }
                else {// 封装成Book对象
                    book = new Book(bookNo, bookName, bookAuthor, bookPublisher, bookTotalnum,0,bookYear,bookMonth,bookDay);
                    bookCtrl.addBook(book);
                    if(SOURCE.equals("BookShow"))
                    {
                        switch(BookShow.Flag)
                        {
                            case 0:
                                b=bookCtrl.getAllBooks();
                            case 1:
                                b=bookCtrl.queryBorrowable();
                            case 2:
                                b=bookCtrl.queryUnBorrowable();
                        }
                        ArrayList<HashMap<String, Object>> data1= new ArrayList<HashMap<String, Object>>();
                        for (int i = 0; i < b.length; i++)
                        {
                            HashMap<String, Object> item = new HashMap<String, Object>();
                            item.put("no", b[i].getNo());
                            item.put("name", b[i].getName());
                            item.put("author", b[i].getAuthor());
                            item.put("publisher", b[i].getPublisher());
                            item.put("date", b[i].getYear()+"."+b[i].getMonth()+"."+b[i].getDay());
                            data1.add(item);
                        }
                        BookShow.data.clear();
                        BookShow.data.addAll(data1);
                        BookShow.adapter.notifyDataSetChanged();
                    }
                    myDialog();
                }
            }
        }
        else
        {
            //Intent intent=new Intent();
            //intent.setClass(StudentInsert.this, StudentManage.class);
            //startActivity(intent);
            finish();
        }
    }

    private void myDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(BookInsert.this);
        builder.setTitle("添加成功。");
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int whichButton) {
            //设置文本框为空
            name.setText("");
            author.setText("");
            publisher.setText("");
            totalnum.setText("");
            year.setText("");
            month.setText("");
            day.setText("");
            no.setText("");
            no.setCursorVisible(true);
            }
        });
        builder.show();
    }
}
