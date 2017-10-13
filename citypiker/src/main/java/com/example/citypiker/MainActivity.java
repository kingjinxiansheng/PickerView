package com.example.citypiker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lljjcoder.citypickerview.widget.CityPicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn;
    private CityPicker cityPicker;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);
        tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                //滚轮文字的大小
                //标题，设置名称
                //标题背景
                cityPicker = new CityPicker.Builder(MainActivity.this)
                        .textSize(16) //滚轮文字的大小
                        .title("城市选择") //标题，设置名称
                        .titleBackgroundColor("#000000") //标题背景
                        .confirTextColor("#FFED7676")
                        .cancelTextColor("#60e9bc")
                        .province("北京")//默认选中的省
                        .city("北京")//默认选中的城市
                        .district("昌平区")//默认选中的区
                        .textColor(Color.BLUE)
                        .provinceCyclic(false)//是否可以无限循环滚动   省
                        .cityCyclic(false)//市
                        .districtCyclic(false)//区
                        .visibleItemsCount(4)//展示多少条
                        .itemPadding(10)//内边距
                        .build();

                cityPicker.show();

                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... strings) {
                        String province = strings[0];
                        String city = strings[1];
                        String area = strings[2];

                        tv.setText(province+city+area);
                    }

                    @Override
                    public void onCancel() {
                        cityPicker.hide();
                    }
                });

                break;
        }
    }
}
