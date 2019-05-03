package com.book_management_system.com.control;

import com.book_management_system.com.model.*;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentControl
{
    private static StudentSet studentSet;
    private static DBAdapter studentDbAdapter;
    private Context context;

    public StudentControl (Context context)
    {
        this.context=context;
        studentSet=StudentSet.getStudentList(context);//读取文件
        studentDbAdapter=new DBAdapter(context);
        studentDbAdapter.open();
    }

    //添加学生
    public void addStudent(Student student)
    {
        studentDbAdapter.insertStudent(student);
        //studentSet.insertStudent(student);
    }

    public void saveAll()
    {
        //把文件中读取出的数据保存到数据库中
        //studentSet.readFile(context);//这一句是多余的。因为再构造方法里，studentSet=StudentSet.getStudentList(context);已经读取了文件，再加上这一句会导致多出一份数据

        //dbAdapter.deleteAllStudents();
        String temp;
        //使用事务可以极大地增加插入速度，不再是一条一条地插入，而是暂存在缓存区最后一起插入数据库
        studentDbAdapter.db.beginTransaction();//开启事务
        for(int i=0;i<studentSet.size();i++)
        {
            temp=studentSet.get(i).getNo();
            if(studentDbAdapter.queryStudent(temp)!=null)
                studentDbAdapter.deleteStudent(temp);//学号重复的用新数据替换掉
            studentDbAdapter.insertStudent(studentSet.get(i));
        }
        studentDbAdapter.db.setTransactionSuccessful();//设置事务标志为成功，当事务结束时就会提交事务
        studentDbAdapter.db.endTransaction();//事务结束
    }

    //删除所有学生
    public void deleteAll()
    {
        studentDbAdapter.deleteAllStudents();
        //studentSet.deleteAll();
    }

    //删除一个学生
    public boolean deleteStudent(String info)
    {
        //ArrayList<Student> student;
        //student=studentSet.queryStudent(info);
        Student student[]=studentDbAdapter.queryStudent(info);
        if(student!=null)
        {
            //studentSet.deleteStudent(info);
            studentDbAdapter.deleteStudent(info);
            return true;
        }
        return false;
    }

    //删除一组学生
    public boolean deleteStudents(Student[] s)
    {
        String info;
        int n=0;
        for(int i=0;i<s.length;i++) {
            info=s[i].getNo();
            Student student[] = studentDbAdapter.queryStudent(info);
            if (student != null) {
                //studentSet.deleteStudent(info);
                studentDbAdapter.deleteStudent(info);
                n++;
            }
        }
        if(n==s.length)
            return true;
        return false;
    }

    //更新
    public void updateStudent(Student student)
    {
        String no=student.getNo();
        //ArrayList<Student> stuList=studentSet.queryStudent(no);//把查询结果放到ArrayList中
        Student s[]=studentDbAdapter.queryStudent(no);
        /*if(stuList!=null)
        {
            studentSet.deleteStudent(no);
            studentSet.insertStudent(student);
        }*/
        if(s!=null)
        {
            studentDbAdapter.updateStudent(no,student);
        }
    }
    //查询
    public Student[] queryStudent(String info)
    {
        /*ArrayList<Student> stuList = studentSet.queryStudent(no);//把查询结果放到ArrayList中
        if (stuList != null)
        {
            Student[] students = new Student[stuList.size()];
            for (int i = 0; i < stuList.size(); i++)
            {//将ArrayList中的学生取出来放到学生数组中
                students[i] = stuList.get(i);
            }
            return students;
        } else
            return null;*/
        return studentDbAdapter.queryStudent(info);
    }

    public Student[] searchStudent(String info)
    {
        Student[] result;
        Student[] result1=studentDbAdapter.searchStudentNo(info);
        Student[] result2=studentDbAdapter.searchStudentName(info);
        Student[] result3=studentDbAdapter.searchStudentMajor(info);
        Student[] result4=studentDbAdapter.searchStudentClass(info);
        Student[] result5=studentDbAdapter.searchStudentPhone(info);
        List<Student > list=new ArrayList<>();
        if(result1!=null)
            Collections.addAll(list,result1);
        if(result2!=null)
            Collections.addAll(list,result2);
        if(result3!=null)
            Collections.addAll(list,result3);
        if(result4!=null)
            Collections.addAll(list,result4);
        if(result5!=null)
            Collections.addAll(list,result5);
        if(list.size()!=0)
        {
            for(int i=0;i<list.size();i++){
                for(int j=i+1;j<list.size();j++){
                    if(list.get(i).getNo().equals(list.get(j).getNo()))
                    {
                        list.remove(j);
                        j--;
                    }
                }
            }
            result=new Student[list.size()];
            list.toArray(result);
            return result;
        }
        return null;
    }


    //获取所有学生信息
    public Student[] getAllStudent()
    {
        /*int size = studentSet.ListLength_S();
        Student students[] = new Student[size];
        for (int i = 0; i < size; i++)
        {
            students[i] = studentSet.get(i);
        }
        return students;*/
        return studentDbAdapter.getAllStu();
    }
}
