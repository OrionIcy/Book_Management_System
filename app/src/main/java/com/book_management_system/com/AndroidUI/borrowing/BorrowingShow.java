package com.book_management_system.com.AndroidUI.borrowing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.book_management_system.R;
import com.book_management_system.com.control.BookControl;
import com.book_management_system.com.control.BorrowingControl;
import com.book_management_system.com.control.StudentControl;
import com.book_management_system.com.model.Book;
import com.book_management_system.com.model.Borrowing;
import com.book_management_system.com.model.Student;

import java.util.*;
import net.sourceforge.pinyin4j.PinyinHelper;//需要第三方jar包pinyin4j.jar

public class BorrowingShow extends AppCompatActivity {
    private BookControl bookCtrl;
    private StudentControl stuCtrl;
    private BorrowingControl borrCtrl;
    private LinearLayout borrDialogLayout;
    static public SimpleAdapter adapter;
    static public ArrayList<HashMap<String, Object>> data;
    static int Flag;
    Bundle bundle;
    String SOURCE;
    static public TextView none1 ;
    static public TextView none2 ;
    static public TextView none3 ;
    static public TextView none4 ;
    static public TextView none5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_show);
        ListView lv = (ListView) findViewById(R.id.lv1);
        //获取到集合数据

        none1 = (TextView)findViewById(R.id.no);
        none2 = (TextView)findViewById(R.id.name);
        none3 = (TextView)findViewById(R.id.author);
        none4 = (TextView)findViewById(R.id.publisher);
        none5 = (TextView)findViewById(R.id.date);
        data= new ArrayList<HashMap<String, Object>>();
        bookCtrl = new BookControl(BorrowingShow.this);
        stuCtrl=new StudentControl(BorrowingShow.this);
        borrCtrl=new BorrowingControl(BorrowingShow.this);
        bundle=this.getIntent().getExtras();
        SOURCE=bundle.getString("source");
        Book[] b;
        /*if(SOURCE.equals("borrowable")) {
            getSupportActionBar().setTitle("可借图书");
            b = bookCtrl.queryBorrowable();
            Flag=1;
        }
        else if(SOURCE.equals("unborrowable"))
        {
            getSupportActionBar().setTitle("不可借阅图书");
            b=bookCtrl.queryUnBorrowable();
            Flag=2;
        }
        else
        {
            getSupportActionBar().setTitle("所有图书");
            b = bookCtrl.getAllBooks();
            Flag=0;
        }*/
        getSupportActionBar().setTitle("所有图书");
        b = bookCtrl.getAllBooks();
        if (b != null)
        {
            for (int i = 0; i < b.length; i++) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("no", b[i].getNo());
                item.put("name", b[i].getName());
                item.put("author", b[i].getAuthor());
                item.put("publisher", b[i].getPublisher());
                item.put("date", b[i].getYear() + "." + b[i].getMonth() + "." + b[i].getDay());
                data.add(item);

            }
            //创建SimpleAdapter适配器将数据绑定到item显示控件上
            adapter = new SimpleAdapter(BorrowingShow.this, data, R.layout.book_listview,
                    new String[]{"no", "name", "author", "publisher", "date"},
                    new int[]{R.id.tv_No, R.id.tv_Name, R.id.tv_Author, R.id.tv_Publisher, R.id.tv_Date});
            //实现列表的显示
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new OnClickShowAll());//设置点击item时候的监听
            lv.setOnItemLongClickListener(new LongClickToDelete());//长按删除
        }
        else
        {
            none1.setText("\n\n\n\n\n\n\n\n                               没有数据。");
            none1.setTextSize(18);
            none2.setText("");
            none3.setText("");
            none4.setText("");
            none5.setText("");
        }
    }

    //长按来删除监听
    public class LongClickToDelete implements AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0,View v,int position,long id)
        {
            borrDialogLayout=(LinearLayout)getLayoutInflater().inflate(R.layout.borrowing_dialog_inshow,null);
            TextView no=(TextView)v.findViewById(R.id.tv_No);
            TextView name=(TextView)v.findViewById(R.id.tv_Name);
            TextView author=(TextView)v.findViewById(R.id.tv_Author);
            TextView publisher=(TextView)v.findViewById(R.id.tv_Publisher);
            TextView date=(TextView)v.findViewById(R.id.tv_Date);
            String[] d=date.getText().toString().split("\\.");//注意这里的“.”要加上转义符\\
            int borrownum=bookCtrl.queryBook(no.getText().toString())[0].getBorrownum();
            int totalnum=bookCtrl.queryBook(no.getText().toString())[0].getTotalnum();
            if(totalnum-borrownum!=0)
                new AlertDialog.Builder(BorrowingShow.this)//借阅对话框
                        .setView(borrDialogLayout).setTitle("借阅:《"+name.getText()+"》"+"("+no.getText()+")")
                        //.setMessage("请输入学号：")
                        .setPositiveButton("确定",new borrowingVerify(no, name, author, publisher, date)).setNegativeButton("取消",null)
                        .show();
            else
                new AlertDialog.Builder(BorrowingShow.this).setTitle("不可借阅图书：此图书已经没有可借阅的副本").setPositiveButton("确定",null).show();
            return true;
        }
    }

    //点击item时候的监听
    public class OnClickShowAll implements OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View v,int arg1,long id)
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
            AlertDialog.Builder dialog=new AlertDialog.Builder(BorrowingShow.this);
            dialog.setTitle("详细信息");
            /*if(SOURCE.equals("borrowable"))
                dialog.setMessage("书号："+no.getText()+"\n"+
                        "书名："+name.getText()+"\n"+
                        "作者："+author.getText()+"\n"+
                        "出版社："+publisher.getText()+"\n"+
                        "出版日期："+d[0]+"年"+d[1]+"月"+d[2]+"日"+"\n"+
                        "馆藏可借阅总量："+totalnum+"\n"+
                        "已借出数量："+borrownum)
                        .setNegativeButton("借阅",new applyForBorrowing(no,name,author,publisher,date)).setPositiveButton("确定",null);
            else if(SOURCE.equals("unborrowable"))
                dialog.setMessage("书号："+no.getText()+"\n"+
                        "书名："+name.getText()+"\n"+
                        "作者："+author.getText()+"\n"+
                        "出版社："+publisher.getText()+"\n"+
                        "出版日期："+d[0]+"年"+d[1]+"月"+d[2]+"日"+"\n"+
                        "馆藏可借阅总量："+totalnum+"\n"+
                        "已借出数量："+borrownum+"\n"+
                        "\n注意，“馆藏可借阅总量”为0时,表示仅剩不可外借的原本，只能在图书馆内阅读。")
                        .setPositiveButton("确定",null);
            else {
                if(totalnum-borrownum>0)
                    dialog.setMessage("书号：" + no.getText() + "\n" +
                        "书名：" + name.getText() + "\n" +
                        "作者：" + author.getText() + "\n" +
                        "出版社：" + publisher.getText() + "\n" +
                        "出版日期：" + d[0] + "年" + d[1] + "月" + d[2] + "日" + "\n" +
                        "馆藏可借阅总量：" + totalnum + "\n" +
                        "已借出数量：" + borrownum + "\n" +
                        "\n注意，“馆藏可借阅总量”为0时,表示仅剩不可外借的原本，只能在图书馆内阅读。")
                        .setNegativeButton("借阅", new applyForBorrowing(no, name, author, publisher, date)).setPositiveButton("确定", null);
                else
                    dialog.setMessage("书号：" + no.getText() + "\n" +
                            "书名：" + name.getText() + "\n" +
                            "作者：" + author.getText() + "\n" +
                            "出版社：" + publisher.getText() + "\n" +
                            "出版日期：" + d[0] + "年" + d[1] + "月" + d[2] + "日" + "\n" +
                            "馆藏可借阅总量：" + totalnum + "\n" +
                            "已借出数量：" + borrownum + "\n" +
                            "\n注意，“馆藏可借阅总量”为0时,表示仅剩不可外借的原本，只能在图书馆内阅读。")
                            .setPositiveButton("确定", null);
            }*/
            if(totalnum-borrownum>0)
                dialog.setMessage("书号：" + no.getText() + "\n" +
                        "书名：" + name.getText() + "\n" +
                        "作者：" + author.getText() + "\n" +
                        "出版社：" + publisher.getText() + "\n" +
                        "出版日期：" + d[0] + "年" + d[1] + "月" + d[2] + "日" + "\n" +
                        "馆藏可借阅总量：" + totalnum + "\n" +
                        "已借出数量：" + borrownum + "\n" +
                        "\n注意，“馆藏可借阅总量”为0时,表示仅剩不可外借的原本，只能在图书馆内阅读。")
                        .setNegativeButton("借阅", new applyForBorrowing(no, name, author, publisher, date)).setPositiveButton("确定", null);
            else
                dialog.setMessage("书号：" + no.getText() + "\n" +
                        "书名：" + name.getText() + "\n" +
                        "作者：" + author.getText() + "\n" +
                        "出版社：" + publisher.getText() + "\n" +
                        "出版日期：" + d[0] + "年" + d[1] + "月" + d[2] + "日" + "\n" +
                        "馆藏可借阅总量：" + totalnum + "\n" +
                        "已借出数量：" + borrownum + "\n" +
                        "\n注意，“馆藏可借阅总量”为0时,表示仅剩不可外借的原本，只能在图书馆内阅读。")
                        .setPositiveButton("确定", null);
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
            new AlertDialog.Builder(BorrowingShow.this)//借阅对话框
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
                    new AlertDialog.Builder(BorrowingShow.this)//删除确认对话框
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
                    new AlertDialog.Builder(BorrowingShow.this).setTitle("借阅失败：超出借书数量限制")
                            .setMessage("每个人最多借阅10本图书。请归还一些图书，然后再试一次。").setPositiveButton("确定",null).show();
            }
            else {
                Toast.makeText(BorrowingShow.this, "找不到此学号", Toast.LENGTH_LONG).show();
                borrDialogLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.borrowing_dialog_inshow, null);
                new AlertDialog.Builder(BorrowingShow.this)//借阅对话框
                        .setView(borrDialogLayout).setTitle("借阅:《" + name.getText() + "》" + "(" + no.getText() + ")")
                        //.setMessage("请输入学号：")
                        .setPositiveButton("确定", new borrowingVerify(no, name, author, publisher, date)).setNegativeButton("取消", null)
                        .show();
            }
        }
    }

    //执行借阅操作
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
            Book[] b;
            if(SOURCE.equals("borrowable"))
            {
                b = bookCtrl.queryBorrowable();
                if(b!=null) {
                    ArrayList<HashMap<String, Object>> data1 = new ArrayList<HashMap<String, Object>>();
                    for (int i = 0; i < b.length; i++) {
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("no", b[i].getNo());
                        item.put("name", b[i].getName());
                        item.put("author", b[i].getAuthor());
                        item.put("publisher", b[i].getPublisher());
                        item.put("date", b[i].getYear() + "." + b[i].getMonth() + "." + b[i].getDay());
                        data1.add(item);
                    }
                    none1.setText("书号");
                    none1.setTextSize(14);
                    none2.setText("书名");
                    none3.setText("作者");
                    none4.setText("出版社");
                    none5.setText("出版日期");
                    data.clear();
                    data.addAll(data1);
                    adapter.notifyDataSetChanged();
                }
            }
            Toast.makeText(BorrowingShow.this,"借阅成功",Toast.LENGTH_LONG).show();
        }
    }

    //搜索
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.borrowing_search_bar_inshow,menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("搜索图书...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                /*Intent intent=new Intent(StudentShow.this,SearchableActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putString("info",s);
                intent.putExtras(bundle1);//将数据绑定在bundle中传入SearchableActivity
                startActivity(intent);*/
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if(adapter!=null)
                {
                    Book[] books=bookCtrl.searchBook(s);
                    if(books!=null)
                    {
                        ArrayList<HashMap<String, Object>> data1= new ArrayList<HashMap<String, Object>>();
                        for (int i = 0; i < books.length; i++) {
                            HashMap<String, Object> item = new HashMap<String, Object>();
                            item.put("no", books[i].getNo());
                            item.put("name", books[i].getName());
                            item.put("author", books[i].getAuthor());
                            item.put("publisher", books[i].getPublisher());
                            item.put("date", books[i].getYear()+"."+books[i].getMonth()+"."+books[i].getDay());
                            data1.add(item);
                        }
                        none1.setText("书号");
                        none1.setTextSize(14);
                        none2.setText("书名");
                        none3.setText("作者");
                        none4.setText("出版社");
                        none5.setText("出版日期");
                        data.clear();
                        data.addAll(data1);
                        adapter.notifyDataSetChanged();
                        return true;
                    }
                    else
                    {
                        none1.setText("\n\n\n\n\n\n\n\n                  没有与搜索条件匹配的项。");
                        none1.setTextSize(18);
                        none2.setText("");
                        none3.setText("");
                        none4.setText("");
                        none5.setText("");
                        data.clear();
                        adapter.notifyDataSetChanged();
                        return true;
                    }
                }
                else
                {
                    return true;
                }
            }
        });
        return true;
    }

    private String key;
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.nosort:
                key="no";
                Collections.sort(data,new LNComparator(key));
                adapter.notifyDataSetChanged();
                break;
            case R.id.autsort:
                key="author";
                Collections.sort(data,new PYComparator(key));
                adapter.notifyDataSetChanged();
                break;
            case R.id.datesort:
                key="date";
                Collections.sort(data,new DateComparator(key));
                adapter.notifyDataSetChanged();
                break;
            case R.id.pubsort:
                key="publisher";
                Collections.sort(data,new PYComparator(key));
                adapter.notifyDataSetChanged();
                break;
            case R.id.namesort:
                key="name";
                Collections.sort(data,new PYComparator(key));
                adapter.notifyDataSetChanged();
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