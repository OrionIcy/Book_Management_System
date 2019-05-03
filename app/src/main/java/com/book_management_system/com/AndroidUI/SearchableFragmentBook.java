package com.book_management_system.com.AndroidUI;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface.OnClickListener;

import com.book_management_system.R;
import com.book_management_system.com.AndroidUI.book.BookUpdate;
import com.book_management_system.com.control.BookControl;
import com.book_management_system.com.control.BorrowingControl;
import com.book_management_system.com.control.StudentControl;
import com.book_management_system.com.model.Book;
import com.book_management_system.com.model.Borrowing;
import com.book_management_system.com.model.Student;

import net.sourceforge.pinyin4j.PinyinHelper;
import java.util.*;

public class SearchableFragmentBook extends Fragment {
    private Bundle bundle2;
    private String info;
    private View view;
    private BookControl bookCtrl;
    private StudentControl stuCtrl;
    private BorrowingControl borrCtrl;
    private LinearLayout borrDialogLayout;
    static public SimpleAdapter adapter1;
    static public ArrayList<HashMap<String, Object>> data1;
    private int booknum;
    //private List<String> tabTitle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookCtrl = new BookControl(getActivity());
        stuCtrl=new StudentControl(getActivity());
        borrCtrl=new BorrowingControl(getActivity());
        setHasOptionsMenu(true);//让Fregment接受onCreateOptionsMenu(),否则无法添加选项菜单
        data1=new ArrayList<HashMap<String, Object>>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.activity_search_bookfragment,container,false);
        ListView lv = (ListView) view.findViewById(R.id.lv1);
        //获取到集合数据

        bundle2=getActivity().getIntent().getExtras();
        info=bundle2.getString("info");//提取从MainActivity中传过来的数据
        Book[] b = SearchableActivity.b;
        if (b != null) {
            //getSupportActionBar().setTitle("「"+info+"」: "+s.length+"个搜索结果");
            booknum=b.length;//提取搜索结果数量
            SearchableActivity.tabTitle[SearchableActivity.bookflag]="图书："+booknum+" 个结果";
            SearchableActivity.tabAdapter.notifyDataSetChanged();

            for (int i = 0; i < b.length; i++) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("no", b[i].getNo());
                item.put("name", b[i].getName());
                item.put("author", b[i].getAuthor());
                item.put("publisher", b[i].getPublisher());
                item.put("date", b[i].getYear()+"."+b[i].getMonth()+"."+b[i].getDay());
                data1.add(item);
            }
            //创建SimpleAdapter适配器将数据绑定到item显示控件上
            adapter1 = new SimpleAdapter(getActivity(), data1, R.layout.book_listview,
                    new String[]{"no", "name", "author", "publisher", "date"},
                    new int[]{R.id.tv_No, R.id.tv_Name, R.id.tv_Author, R.id.tv_Publisher, R.id.tv_Date});
            //实现列表的显示
            lv.setAdapter(adapter1);
            lv.setOnItemClickListener(new onClickShowResult());
            lv.setOnItemLongClickListener(new LongClickToDelete());//长按删除
        }
        else
        {
            booknum=0;
            SearchableActivity.tabTitle[SearchableActivity.bookflag]="图书："+booknum+" 个结果";
            SearchableActivity.tabAdapter.notifyDataSetChanged();

            TextView none1 = (TextView)view.findViewById(R.id.no);
            none1.setText("\n\n\n\n\n\n\n\n                  没有与搜索条件匹配的项。");
            none1.setTextSize(18);
            TextView none2 = (TextView)view.findViewById(R.id.name);
            none2.setText("");
            TextView none3 = (TextView)view.findViewById(R.id.author);
            none3.setText("");
            TextView none4 = (TextView)view.findViewById(R.id.publisher);
            none4.setText("");
            TextView none5 = (TextView)view.findViewById(R.id.date);
            none5.setText("");
        }
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    public class LongClickToDelete implements AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0,View v,int position,long id)
        {
            TextView no=(TextView)v.findViewById(R.id.tv_No);
            TextView name=(TextView)v.findViewById(R.id.tv_Name);
            TextView author=(TextView)v.findViewById(R.id.tv_Author);
            TextView publisher=(TextView)v.findViewById(R.id.tv_Publisher);
            TextView date=(TextView)v.findViewById(R.id.tv_Date);
            String[] d=date.getText().toString().split("\\.");//注意这里的“.”要加上转义符\\
            int borrownum=bookCtrl.queryBook(no.getText().toString())[0].getBorrownum();
            int totalnum=bookCtrl.queryBook(no.getText().toString())[0].getTotalnum();
            if(borrownum==0) {
                new AlertDialog.Builder(getActivity())//删除确认对话框
                        .setTitle("确实要删除此图书吗？")
                        .setMessage("书号：" + no.getText() + "\n" +
                                "书名：" + name.getText() + "\n" +
                                "作者：" + author.getText() + "\n" +
                                "出版社：" + publisher.getText() + "\n" +
                                "出版日期：" + date.getText().toString().split("\\.")[0] + "年" + date.getText().toString().split("\\.")[1] + "月" + date.getText().toString().split("\\.")[2] + "日" + "\n" +
                                "馆藏可借阅总量：" + totalnum + "\n" +
                                "已借出数量：" + borrownum)
                        .setPositiveButton("确定", new deleteOK(no)).setNegativeButton("取消", null)
                        .show();
                return true;
            }
            else {
                new AlertDialog.Builder(getActivity()).setTitle("不可删除图书：此图书仍有副本外借中").setPositiveButton("确定", null).show();
                return true;
            }
        }
    }

    //点击item时候的监听
    public class onClickShowResult implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int arg1, long arg2)
        {
            //Toast.makeText(StudentShow.this,"监听成功", Toast.LENGTH_SHORT).show();
            TextView no=(TextView)v.findViewById(R.id.tv_No);
            TextView name=(TextView)v.findViewById(R.id.tv_Name);
            TextView author=(TextView)v.findViewById(R.id.tv_Author);
            TextView publisher=(TextView)v.findViewById(R.id.tv_Publisher);
            TextView date=(TextView)v.findViewById(R.id.tv_Date);
            String[] d=date.getText().toString().split("\\.");//注意这里的“.”要加上转义符\\
            int borrownum=bookCtrl.queryBook(no.getText().toString())[0].getBorrownum();
            int totalnum=bookCtrl.queryBook(no.getText().toString())[0].getTotalnum();
            AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
            dialog.setTitle("详细信息");
            if(totalnum-borrownum>0)
                dialog.setMessage("书号：" + no.getText() + "\n" +
                        "书名：" + name.getText() + "\n" +
                        "作者：" + author.getText() + "\n" +
                        "出版社：" + publisher.getText() + "\n" +
                        "出版日期：" + d[0] + "年" + d[1] + "月" + d[2] + "日" + "\n" +
                        "馆藏可借阅总量：" + totalnum + "\n" +
                        "已借出数量：" + borrownum + "\n" +
                        "\n注意，“馆藏可借阅总量”为0时,表示仅剩不可外借的原本，只能在图书馆内阅读。")
                        .setPositiveButton("借阅", new applyForBorrowing(no, name, author, publisher, date)).setNegativeButton("更多操作", new other(no,name,author,publisher,date));
            else
                dialog.setMessage("书号：" + no.getText() + "\n" +
                        "书名：" + name.getText() + "\n" +
                        "作者：" + author.getText() + "\n" +
                        "出版社：" + publisher.getText() + "\n" +
                        "出版日期：" + d[0] + "年" + d[1] + "月" + d[2] + "日" + "\n" +
                        "馆藏可借阅总量：" + totalnum + "\n" +
                        "已借出数量：" + borrownum + "\n" +
                        "\n注意，“馆藏可借阅总量”为0时,表示仅剩不可外借的原本，只能在图书馆内阅读。")
                        .setPositiveButton("更多操作", new other(no,name,author,publisher,date));
            dialog.show();
        }
    }

    //“借阅”按钮监听事件
    public class applyForBorrowing implements OnClickListener
    {
        private TextView no;
        private TextView name;
        private TextView author;
        private TextView publisher;
        private TextView date;
        private applyForBorrowing(TextView no,TextView name,TextView author,TextView publisher,TextView date)
        {
            this.no=no;
            this.name=name;
            this.author=author;
            this.publisher=publisher;
            this.date=date;
        }

        public void onClick(DialogInterface dialog,int which)
        {
            borrDialogLayout=(LinearLayout)getLayoutInflater().inflate(R.layout.borrowing_dialog_inshow,null);
            new AlertDialog.Builder(getActivity())//借阅对话框
                    .setView(borrDialogLayout).setTitle("借阅:《"+name.getText()+"》"+"("+no.getText()+")")
                    //.setMessage("请输入学号：")
                    .setPositiveButton("确定",new borrowingVerify(no, name, author, publisher, date)).setNegativeButton("取消",null)
                    .show();
        }
    }

    //“借阅”对话框中“确定”按钮的监听事件
    public class borrowingVerify implements OnClickListener
    {
        private TextView no;
        private TextView name;
        private TextView author;
        private TextView publisher;
        private TextView date;
        private EditText stuno;
        private borrowingVerify(TextView no,TextView name,TextView author,TextView publisher,TextView date)
        {
            this.no=no;
            this.name=name;
            this.author=author;
            this.publisher=publisher;
            this.date=date;
        }
        @Override
        public void onClick(DialogInterface dialog,int which)
        {
            stuno=(EditText)borrDialogLayout.findViewById(R.id.borrstuno);
            Student[] s=stuCtrl.queryStudent(stuno.getText().toString());
            int n=borrCtrl.borrowingNum(stuno.getText().toString());
            if(s!=null)
            {
                if(n<10)
                    new AlertDialog.Builder(getActivity())//删除确认对话框
                            .setTitle("确实要借阅此图书吗？")
                            .setMessage("学号：" + s[0].getNo() + "\n" +
                                    "姓名：" + s[0].getName() + "\n" +
                                    "专业：" + s[0].getMajor() + "\n\n" +
                                    "书号：" + no.getText() + "\n" +
                                    "书名：" + name.getText() + "\n" +
                                    "作者：" + author.getText() + "\n" +
                                    "出版社：" + publisher.getText() + "\n" +
                                    "出版日期：" + date.getText().toString().split("\\.")[0] + "年" + date.getText().toString().split("\\.")[1] + "月" + date.getText().toString().split("\\.")[2] + "日" + "\n")
                            .setPositiveButton("确定", new borrow(s[0].getNo(), no.getText().toString())).setNegativeButton("取消", null)
                            .show();
                else
                    new AlertDialog.Builder(getActivity()).setTitle("借阅失败：超出借书数量限制")
                            .setMessage("每个人最多借阅10本图书。请归还一些图书，然后再试一次。").setPositiveButton("确定",null).show();
            }
            else {
                Toast.makeText(getActivity(), "找不到此学号", Toast.LENGTH_LONG).show();
                borrDialogLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.borrowing_dialog_inshow, null);
                new AlertDialog.Builder(getActivity())//借阅对话框
                        .setView(borrDialogLayout).setTitle("借阅:《" + name.getText() + "》" + "(" + no.getText() + ")")
                        //.setMessage("请输入学号：")
                        .setPositiveButton("确定", new borrowingVerify(no, name, author, publisher, date)).setNegativeButton("取消", null)
                        .show();
            }
        }
    }

    public class borrow implements OnClickListener
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

    //“其他操作”按钮监听事件
    public class other implements OnClickListener
    {
        private TextView no;
        private TextView name;
        private TextView author;
        private TextView publisher;
        private TextView date;
        private other(TextView no,TextView name,TextView author,TextView publisher,TextView date)
        {
            this.no=no;
            this.name=name;
            this.author=author;
            this.publisher=publisher;
            this.date=date;
        }

        public void onClick(DialogInterface dialog,int which)
        {
            String[] d=date.getText().toString().split("\\.");
            new AlertDialog.Builder(getActivity())
                    .setTitle("其他操作")
                    .setMessage("书号："+no.getText()+"\n"+
                            "书名："+name.getText()+"\n"+
                            "作者："+author.getText()+"\n"+
                            "出版社："+publisher.getText()+"\n"+
                            "出版日期："+d[0]+"年"+d[1]+"月"+d[2]+"日"+"\n"+
                            "馆藏可借阅总量："+bookCtrl.queryBook(no.getText().toString())[0].getTotalnum()+"\n"+
                            "已借出数量："+bookCtrl.queryBook(no.getText().toString())[0].getBorrownum())
                    .setNegativeButton("修改信息",new modify(no)).setPositiveButton("删除",new delete(no,name,author,publisher,date)).show();
        }
    }


    //“删除”按钮的监听事件
    public class delete implements DialogInterface.OnClickListener
    {
        private TextView no;
        private TextView name;
        private TextView author;
        private TextView publisher;
        private TextView date;
        private delete(TextView no,TextView name,TextView author,TextView publisher,TextView date)
        {
            this.no=no;
            this.name=name;
            this.author=author;
            this.publisher=publisher;
            this.date=date;
        }
        public void onClick(DialogInterface dialog,int which)
        {
            int borrownum=bookCtrl.queryBook(no.getText().toString())[0].getBorrownum();
            int totalnum=bookCtrl.queryBook(no.getText().toString())[0].getTotalnum();
            if(borrownum==0)
                new AlertDialog.Builder(getActivity())//删除确认对话框
                    .setTitle("确实要删除此图书吗？")
                    .setMessage("书号："+ no.getText() + "\n" +
                            "书名："+ name.getText() + "\n" +
                            "作者："+ author.getText() + "\n" +
                            "出版社："+ publisher.getText() + "\n" +
                            "出版日期："+ date.getText().toString().split("\\.")[0]+"年"+date.getText().toString().split("\\.")[1]+"月"+date.getText().toString().split("\\.")[2]+"日" +"\n"+
                            "馆藏可借阅总量："+totalnum+"\n"+
                            "已借出数量："+borrownum)
                    .setPositiveButton("确定",new deleteOK(no)).setNegativeButton("取消",null)
                    .show();
            else
                new AlertDialog.Builder(getActivity()).setTitle("此图书仍有副本外借中").setPositiveButton("确定",null);
        }
    }

    //删除确认对话框“确定”按钮监听事件
    public class deleteOK implements DialogInterface.OnClickListener
    {
        private TextView no;
        private deleteOK(TextView no)
        {
            this.no=no;
        }
        public void onClick(DialogInterface dialog,int which)
        {
            bookCtrl.deleteBook(no.getText().toString());
            TextView no=(TextView)view.findViewById(R.id.no);
            TextView name=(TextView)view.findViewById(R.id.name);
            TextView author=(TextView)view.findViewById(R.id.author);
            TextView publisher=(TextView)view.findViewById(R.id.publisher);
            TextView date=(TextView)view.findViewById(R.id.date);
            Book b[] = bookCtrl.searchBook(info);
            if(b==null)
            {
                booknum=0;
                //SearchableActivity.tabTitle.set(0,"0");
                SearchableActivity.tabTitle[SearchableActivity.bookflag]="图书："+booknum+" 个结果";
                SearchableActivity.tabAdapter.notifyDataSetChanged();
                no.setText("\n\n\n\n\n\n\n\n                  没有与搜索条件匹配的项。");
                no.setTextSize(18);
                author.setText("");
                publisher.setText("");
                date.setText("");
                name.setText("");
                data1.clear();
                adapter1.notifyDataSetChanged();
                Toast.makeText(getContext(),"删除成功...",Toast.LENGTH_LONG).show();
            }
            else{
                booknum=b.length;
                SearchableActivity.tabTitle[SearchableActivity.bookflag]="图书："+booknum+" 个结果";
                ArrayList<HashMap<String, Object>> data2= new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < b.length; i++) {
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("no", b[i].getNo());
                    item.put("name", b[i].getName());
                    item.put("author", b[i].getAuthor());
                    item.put("publisher", b[i].getPublisher());
                    item.put("date", b[i].getYear()+"."+b[i].getMonth()+"."+b[i].getDay());
                    data2.add(item);
                }
                data1.clear();
                data1.addAll(data2);
                adapter1.notifyDataSetChanged();
                Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_LONG).show();
            }
        }
    }

    //“修改信息”按钮监听事件
    public class modify implements DialogInterface.OnClickListener
    {
        private TextView no;
        private String no1;
        modify(TextView no)
        {
            this.no=no;
        }

        @Override
        public void onClick(DialogInterface dialog,int which)
        {
            no1=no.getText().toString();
            Intent intent=new Intent();
            intent.setClass(getActivity(),BookUpdate.class);
            Bundle bundle1=new Bundle();
            bundle1.putString("source","SearchableActivity");
            bundle1.putString("bookno",no1);
            bundle1.putString("info",info);
            intent.putExtras(bundle1);
            startActivity(intent);
        }
    }

    //排序选项菜单
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {//注意这里，和Activity中相比，要增加一个参数inflater，因为在Fregment中无法调用getMenuInflater()函数，必须在参数表中声明
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.activity_search_resultbook_sort,menu);//使用参数表中的inflater参数而不是调用getMenuInflater()函数
    }

    private String key;
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.nosort:
                key="no";
                Collections.sort(data1,new LNComparator(key));
                adapter1.notifyDataSetChanged();
                break;
            case R.id.autsort:
                key="author";
                Collections.sort(data1,new PYComparator(key));
                adapter1.notifyDataSetChanged();
                break;
            case R.id.datesort:
                key="date";
                Collections.sort(data1,new DateComparator(key));
                adapter1.notifyDataSetChanged();
                break;
            case R.id.pubsort:
                key="publisher";
                Collections.sort(data1,new PYComparator(key));
                adapter1.notifyDataSetChanged();
                break;
            case R.id.namesort:
                key="name";
                Collections.sort(data1,new PYComparator(key));
                adapter1.notifyDataSetChanged();
                break;
            case R.id.action_search_again:
                getActivity().finish();
                break;
        }
        return true;
    }

    //字母和数字排序比较器
    public class LNComparator implements Comparator<HashMap<String, Object>>
    {
        private String key;
        private LNComparator(String key)
        {
            this.key=key;
        }
        @Override
        public int compare(HashMap<String, Object> item1,HashMap<String, Object> item2)
        {
            return item1.get(key).toString().compareTo(item2.get(key).toString());
        }
    }

    //拼音排序比较器
    public class PYComparator implements Comparator<HashMap<String,Object>>
    {
        private String key;
        private PYComparator(String key)
        {
            this.key=key;
        }
        private String ToPinYinString(String str)
        {
            StringBuilder sb=new StringBuilder();
            String[] arr=null;
            for(int i=0;i<str.length();i++)
            {
                arr=PinyinHelper.toHanyuPinyinStringArray(str.charAt(i));//将汉字转成拼音
                if(arr!=null && arr.length>0)
                    for(String string:arr)
                        sb.append(string);
            }
            return sb.toString();
        }
        @Override
        public int compare(HashMap<String,Object> item1,HashMap<String,Object> item2)
        {
            return ToPinYinString(item1.get(key).toString()).compareTo(ToPinYinString(item2.get(key).toString()));
        }
    }

    //日期排序比较器
    public class DateComparator implements Comparator<HashMap<String,Object>>
    {
        private String key;
        private DateComparator(String key)
        {
            this.key=key;
        }
        public int compare (HashMap<String,Object> item1,HashMap<String,Object> item2)
        {
            String[] d1=item1.get(key).toString().split("\\.");
            String[] d2=item2.get(key).toString().split("\\.");
            int date1=Integer.parseInt(d1[0])*10000+Integer.parseInt(d1[1])*100+Integer.parseInt(d1[2]);
            int date2=Integer.parseInt(d2[0])*10000+Integer.parseInt(d2[1])*100+Integer.parseInt(d2[2]);
            return date1-date2;
        }
    }
}

