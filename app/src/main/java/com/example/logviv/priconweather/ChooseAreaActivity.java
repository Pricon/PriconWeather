package com.example.logviv.priconweather;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logviv.priconweather.R;
import com.example.logviv.priconweather.db.City;
import com.example.logviv.priconweather.db.County;
import com.example.logviv.priconweather.db.County_Addition;
import com.example.logviv.priconweather.db.Province;
import com.example.logviv.priconweather.util.HttpUtil;
import com.example.logviv.priconweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 用于遍历省市县数据的碎片
 */

public class ChooseAreaActivity extends Fragment {
    public static final int LEVEL_PROVINCE = 0; //省
    public static final int LEVEL_CITY = 1;  //市
    public static final int LEVEL_COUNTY = 2;  //县
    private ProgressDialog progressDialog;  //更换地区时显示的进度对话框
    private TextView titleText;  //在标题上显示选中地区的名字
    private Button back;   //后退按钮
    private ListView listView;
    private ArrayAdapter<String> adapter; //适配器
    private List<String> dataList = new ArrayList<>();
    //省列表
    private List<Province> provinceList;
    //市列表
    private List<City> cityList;
    //县列表
    private List<County> countyList;
    //选中的省份
    private Province selectedProvince;
    //选中的城市
    private City selectedCity;
    //当前选中的级别
    private int currentLevel;
    private int number=0;
    private County_Addition county_addition;
    private Boolean flag=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //初始化组件
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        back = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //listView的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //根据当前级别来判断调用哪个方法来查询数据
                if (currentLevel == LEVEL_PROVINCE) { //当前选中级别为省份
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) { //为城市
                    selectedCity = cityList.get(position);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) { //为县
                    String weatherId = countyList.get(position).getWeatherId();
                    if (getActivity() instanceof MainActivity ) {  //判断一个对象是否属于MainActivity的实例
                        //传递数据
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }
//                    else if (getActivity() instanceof WeatherActivity ) {
//                        WeatherActivity activity = (WeatherActivity) getActivity();
//                       // AddAreaActivity activity=(AddAreaActivity)getActivity();
//                        activity.swipeRefresh.setRefreshing(true);
//                        activity.requestWeather(weatherId);
//                    }
                }
            }
        });
        //Button的点击事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) { //如果当前级别为县，则返回市级列表
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {  //如果当前级级别为市，则返回省级列表
                    queryProvinces();
                }
            }
        });
        queryProvinces();  //加载省级数据
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryProvinces() {
        titleText.setText("中国"); //标题首先设置为中国
        back.setVisibility(View.GONE); //隐藏后退按钮
        provinceList = DataSupport.findAll(Province.class); //读取数据
        if (provinceList.size() > 0) { //将数据显示在界面上
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            //如果没有读取到数据，就从服务器上查询数据
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        back.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        back.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县数据
     */
    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        //向服务器发送请求
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                //解析数据
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                }
                if (result) {
                    //从子线程切换到主线程
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
