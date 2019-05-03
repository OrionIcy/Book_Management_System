package com.book_management_system.com.AndroidUI.borrowing;

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
import android.widget.TextView;
import android.widget.Toast;

import com.book_management_system.R;
import com.book_management_system.com.AndroidUI.SearchableActivity;
import com.book_management_system.com.control.BorrowingControl;
import com.book_management_system.com.control.BookControl;
import com.book_management_system.com.control.StudentControl;
import com.book_management_system.com.model.Book;
import com.book_management_system.com.model.Borrowing;
import com.book_management_system.com.model.Student;

import java.util.ArrayList;
import java.util.List;

public class BorrowingManageFragment extends Fragment
{
    private EditText edit;
    private BorrowingControl borrCtrl;
    private BookControl bookCtrl;
    private StudentControl stuCtrl;
    private LinearLayout borrDialogLayout;
    private static final String[] options = {"借阅", "还书", "显示所有图书"/*, "显示所有可借阅图书", "显示所有不可借阅图书"*/};
    private List<String> contentList;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getActivity();
        borrCtrl = new BorrowingControl(context);
        bookCtrl=new BookControl(context);
        stuCtrl=new StudentControl(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_borrowing_manage, container, false);
        mListView = (ListView) view.findViewById(R.id.ListView03);
        contentList = new ArrayList<String>();
        for (int i = 0; i < options.length; i++)
            contentList.add(options[i]);
        Context contextop = getActivity();
        mListView.setAdapter(new ArrayAdapter<String>(contextop, android.R.layout.simple_list_item_1, options));
        mListView.setOnItemClickListener(new onClick());
        return view;
    }

    public class onClick implements OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> arg0,View v,int position,long id)
        {
            AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
            switch(position)
            {
                case 0:
                    borrDialogLayout=(LinearLayout)getLayoutInflater().inflate(R.layout.borrowing_dialog,null);
                    dialog.setView(borrDialogLayout).setTitle("借阅：请输入学号和书号")
                            .setPositiveButton("确定",new borrowingVerify()).setNegativeButton("取消",null).create().show();
                    break;
                case 1:
                    borrDialogLayout=(LinearLayout)getLayoutInflater().inflate(R.layout.borrowing_dialog,null);
                    dialog.setView(borrDialogLayout).setTitle("还书：请输入学号和书号")
                            .setPositiveButton("确定",new returningVerify()).setNegativeButton("取消",null).create().show();
                    break;
                case 2: {
                    Context context = getActivity();
                    Bundle bundle = new Bundle();
                    bundle.putString("source", "showall");
                    Intent intent = new Intent(context, BorrowingShow.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                }
                case 3:{
                    Context context = getActivity();
                    Bundle bundle=new Bundle();
                    bundle.putString("source","borrowable");
                    Intent intent=new Intent(context,BorrowingShow.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                }
                case 4: {
                    Context context = getActivity();
                    Bundle bundle=new Bundle();
                    bundle.putString("source","unborrowable");
                    Intent intent=new Intent(context,BorrowingShow.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                }
            }
        }
    }

    //“确定”按钮的监听事件
    public class borrowingVerify implements DialogInterface.OnClickListener
    {
        private EditText bookno;
        private EditText stuno;
        @Override
        public void onClick(DialogInterface dialog,int which)
        {
            stuno=(EditText)borrDialogLayout.findViewById(R.id.borrstuno);
            bookno=(EditText)borrDialogLayout.findViewById(R.id.borrbookno);
            Student[] s=stuCtrl.queryStudent(stuno.getText().toString());
            Book[] b=bookCtrl.queryBook(bookno.getText().toString());
            int m=borrCtrl.borrowingNum(stuno.getText().toString());
            int n=bookCtrl.getRemaining(bookno.getText().toString());
            if(stuno.getText().toString().equals("")||bookno.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "请填写完整", Toast.LENGTH_LONG).show();
                borrDialogLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.borrowing_dialog, null);
                new AlertDialog.Builder(getActivity()).setTitle("借阅：请输入学号和书号").setView(borrDialogLayout)
                        .setPositiveButton("确定",new borrowingVerify()).setNegativeButton("取消",null).create().show();
            }
            else if(s!=null&&b!=null)
            {
                if(m<10&&n!=0)
                    new AlertDialog.Builder(getActivity())
                        .setTitle("确实要借阅此图书吗？")
                        .setMessage("学号：" + s[0].getNo() + "\n" +
                                "姓名：" + s[0].getName() + "\n" +
                                "专业：" + s[0].getMajor() + "\n\n" +
                                "书号：" + b[0].getNo() + "\n" +
                                "书名：" + b[0].getName() + "\n" +
                                "作者：" + b[0].getAuthor() + "\n" +
                                "出版社：" + b[0].getPublisher() + "\n" +
                                "出版日期：" + b[0].getYear() + "年" + b[0].getMonth() + "月" + b[0].getDay() + "日" + "\n")
                        .setPositiveButton("确定", new borrow(s[0].getNo(), b[0].getNo())).setNegativeButton("取消", null)
                        .show();
                else if(m==10&&n!=0)
                    new AlertDialog.Builder(getActivity()).setTitle("借阅失败：超出借书数量限制")
                            .setMessage("每个人最多借阅10本图书。请归还一些图书，然后再试一次。").setPositiveButton("确定",null).show();
                else if(m<10&&n==0)
                    new AlertDialog.Builder(getActivity()).setTitle("借阅失败：不可借阅图书")
                            .setMessage("此图书已经没有可借阅的副本。").setPositiveButton("确定",null).show();
                else
                    new AlertDialog.Builder(getActivity()).setTitle("借阅失败：该图书现在不可借阅且你已达到借书数量限制")
                            .setMessage("请归还一些图书并且等待一段时间，然后再试一次。").setPositiveButton("确定",null).show();
            }
            else {
                Toast.makeText(getActivity(), "找不到学号或书号", Toast.LENGTH_LONG).show();
                borrDialogLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.borrowing_dialog, null);
                new AlertDialog.Builder(getActivity()).setTitle("借阅：请输入学号和书号").setView(borrDialogLayout)
                        .setPositiveButton("确定",new borrowingVerify()).setNegativeButton("取消",null).create().show();
            }
        }
    }

    //执行借阅操作
    public class borrow implements DialogInterface.OnClickListener
    {
        private String stuno,bookno;
        private borrow(String stuno,String bookno)
        {
            this.stuno=stuno;
            this.bookno=bookno;
        }
        public void onClick(DialogInterface dialog,int which)
        {
            borrCtrl.borrowBook(stuno,bookno);
            Toast.makeText(getActivity(),"借阅成功",Toast.LENGTH_LONG).show();
        }
    }

    public class returningVerify implements DialogInterface.OnClickListener
    {
        private EditText bookno;
        private EditText stuno;
        @Override
        public void onClick(DialogInterface dialog,int which)
        {
            stuno=(EditText)borrDialogLayout.findViewById(R.id.borrstuno);
            bookno=(EditText)borrDialogLayout.findViewById(R.id.borrbookno);
            String sno=stuno.getText().toString();
            String bno=bookno.getText().toString();
            Student[] s=stuCtrl.queryStudent(sno);
            Book[] b=bookCtrl.queryBook(bno);
            boolean n=borrCtrl.ifBorrowed(sno,bno);
            String time;
            if(sno.equals("")||bno.equals("")) {
                Toast.makeText(getActivity(), "请填写完整", Toast.LENGTH_LONG).show();
                borrDialogLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.borrowing_dialog, null);
                new AlertDialog.Builder(getActivity()).setTitle("借阅：请输入学号和书号").setView(borrDialogLayout)
                        .setPositiveButton("确定", new returningVerify()).setNegativeButton("取消", null).create().show();
            }
            else if(s!=null&&b!=null&&n)
            {
                time=borrCtrl.getBorrowTime(sno,bno);
                new AlertDialog.Builder(getActivity())//删除确认对话框
                        .setTitle("确实要归还此图书吗？")
                        .setMessage("学号："+ s[0].getNo() + "\n" +
                                "姓名："+ s[0].getName() + "\n" +
                                "专业："+ s[0].getMajor() + "\n\n" +
                                "书号："+ b[0].getNo() + "\n" +
                                "书号："+ b[0].getNo() + "\n" +
                                "书名："+ b[0].getName() + "\n" +
                                "作者："+ b[0].getAuthor() + "\n" +
                                "出版社："+ b[0].getPublisher() + "\n" +
                                "出版日期："+ b[0].getYear()+"年"+b[0].getMonth()+"月"+b[0].getDay()+"日" +"\n")
                        .setPositiveButton("确定",new Return(s[0].getNo(),b[0].getNo())).setNegativeButton("取消",null)
                        .show();
            }
            else if(s==null||b==null)
            {
                Toast.makeText(getActivity(), "找不到学号或书号", Toast.LENGTH_LONG).show();
                borrDialogLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.borrowing_dialog, null);
                new AlertDialog.Builder(getActivity()).setTitle("借阅：请输入学号和书号").setView(borrDialogLayout)
                        .setPositiveButton("确定", new returningVerify()).setNegativeButton("取消", null).create().show();
            }
            else
            {
                Toast.makeText(getActivity(), "你没有借阅该图书", Toast.LENGTH_LONG).show();
                borrDialogLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.borrowing_dialog, null);
                new AlertDialog.Builder(getActivity()).setTitle("借阅：请输入学号和书号").setView(borrDialogLayout)
                        .setPositiveButton("确定", new returningVerify()).setNegativeButton("取消", null).create().show();
            }
        }
    }

    //执行归还操作
    public class Return implements DialogInterface.OnClickListener
    {
        private String stuno,bookno;
        private Return(String stuno,String bookno)
        {
            this.stuno=stuno;
            this.bookno=bookno;
        }
        public void onClick(DialogInterface dialog,int which)
        {
            borrCtrl.returnBook(stuno,bookno);
            Toast.makeText(getActivity(),"还书成功",Toast.LENGTH_LONG).show();
        }
    }
}