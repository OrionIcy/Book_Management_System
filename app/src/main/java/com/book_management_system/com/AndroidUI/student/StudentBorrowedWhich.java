package com.book_management_system.com.AndroidUI.student;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.book_management_system.R;
import com.book_management_system.com.control.BorrowingControl;
import com.book_management_system.com.control.StudentControl;
import com.book_management_system.com.model.Borrowing;
import com.book_management_system.com.model.DBAdapter;
import com.book_management_system.com.model.Student;

public class StudentBorrowedWhich extends AppCompatActivity
{
    private BorrowingControl borrCtrl;
    private StudentControl stuCtrl;
    private TextView stu,boo;
    Bundle bundle;
    String info;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_borrowed_which);
        stu=(TextView)findViewById(R.id.student);
        boo=(TextView)findViewById(R.id.book);
        borrCtrl=new BorrowingControl(StudentBorrowedWhich.this);
        stuCtrl=new StudentControl(StudentBorrowedWhich.this);
        bundle=this.getIntent().getExtras();
        info=bundle.getString("info");
        Student[] s=stuCtrl.queryStudent(info);
        Borrowing[] borr=borrCtrl.queryBorrowing(info);
        String student=s[0].getNo()+" "+s[0].getName();
        String book;
        if(borr!=null)
            book=DBAdapter.listToStringWithSeparator(borr[0].getBorrBooks(),"\n");
        else
            book="该生暂无借阅信息。";
        stu.setText(student);
        stu.setTextSize(18);
        boo.setText(book);
        boo.setTextSize(18);
    }
}
