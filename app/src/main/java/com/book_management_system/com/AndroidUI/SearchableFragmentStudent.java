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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.book_management_system.R;
import com.book_management_system.com.AndroidUI.student.StudentBorrowedWhich;
import com.book_management_system.com.AndroidUI.student.StudentUpdate;
import com.book_management_system.com.control.BorrowingControl;
import com.book_management_system.com.control.StudentControl;
import com.book_management_system.com.model.Student;

import net.sourceforge.pinyin4j.PinyinHelper;
import java.util.*;

public class SearchableFragmentStudent extends Fragment {
    private Bundle bundle2;
    private String info;
    private View view;
    private StudentControl stuCtrl;
    private BorrowingControl borrCtrl;
    static public SimpleAdapter adapter1;
    static public ArrayList<HashMap<String, Object>> data1;
    private int stunum;
    //private List<String> tabTitle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stuCtrl = new StudentControl(getActivity());
        borrCtrl=new BorrowingControl(getActivity());
        setHasOptionsMenu(true);//让Fregment接受onCreateOptionsMenu(),否则无法添加选项菜单
        data1=new ArrayList<HashMap<String, Object>>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.activity_search_studentfragment,container,false);
        ListView lv = (ListView) view.findViewById(R.id.lv);
        //获取到集合数据

        bundle2=getActivity().getIntent().getExtras();
        info=bundle2.getString("info");//提取从MainActivity中传过来的数据
        Student[] s = SearchableActivity.s;

        if (s != null) {
            //getSupportActionBar().setTitle("「"+info+"」: "+s.length+"个搜索结果");
            stunum=s.length;//提取搜索结果数量
            SearchableActivity.tabTitle[SearchableActivity.studentflag]="学生："+stunum+"个结果";
            SearchableActivity.tabAdapter.notifyDataSetChanged();
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
            adapter1 = new SimpleAdapter(getActivity(), data1, R.layout.student_listview,
                    new String[]{"no", "name", "class", "major", "phone"},
                    new int[]{R.id.tv_No, R.id.tv_Name, R.id.tv_Class, R.id.tv_Major, R.id.tv_Mobile});
            //实现列表的显示
            lv.setAdapter(adapter1);
            lv.setOnItemClickListener(new onClickShowResult());
            lv.setOnItemLongClickListener(new LongClickToDelete());
        }
        else
        {
            //getSupportActionBar().setTitle("「"+info+"」: 0个搜索结果");
            stunum=0;
            SearchableActivity.tabTitle[SearchableActivity.studentflag]="学生："+stunum+"个结果";
            SearchableActivity.tabAdapter.notifyDataSetChanged();
            TextView none1 = (TextView)view.findViewById(R.id.no);
            none1.setText("\n\n\n\n\n\n\n\n                  没有与搜索条件匹配的项。");
            none1.setTextSize(18);
            TextView none2 = (TextView)view.findViewById(R.id.name);
            none2.setText("");
            TextView none3 = (TextView)view.findViewById(R.id.classes);
            none3.setText("");
            TextView none4 = (TextView)view.findViewById(R.id.major);
            none4.setText("");
            TextView none5 = (TextView)view.findViewById(R.id.phone);
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
            TextView classes=(TextView)v.findViewById(R.id.tv_Class);
            TextView major=(TextView)v.findViewById(R.id.tv_Major);
            TextView phone=(TextView)v.findViewById(R.id.tv_Mobile);
            int n=borrCtrl.borrowingNum(no.getText().toString());
            if(n==0)
                new AlertDialog.Builder(getActivity())//删除确认对话框
                        .setTitle("确实要删除这名学生吗？")
                        .setMessage("学号："+ no.getText() + "\n" +
                                "姓名："+ name.getText() + "\n" +
                                "专业："+ classes.getText() + "\n" +
                                "班级："+ major.getText() + "\n" +
                                "手机号码："+ phone.getText())
                        .setPositiveButton("确定",new deleteOK(no)).setNegativeButton("取消",null)
                        .show();
            else
                new AlertDialog.Builder(getActivity()).setTitle("无法删除学生：此学生仍有图书未归还").setPositiveButton("确定", null).show();
            return true;
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
            AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
            dialog.setTitle("你想要对该生进行什么操作？");
            dialog.setMessage("学号："+no.getText()+"\n"+
                    "姓名："+name.getText()+"\n"+
                    "专业："+major.getText()+"\n"+
                    "班级："+classes.getText()+"\n"+
                    "手机号码："+phone.getText());
            dialog.setNegativeButton("更多操作",new other(no,name,classes,major,phone)).setPositiveButton("查看借阅信息",new check(no));
            dialog.show();
        }
    }

    public class check implements DialogInterface.OnClickListener
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
            Intent intent=new Intent(getActivity(),StudentBorrowedWhich.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public class other implements DialogInterface.OnClickListener
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
            new AlertDialog.Builder(getActivity())//删除确认对话框
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
            int n=borrCtrl.borrowingNum(no.getText().toString());
            if(n==0)
            new AlertDialog.Builder(getContext())//删除确认对话框
                    .setTitle("确实要删除这名学生吗？")
                    .setMessage("学号："+ no.getText() + "\n" +
                            "姓名："+ name.getText() + "\n" +
                            "专业："+ classes.getText() + "\n" +
                            "班级："+ major.getText() + "\n" +
                            "手机号码："+ phone.getText())
                    .setPositiveButton("确定",new deleteOK(no)).setNegativeButton("取消",null)
                    .show();
            else
                new AlertDialog.Builder(getActivity()).setTitle("无法删除学生：此学生仍有图书未归还").setPositiveButton("确定", null).show();
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
            TextView none1 = (TextView)view.findViewById(R.id.no);
            TextView none2 = (TextView)view.findViewById(R.id.name);
            TextView none3 = (TextView)view.findViewById(R.id.classes);
            TextView none4 = (TextView)view.findViewById(R.id.major);
            TextView none5 = (TextView)view.findViewById(R.id.phone);
            Student s[] = stuCtrl.searchStudent(info);
            if(s==null)
            {
                stunum=0;
                //SearchableActivity.tabTitle.set(0,"0");
                SearchableActivity.tabTitle[SearchableActivity.studentflag]="学生："+stunum+"个结果";
                SearchableActivity.tabAdapter.notifyDataSetChanged();
                none1.setText("\n\n\n\n\n\n\n\n                  没有与搜索条件匹配的项。");
                none1.setTextSize(18);
                none2.setText("");
                none3.setText("");
                none4.setText("");
                none5.setText("");
                data1.clear();
                adapter1.notifyDataSetChanged();
                Toast.makeText(getContext(),"删除成功",Toast.LENGTH_LONG).show();
            }
            else{
                stunum=s.length;
                SearchableActivity.tabTitle[SearchableActivity.studentflag]="学生："+stunum+"个结果";
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
            intent.setClass(getActivity(),StudentUpdate.class);
            Bundle bundle1=new Bundle();
            bundle1.putString("source","SearchableActivity");
            bundle1.putString("stuno",no1);
            bundle1.putString("info",info);
            intent.putExtras(bundle1);
            startActivity(intent);
        }
    }

    //排序选项菜单
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {//注意这里，和Activity中相比，要增加一个参数inflater，因为在Fregment中无法调用getMenuInflater()函数，必须在参数表中声明
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.activity_search_resultstu_sort,menu);//使用参数表中的inflater参数而不是调用getMenuInflater()函数
    }

    private String key;
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.nosort:
                key="no";
                if(data1!=null){
                    Collections.sort(data1,new LNComparator(key));
                    adapter1.notifyDataSetChanged();}
                break;
            case R.id.classsort:
                key="class";
                if(data1!=null){
                    Collections.sort(data1,new LNComparator(key));
                    adapter1.notifyDataSetChanged();}
                break;
            case R.id.majorsort:
                key="major";
                if(data1!=null){
                    Collections.sort(data1,new PYComparator(key));
                    adapter1.notifyDataSetChanged();}
                break;
            case R.id.namesort:
                key="name";
                if(data1!=null){
                    Collections.sort(data1,new PYComparator(key));
                    adapter1.notifyDataSetChanged();
                }
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
}
