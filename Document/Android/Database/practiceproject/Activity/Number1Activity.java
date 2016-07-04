package com.sokasyn.android.practice.SQLiteProject.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sokasyn.android.practice.SQLiteProject.EMModel.EMDaoConfig;
import com.sokasyn.android.practice.SQLiteProject.EMModel.EMDatabase;
import com.sokasyn.android.practice.SQLiteProject.EMModel.EMDatabaseManager;
import com.sokasyn.android.practice.SQLiteProject.R;
import com.sokasyn.android.practice.SQLiteProject.Util.BuildDeleteJSON;
import com.sokasyn.android.practice.SQLiteProject.Util.BuildTableJSON;
import com.sokasyn.android.practice.SQLiteProject.common.ConstantTable;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Samson on 16/6/22.
 */
public class Number1Activity extends Activity implements View.OnClickListener {

    private static final String TAG = Number1Activity.class.getSimpleName();

    private static final String DB_NAME = "fish_1.db";

    private static boolean isDebug = false;

    //声明五个控件对象
    Button createDatabase=null;
    Button updateDatabase=null;
    Button insert=null;
    Button update=null;
    Button query=null;
    Button delete=null;

    EditText inputTestCase = null;
    EditText inputTestMethod = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        findViews();
    }

    private void findViews() {
        //根据控件ID得到控件
        createDatabase = (Button) this.findViewById(R.id.createDatabase);
        updateDatabase = (Button) this.findViewById(R.id.updateDatabase);
        insert = (Button) this.findViewById(R.id.insert);
        update = (Button) this.findViewById(R.id.update);
        query = (Button) this.findViewById(R.id.query);
        delete = (Button) this.findViewById(R.id.delete);

        // input text
        inputTestMethod = (EditText)findViewById(R.id.idTestMethodNum);
        inputTestCase = (EditText)findViewById(R.id.idTestCaseNum);

        //添加监听器
        createDatabase.setOnClickListener(this);
        updateDatabase.setOnClickListener(this);
        insert.setOnClickListener(this);
        update.setOnClickListener(this);
        query.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    // 创建数据库
    private static final String DB_NAME_IN_NUMBER1_ACTIVITY = "number1_1.db";
    private void createDatabaseWithEMLib(){
        Log.i(TAG,"createDatabaseWithEMLib");
        // 构造配置模版
        EMDaoConfig daoConfig = new EMDaoConfig();
        daoConfig.setContext(Number1Activity.this);
        daoConfig.setDbName(DB_NAME_IN_NUMBER1_ACTIVITY);
        daoConfig.setDbVersion(1);
        daoConfig.setTargetDirectory(null);
        EMDatabase database = EMDatabase.create(daoConfig);
        EMDatabaseManager.getInstance().createOrOpenDatabase(daoConfig);
    }


    //  = = = = = = = = = = = = = = = 数据库的更新(表创建,表更新) = = = = = = = = = = = = = = =
    public enum TestTableMethod{
        createTables,
        dropTable,
        dropAllTables,
        updateTables
    }

    private void testUpdateDatabase() throws Exception{

        TestTableMethod testMethod = TestTableMethod.createTables;
        int methodNum = Integer.parseInt(inputTestMethod.getText().toString());
        switch (methodNum){
            case 0:{
                testMethod = TestTableMethod.createTables;
                break;
            }
            case 1:{
                testMethod = TestTableMethod.dropTable;
                break;
            }
            case 2:{
                testMethod = TestTableMethod.dropAllTables;
                break;
            }
            case 3:{
                testMethod = TestTableMethod.updateTables;
                break;
            }
            default:{
                break;
            }
        }

        switch (testMethod){
            case createTables:{
                Log.i(TAG,"Test createTables");
                JSONObject tableJson = new JSONObject();
                // table:user
                tableJson.put(ConstantTable.TBL_USER,"name,age");

                // table:account
                tableJson.put(ConstantTable.TBL_ACCOUNT,"name,password");

                // table:address
                tableJson.put(ConstantTable.TBL_ADDRESS,"id,pid,name");

                String jsonStr = tableJson.toString();
                Log.i(TAG,"json string:" + jsonStr);

                EMDatabaseManager.getInstance().createTables(jsonStr);
                break;
            }
            case dropTable:{
                Log.i(TAG,"Test dropTable");
//                String tableName = inputTestCase.getText().toString();
//                if(tableName.trim() == "" || tableName == null){
//                    tableName = ConstantTable.TBL_USER;
//                }
//                Log.i(TAG,"Table Name :" + tableName);
//
                int caseNumber = Integer.parseInt(inputTestCase.getText().toString());
                String jsonString = BuildTableJSON.buildDropJson(caseNumber);
                Log.i(TAG,"input Case : " + caseNumber + "Json String:" + jsonString);

                EMDatabaseManager.getInstance().dropTableWithJson(jsonString);
                break;
            }
            case dropAllTables:{
                Log.i(TAG,"Test dropAllTables");
                EMDatabaseManager.getInstance().dropAllTables();

                break;
            }
            case updateTables:{
                Log.i(TAG,"Test updateTables");
                String jsonStr = BuildTableJSON.buildAlterTable(getCaseNumber());
                Log.i(TAG,"JSON String:" + jsonStr);
                EMDatabaseManager.getInstance().alterTable(jsonStr);
                break;
            }
            default:{
                break;
            }
        }

    }

    // 插入表数据
    private void insertData(){

        int count = 10;
        // 一个json string
        try {
            JSONObject dataObj = new JSONObject();

            // ----- TABLE USER
            // 多条数据存放与array中
            JSONArray userDataArray = new JSONArray();
            for(int i = 0 ; i < count ; i++){
                JSONObject userObj = new JSONObject();
                userObj.put("name","Kate" + i);
                userObj.put("age","" + i);
                userDataArray.put(i,userObj);
            }
            dataObj.put(ConstantTable.TBL_USER,userDataArray);


            // ----- TABLE ACCOUNT
            JSONArray accountDataArray = new JSONArray();
            for(int i = 0 ; i < count ; i++){
                JSONObject obj = new JSONObject();
                obj.put("name","Sam" + i);
                obj.put("password","" + i);
                accountDataArray.put(i,obj);
            }
            dataObj.put(ConstantTable.TBL_ACCOUNT,accountDataArray);

            // ----- TABLE ADDRESS
            // 单条数据直接一个JSONObject
            JSONObject addrObj = new JSONObject();
            addrObj.put("id","1");
            addrObj.put("pid","");
            addrObj.put("name","Michael");
            dataObj.put(ConstantTable.TBL_ADDRESS,addrObj);

            EMDatabaseManager.getInstance().insertRecords(dataObj.toString());

        }catch (JSONException e){
            Log.e(TAG,e.getMessage());
        }

    }


    //  = = = = = = = = = = = = = = = = = = = = 删除数据 = = = = = = = = = = = = = = = = = = = =
    public enum TestDeleteMethod{
        deleteWithJsonString,
        deleteFromTable,
        clearFromTable
    }

    private void testDeleteData() throws JSONException{
        TestDeleteMethod testMethod = TestDeleteMethod.deleteWithJsonString;
        int methodNum = Integer.parseInt(inputTestMethod.getText().toString());
        switch (methodNum){
            case 0:{
                testMethod = TestDeleteMethod.deleteWithJsonString;
                break;
            }
            case 1:{
                testMethod = TestDeleteMethod.deleteFromTable;
                break;
            }
            case 2:{
                testMethod = TestDeleteMethod.clearFromTable;
                break;
            }
            default:{
                break;
            }
        }

        switch (testMethod){
            case deleteWithJsonString:{
                if(isDebug){
                    for(int i = 0 ; i < 10; i++){
                        String jsonString = BuildDeleteJSON.buildDeleteJsonForCase(i);
                        Log.i(TAG,"Case " + i + " JSON String:" + jsonString);
                    }
                }
                Log.i(TAG,"Test deleteWithJsonString");
                String jsonString = BuildDeleteJSON.buildDeleteJsonForCase(getCaseNumber());
                Log.i(TAG,"Json String:" + jsonString);
                EMDatabaseManager.getInstance().deleteWithJsonString(jsonString);
                break;
            }
            case deleteFromTable:{
                Log.i(TAG,"Test deleteFromTable");
                String tableName = ConstantTable.TBL_USER;
                String jsonString = BuildDeleteJSON.buildDeleteJsonWithTable(tableName);
                EMDatabaseManager.getInstance().deleteFromTable(tableName, jsonString);
                break;
            }
            case clearFromTable:{
                Log.i(TAG,"Test clearFromTable");
                String tableName = ConstantTable.TBL_USER;
                EMDatabaseManager.getInstance().clearFromTable(tableName);
                break;
            }
            default:{
                break;
            }
        }
    }

    private int getCaseNumber(){
        String inputCaseNum = inputTestCase.getText().toString();
        if(inputCaseNum.trim() == "" || inputCaseNum == null){
            Log.i(TAG,"No input");
            return 0;
        }

        int caseNumber = Integer.parseInt(inputCaseNum);
        Log.i(TAG,"input Case : " + caseNumber);
        return caseNumber;
    }

    // 更新数据
    private void updateData(){
        Log.i(TAG,"test update data");
        // update address set pid='p2',name='Sam' where id='1';
        // {"address":"set pid='p2',name='Sam' where id='1'"}
        // {"table":"address",
        //  "set":{"pid":"p2","name":"Sam"},
        //  "where":{"id":"1","name":"Kate"}
        // }
        try{
            JSONObject obj = new JSONObject();
            String strSql = "set pid='p2',name='Sam' where id='1'";
            obj.put("address",strSql);
            EMDatabaseManager.getInstance().updateRecords(obj.toString());
        }catch (JSONException e){
            Log.e(TAG,"!!!!!!" + e.getMessage());
        }
    }

    // 查询数据
    private void queryData(){
        try{
            String jsonString = EMDatabaseManager.getInstance().query();
            Log.i(TAG,"JSON STRING :" + jsonString);
        }catch (JSONException e){
            Log.e(TAG,e.getMessage());
        }
    }

    // 清空数据库
    private void clearDbData(){
        EMDatabaseManager.getInstance().dropAllTables();
    }


    @Override
    public void onClick(View v) {
        //判断所触发的被监听控件，并执行命令
        switch(v.getId()){
            //创建数据库
            case R.id.createDatabase:
                //创建一个DatabaseHelper对象
//                DatabaseHelper dbHelper1 = new DatabaseHelper(Number1Activity.this, DB_NAME);
                //取得一个只读的数据库对象
//                SQLiteDatabase db1 = dbHelper1.getReadableDatabase();

                createDatabaseWithEMLib();
                break;
            //更新数据库
            case R.id.updateDatabase:
                try{
                    testUpdateDatabase();
                }catch (Exception e){
                    e.printStackTrace();
                }

//                DatabaseHelper dbHelper2 = new DatabaseHelper(getApplicationContext(), "fish_2.db");
//                SQLiteDatabase db2 = dbHelper2.getReadableDatabase();

//                DatabaseHelper dbHelper2 = new DatabaseHelper(Number1Activity.this, DB_NAME, 2);
//                SQLiteDatabase db2 = dbHelper2.getReadableDatabase();

//                DatabaseManager dbManager = DatabaseManager.getInstance(getApplicationContext());
//                SQLiteDatabase db2 = dbManager.getDatabase();

                break;
            //插入数据
            case R.id.insert:
                Log.i(TAG,"insert event called....");
                insertData();

                /*
                try{
                    JSONObject obj = new JSONObject();
                    obj.putOpt("id", "1");
                    obj.putOpt("name", "cocoa");
                    String jsonStr = obj.toString();
                    DatabaseManager.getInstance().insert("user",jsonStr);
                }catch (JSONException e){
                    DebugLog.e(TAG,"JSONException occured--------");
                    e.printStackTrace();
                }
                */

                /*
                //创建存放数据的ContentValues对象
                ContentValues values = new ContentValues();
                //像ContentValues中存放数据
                values.put("id", 1);
                values.put("name","zhangsan");
                DatabaseHelper dbHelper3 = new DatabaseHelper(Number1Activity.this, DB_NAME);
                SQLiteDatabase db3 = dbHelper3.getWritableDatabase();
                //数据库执行插入命令
                db3.insert("user", null, values);
                */
                break;
            //更新数据信息
            case R.id.update:
                Log.i(TAG,"update event...");
                updateData();
                /*
                DatabaseHelper dbHelper4 = new DatabaseHelper(ThisApplication.getAppContext());
                SQLiteDatabase db4 = dbHelper4.getReadableDatabase();
                */

                /*
                DatabaseHelper dbHelper4 = new DatabaseHelper(Number1Activity.this, DB_NAME);
                SQLiteDatabase db4 = dbHelper4.getWritableDatabase();
                ContentValues values2 = new ContentValues();
                values2.put("name", "xiaosan");
                db4.update("user", values2, "id=?", new String[]{"1"});
                */
                break;
            //查询信息
            case R.id.query:
                Log.i(TAG,"QUERY event..");
                queryData();

                /*
                DatabaseHelper dbHelper5 = new DatabaseHelper(Number1Activity.this, DB_NAME);
                SQLiteDatabase db5 = dbHelper5.getReadableDatabase();
                //创建游标对象
                Cursor cursor = db5.query("user", new String[]{"id","name"}, "id=?", new String[]{"1"}, null, null, null, null);
                //利用游标遍历所有数据对象
                while(cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    //日志打印输出
                    DebugLog.i(TAG,"query-->"+name);
                }*/
                break;
            //删除记录
            case R.id.delete:

                try{
                    testDeleteData();
                } catch (JSONException e){
                    Log.e(TAG,"JSON Format error！！");
                }

                /*
                DatabaseHelper dbHelper6 = new DatabaseHelper(Number1Activity.this,"test_db");
                SQLiteDatabase db6 = dbHelper6.getWritableDatabase();
                db6.delete("user", "id=?", new String[]{"1"});
                */
                break;
            default:
                Log.i(TAG,"error");
                break;
        }
    }
}
