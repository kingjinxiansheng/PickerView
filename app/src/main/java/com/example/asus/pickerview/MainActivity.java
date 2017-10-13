package com.example.asus.pickerview;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textview;
    private Button timeshow;
    private TimePickerView timePickerView;
    private Button textshow;

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private OptionsPickerView optionsPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setPropertyTime();

    }

    private void setPropertyText() {

        optionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String tx = options1Items.get(options1).getPickerViewText()+
                        options2Items.get(options1).get(options2)+
                        options3Items.get(options1).get(options2).get(options3);
                textview.setText(tx);
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .build();

        optionsPickerView.setPicker(options1Items,options2Items,options3Items);
        optionsPickerView.show();
    }

    private void setPropertyTime() {

        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 11, 31);

        timePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                textview.setText(getTime(date));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})//默认全部显示
                .setLabel("", "", "", "", "", "")//设置为年月日时分秒
                .isCenterLabel(false)//是否只显示 中间选项项的标签文字，虚假每个项目全部都带有标签
                .setContentSize(20)//滚轮文字大小
                .setTitleText("时间")//标题文字
                .setTitleSize(20)//标题文字大小
                .setDividerColor(Color.BLUE)//边框颜色
                .setTextColorOut(Color.RED)//选中之外文字颜色
                .setTextColorCenter(Color.YELLOW)//选中文字颜色
                .setDate(selectedDate)//如果不设置的话，默认是 系统时间* /
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .isDialog(false)//是否显示为对话框样式
                .isCyclic(false)//是否循环滚动
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .build();
    }

    //可根据需要自行截取数据显示
    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    private void initView() {
        textview = (TextView) findViewById(R.id.textview);
        timeshow = (Button) findViewById(R.id.timeshow);
        timeshow.setOnClickListener(this);
        textshow = (Button) findViewById(R.id.textshow);
        textshow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timeshow:
                timePickerView.show();
                break;

            case R.id.textshow:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initJsonData();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setPropertyText();
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    //获取assets目录下的json文件数据
    private void initJsonData() {
        StringBuffer stringBuffer = new StringBuffer();

        AssetManager assetManager = this.getAssets();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open("province.json")));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String string = stringBuffer.toString();

        //用Gson 转成实体
        ArrayList<JsonBean> jsonBean = parseData(string);

        //省份数据
        options1Items = jsonBean;

        for (int i=0;i<jsonBean.size();i++){//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c=0; c<jsonBean.get(i).getCityList().size(); c++){//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        ||jsonBean.get(i).getCityList().get(c).getArea().size()==0) {
                    City_AreaList.add("");
                }else {

                    for (int d=0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            //添加城市数据
            options2Items.add(CityList);

            //添加地区数据
            options3Items.add(Province_AreaList);
        }

    }

        //解析
    private ArrayList<JsonBean> parseData(String result) {
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }
}
