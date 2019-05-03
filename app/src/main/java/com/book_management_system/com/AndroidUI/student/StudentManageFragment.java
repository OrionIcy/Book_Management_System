package com.book_management_system.com.AndroidUI.student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.book_management_system.R;
import com.book_management_system.com.AndroidUI.SearchableActivity;
import com.book_management_system.com.control.StudentControl;
import com.book_management_system.com.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentManageFragment extends Fragment
{
    private EditText edit;
    private LinearLayout studel;
    private StudentControl stuCtrl;
    private static final String[] options={"显示所有学生","修改学生信息","删除学生","删除所有学生","导入学生数据"};
    private List<String> contentList;
    private ListView mListView;
   @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Context context=getActivity();
        stuCtrl=new StudentControl(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_student_manage, container, false);
        //initView(view);
//        Button button1=(Button)view.findViewById(R.id.stubyno);
//        Button button2=(Button)view.findViewById(R.id.stumod);
//        Button button3=(Button)view.findViewById(R.id.studelbyno);
//        Button button4=(Button)view.findViewById(R.id.studelall);
//        Button button5=(Button)view.findViewById(R.id.stuall);
//        Button button7=(Button)view.findViewById(R.id.buttonSaveAll);
//        button1.setOnClickListener(new onClickListener());
//        button2.setOnClickListener(new onClickListener());
//        button3.setOnClickListener(new onClickListener());
//        button4.setOnClickListener(new onClickListener());
//        button5.setOnClickListener(new onClickListener());
//        button7.setOnClickListener(new onClickListener());
        mListView=(ListView)view.findViewById(R.id.ListView01);
        contentList=new ArrayList<String>();
        for(int i=0;i<options.length;i++)
            contentList.add(options[i]);
        Context contextop=getActivity();
        mListView.setAdapter(new ArrayAdapter<String>(contextop,android.R.layout.simple_list_item_1,options));
        mListView.setOnItemClickListener(new onClick());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    public class onClick implements OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> arg0,View v,int position,long arg3)
        {
            AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
            switch(position) {
                /*case R.id.stuadd: {
                    Context context=getActivity();
                    startActivity(new Intent(context, StudentInsert.class));
                    //Toast.makeText(getActivity(), "switch 1:已执行", Toast.LENGTH_SHORT).show();
                    break;
                }*/
                case 2:{//提供学号删除学生
                    studel = (LinearLayout) getLayoutInflater().inflate(R.layout.student_dialogdel, null);
                    dialog.setTitle("删除记录").setView(studel);
                    dialog.setPositiveButton("确定", new StudentManageFragment.delClick());
                    dialog.setNegativeButton("取消", null);
                    dialog.create();
                    dialog.show();
                    break;
                }
                case 1: {//按学号修改学生
                    studel = (LinearLayout) getLayoutInflater().inflate(R.layout.student_dialogmod, null);
                    dialog.setTitle("修改学生信息").setView(studel);
                    dialog.setPositiveButton("确定", new StudentManageFragment.stumodClick());
                    dialog.setNegativeButton("取消", null);
                    dialog.create();
                    dialog.show();
                    break;
                }
                /*case 0: {//提供按学号查找学生
                    studel = (LinearLayout) getLayoutInflater().inflate(R.layout.student_dialogsearch, null);
                    dialog.setTitle("查找学生").setView(studel);
                    dialog.setPositiveButton("确定", new StudentManageFragment.stunoClick());
                    dialog.setNegativeButton("取消", null);
                    dialog.create();
                    dialog.show();
                    break;
                }*/
                case 3: {//删除所有学生
                    studel = (LinearLayout) getLayoutInflater().inflate(R.layout.student_dialogdelall, null);
                    dialog.setTitle("删除全部记录").setView(studel);
                    dialog.setPositiveButton("确定", new StudentManageFragment.delallClick());
                    dialog.setNegativeButton("取消", null);
                    dialog.create();
                    dialog.show();
                    break;
                }
                case 0: {//跳转到显示所有学生界面
                    Context context = getActivity();
                    startActivity(new Intent(context, StudentShow.class));
                    break;
                }
                case 4:
                {
                    stuCtrl.saveAll();
                    Toast.makeText(getContext(),"数据导入完成",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

    //删除全部学生对话框“确定”按钮事件
    class delallClick implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog,int which)
        {
            stuCtrl.deleteAll();
            Context context = getActivity();
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }


    /*  删除学生记录对话框的“确定”按钮事件   */
    class delClick implements  DialogInterface.OnClickListener
    {
        EditText txt;
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            txt = (EditText)studel.findViewById(R.id.stuno);
            //取出输入编辑框的值
            String na=txt.getText().toString();
            Student s[] =stuCtrl.queryStudent(na) ;
            if (s != null)
            {

                Context context = getActivity();
                new AlertDialog.Builder(context)
                        .setTitle("确实要删除这名学生吗？")
                        .setMessage("学号："+ s[0].getNo() + "\n" +
                                "姓名："+ s[0].getName() + "\n" +
                                "专业："+ s[0].getMajor() + "\n" +
                                "班级："+ s[0].getClasses() + "\n" +
                                "手机号码："+ s[0].getPhone())
                        .setPositiveButton("确定",new StudentManageFragment.delClickyes(na)).setNegativeButton("取消",null)
                        .show();
            }
            else
                Toast.makeText(getContext(),"没有符合条件的记录", Toast.LENGTH_SHORT).show();
            //dialog.dismiss();
        }
    }
    /*  确认删除学生记录对话框的“确定”按钮事件   */
    class delClickyes implements DialogInterface.OnClickListener
    {
        private String na;
        private delClickyes(String na)
        {
            this.na=na;
        }
        @Override
        public void onClick(DialogInterface dialog,int which)
        {
            stuCtrl.deleteStudent(na);
            Context context = getActivity();
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            //dialog.dismiss();
        }
    }

    /*  按学号修改查找学生记录对话框的“确定”按钮事件   */
    class stumodClick implements  DialogInterface.OnClickListener
    {
        EditText txt;
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            txt = (EditText)studel.findViewById(R.id.stuno);
            //取出输入编辑框的值
            String na=txt.getText().toString();
            Student s[] = stuCtrl.queryStudent(na);
            if (s != null){
                Intent intent = new Intent();
                Context context = getActivity();
                intent.setClass(context, StudentUpdate.class);
                Bundle bundle1 = new Bundle();            //创建Bundle 对象
                bundle1.putString("stuno", na);// 把编辑框中的字符串作为传递值，键名为stuno
                bundle1.putString("source","StudentManageFragment");
                intent.putExtras(bundle1);
                startActivity(intent);
            }
            else
                Toast.makeText(getContext(),"没有符合条件的记录。", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }
    /*  按学号查找学生记录对话框的“确定”按钮事件   */
    /*class stunoClick implements  DialogInterface.OnClickListener
    {
        EditText txt;
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            txt = (EditText)studel.findViewById(R.id.stuno);
            //取出输入编辑框的值
            String na=txt.getText().toString();
            Student s[] = stuCtrl.queryStudent(na);
            if (s != null)
                new AlertDialog.Builder(getActivity())
                        .setTitle("该学生信息：")
                        .setMessage(s[0].getNo() + "\n" +
                                s[0].getName() + "\n" +
                                s[0].getMajor() + "\n" +
                                s[0].getClasses() + "\n" +
                                s[0].getPhone())
                        .setPositiveButton("确定",null).show();
            else
                Toast.makeText(getContext(),"没有与搜索条件匹配的项。", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }*/
}

