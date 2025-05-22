package com.example.accountbookapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.accountbookapp.R;
import com.example.accountbookapp.SettingsActivity;

public class HomeFragment extends Fragment {

    private Button AddButton;
    private Button SettingButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        AddButton = view.findViewById(R.id.add);
        SettingButton = view.findViewById(R.id.settings);

        // 设置点击监听器
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建并显示底部弹窗 Fragment
                //BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                //bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetDialog");

                //AddRecordFragment addRecordFragment = new AddRecordFragment();
                //addRecordFragment.show(getSupportFragmentManager(), "BottomSheetDialog");

                AddRecordFragment dialogFragment = new AddRecordFragment();
                dialogFragment.show(getParentFragmentManager(), "AddRecordFragment");
            }
        });

        SettingButton.setOnClickListener(v -> navigateToSettings());


        return view;
    }
    private void navigateToSettings() {
        // 创建意图并启动SettingsActivity
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }
}

//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import androidx.fragment.app.Fragment;
//import com.example.accountbookapp.MainActivity;
//import com.example.accountbookapp.R;
//import com.example.accountbookapp.Record;
//import com.example.accountbookapp.database.RecordDao;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//public class HomeFragment extends Fragment {
//
//    private Spinner timeFilterSpinner;
//    private ListView recordsListView;
//    private List<Record> allRecords;
//    private ArrayAdapter<Record> recordsAdapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        // 初始化视图组件
//        timeFilterSpinner = view.findViewById(R.id.time_filter_spinner);
//        recordsListView = view.findViewById(R.id.records_list_view);
//
//        // 设置时间过滤选择器
//        ArrayAdapter<CharSequence> timeFilterAdapter = ArrayAdapter.createFromResource(
//                requireContext(),
//                R.array.time_filters,
//                android.R.layout.simple_spinner_item
//        );
//        timeFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        timeFilterSpinner.setAdapter(timeFilterAdapter);
//
//        // 监听时间过滤选择变化
//        timeFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                filterRecords(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        // 初始化记录列表
//        allRecords = new ArrayList<>();
//        recordsAdapter = new ArrayAdapter<Record>(
//                requireContext(),
//                android.R.layout.simple_list_item_2,
//                android.R.id.text1,
//                allRecords
//        ) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                Record record = allRecords.get(position);
//
//                // 设置第一行文本（金额和类型）
//                TextView text1 = view.findViewById(android.R.id.text1);
//                String type = record.getType().equals("收入") ? "+" : "-";
//                text1.setText(type + record.getAmount() + " 元 - " + record.getCategory());
//
//                // 设置第二行文本（日期和备注）
//                TextView text2 = view.findViewById(android.R.id.text2);
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
//                String dateStr = dateFormat.format(record.getDate());
//                text2.setText(dateStr + " - " + record.getNote());
//
//                return view;
//            }
//        };
//        recordsListView.setAdapter(recordsAdapter);
//
//        // 设置记录项点击事件 - 进入编辑页面
//        recordsListView.setOnItemClickListener((parent, view1, position, id) -> {
//            Record selectedRecord = allRecords.get(position);
//            ((MainActivity) requireActivity()).replaceFragment(EditRecordFragment.newInstance(selectedRecord));
//        });
//
//        // 加载所有记录
//        loadAllRecords();
//
//        return view;
//    }
//
//    private void loadAllRecords() {
//        RecordDao recordDao = new RecordDao(requireContext());
//        allRecords.clear();
//        allRecords.addAll(recordDao.getAllRecords());
//        recordsAdapter.notifyDataSetChanged();
//    }
//
//    private void filterRecords(int timeFilterPosition) {
//        RecordDao recordDao = new RecordDao(requireContext());
//        allRecords.clear();
//
//        switch (timeFilterPosition) {
//            case 0: // 全部
//                allRecords.addAll(recordDao.getAllRecords());
//                break;
//            case 1: // 今日
//                allRecords.addAll(recordDao.getTodayRecords());
//                break;
//            case 2: // 本周
//                allRecords.addAll(recordDao.getThisWeekRecords());
//                break;
//            case 3: // 本月
//                allRecords.addAll(recordDao.getThisMonthRecords());
//                break;
//            case 4: // 本年
//                allRecords.addAll(recordDao.getThisYearRecords());
//                break;
//        }
//
//        recordsAdapter.notifyDataSetChanged();
//    }
//}