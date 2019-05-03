//package com.book_management_system.backup;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.book_management_system.R;
//import com.book_management_system.com.AndroidUI.student.StudentShow;
//import com.book_management_system.com.AndroidUI.student.StudentUpdate;
//import com.book_management_system.com.control.StudentControl;
//import com.book_management_system.com.model.Student;
//
//
//public class StudentManage extends AppCompatActivity implements View.OnClickListener
//{
//
//    private StudentControl stuCtrl;
//    /*public StudentManage(Context context)
//    {
//        this(context,null);
//    }
//
//    public StudentManage(Context context, AttributeSet attrs)
//    {
//        this(context,attrs,0);
//    }
//
//    public StudentManage(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        View.inflate(context, R.layout.activity_student_manage_fragment, this);
//
//
//
//    }*/
//    LinearLayout studel;
//    ListView list;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_manage_fragment);
//        stuCtrl=new StudentControl(StudentManage.this);
//    }
//
//    @Override
//    public void onClick(View v)
//    {
//        Intent intent=new Intent();
//        AlertDialog.Builder dialog=new AlertDialog.Builder(StudentManage.this);
//        switch(v.getId())//响应每个菜单项(通过菜单项的ID)
//        {
//            case R.id.stuadd://跳转到加入学生界面
//            {
//                //intent.setClass(StudentManage.this, StudentInsert.class);
//                this.startActivity(intent);
//                break;
//            }
//            case R.id.studelbyno://提供学号删除学生
//            {
//                studel=(LinearLayout)getLayoutInflater().inflate(R.layout.student_dialogdel,null);
//                dialog.setTitle("删除记录").setView(studel);
//                dialog.setPositiveButton("确定", new delClick());
//                dialog.setNegativeButton("取消", null);
//                dialog.create();
//                dialog.show();
//                break;
//            }
//            case R.id.stumod: {//按学号修改学生
//                studel = (LinearLayout) getLayoutInflater().inflate(R.layout.student_dialogmod, null);
//
//                dialog.setTitle("修改学生信息").setView(studel);
//                dialog.setPositiveButton("确定", new stumodClick());
//                dialog.setNegativeButton("取消",null);
//                dialog.create();
//                dialog.show();
//                break;
//            }
//            case R.id.stubyno: {//提供按学号查找学生
//                studel = (LinearLayout) getLayoutInflater().inflate(R.layout.student_dialogsearch, null);
//                dialog.setTitle("查找学生").setView(studel);
//                dialog.setPositiveButton("确定", new stunoClick());
//                dialog.setNegativeButton("取消",null);
//                dialog.create();
//                dialog.show();
//                break;
//            }
//            case R.id.studelall:{//删除所有学生
//                studel=(LinearLayout)getLayoutInflater().inflate(R.layout.student_dialogdelall,null);
//                dialog.setTitle("删除全部记录").setView(studel);
//                dialog.setPositiveButton("确定", new delallClick());
//                dialog.setNegativeButton("取消",null);
//                dialog.create();
//                dialog.show();
//                break;
//            }
//            case R.id.stuall:{//跳转到显示所有学生界面
//                intent.setClass(StudentManage.this, StudentShow.class);
//                this.startActivity(intent);
//                break;
//            }
//        }
//    }
//
//    //删除全部学生对话框“确定”按钮事件
//    class delallClick implements DialogInterface.OnClickListener
//    {
//        @Override
//        public void onClick(DialogInterface dialog,int which)
//        {
//            stuCtrl.deleteAll();
//            Toast.makeText(StudentManage.this, "删除成功。", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        }
//    }
//
//
//    /*  删除学生记录对话框的“确定”按钮事件   */
//    class delClick implements  DialogInterface.OnClickListener
//    {
//        EditText txt;
//        @Override
//        public void onClick(DialogInterface dialog, int which)
//        {
//            txt = (EditText)studel.findViewById(R.id.stuno);
//            //取出输入编辑框的值
//            String na=txt.getText().toString();
//            Student s[] =stuCtrl.queryStudent(na) ;
//            if (s != null)
//            {
//                new AlertDialog.Builder(StudentManage.this)
//                        .setTitle("确实要删除这名学生吗？")
//                .setMessage("学号："+ s[0].getNo() + "\n" +
//                                "姓名："+ s[0].getName() + "\n" +
//                                "专业："+ s[0].getMajor() + "\n" +
//                                "班级："+ s[0].getClasses() + "\n" +
//                                "手机号码："+ s[0].getPhone())
//                        .setPositiveButton("确定",new delClickyes(na)).setNegativeButton("取消",null)
//                        .show();
//            }
//            else
//                Toast.makeText(getApplicationContext(),"没有符合条件的记录。", Toast.LENGTH_SHORT).show();
//            //dialog.dismiss();
//        }
//    }
//    /*  确认删除学生记录对话框的“确定”按钮事件   */
//    class delClickyes implements DialogInterface.OnClickListener
//    {
//        private String na;
//        public delClickyes(String na)
//        {
//            this.na=na;
//        }
//        @Override
//        public void onClick(DialogInterface dialog,int which)
//        {
//            stuCtrl.deleteStudent(na);
//            Toast.makeText(StudentManage.this, "删除成功。", Toast.LENGTH_SHORT).show();
//            //dialog.dismiss();
//        }
//    }
//
//    /*  按学号修改查找学生记录对话框的“确定”按钮事件   */
//    class stumodClick implements  DialogInterface.OnClickListener
//    {
//        EditText txt;
//        @Override
//        public void onClick(DialogInterface dialog, int which)
//        {
//            txt = (EditText)studel.findViewById(R.id.stuno);
//            //取出输入编辑框的值
//            String na=txt.getText().toString();
//            Student s[] = stuCtrl.queryStudent(na);
//            if (s != null){
//                Intent intent = new Intent();
//                intent.setClass(StudentManage.this, StudentUpdate.class);
//                Bundle bundle1 = new Bundle();            //创建Bundle 对象
//                bundle1.putString("stuno", na);          // 把编辑框中的字符串作为传递值，键名为user
//                intent.putExtras(bundle1);
//                StudentManage.this.startActivity(intent);
//            }
//            else
//                Toast.makeText(getApplicationContext(),"没有符合条件的记录。", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        }
//    }
//    /*  按学号查找学生记录对话框的“确定”按钮事件   */
//    class stunoClick implements  DialogInterface.OnClickListener
//    {
//        EditText txt;
//        @Override
//        public void onClick(DialogInterface dialog, int which)
//        {
//            txt = (EditText)studel.findViewById(R.id.stuno);
//            //取出输入编辑框的值
//            String na=txt.getText().toString();
//            Student s[] = stuCtrl.queryStudent(na);
//            if (s != null)
//                new AlertDialog.Builder(StudentManage.this)
//                        .setTitle("该学生信息：")
//                        .setMessage(s[0].getNo() + "\n" +
//                                s[0].getName() + "\n" +
//                                s[0].getMajor() + "\n" +
//                                s[0].getClasses() + "\n" +
//                                s[0].getPhone())
//                        .setPositiveButton("确定",null).show();
//            else
//                Toast.makeText(getApplicationContext(),"没有与搜索条件匹配的项。", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        }
//    }
//}