package com.example.accountbookapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.accountbookapp.MainActivity;
import com.example.accountbookapp.R;
import com.example.accountbookapp.Record;
import com.example.accountbookapp.database.RecordDao;
import java.util.Date;

public class AddRecordFragment extends Fragment {

    private Spinner typeSpinner;
    private Spinner categorySpinner;
    private EditText amountEditText;
    private EditText noteEditText;
    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_record, container, false);

        // 初始化视图组件
        typeSpinner = view.findViewById(R.id.type_spinner);
        categorySpinner = view.findViewById(R.id.category_spinner);
        amountEditText = view.findViewById(R.id.amount_edit_text);
        noteEditText = view.findViewById(R.id.note_edit_text);
        saveButton = view.findViewById(R.id.save_button);

        // 设置类型选择器
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.record_types,
                android.R.layout.simple_spinner_item
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        // 监听类型选择变化，更新分类选择器
        typeSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                updateCategorySpinner();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        // 初始化分类选择器
        updateCategorySpinner();

        // 设置保存按钮点击事件
        saveButton.setOnClickListener(v -> saveRecord());

        return view;
    }

    private void updateCategorySpinner() {
        int typePosition = typeSpinner.getSelectedItemPosition();
        int categoryArrayId;

        if (typePosition == 0) { // 收入
            categoryArrayId = R.array.income_categories;
        } else { // 支出
            categoryArrayId = R.array.expense_categories;
        }

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                categoryArrayId,
                android.R.layout.simple_spinner_item
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
    }

    private void saveRecord() {
        String amountText = amountEditText.getText().toString();
        if (amountText.isEmpty()) {
            Toast.makeText(requireContext(), "请输入金额", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountText);
        String type = typeSpinner.getSelectedItem().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String note = noteEditText.getText().toString();
        Date date = new Date();

        Record record = new Record(amount, type, category, note, date);

        // 保存到数据库
        RecordDao recordDao = new RecordDao(requireContext());
        long id = recordDao.addRecord(record);

        if (id != -1) {
            Toast.makeText(requireContext(), "记录保存成功", Toast.LENGTH_SHORT).show();
            // 返回主页
            ((MainActivity) requireActivity()).replaceFragment(new HomeFragment());
        } else {
            Toast.makeText(requireContext(), "记录保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}  