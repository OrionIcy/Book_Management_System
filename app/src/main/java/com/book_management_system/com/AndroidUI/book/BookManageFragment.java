package com.book_management_system.com.AndroidUI.book;

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
import com.book_management_system.com.AndroidUI.book.BookShow;
import com.book_management_system.com.control.BookControl;
import com.book_management_system.com.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookManageFragment extends Fragment
{
    private EditText edit;
    LinearLayout bookdel;
    private BookControl bookCtrl;
    private static final String[] options={"显示所有图书","修改图书信息","删除图书","删除所有图书","导入图书数据"};
    private List<String> contentList;
    private ListView mListView;
   @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Context context=getActivity();
        bookCtrl=new BookControl(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_book_manage, container, false);
        mListView=(ListView)view.findViewById(R.id.ListView02);
        contentList=new ArrayList<String>();
        for(int i=0;i<options.length;i++)
            contentList.add(options[i]);
        Context contextop=getActivity();
        mListView.setAdapter(new ArrayAdapter<String>(contextop,android.R.layout.simple_list_item_1,options));
        mListView.setOnItemClickListener(new onClick1());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    public class onClick1 implements OnItemClickListener
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
                case 2:{//提供书号删除图书
                    bookdel = (LinearLayout) getLayoutInflater().inflate(R.layout.book_dialogdel, null);
                    dialog.setTitle("删除记录").setView(bookdel);
                    dialog.setPositiveButton("确定", new BookManageFragment.delClick());
                    dialog.setNegativeButton("取消", null);
                    dialog.create();
                    dialog.show();
                    break;
                }
                case 1: {//按书号修改图书信息
                    bookdel = (LinearLayout) getLayoutInflater().inflate(R.layout.book_dialogmod, null);
                    dialog.setTitle("修改图书信息").setView(bookdel);
                    dialog.setPositiveButton("确定", new BookManageFragment.bookmodClick());
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
                case 3: {//删除所有图书
                    bookdel = (LinearLayout) getLayoutInflater().inflate(R.layout.book_dialogdelall, null);
                    dialog.setTitle("删除全部记录").setView(bookdel);
                    dialog.setPositiveButton("确定", new BookManageFragment.delallClick());
                    dialog.setNegativeButton("取消", null);
                    dialog.create();
                    dialog.show();
                    break;
                }
                case 0: {//跳转到显示所有图书界面
                    Context context = getActivity();
                    Bundle bundle=new Bundle();
                    bundle.putString("source","showall");
                    Intent intent=new Intent(context,BookShow.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    //Toast.makeText(context,"switch监听成功",Toast.LENGTH_SHORT).show();
                    break;
                }
                case -1: {//跳转到显示可借阅图书界面
                    Context context = getActivity();
                    Bundle bundle=new Bundle();
                    bundle.putString("source","borrowable");
                    Intent intent=new Intent(context,BookShow.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    //Toast.makeText(context,"switch监听成功",Toast.LENGTH_SHORT).show();
                    break;}
                case -2: {//跳转到显示不可借阅图书界面
                    Context context = getActivity();
                    Bundle bundle=new Bundle();
                    bundle.putString("source","unborrowable");
                    Intent intent=new Intent(context,BookShow.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    //Toast.makeText(context,"switch监听成功",Toast.LENGTH_SHORT).show();
                    break;}
                case 4:
                {
                    bookCtrl.saveAll();
                    Toast.makeText(getContext(),"数据导入完成",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

    //删除全部图书对话框“确定”按钮事件
    class delallClick implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog,int which)
        {
            bookCtrl.deleteAll();
            Context context = getActivity();
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }


    /*  删除图书记录对话框的“确定”按钮事件   */
    class delClick implements  DialogInterface.OnClickListener
    {
        EditText txt;
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            txt = (EditText)bookdel.findViewById(R.id.bookno);
            //取出输入编辑框的值
            String na=txt.getText().toString();
            Book b[] =bookCtrl.queryBook(na) ;
            if (b != null)
            {

                Context context = getActivity();
                new AlertDialog.Builder(context)
                        .setTitle("确实要删除此图书吗？")
                        .setMessage("书号："+ b[0].getNo() + "\n" +
                                "书名："+ b[0].getName() + "\n" +
                                "作者："+ b[0].getAuthor() + "\n" +
                                "出版社："+ b[0].getPublisher() + "\n" +
                                "出版时间："+ b[0].getYear()+"年"+b[0].getMonth()+"月"+b[0].getDay()+"日"+"\n"+
                                "馆藏可借阅总量："+b[0].getTotalnum()+"\n"+
                                "已借出数量："+b[0].getBorrownum())
                        .setPositiveButton("确定",new BookManageFragment.delClickyes(na)).setNegativeButton("取消",null)
                        .show();
            }
            else
                Toast.makeText(getContext(),"没有符合条件的记录", Toast.LENGTH_SHORT).show();
            //dialog.dismiss();
        }
    }
    /*  确认删除图书记录对话框的“确定”按钮事件   */
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
            bookCtrl.deleteBook(na);
            Context context = getActivity();
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            //dialog.dismiss();
        }
    }

    /*  按书号修改查找图书记录对话框的“确定”按钮事件   */
    class bookmodClick implements  DialogInterface.OnClickListener
    {
        EditText txt;
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            txt = (EditText)bookdel.findViewById(R.id.bookno);
            //取出输入编辑框的值
            String na=txt.getText().toString();
            Book b[] = bookCtrl.queryBook(na);
            if (b != null){
                Intent intent = new Intent();
                Context context = getActivity();
                intent.setClass(context, BookUpdate.class);
                Bundle bundle1 = new Bundle();            //创建Bundle 对象
                bundle1.putString("bookno", na);// 把编辑框中的字符串作为传递值，键名为bookno
                bundle1.putString("source","StudentManageFragment");//标记是从这个Fragment跳转到的修改，而不是“搜索结果”界面或者“所有图书”界面
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

