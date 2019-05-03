package com.book_management_system.com.AndroidUI.book;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.book_management_system.R;
import com.book_management_system.com.AndroidUI.SearchableActivity;
import com.book_management_system.com.AndroidUI.SearchableFragmentBook;
import com.book_management_system.com.control.BookControl;
import com.book_management_system.com.model.Book;

import java.util.ArrayList;
import java.util.HashMap;

public class BookUpdate extends AppCompatActivity {
    private EditText author;
    private EditText publisher;
    private TextView no;
    private EditText totalnum;
    private TextView borrownum;
    private EditText name;
    private EditText year;
    private EditText month;
    private EditText day;
    BookControl bookCtrl;
    private Button btnmod,btncancel;
    private String SOURCE,info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_update);
        author = (EditText) findViewById(R.id.etupdataauthor);
        publisher = (EditText) findViewById(R.id.etupdatapublisher);
        year = (EditText) findViewById(R.id.etupdatayear);
        month = (EditText) findViewById(R.id.etupdatamonth);
        day = (EditText) findViewById(R.id.etupdataday);
        name = (EditText) findViewById(R.id.etupdataname);
        no = (TextView) findViewById(R.id.etupdatano);
        totalnum = (EditText) findViewById(R.id.etupdatatotal);
        borrownum = (TextView) findViewById(R.id.etupdataborrow);

        btnmod=(Button)findViewById(R.id.update);
        btncancel=(Button)findViewById(R.id.modcancel);

        btnmod.setOnClickListener(new modClick());
        btncancel.setOnClickListener(new modcancelClick());

        Bundle bundle=this.getIntent().getExtras();//从传递过来的Intent对象中取出Bundle对象
        String na=bundle.getString("bookno");
        SOURCE=bundle.getString("source");
        info=bundle.getString("info");

        bookCtrl=new BookControl(this);
        Book b[] =bookCtrl.queryBook(na) ;
        no.setText(b[0].getNo());
        name.setText(b[0].getName());
        author.setText(b[0].getAuthor());
        publisher.setText(b[0].getPublisher());
        totalnum.setText(String.valueOf(b[0].getTotalnum()));
        borrownum.setText(String.valueOf(b[0].getBorrownum()));
        year.setText(b[0].getYear());
        month.setText(b[0].getMonth());
        day.setText(b[0].getDay());
    }

    public class modClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            String bookNo = no.getText().toString().trim();
            String bookName = name.getText().toString().trim();
            String bookAuthor = author.getText().toString().trim();
            String bookPublisher = publisher.getText().toString().trim();
            String bookTotal = totalnum.getText().toString().trim();
            int bookTotalnum = Integer.parseInt(bookTotal);
            String bookBorrow = borrownum.getText().toString().trim();
            int bookBorrownum = Integer.parseInt(bookBorrow);
            String bookYear = year.getText().toString().trim();
            String bookMonth = month.getText().toString().trim();
            String bookDay = day.getText().toString().trim();

            Book[] b1;

            if (bookName.equals("") || bookAuthor.equals("") || bookPublisher.equals("") || bookYear.equals("") || bookMonth.equals("")|| bookDay.equals("") || bookTotal.equals("")
                    ||bookTotalnum<0||bookTotalnum<bookBorrownum)
            {
                Toast.makeText(BookUpdate.this, "请填写完整且合法的数据", Toast.LENGTH_SHORT).show();
            } else {
                //if (stuCtrl.queryStudent(studentNo) != null) {
                Book book = new Book(bookNo, bookName, bookAuthor, bookPublisher, bookTotalnum,bookBorrownum,bookYear,bookMonth,bookDay);
                bookCtrl.updateBook(book);

                    //no.setText("");
                    //major.setText("");
                    //phone.setText("");
                    //name.setText("");
                    //classes.setText("");

                if(SOURCE.equals("BookShow"))
                {
                    if(BookShow.Flag==0)
                        b1=bookCtrl.getAllBooks();
                    else if(BookShow.Flag==1)
                        b1=bookCtrl.queryBorrowable();
                    else
                        b1=bookCtrl.queryUnBorrowable();
                    ArrayList<HashMap<String, Object>> data1= new ArrayList<HashMap<String, Object>>();
                    if(b1!=null)
                    {
                        for (int i = 0; i < b1.length; i++) {
                            HashMap<String, Object> item = new HashMap<String, Object>();
                            item.put("no", b1[i].getNo());
                            item.put("name", b1[i].getName());
                            item.put("author", b1[i].getAuthor());
                            item.put("publisher", b1[i].getPublisher());
                            item.put("date", b1[i].getYear() + "." + b1[i].getMonth() + "." + b1[i].getDay());
                            data1.add(item);
                        }
                        BookShow.data.clear();
                        BookShow.data.addAll(data1);
                        BookShow.adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        BookShow.none1.setText("没有数据。");
                        BookShow.none1.setTextSize(18);
                        BookShow.none2.setText("");
                        BookShow.none3.setText("");
                        BookShow.none4.setText("");
                        BookShow.none5.setText("");
                        BookShow.data.clear();
                        BookShow.adapter.notifyDataSetChanged();
                    }
                }

                if(SOURCE.equals("SearchableActivity"))
                {
                    b1=bookCtrl.searchBook(info);
                    ArrayList<HashMap<String, Object>> data2= new ArrayList<HashMap<String, Object>>();
                    for (int i = 0; i < b1.length; i++)
                    {
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("no", b1[i].getNo());
                        item.put("name", b1[i].getName());
                        item.put("author", b1[i].getAuthor());
                        item.put("publisher", b1[i].getPublisher());
                        item.put("date", b1[i].getYear()+"."+b1[i].getMonth()+"."+b1[i].getDay());
                        data2.add(item);
                    }
                    SearchableFragmentBook.data1.clear();
                    SearchableFragmentBook.data1.addAll(data2);
                    SearchableFragmentBook.adapter1.notifyDataSetChanged();
                }
                    buildDialog();

                //}
                //else {
                    //Toast.makeText(StudentUpdate.this, "系统找不到此学生。", Toast.LENGTH_SHORT).show();
                //}
            }
        }
    }

    public class modcancelClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            //Intent intent=new Intent();
            //intent.setClass(StudentUpdate.this, StudentManage.class);
            //startActivity(intent);
            finish();
        }
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BookUpdate.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("修改成功");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        builder.show();
    }
}
