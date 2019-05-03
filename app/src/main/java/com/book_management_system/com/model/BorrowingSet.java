package com.book_management_system.com.model;

import android.content.Context;
import com.book_management_system.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowingSet extends ArrayList<Borrowing>
{
    private static BorrowingSet borrowingList=null;

    private BorrowingSet(){}//将构造方法声明为private，封装构造方法

    //静态成员方法，单例模式
    public static BorrowingSet getBorrowingList(Context con)//用静态方法生成集合对象
    {
        if(borrowingList==null)
        {
            borrowingList=new BorrowingSet();
            borrowingList.readFile(con);
        }
        return borrowingList;
    }

    //从文件读取学生信息，并将读取的信息转换成学生类的对象，存储到容器Arraylist中
    public void readFile(Context io)
    {
        try
        {
            InputStream in= io.getResources().openRawResource(R.raw.borrowing1);
            //把字节流转换成字符流并设置编码为国标码
            BufferedReader br=new BufferedReader(new InputStreamReader(in,"utf-8"));
            String temp;
            while((temp=br.readLine())!=null)
            {
                String[] borr=temp.split(" ");
                String studentNo=borr[0];
                List<String> borrBook=new ArrayList<>();
                for(int i=1;i<10;i++)
                {
                    borrBook.add(borr[i]);
                }
                Borrowing borrowing=new Borrowing(studentNo,borrBook);
                borrowingList.add(borrowing);
            }
            br.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.print("文件读取失败。");
        }
        System.out.println("文件读取成功。");
    }

    //插入一名学生
    /*public void insertStudent(Student student)
    {
        this.add(student);
    }

    //删除一名学生或一组学生
    public void deleteStudent(String info)
    {
        Student student;
        for(int i=0;i<this.size();i++)
        {
            student=this.get(i);
            if(student.getNo().equals(info)||student.getMajor().equals(info)|| student.getClasses().equals(info)||student.getName().equals(info)|| student.getPhone().equals(info))
            {
                this.remove(i);
                i--;
            }
        }
    }

    //删除所有学生
    public void deleteAll()
    {
        this.clear();
    }

    //获取容器大小
    public int ListLength_S()
    {
        return this.size();
    }

    //查询学生信息
    public ArrayList<Student> queryStudent(String info)
    {
        Student student;
        ArrayList<Student> stuList=new ArrayList<>();//新建容器，用于存放搜索结果
        int i=0;
        for (int j = 0; j < this.size(); j++) {
            student = this.get(j);
            if ((student.getMajor().equals(info)) || (student.getPhone().equals(info)) || (student.getName().equals(info)) || (student.getNo().equals(info))|| (student.getClasses().equals(info)))
            {
                stuList.add(student);
                i++;
            }
        }
        if (i!=0) {
            System.out.println("共找到"+i+"个符合条件的人员");
            return stuList;
        } else {
            System.out.println("没有与搜索条件匹配的项。");
            return null;
        }
    }*/
}
