package com.book_management_system.com.AndroidUI.student;

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
import com.book_management_system.com.AndroidUI.SearchableFragmentStudent;
import com.book_management_system.com.control.StudentControl;
import com.book_management_system.com.model.Student;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentUpdate extends AppCompatActivity {
    private EditText major;
    private EditText phone;
    private TextView no;
    private EditText name;
    private EditText classes;
    StudentControl stuCtrl;
    private Button btnmod,btncancel;
    private String SOURCE,info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_update);
        major = (EditText) findViewById(R.id.etupdatamajor);
        phone = (EditText) findViewById(R.id.etupdatamobile);
        no = (TextView) findViewById(R.id.etupdatano);
        classes = (EditText) findViewById(R.id.etupdataclass);
        name = (EditText) findViewById(R.id.etupdataname);

        btnmod=(Button)findViewById(R.id.update);
        btncancel=(Button)findViewById(R.id.modcancel);

        btnmod.setOnClickListener(new modClick());
        btncancel.setOnClickListener(new modcancelClick());

        Bundle bundle=this.getIntent().getExtras();//从传递过来的Intent对象中取出Bundle对象
        String na=bundle.getString("stuno");
        SOURCE=bundle.getString("source");
        info=bundle.getString("info");//用于搜索列表更新

        stuCtrl=new StudentControl(this);
        Student s[] =stuCtrl.queryStudent(na) ;
        no.setText(s[0].getNo());
        name.setText(s[0].getName());
        major.setText(s[0].getMajor());
        classes.setText(s[0].getClasses());
        phone.setText(s[0].getPhone());
    }

    public class modClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            String studentNo = no.getText().toString().trim();
            String studentMajor = major.getText().toString().trim();
            String studentPhone = phone.getText().toString().trim();
            String studentClasses = classes.getText().toString().trim();
            String studentName = name.getText().toString().trim();

            Student[] s1;

            if (studentNo.equals("") || studentMajor.equals("") || studentPhone.equals("") || studentClasses.equals("") || studentName.equals(""))
            {
                Toast.makeText(StudentUpdate.this, "请填写完整", Toast.LENGTH_SHORT).show();
            }
            else {
                //if (stuCtrl.queryStudent(studentNo) != null) {
                    Student student = new Student(studentNo, studentName, studentMajor, studentClasses, studentPhone);
                    stuCtrl.updateStudent(student);

                    //no.setText("");
                    //major.setText("");
                    //phone.setText("");
                    //name.setText("");
                    //classes.setText("");

                if(SOURCE.equals("StudentShow"))
                {
                    s1=stuCtrl.getAllStudent();
                    ArrayList<HashMap<String, Object>> data1= new ArrayList<HashMap<String, Object>>();
                    for (int i = 0; i < s1.length; i++)
                    {
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("no", s1[i].getNo());
                        item.put("name", s1[i].getName());
                        item.put("phone", s1[i].getPhone());
                        item.put("class", s1[i].getClasses());
                        item.put("major", s1[i].getMajor());
                        data1.add(item);
                    }
                    StudentShow.data.clear();
                    StudentShow.data.addAll(data1);
                    StudentShow.adapter.notifyDataSetChanged();
                }

                if(SOURCE.equals("SearchableActivity"))
                {
                    s1=stuCtrl.searchStudent(info);
                    ArrayList<HashMap<String, Object>> data2= new ArrayList<HashMap<String, Object>>();
                    for (int i = 0; i < s1.length; i++)
                    {
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("no", s1[i].getNo());
                        item.put("name", s1[i].getName());
                        item.put("phone", s1[i].getPhone());
                        item.put("class", s1[i].getClasses());
                        item.put("major", s1[i].getMajor());
                        data2.add(item);
                    }
                    SearchableFragmentStudent.data1.clear();
                    SearchableFragmentStudent.data1.addAll(data2);
                    SearchableFragmentStudent.adapter1.notifyDataSetChanged();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentUpdate.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("修改成功");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        builder.show();
    }
}
