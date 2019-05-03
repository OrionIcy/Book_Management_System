package com.book_management_system.com.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
//import org.apache.commons.lang.StringUtils;//不用这个，这玩意不能以x86架构的处理器为编译目标

import java.text.SimpleDateFormat;
import java.util.*;

public class DBAdapter
{
    private static final String DB_NAME="Manage.db";
    private static final String DB_TABLE_STUDENT="student";
    private static final String DB_TABLE_BOOK="book";
    private static final String DB_TABLE_BORROWING="borrowing";
    private static final int DB_version=1;

    private static final String KEY_ID_STUDENT="id";
    private static final String KEY_NO_STUDENT="no";
    private static final String KEY_NAME="name";
    private static final String KEY_MAJOR="major";
    private static final String KEY_CLASS="class";
    private static final String KEY_PHONE="phone";

    private static final String KEY_ID_BOOK="id";
    private static final String KEY_NO_BOOK="no";
    private static final String KEY_NAME_BOOK="name";
    private static final String KEY_AUTHOR="author";
    private static final String KEY_PUBLISHER="publisher";
    private static final String KEY_TOTALNUM="totalnum";
    private static final String KEY_BORROWNUM="borrownum";
    private static final String KEY_YEAR="year";
    private static final String KEY_MONTH="month";
    private static final String KEY_DAY="day";
    private static final String KEY_REMAINING="remaining";

    private static final String KEY_ID_BORROWING="id";
    private static final String KEY_BORRSTUNO="no";
    private static final String KEY_BORRBOOKS="borrbooks";
    /*private static final String KEY_BOOK2="book2";
    private static final String KEY_BOOK3="book3";
    private static final String KEY_BOOK4="book4";
    private static final String KEY_BOOK5="book5";
    private static final String KEY_BOOK6="book6";
    private static final String KEY_BOOK7="book7";
    private static final String KEY_BOOK8="book8";
    private static final String KEY_BOOK9="book9";
    private static final String KEY_BOOK10="book10";*/

    public SQLiteDatabase db;
    private final Context context;

    private static class DBOpenHelper extends SQLiteOpenHelper
    {
        public DBOpenHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version)
        {
            super(context,name,factory,version);
        }

        private static final String DB_CREATE_STUDENT = "create table " +
                DB_TABLE_STUDENT + "(" +
                KEY_ID_STUDENT + " integer primary key autoincrement,"  +
                KEY_NO_STUDENT + " varchar(20)," +
                KEY_NAME + " varchar(20)," +
                KEY_CLASS + " varchar(20)," +
                KEY_MAJOR + " varchar(20)," +
                KEY_PHONE + " varchar(20)" +
                ")";

        private static final String DB_CREATE_BOOK = "create table " +
                DB_TABLE_BOOK + "(" +
                KEY_ID_BOOK + " integer primary key autoincrement," +
                KEY_NO_BOOK + " varchar(20)," +
                KEY_NAME_BOOK + " varchar(20)," +
                KEY_AUTHOR + " varchar(20)," +
                KEY_PUBLISHER + " varchar(20)," +
                KEY_TOTALNUM + " varchar(20)," +
                KEY_BORROWNUM + " varchar(20)," +
                KEY_YEAR + " varchar(20)," +
                KEY_MONTH + " varchar(20)," +
                KEY_DAY + " varchar(20)," +
                KEY_REMAINING + " varchar(20)" +
                ")";

        private static final String DB_CREATE_BORROWING = "create table " +
                DB_TABLE_BORROWING + "(" +
                KEY_ID_BORROWING + " integer primary key autoincrement," +
                KEY_BORRSTUNO + " varchar(20)," +
                KEY_BORRBOOKS + " varchar(20)" +
                ")";

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DB_CREATE_STUDENT);
            db.execSQL(DB_CREATE_BOOK);
            db.execSQL(DB_CREATE_BORROWING);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE_STUDENT);
            db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE_BOOK);
            db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE_BORROWING);
            onCreate(db);
        }
    }

    private DBOpenHelper dbOpenHelper;

    public DBAdapter(Context context)
    {
        this.context=context;
    }

    //打开数据库：
    public void open() throws SQLiteException
    {
        dbOpenHelper=new DBOpenHelper(context,DB_NAME,null,DB_version);
        try
        {
            db=dbOpenHelper.getWritableDatabase();
        }
        catch(SQLiteException ex)
        {
            db=dbOpenHelper.getReadableDatabase();
        }
    }

    public void close()
    {
        if(db!=null)
        {
            db.close();
            db=null;
        }
    }






    //BORROWING TABLE
    private Borrowing[] ConvertToBorrowing(Cursor cursor)
    {
        int resultCounts=cursor.getCount();//@return the current position
        if(resultCounts==0||!cursor.moveToFirst())
            return null;
        Borrowing[] peoples=new Borrowing[resultCounts];
        for(int i=0;i<resultCounts;i++)
        {
            peoples[i]=new Borrowing();
            peoples[i].setStudentNo(cursor.getString(cursor.getColumnIndex(KEY_BORRSTUNO)));
            String[] borrbooks=cursor.getString(cursor.getColumnIndex(KEY_BORRBOOKS)).split(" ");
            peoples[i].setBorrBooks(Arrays.asList(borrbooks));
            cursor.moveToNext();
        }
        return peoples;
    }

    private String ListToStringWithSeparator(List list, String separator)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    static public String listToStringWithSeparator(List list, String separator)//供外部调用
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public long insertBorrowing(String stuno)
    {
        ContentValues newValues=new ContentValues();
        newValues.put(KEY_BORRSTUNO,stuno);
        newValues.put(KEY_BORRBOOKS,"");
        return db.insert(DB_TABLE_BORROWING,null,newValues);
    }

    public long borrowBook(String stuno,String bookno)
    {
        SimpleDateFormat simpleDateFormat;
        ContentValues updateValues=new ContentValues();
        updateValues.put(KEY_BORRSTUNO,stuno);
        Borrowing[] borr=queryBorrowing(stuno);
        Book[] b=queryBook(bookno);
        List<String> list=borr[0].getBorrBooks();
        List borrbook=new ArrayList(list);
        simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String temp=bookno + "，" + simpleDateFormat.format(date);
        borrbook.add(temp);
        if(borrbook.get(0).equals(""))
            borrbook.remove(0);
        String borrbooks = ListToStringWithSeparator(borrbook, " ");//拼接List中的元素并加入分隔符
        Book b1 = new Book(b[0].getNo(), b[0].getName(), b[0].getAuthor(), b[0].getPublisher(),
                b[0].getTotalnum(), b[0].getBorrownum() + 1, b[0].getYear(), b[0].getMonth(), b[0].getDay());
        updateValues.put(KEY_BORRBOOKS, borrbooks);
        updateBook(bookno, b1);
        return db.update(DB_TABLE_BORROWING, updateValues, KEY_NO_STUDENT + " like ? ", new String[]{stuno});
    }

    public long returnBook(String stuno,String bookno)
    {
        ContentValues updateValues=new ContentValues();
        updateValues.put(KEY_BORRSTUNO,stuno);
        Borrowing[] borr=queryBorrowing(stuno);
        Book[] b=queryBook(bookno);
        List<String> list=borr[0].getBorrBooks();
        List borrbook=new ArrayList(list);
        for(int i=0;i<borrbook.size();++i)
        {
            String bookwithdate=list.get(i);
            String[] bookanddate=bookwithdate.split("，");
            if(bookanddate[0].equals(bookno))
            {
                borrbook.remove(i);
                break;
            }
        }
        //borrbook.add(bookno+","+new SimpleDateFormat("HH").format(new Date()));
        String borrbooks;
        if(borrbook.size()!=0)
            borrbooks=ListToStringWithSeparator(borrbook," ");//拼接List中的元素并加入分隔符
        else
            borrbooks="";
        updateValues.put(KEY_BORRBOOKS,borrbooks);
        Book b1=new Book(b[0].getNo(),b[0].getName(),b[0].getAuthor(),b[0].getPublisher(),
                b[0].getTotalnum(),b[0].getBorrownum()-1,b[0].getYear(),b[0].getMonth(),b[0].getDay());
        updateBook(bookno,b1);
        return db.update(DB_TABLE_BORROWING,updateValues,KEY_NO_STUDENT+" like ? ",new String[]{stuno});
    }

    public boolean ifBorrowed(String stuno,String bookno)
    {
        Borrowing[] borr=queryBorrowing(stuno);
        List<String> borrbook=borr[0].getBorrBooks();
        for(int i=0;i<borrbook.size();++i)
        {
            String bookwithdate=borrbook.get(i);
            String[] bookanddate=bookwithdate.split("，");
            if(bookanddate[0].equals(bookno))
                return true;
        }
        return false;
    }

    public boolean inDatabase(String stuno)
    {
        Borrowing[] result=queryBorrowing(stuno);;
        return result!=null;
    }

    public int borrowingNum(String stuno)
    {
        Borrowing[] borr=queryBorrowing(stuno);
        if(borr==null)
            return 0;
        List<String> list=borr[0].getBorrBooks();
        List borrbook=new ArrayList(list);
        if(borrbook.get(0).equals(""))
            borrbook.remove(0);
        return borrbook.size();
    }

    public Borrowing[] queryBorrowing(String info) {
        Cursor cursor = db.query(DB_TABLE_BORROWING, new String[]{KEY_BORRSTUNO,KEY_BORRBOOKS},
                KEY_BORRSTUNO + " like ? ", new String[]{info}, null, null, null, null);
        return ConvertToBorrowing(cursor);
    }

    public Borrowing[] getAllRecords()
    {
        Cursor cursor = db.query(DB_TABLE_BORROWING, new String[]{KEY_BORRSTUNO,KEY_BORRBOOKS},
                null, null, null, null, null);
        return ConvertToBorrowing(cursor);
    }

    //STUDENT TABLE
    public long insertStudent(Student s1)
    {
        ContentValues newValues=new ContentValues();
        newValues.put(KEY_NO_STUDENT,s1.getNo());
        newValues.put(KEY_NAME,s1.getName());
        newValues.put(KEY_CLASS,s1.getClasses());
        newValues.put(KEY_MAJOR,s1.getMajor());
        newValues.put(KEY_PHONE,s1.getPhone());
        return db.insert(DB_TABLE_STUDENT,null,newValues);
    }

    public long deleteAllStudents()
    {
        return db.delete(DB_TABLE_STUDENT,null,null);
    }

    public long deleteStudent(String no)
    {
        return db.delete(DB_TABLE_STUDENT,KEY_NO_STUDENT+" like ? ",new String[]{no});
    }

    public long updateStudent(String no,Student student)
    {
        ContentValues updateValues=new ContentValues();
        updateValues.put(KEY_NO_STUDENT,student.getNo());
        updateValues.put(KEY_NAME, student.getName());
        updateValues.put(KEY_CLASS, student.getClasses());
        updateValues.put(KEY_MAJOR, student.getMajor());
        updateValues.put(KEY_PHONE, student.getPhone());
        return db.update(DB_TABLE_STUDENT,updateValues,KEY_NO_STUDENT+" like ? ",new String[]{no});
    }

    private Student[] ConvertToStudent(Cursor cursor)
    {
        int resultCounts=cursor.getCount();//@return the current position
        if(resultCounts==0||!cursor.moveToFirst())
            return null;
        Student[] peoples=new Student[resultCounts];
        for(int i=0;i<resultCounts;i++)
        {
            peoples[i]=new Student();
            peoples[i].setNo(cursor.getString(cursor.getColumnIndex(KEY_NO_STUDENT)));
            peoples[i].setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            peoples[i].setMajor(cursor.getString(cursor.getColumnIndex(KEY_MAJOR)));
            peoples[i].setClasses(cursor.getString(cursor.getColumnIndex(KEY_CLASS)));
            peoples[i].setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            cursor.moveToNext();
        }
        return peoples;
    }

    public Student[] queryStudent(String info)
    {
        Cursor cursor=db.query(DB_TABLE_STUDENT,new String[]{KEY_NO_STUDENT,KEY_NAME,KEY_CLASS,KEY_MAJOR,KEY_PHONE},
                KEY_NO_STUDENT+" like ? ",new String[]{info},null,null,null,null);
        return ConvertToStudent(cursor);
    }

    public Student[] searchStudentName(String info)
    {
        Cursor cursor=db.query(DB_TABLE_STUDENT,new String[]{KEY_NO_STUDENT,KEY_NAME,KEY_CLASS,KEY_MAJOR,KEY_PHONE},
                KEY_NAME +" like ? ",new String[]{"%"+info+"%"},null,null,null,null);
        return ConvertToStudent(cursor);
    }

    public Student[] searchStudentNo(String info)
    {
        Cursor cursor=db.query(DB_TABLE_STUDENT,new String[]{KEY_NO_STUDENT,KEY_NAME,KEY_CLASS,KEY_MAJOR,KEY_PHONE},
                KEY_NO_STUDENT +" like ? ",new String[]{"%"+info+"%"},null,null,null,null);
        return ConvertToStudent(cursor);
    }

    public Student[] searchStudentMajor(String info)
    {
        Cursor cursor=db.query(DB_TABLE_STUDENT,new String[]{KEY_NO_STUDENT,KEY_NAME,KEY_CLASS,KEY_MAJOR,KEY_PHONE},
                KEY_MAJOR +" like ? ",new String[]{"%"+info+"%"},null,null,null,null);
        return ConvertToStudent(cursor);
    }

    public Student[] searchStudentClass(String info)
    {
        Cursor cursor=db.query(DB_TABLE_STUDENT,new String[]{KEY_NO_STUDENT,KEY_NAME,KEY_CLASS,KEY_MAJOR,KEY_PHONE},
                KEY_CLASS +" like ? ",new String[]{"%"+info+"%"},null,null,null,null);
        return ConvertToStudent(cursor);
    }

    public Student[] searchStudentPhone(String info)
    {
        Cursor cursor=db.query(DB_TABLE_STUDENT,new String[]{KEY_NO_STUDENT,KEY_NAME,KEY_CLASS,KEY_MAJOR,KEY_PHONE},
                KEY_PHONE +" like ? ",new String[]{"%"+info+"%"},null,null,null,null);
        return ConvertToStudent(cursor);
    }

    public Student[] getAllStu()
    {
        Cursor cursor=db.query(DB_TABLE_STUDENT, new String[]{KEY_NO_STUDENT, KEY_NAME, KEY_CLASS, KEY_MAJOR, KEY_PHONE}, null, null, null, null, null);
        return ConvertToStudent(cursor);
    }




    //BOOK TABLE
    public long insertBook(Book b1)
    {
        ContentValues newValues=new ContentValues();
        newValues.put(KEY_NO_BOOK,b1.getNo());
        newValues.put(KEY_NAME_BOOK,b1.getName());
        newValues.put(KEY_AUTHOR,b1.getAuthor());
        newValues.put(KEY_PUBLISHER,b1.getPublisher());
        newValues.put(KEY_TOTALNUM,b1.getTotalnum());
        newValues.put(KEY_BORROWNUM,b1.getBorrownum());
        newValues.put(KEY_YEAR,b1.getYear());
        newValues.put(KEY_MONTH,b1.getMonth());
        newValues.put(KEY_DAY,b1.getDay());
        if(b1.getRemaining()>0)
            newValues.put(KEY_REMAINING,1);
        else
            newValues.put(KEY_REMAINING,0);
        return db.insert(DB_TABLE_BOOK,null,newValues);
    }

    public long deleteAllBooks()
    {
        return db.delete(DB_TABLE_BOOK,null,null);
    }

    public long deleteBook(String no)
    {
        return db.delete(DB_TABLE_BOOK,KEY_NO_BOOK+" like ? ",new String[]{no});
    }

    public long updateBook(String no,Book book)
    {
        ContentValues updateValues=new ContentValues();
        updateValues.put(KEY_NO_BOOK,book.getNo());
        updateValues.put(KEY_NAME_BOOK, book.getName());
        updateValues.put(KEY_AUTHOR, book.getAuthor());
        updateValues.put(KEY_PUBLISHER, book.getPublisher());
        updateValues.put(KEY_TOTALNUM, book.getTotalnum());
        updateValues.put(KEY_BORROWNUM, book.getBorrownum());
        updateValues.put(KEY_YEAR, book.getYear());
        updateValues.put(KEY_MONTH, book.getMonth());
        updateValues.put(KEY_DAY, book.getDay());
        if(book.getRemaining()>0)
            updateValues.put(KEY_REMAINING,1);
        else
            updateValues.put(KEY_REMAINING,0);
        return db.update(DB_TABLE_BOOK,updateValues,KEY_NO_BOOK+" like ? ",new String[]{no});
    }

    private Book[] ConvertToBook(Cursor cursor)
    {
        int resultCounts=cursor.getCount();//@return the current position
        if(resultCounts==0||!cursor.moveToFirst())
            return null;
        Book[] peoples=new Book[resultCounts];
        for(int i=0;i<resultCounts;i++)
        {
            peoples[i]=new Book();
            peoples[i].setNo(cursor.getString(cursor.getColumnIndex(KEY_NO_BOOK)));
            peoples[i].setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_BOOK)));
            peoples[i].setAuthor(cursor.getString(cursor.getColumnIndex(KEY_AUTHOR)));
            peoples[i].setPublisher(cursor.getString(cursor.getColumnIndex(KEY_PUBLISHER)));
            peoples[i].setTotalnum(cursor.getInt(cursor.getColumnIndex(KEY_TOTALNUM)));
            peoples[i].setBorrownum(cursor.getInt(cursor.getColumnIndex(KEY_BORROWNUM)));
            peoples[i].setYear(cursor.getString(cursor.getColumnIndex(KEY_YEAR)));
            peoples[i].setMonth(cursor.getString(cursor.getColumnIndex(KEY_MONTH)));
            peoples[i].setDay(cursor.getString(cursor.getColumnIndex(KEY_DAY)));
            cursor.moveToNext();
        }
        return peoples;
    }

    public Book[] queryBook(String info)
    {
        Cursor cursor=db.query(DB_TABLE_BOOK,new String[]{KEY_NO_BOOK,KEY_NAME_BOOK,KEY_AUTHOR,KEY_PUBLISHER,KEY_TOTALNUM,KEY_BORROWNUM,KEY_YEAR,KEY_MONTH,KEY_DAY},
                KEY_NO_BOOK+" like ? ",new String[]{info},null,null,null,null);
        return ConvertToBook(cursor);
    }

    public Book[] queryUnborrowable()
    {
        Cursor cursor=db.query(DB_TABLE_BOOK,new String[]{KEY_NO_BOOK,KEY_NAME_BOOK,KEY_AUTHOR,KEY_PUBLISHER,KEY_TOTALNUM,KEY_BORROWNUM,KEY_YEAR,KEY_MONTH,KEY_DAY,KEY_REMAINING},
                KEY_REMAINING+" like ? ",new String[] {String.valueOf(0)},null,null,null,null);
        return ConvertToBook(cursor);
    }

    public Book[] queryBorrowable()
    {
        Cursor cursor=db.query(DB_TABLE_BOOK,new String[]{KEY_NO_BOOK,KEY_NAME_BOOK,KEY_AUTHOR,KEY_PUBLISHER,KEY_TOTALNUM,KEY_BORROWNUM,KEY_YEAR,KEY_MONTH,KEY_DAY,KEY_REMAINING},
                KEY_REMAINING+" like ? ",new String[] {String.valueOf(1)},null,null,null,null);
        return ConvertToBook(cursor);
    }

    public Book[] searchBookName(String info)
    {
        Cursor cursor=db.query(DB_TABLE_BOOK,new String[]{KEY_NO_BOOK,KEY_NAME_BOOK,KEY_AUTHOR,KEY_PUBLISHER,KEY_TOTALNUM,KEY_BORROWNUM,KEY_YEAR,KEY_MONTH,KEY_DAY},
                KEY_NAME_BOOK +" like ? ",new String[]{"%"+info+"%"},null,null,null,null);
        return ConvertToBook(cursor);
    }

    public Book[] searchBookNo(String info)
    {
        Cursor cursor=db.query(DB_TABLE_BOOK,new String[]{KEY_NO_BOOK,KEY_NAME_BOOK,KEY_AUTHOR,KEY_PUBLISHER,KEY_TOTALNUM,KEY_BORROWNUM,KEY_YEAR,KEY_MONTH,KEY_DAY},
                KEY_NO_BOOK +" like ? ",new String[]{"%"+info+"%"},null,null,null,null);
        return ConvertToBook(cursor);
    }

    public Book[] searchBookAuthor(String info)
    {
        Cursor cursor=db.query(DB_TABLE_BOOK,new String[]{KEY_NO_BOOK,KEY_NAME_BOOK,KEY_AUTHOR,KEY_PUBLISHER,KEY_TOTALNUM,KEY_BORROWNUM,KEY_YEAR,KEY_MONTH,KEY_DAY},
                KEY_AUTHOR +" like ? ",new String[]{"%"+info+"%"},null,null,null,null);
        return ConvertToBook(cursor);
    }

    public Book[] searchBookPublisher(String info)
    {
        Cursor cursor=db.query(DB_TABLE_BOOK,new String[]{KEY_NO_BOOK,KEY_NAME_BOOK,KEY_AUTHOR,KEY_PUBLISHER,KEY_TOTALNUM,KEY_BORROWNUM,KEY_YEAR,KEY_MONTH,KEY_DAY},
                KEY_PUBLISHER +" like ? ",new String[]{"%"+info+"%"},null,null,null,null);
        return ConvertToBook(cursor);
    }

    public Book[] searchBookDate(String info)
    {
        Cursor cursor=db.query(DB_TABLE_BOOK,new String[]{KEY_NO_BOOK,KEY_NAME_BOOK,KEY_AUTHOR,KEY_PUBLISHER,KEY_TOTALNUM,KEY_BORROWNUM,KEY_YEAR,KEY_MONTH,KEY_DAY},
                KEY_NO_BOOK +" like ? ",new String[]{"%"+info+"%"},null,null,null,null);
        return ConvertToBook(cursor);
    }

    public Book[] getAllBook()
    {
        Cursor cursor=db.query(DB_TABLE_BOOK, new String[]{KEY_NO_BOOK,KEY_NAME_BOOK,KEY_AUTHOR,KEY_PUBLISHER,KEY_TOTALNUM,KEY_BORROWNUM,KEY_YEAR,KEY_MONTH,KEY_DAY}, null, null, null, null, null);
        return ConvertToBook(cursor);
    }

    public int getRemaining(String bookno)
    {
        Book[] book=queryBook(bookno);
        if(book!=null)
            return book[0].getRemaining();
        return -1;
    }
}
