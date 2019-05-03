package com.book_management_system.com.AndroidUI.student;

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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.book_management_system.R;
import com.book_management_system.com.control.BorrowingControl;
import com.book_management_system.com.control.StudentControl;
import com.book_management_system.com.model.Borrowing;
import com.book_management_system.com.model.Student;

import java.util.*;
import net.sourceforge.pinyin4j.PinyinHelper;//需要第三方jar包pinyin4j.jar

public class StudentShow extends AppCompatActivity {
    private StudentControl stuCtrl;
    private BorrowingControl borrCtrl;
    static private boolean hasfocus;
    private String searchString="";
    static public SimpleAdapter adapter;
    static public ArrayList<HashMap<String, Object>> data;
    static public TextView none1 ;
    static public TextView none2 ;
    static public TextView none3 ;
    static public TextView none4 ;
    static public TextView none5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_show);
        borrCtrl=new BorrowingControl(StudentShow.this);
        ListView lv = (ListView) findViewById(R.id.lv);
        none1 = (TextView)findViewById(R.id.no);
        none2 = (TextView)findViewById(R.id.name);
        none3 = (TextView)findViewById(R.id.major);
        none4 = (TextView)findViewById(R.id.classes);
        none5 = (TextView)findViewById(R.id.phone);
        //获取到集合数据
        data= new ArrayList<HashMap<String, Object>>();
        stuCtrl = new StudentControl(StudentShow.this);
        Student s[] = stuCtrl.getAllStudent();
        if (s != null) {
            for (int i = 0; i < s.length; i++) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("no", s[i].getNo());
                item.put("name", s[i].getName());
                item.put("phone", s[i].getPhone());
                item.put("class", s[i].getClasses());
                item.put("major", s[i].getMajor());
                data.add(item);
            }
            //创建SimpleAdapter适配器将数据绑定到item显示控件上
            adapter = new SimpleAdapter(StudentShow.this, data, R.layout.student_listview,
                    new String[]{"no", "name", "class", "major", "phone"},
                    new int[]{R.id.tv_No, R.id.tv_Name, R.id.tv_Class, R.id.tv_Major, R.id.tv_Mobile});
            //实现列表的显示
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new OnClickShowAll());//设置点击item时候的监听
            lv.setOnItemLongClickListener(new LongClickToDelete());
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

    public class LongClickToDelete implements AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0,View v,int position,long id)
        {
            TextView no=(TextView)v.findViewById(R.id.tv_No);
            TextView name=(TextView)v.findViewById(R.id.tv_Name);
            TextView classes=(TextView)v.findViewById(R.id.tv_Class);
            TextView major=(TextView)v.findViewById(R.id.tv_Major);
            TextView phone=(TextView)v.findViewById(R.id.tv_Mobile);
            int n=borrCtrl.borrowingNum(no.getText().toString());
            if(n==0)
                new AlertDialog.Builder(StudentShow.this)//删除确认对话框
                    .setTitle("确实要删除这名学生吗？")
                    .setMessage("学号："+ no.getText() + "\n" +
                            "姓名："+ name.getText() + "\n" +
                            "专业："+ classes.getText() + "\n" +
                            "班级："+ major.getText() + "\n" +
                            "手机号码："+ phone.getText())
                    .setPositiveButton("确定",new deleteOK(no)).setNegativeButton("取消",null)
                    .show();
            else
                new AlertDialog.Builder(StudentShow.this).setTitle("无法删除学生：此学生仍有图书未归还").setPositiveButton("确定", null).show();
            return true;
        }
    }

    //点击item时候的监听
    public class OnClickShowAll implements OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View v,int arg1,long arg2)
        {
            //Toast.makeText(StudentShow.this,"监听成功", Toast.LENGTH_SHORT).show();
            TextView no=(TextView)v.findViewById(R.id.tv_No);
            TextView name=(TextView)v.findViewById(R.id.tv_Name);
            TextView classes=(TextView)v.findViewById(R.id.tv_Class);
            TextView major=(TextView)v.findViewById(R.id.tv_Major);
            TextView phone=(TextView)v.findViewById(R.id.tv_Mobile);
            AlertDialog.Builder dialog=new AlertDialog.Builder(StudentShow.this);
            dialog.setTitle("更多");
            dialog.setMessage("学号："+no.getText()+"\n"+
                    "姓名："+name.getText()+"\n"+
                    "专业："+major.getText()+"\n"+
                    "班级："+classes.getText()+"\n"+
                    "手机号码："+phone.getText());
            dialog.setNegativeButton("更多操作",new other(no,name,classes,major,phone)).setPositiveButton("查看借阅信息",new check(no));
            dialog.show();
        }
    }

    public class other implements OnClickListener
    {
        private TextView no;
        private TextView name;
        private TextView classes;
        private TextView major;
        private TextView phone;
        private other(TextView no,TextView name,TextView classes,TextView major,TextView phone)
        {
            this.no=no;
            this.name=name;
            this.classes=classes;
            this.major=major;
            this.phone=phone;
        }
        public void onClick(DialogInterface dialog,int which)
        {
            new AlertDialog.Builder(StudentShow.this)//删除确认对话框
                    .setTitle("你想要进行什么操作？")
                    .setMessage("学号："+ no.getText() + "\n" +
                            "姓名："+ name.getText() + "\n" +
                            "专业："+ classes.getText() + "\n" +
                            "班级："+ major.getText() + "\n" +
                            "手机号码："+ phone.getText())
                    .setNegativeButton("修改信息",new modify(no)).setPositiveButton("删除",new delete(no,name,classes,major,phone))
                    .show();
        }
    }

    //查看借阅信息
    public class check implements OnClickListener
    {
        private TextView no;
        check(TextView no)
        {
            this.no=no;
        }
        public void onClick(DialogInterface dialog,int which)
        {
            Bundle bundle=new Bundle();
            bundle.putString("info",no.getText().toString());
            Intent intent=new Intent(StudentShow.this,StudentBorrowedWhich.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    //“删除”按钮的监听事件
    public class delete implements OnClickListener
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
            int n=borrCtrl.borrowingNum(no.getText().toString());
            if(n==0)
                new AlertDialog.Builder(StudentShow.this)//删除确认对话框
                    .setTitle("确实要删除这名学生吗？")
                    .setMessage("学号："+ no.getText() + "\n" +
                            "姓名："+ name.getText() + "\n" +
                            "专业："+ classes.getText() + "\n" +
                            "班级："+ major.getText() + "\n" +
                            "手机号码："+ phone.getText())
                    .setPositiveButton("确定",new deleteOK(no)).setNegativeButton("取消",null)
                    .show();
            else
                new AlertDialog.Builder(StudentShow.this).setTitle("无法删除学生：此学生仍有图书未归还").setPositiveButton("确定", null).show();
        }
    }

    //删除确认对话框“确定”按钮监听事件
    public class deleteOK implements OnClickListener
    {
        private TextView no;
        private deleteOK(TextView no)
        {
            this.no=no;
        }
        public void onClick(DialogInterface dialog,int which)
        {
            stuCtrl.deleteStudent(no.getText().toString());
            Student s[];
            if(!hasfocus)
                s = stuCtrl.getAllStudent();
            else
                s=stuCtrl.searchStudent(searchString);
            ArrayList<HashMap<String, Object>> data1= new ArrayList<HashMap<String, Object>>();
            if(s!=null) {
                for (int i = 0; i < s.length; i++) {
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("no", s[i].getNo());
                    item.put("name", s[i].getName());
                    item.put("phone", s[i].getPhone());
                    item.put("class", s[i].getClasses());
                    item.put("major", s[i].getMajor());
                    data1.add(item);
                }
                data.clear();
                data.addAll(data1);
                adapter.notifyDataSetChanged();
                Toast.makeText(StudentShow.this, "删除成功", Toast.LENGTH_LONG).show();
            }
            else
            {
                if(!hasfocus)
                    none1.setText("\n\n\n\n\n\n\n\n                               没有数据。");
                else
                    none1.setText("\n\n\n\n\n\n\n\n                  没有与搜索条件匹配的项。");
                none1.setTextSize(18);
                none2.setText("");
                none3.setText("");
                none4.setText("");
                none5.setText("");
            }
        }
    }

    //“修改信息”按钮监听事件
    public class modify implements OnClickListener
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
            intent.setClass(StudentShow.this,StudentUpdate.class);
            Bundle bundle1=new Bundle();
            bundle1.putString("source","StudentShow");
            bundle1.putString("stuno",no1);
            intent.putExtras(bundle1);
            startActivity(intent);
        }
    }

    //搜索和添加
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.student_search_bar_inshow,menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("搜索学生...");
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
                    searchString=s;
                    Student[] students=stuCtrl.searchStudent(s);
                    TextView none1 = (TextView)findViewById(R.id.no);
                    TextView none2 = (TextView)findViewById(R.id.name);
                    TextView none3 = (TextView)findViewById(R.id.classes);
                    TextView none4 = (TextView)findViewById(R.id.major);
                    TextView none5 = (TextView)findViewById(R.id.phone);
                    if(students!=null)
                    {
                        ArrayList<HashMap<String, Object>> data1= new ArrayList<HashMap<String, Object>>();
                        for (int i = 0; i < students.length; i++) {
                            HashMap<String, Object> item = new HashMap<String, Object>();
                            item.put("no", students[i].getNo());
                            item.put("name", students[i].getName());
                            item.put("phone", students[i].getPhone());
                            item.put("class", students[i].getClasses());
                            item.put("major", students[i].getMajor());
                            data1.add(item);
                        }
                        none1.setText("学号");
                        none1.setTextSize(14);
                        none2.setText("姓名");
                        none3.setText("班级");
                        none4.setText("专业");
                        none5.setText("手机");
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
                {return true;}
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //Toast.makeText(getApplicationContext(), "hasFocus: " + hasFocus, Toast.LENGTH_SHORT).show();
                hasfocus=hasFocus;
            }
        });
        return true;
    }

    private String key;
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_add:
                Intent intent=new Intent();
                intent.setClass(StudentShow.this, StudentInsert.class);
                Bundle bundle1=new Bundle();
                bundle1.putString("source","StudentShow");
                intent.putExtras(bundle1);
                startActivity(intent);
                break;
            case R.id.nosort:
                key="no";
                Collections.sort(data,new LNComparator(key));
                adapter.notifyDataSetChanged();
                break;
            case R.id.classsort:
                key="class";
                Collections.sort(data,new LNComparator(key));
                adapter.notifyDataSetChanged();
                break;
            case R.id.majorsort:
                key="major";
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
}