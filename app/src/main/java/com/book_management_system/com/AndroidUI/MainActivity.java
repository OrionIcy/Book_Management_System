package com.book_management_system.com.AndroidUI;

import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;//不能用import android.widget.SearchView;，否则闪退
import android.widget.Toast;

import com.book_management_system.R;
import com.book_management_system.TabAdapter;
import com.book_management_system.com.AndroidUI.book.BookInsert;
import com.book_management_system.com.AndroidUI.book.BookManageFragment;
import com.book_management_system.com.AndroidUI.borrowing.BorrowingManageFragment;
import com.book_management_system.com.AndroidUI.student.StudentInsert;
import com.book_management_system.com.AndroidUI.student.StudentManageFragment;
import com.book_management_system.com.model.Book;
import com.book_management_system.com.model.Student;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    /*private static final int item1=Menu.FIRST;
    private static final int item2=Menu.FIRST+1;
    private static final int item3=Menu.FIRST+2;
    private boolean mlsExit;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    /*@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //菜单
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0,item1,0,"学生管理");
        menu.add(0,item2,0,"图书管理");
        menu.add(0,item3,0,"借阅管理");
        return true;
    }*/

    /*public class mClick implements TabLayout.OnTabSelectedListener
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        @Override
        public void onTabSelected(TabLayout.Tab tab)
        {
            switch (tab.getPosition())
            {
                case 1: {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, StudentManage.class);
                    startActivity(intent);
                    break;
                }
                case 0: {
                    //设置对话框的标题
                    dialog.setTitle("抱歉");
                    //设置对话框的图标
                    dialog.setIcon(R.drawable.icon1);
                    //设置对话框显示的内容
                    dialog.setMessage("本项操作维护中，敬请期待！");
                    //设置对话框的“确定”按钮
                    dialog.setPositiveButton("确定", new okClick());
                    //创建对象框
                    dialog.create();
                    //显示对象框
                    dialog.show();
                    break;
                }
                case 2: {
                    //设置对话框的标题
                    dialog.setTitle("抱歉");
                    //设置对话框的图标
                    dialog.setIcon(R.drawable.icon1);
                    //设置对话框显示的内容
                    dialog.setMessage("本项操作维护中，敬请期待！");
                    //设置对话框的“确定”按钮
                    dialog.setPositiveButton("确定", new okClick());
                    //创建对象框
                    dialog.create();
                    //显示对象框
                    dialog.show();
                    break;
                }
            }
        }
        public void onTabUnselected(TabLayout.Tab tab){}
        public void onTabReselected(TabLayout.Tab tab){}
    }*/



    private TabLayout tabLayout;
    private ViewPager viewPager;
    static private TabAdapter adapter;
    private List<Fragment> list;
    private boolean mlsExit;
    public String[] tabTitle={"借阅管理","学生管理","图书管理"};
    Fragment BorrowingManageFragment=new BorrowingManageFragment();
    Fragment BookManageFragment=new BookManageFragment();
    Fragment StudentManageFragment=new StudentManageFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout=(TabLayout)findViewById(R.id.tab);
        viewPager=(ViewPager)findViewById(R.id.vp_content);
        list=new ArrayList<>();
        //for(int i=0;i<tabTitle.length;i++)
        //{

        list.add(BorrowingManageFragment);
        list.add(StudentManageFragment);
        list.add(BookManageFragment);
        //list.add(new Fragment());
        //list.add(new Fragment());
        //}
        adapter=new TabAdapter(getSupportFragmentManager(),list,tabTitle);;
        //给ViewPager设置适配器：
        viewPager.setAdapter(adapter);
        //将TabLayout和ViewPager关联起来：
        tabLayout.setupWithViewPager(viewPager);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_search_bar,menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("搜索...");
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent=new Intent(MainActivity.this,SearchableActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putString("info",s);
                intent.putExtras(bundle1);//将数据绑定在bundle中传入SearchableActivity
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        //menu.getItem(1).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int i=0;
        switch(item.getItemId())
        {
            case R.id.setting1:
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, StudentInsert.class);
                Bundle bundle1=new Bundle();
                bundle1.putString("source","MainActivity");
                intent.putExtras(bundle1);
                startActivity(intent);
                break;
            case R.id.setting2:
                Intent intent1=new Intent();
                intent1.setClass(MainActivity.this, BookInsert.class);
                Bundle bundle2=new Bundle();
                bundle2.putString("source","MainActivity");
                intent1.putExtras(bundle2);
                startActivity(intent1);
                break;
        }
        return true;
    }




    //普通对话框“确定”按钮事件
    /*class okClick implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog,int which)
        {
            dialog.cancel();
        }
    }*/


    //双击返回键退出
    /*@Override
    public boolean onKeyDown(int keyCode,KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(mlsExit)
            {
                this.finish();
                //System.exit(0);
            }
            else
            {
                Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
                mlsExit=true;
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mlsExit=false;
                    }
                },2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }*/
}
