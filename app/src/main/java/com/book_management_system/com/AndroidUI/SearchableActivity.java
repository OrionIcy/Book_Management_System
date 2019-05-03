package com.book_management_system.com.AndroidUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.book_management_system.R;
import com.book_management_system.TabAdapter;
import com.book_management_system.com.AndroidUI.student.StudentUpdate;
import com.book_management_system.com.control.BookControl;
import com.book_management_system.com.control.StudentControl;
import com.book_management_system.com.model.Book;
import com.book_management_system.com.model.Student;

import net.sourceforge.pinyin4j.PinyinHelper;
import java.util.*;

public class SearchableActivity extends AppCompatActivity
{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    static public TabAdapter tabAdapter;
    private List<Fragment> list;
    static public String[] tabTitle;
    Bundle bundle2;
    String info;
    private StudentControl stuCtrl;
    private BookControl bookCtrl;
    static public Student[] s;
    static public Book[] b;
    static int studentflag,bookflag;
    //private boolean mlsExit;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);
        tabLayout=(TabLayout)findViewById(R.id.tab_search);
        viewPager=(ViewPager)findViewById(R.id.vp_content_search);
        list=new ArrayList<>();
        tabTitle=new String[2];
        stuCtrl=new StudentControl(SearchableActivity.this);
        bookCtrl=new BookControl(SearchableActivity.this);
        bundle2=this.getIntent().getExtras();
        info=bundle2.getString("info");//提取从MainActivity中传过来的数据
        s=stuCtrl.searchStudent(info);//执行搜索操作
        b=bookCtrl.searchBook(info);

        SearchableFragmentStudent student=new SearchableFragmentStudent();
        SearchableFragmentBook book=new SearchableFragmentBook();

        if(s==null&&b!=null){
            list.add(book);
            list.add(student);
            studentflag=1;
            bookflag=0;
        }
        else
        {
            list.add(student);
            list.add(book);
            studentflag=0;
            bookflag=1;
        }
;

        tabAdapter=new TabAdapter(getSupportFragmentManager(),list,tabTitle);
        //给ViewPager设置适配器：
        viewPager.setAdapter(tabAdapter);
        //将TabLayout和ViewPager关联起来
        tabLayout.setupWithViewPager(viewPager);
        getSupportActionBar().setTitle("「"+info+"」的"+"搜索结果");
    }
    /*Bundle bundle2;
    String info;
    private StudentControl stuCtrl;
    static public SimpleAdapter adapter1;
    static public ArrayList<HashMap<String, Object>> data1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_studentfragment);

        ListView lv = (ListView) findViewById(R.id.lv);
        //获取到集合数据
        data1 = new ArrayList<HashMap<String, Object>>();
        stuCtrl = new StudentControl(SearchableActivity.this);
        bundle2=this.getIntent().getExtras();
        info=bundle2.getString("info");//提取从MainActivity中传过来的数据
        Student[] s = stuCtrl.searchStudent(info);//执行搜索操作
        if (s != null) {
            getSupportActionBar().setTitle("「"+info+"」: "+s.length+"个搜索结果");
            for (int i = 0; i < s.length; i++) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("no", s[i].getNo());
                item.put("name", s[i].getName());
                item.put("phone", s[i].getPhone());
                item.put("class", s[i].getClasses());
                item.put("major", s[i].getMajor());
                data1.add(item);
            }
            //创建SimpleAdapter适配器将数据绑定到item显示控件上
            adapter1 = new SimpleAdapter(this, data1, R.layout.student_listview,
                    new String[]{"no", "name", "class", "major", "phone"},
                    new int[]{R.id.tv_No, R.id.tv_Name, R.id.tv_Class, R.id.tv_Major, R.id.tv_Mobile});
            //实现列表的显示
            lv.setAdapter(adapter1);
            lv.setOnItemClickListener(new onClickShowResult());
        }
        else
        {
            getSupportActionBar().setTitle("「"+info+"」: 0个搜索结果");
            TextView none1 = (TextView)findViewById(R.id.no);
            none1.setText("没有与搜索条件匹配的项。");
            none1.setTextSize(18);
            TextView none2 = (TextView)findViewById(R.id.name);
            none2.setText("");
            TextView none3 = (TextView)findViewById(R.id.classes);
            none3.setText("");
            TextView none4 = (TextView)findViewById(R.id.major);
            none4.setText("");
            TextView none5 = (TextView)findViewById(R.id.phone);
            none5.setText("");
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
            TextView classes=(TextView)v.findViewById(R.id.tv_Class);
            TextView major=(TextView)v.findViewById(R.id.tv_Major);
            TextView phone=(TextView)v.findViewById(R.id.tv_Mobile);
            AlertDialog.Builder dialog=new AlertDialog.Builder(SearchableActivity.this);
            dialog.setTitle("你想要对该生进行什么操作？");
            dialog.setMessage("学号："+no.getText()+"\n"+
                    "姓名："+name.getText()+"\n"+
                    "专业："+major.getText()+"\n"+
                    "班级："+classes.getText()+"\n"+
                    "手机号码："+phone.getText());
            dialog.setNegativeButton("修改信息",new modify(no)).setPositiveButton("删除",new delete(no,name,classes,major,phone));
            dialog.show();
        }
    }

    //“删除”按钮的监听事件
    public class delete implements DialogInterface.OnClickListener
    {
        private TextView no;
        private TextView name;
        private TextView classes;
        private TextView major;
        private TextView phone;
        private delete(TextView no,TextView name,TextView classes,TextView major,TextView phone)
        {
            this.no=no;
            this.name=name;
            this.classes=classes;
            this.major=major;
            this.phone=phone;
        }
        public void onClick(DialogInterface dialog,int which)
        {
            new AlertDialog.Builder(SearchableActivity.this)//删除确认对话框
                    .setTitle("确实要删除这名学生吗？")
                    .setMessage("学号："+ no.getText() + "\n" +
                            "姓名："+ name.getText() + "\n" +
                            "专业："+ classes.getText() + "\n" +
                            "班级："+ major.getText() + "\n" +
                            "手机号码："+ phone.getText())
                    .setPositiveButton("确定",new deleteOK(no)).setNegativeButton("取消",null)
                    .show();
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
            stuCtrl.deleteStudent(no.getText().toString());
            TextView none1 = (TextView)findViewById(R.id.no);
            TextView none2 = (TextView)findViewById(R.id.name);
            TextView none3 = (TextView)findViewById(R.id.classes);
            TextView none4 = (TextView)findViewById(R.id.major);
            TextView none5 = (TextView)findViewById(R.id.phone);
            Student s[] = stuCtrl.searchStudent(info);
            if(s==null)
            {
                none1.setText("没有与搜索条件匹配的项。");
                none1.setTextSize(18);
                none2.setText("");
                none3.setText("");
                none4.setText("");
                none5.setText("");
                data1.clear();
                adapter1.notifyDataSetChanged();
                Toast.makeText(SearchableActivity.this,"删除成功",Toast.LENGTH_LONG).show();
            }
            else{
                ArrayList<HashMap<String, Object>> data2= new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < s.length; i++) {
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("no", s[i].getNo());
                    item.put("name", s[i].getName());
                    item.put("phone", s[i].getPhone());
                    item.put("class", s[i].getClasses());
                    item.put("major", s[i].getMajor());
                    data2.add(item);
                }
                data1.clear();
                data1.addAll(data2);
                adapter1.notifyDataSetChanged();
                Toast.makeText(SearchableActivity.this,"删除成功",Toast.LENGTH_LONG).show();
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
            intent.setClass(SearchableActivity.this,StudentUpdate.class);
            Bundle bundle1=new Bundle();
            bundle1.putString("source","SearchableActivity");
            bundle1.putString("stuno",no1);
            bundle1.putString("info",info);
            intent.putExtras(bundle1);
            startActivity(intent);
        }
    }

    //排序选项菜单
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_search_result_sort,menu);
        return true;
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
            case R.id.classsort:
                key="class";
                Collections.sort(data1,new LNComparator(key));
                adapter1.notifyDataSetChanged();
                break;
            case R.id.majorsort:
                key="major";
                Collections.sort(data1,new PYComparator(key));
                adapter1.notifyDataSetChanged();
                break;
            case R.id.namesort:
                key="name";
                Collections.sort(data1,new PYComparator(key));
                adapter1.notifyDataSetChanged();
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
    }*/
}
