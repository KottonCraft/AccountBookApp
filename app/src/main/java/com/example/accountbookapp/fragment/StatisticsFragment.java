package com.example.accountbookapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.accountbookapp.R;
import com.example.accountbookapp.Record;
import com.example.accountbookapp.SettingsActivity;
import com.example.accountbookapp.database.RecordDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsFragment extends Fragment {

    private Spinner timeRangeSpinner;
    private TextView incomeTextView;
    private TextView expenseTextView;
    private TextView balanceTextView;
    private ListView categoryListView;

    private List<Record> currentRecords;

    private Button SettingButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // 初始化视图组件
        timeRangeSpinner = view.findViewById(R.id.time_range_spinner);
        incomeTextView = view.findViewById(R.id.income_text_view);
        expenseTextView = view.findViewById(R.id.expense_text_view);
        balanceTextView = view.findViewById(R.id.balance_text_view);
        categoryListView = view.findViewById(R.id.category_list_view);
        SettingButton = view.findViewById(R.id.settings);

        // 设置时间范围选择器
        ArrayAdapter<CharSequence> timeRangeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.time_ranges,
                android.R.layout.simple_spinner_item
        );
        timeRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeRangeSpinner.setAdapter(timeRangeAdapter);

        // 监听时间范围选择变化
        timeRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateStatistics(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SettingButton.setOnClickListener(v -> navigateToSettings());

        // 初始化显示
        updateStatistics(0);

        return view;
    }

    private void updateStatistics(int timeRangePosition) {
        RecordDao recordDao = new RecordDao(requireContext());
        switch (timeRangePosition) {
            case 0: // 今日
                currentRecords = recordDao.getTodayRecords();
                break;
            case 1: // 本周
                currentRecords = recordDao.getThisWeekRecords();
                break;
            case 2: // 本月
                currentRecords = recordDao.getThisMonthRecords();
                break;
            case 3: // 本年
                currentRecords = recordDao.getThisYearRecords();
                break;
            default:
                currentRecords = new ArrayList<>();
        }

        // 计算总收入、总支出和余额
        double totalIncome = 0;
        double totalExpense = 0;

        for (Record record : currentRecords) {
            if (record.getType().equals("收入")) {
                totalIncome += record.getAmount();
            } else {
                totalExpense += record.getAmount();
            }
        }

        double balance = totalIncome - totalExpense;

        // 更新UI
        incomeTextView.setText(String.format("收入: %.2f", totalIncome));
        expenseTextView.setText(String.format("支出: %.2f", totalExpense));
        balanceTextView.setText(String.format("余额: %.2f", balance));

        // 按类别统计
        updateCategoryStatistics();
    }

    private void updateCategoryStatistics() {
        // 按类别统计收入和支出
        Map<String, Double> incomeCategoryMap = new HashMap<>();
        Map<String, Double> expenseCategoryMap = new HashMap<>();

        for (Record record : currentRecords) {
            String category = record.getCategory();
            double amount = record.getAmount();

            if (record.getType().equals("收入")) {
                incomeCategoryMap.put(category, incomeCategoryMap.getOrDefault(category, 0.0) + amount);
            } else {
                expenseCategoryMap.put(category, expenseCategoryMap.getOrDefault(category, 0.0) + amount);
            }
        }

        // 创建统计数据列表
        List<String> statisticsList = new ArrayList<>();

        // 添加收入类别统计
        statisticsList.add("收入类别统计:");
        for (Map.Entry<String, Double> entry : incomeCategoryMap.entrySet()) {
            statisticsList.add(entry.getKey() + ": " + String.format("%.2f", entry.getValue()));
        }

        // 添加支出类别统计
        statisticsList.add("\n支出类别统计:");
        for (Map.Entry<String, Double> entry : expenseCategoryMap.entrySet()) {
            statisticsList.add(entry.getKey() + ": " + String.format("%.2f", entry.getValue()));
        }

        // 更新ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                statisticsList
        );
        categoryListView.setAdapter(adapter);
    }

    private void navigateToSettings() {
        // 创建意图并启动SettingsActivity
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }
}  