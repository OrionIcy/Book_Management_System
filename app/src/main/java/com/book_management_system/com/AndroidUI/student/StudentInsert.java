package com.book_management_system.com.AndroidUI.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.book_management_system.R;
import com.book_management_system.com.control.StudentControl;
import com.book_management_system.com.model.Student;
import com.book_management_system.com.AndroidUI.student.*;

import java.util.ArrayList;
import java.util.HashMap;


public class StudentInsert extends AppCompatActivity implements View.OnClickListener
{
    //定义控件对象
    private EditText name;
    private EditText no;
    private EditText major;
    private EditText classes;
    private EditText phone;
    Bundle bundle2;
    String SOURCE;
    Student[] s;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_insert);
        //绑定控件
        no=(EditText)findViewById(R.id.etNo);
        name=(EditText)findViewById(R.id.etName);
        classes=(EditText)findViewById(R.id.etClass);
        major=(EditText)findViewById(R.id.etMajor);
        phone=(EditText)findViewById(R.id.etMobile);
        bundle2=this.getIntent().getExtras();
        SOURCE=bundle2.getString("source");
    }

    @Override
    public void onClick(View v)
    {
        Student stu;
        StudentControl stuCtrl=new StudentControl(StudentInsert.this);
        if(v.getId()==R.id.insert)
        {
            String studentNo = no.getText().toString().trim();
            String studentName = name.getText().toString().trim();
            String studentClasses = classes.getText().toString().trim();
            String studentMajor = major.getText().toString().trim();
            String studentMobile = phone.getText().toString().trim();
            if (studentName.equals("") || studentNo.equals("") || studentMajor.equals("") || studentClasses.equals("") || studentMobile.equals(""))
                Toast.makeText(StudentInsert.this, "请填写完整", Toast.LENGTH_SHORT).show();
            else {
                if (stuCtrl.queryStudent(studentNo) != null)
                    Toast.makeText(StudentInsert.this, "该学生已经存在", Toast.LENGTH_SHORT).show();
                else {// 封装成Student对象
                    stu = new Student(studentNo, studentName, studentMajor, studentClasses, studentMobile);
                    stuCtrl.addStudent(stu);
                    if(SOURCE.equals("StudentShow"))
                    {
                        s=stuCtrl.getAllStudent();
                        ArrayList<HashMap<String, Object>> data1= new ArrayList<HashMap<String, Object>>();
                        for (int i = 0; i < s.length; i++)
                        {
                            HashMap<String, Object> item = new HashMap<String, Object>();
                            item.put("no", s[i].getNo());
                            item.put("name", s[i].getName());
                            item.put("phone", s[i].getPhone());
                            item.put("class", s[i].getClasses());
                            item.put("major", s[i].getMajor());
                            data1.add(item);
                        }
                        StudentShow.data.clear();
                        StudentShow.data.addAll(data1);
                        StudentShow.adapter.notifyDataSetChanged();
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
        AlertDialog.Builder builder=new AlertDialog.Builder(StudentInsert.this);
        builder.setTitle("添加成功。");
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int whichButton) {
            //设置文本框为空
            name.setText("");
            major.setText("");
            classes.setText("");
            phone.setText("");
            no.setText("");
            no.setCursorVisible(true);
            }
        });
        builder.show();
    }
}
