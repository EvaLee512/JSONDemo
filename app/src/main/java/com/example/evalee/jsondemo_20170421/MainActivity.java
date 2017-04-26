package com.example.evalee.jsondemo_20170421;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "JSONDemo_lsn";
    private EditText edit_object;
    private EditText edit_number;
    private EditText edit_exp_month;
    private EditText edit_exp_year;
    private EditText edit_cvc;
    private TextView showInfo;

    private JsonObjectClass jsonObjectClass;
    private boolean isCanRead = false;
    private String json = "{\"jsonObjectClass\":{\"object\":\"a\",\"number\":\"123\",\"exp_month\":\"12\",\"exp_year\":\"1\",\"cvc\":\"aff\"}}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        edit_object = (EditText)findViewById(R.id.edit_object);
        edit_number = (EditText)findViewById(R.id.edit_number);
        edit_exp_month = (EditText)findViewById(R.id.edit_exp_month);
        edit_exp_year = (EditText)findViewById(R.id.edit_exp_year);
        edit_cvc = (EditText)findViewById(R.id.edit_cvc);

        showInfo = (TextView)findViewById(R.id.showInfo);
        jsonObjectClass = new JsonObjectClass();

    }

    public void mainClickHandler(View v) {

        switch (v.getId()) {
            case R.id.readJSON:
                if(!isCanRead){
                    Toast.makeText(this,"Read before you write some something",Toast.LENGTH_SHORT).show();
                    return;
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//一个toggle开关控制输入键盘的
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                readJson(json);
                break;

            case R.id.writeJSON:
                writeJson(jsonObjectClass);
                break;

            case R.id.eraseJSON:
                eraseJson();
                break;

            default:
                Toast.makeText(this,"Unknow error!",Toast.LENGTH_SHORT).show();
        }
    }


    public void readJson(String json){
        if(json.startsWith("error")){
            return;
        }
        JsonObjectClass jsonObjectClass1 = new JsonObjectClass();
        try{
            JSONObject jsonObjects = new JSONObject(json);
            jsonObjects=jsonObjects.getJSONObject("jsonObjectClass");//这一句是什么意思，在写入的时候是把数据写入到了JsonObjectClass类中，所以数据应该是已经存在对象jsonObjectClass了
Log.i(TAG,"readJson->jsonObjects = "+jsonObjects);
            String object = jsonObjects.getString("object");
            String number = jsonObjects.getString("number");
            String exp_month = jsonObjects.getString("exp_month");
            String exp_year = jsonObjects.getString("exp_year");
            String cvc = jsonObjects.getString("cvc");
Log.i(TAG,"setText");
            showInfo.setText("JSON: "+"\n"+
            "object = "+object+"\n"+
            "number = "+number+"\n"+
            "exp_month = "+exp_month+"\n"+
            "exp_year = "+exp_year+"\n"+
            "cvc = "+cvc);

            jsonObjectClass1.setObject(object);
            jsonObjectClass1.setNumber(number);
            jsonObjectClass1.setExp_month(exp_month);
            jsonObjectClass1.setExp_year(exp_year);
            jsonObjectClass1.setCvc(cvc);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String writeJson(JsonObjectClass jsonObjectClass){
        //将用户的输入值获取进来
        String jsonResult = "";
        JSONObject jsonObjects = new JSONObject();
        try{
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("object",edit_object.getText().toString());//把每个数据当做一个对象添加到数组里
            jsonObject.put("number",edit_number.getText().toString());
            jsonObject.put("exp_month",edit_exp_month.getText().toString());
            jsonObject.put("exp_year",edit_exp_year.getText().toString());
            jsonObject.put("cvc",edit_cvc.getText().toString());
            jsonArray.put(jsonObject);//向json数组添加jsonObject对象
            jsonObjects.put("jsonObjectClass",jsonObject);//向总对象里面添加包含JsonObjectClass的数组
            jsonResult = jsonObjects.toString();

        }catch (JSONException e){
            e.printStackTrace();
        }

        Log.i(TAG,"write()->jsonResult = "+jsonResult);
        isCanRead = true;
        return jsonResult;
    }

    public void eraseJson(){

    }

    public static String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        Log.i(TAG,"data_json = "+data_json);
        return data_json;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCanRead = false;
    }
}
