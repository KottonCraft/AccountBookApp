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

public class EditRecordFragment extends Fragment {

    private static final String ARG_RECORD = "record";

    private Spinner typeSpinner;
    private Spinner categorySpinner;
    private EditText amountEditText;
    private EditText noteEditText;
    private Button saveButton;
    private Button deleteButton;

    private Record record;

    public static EditRecordFragment newInstance(Record record) {
        EditRecordFragment fragment = new EditRecordFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECORD, record);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            record = (Record) getArguments().getSerializable(ARG_RECORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_record, container, false);

        // 初始化视图组件
        typeSpinner = view.findViewById(R.id.type_spinner);
        categorySpinner = view.findViewById(R.id.category_spinner);
        amountEditText = view.findViewById(R.id.amount_edit_text);
        noteEditText = view.findViewById(R.id.note_edit_text);
        saveButton = view.findViewById(R.id.save_button);
        deleteButton = view.findViewById(R.id.delete_button);

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

        // 填充现有记录数据
        if (record != null) {
            // 设置类型选择器位置
            if (record.getType().equals("收入")) {
                typeSpinner.setSelection(0);
            } else {
                typeSpinner.setSelection(1);
            }

            // 更新分类选择器
            updateCategorySpinner();

            // 设置分类选择器位置
            ArrayAdapter<CharSequence> categoryAdapter = (ArrayAdapter<CharSequence>) categorySpinner.getAdapter();
            int categoryPosition = categoryAdapter.getPosition(record.getCategory());
            if (categoryPosition != -1) {
                categorySpinner.setSelection(categoryPosition);
            }

            // 设置金额和备注
            amountEditText.setText(String.valueOf(record.getAmount()));
            noteEditText.setText(record.getNote());
        }

        // 设置保存按钮点击事件
        saveButton.setOnClickListener(v -> updateRecord());

        // 设置删除按钮点击事件
        deleteButton.setOnClickListener(v -> deleteRecord());

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

    private void updateRecord() {
        if (record == null) {
            Toast.makeText(requireContext(), "记录为空，无法更新", Toast.LENGTH_SHORT).show();
            return;
        }

        String amountText = amountEditText.getText().toString();
        if (amountText.isEmpty()) {
            Toast.makeText(requireContext(), "请输入金额", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountText);
        String type = typeSpinner.getSelectedItem().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String note = noteEditText.getText().toString();
        Date date = record.getDate(); // 保持原日期不变

        // 更新记录对象
        record.setAmount(amount);
        record.setType(type);
        record.setCategory(category);
        record.setNote(note);
        record.setDate(date);

        // 更新到数据库
        RecordDao recordDao = new RecordDao(requireContext());
        int rowsAffected = recordDao.updateRecord(record);

        if (rowsAffected > 0) {
            Toast.makeText(requireContext(), "记录更新成功", Toast.LENGTH_SHORT).show();
            // 返回主页
            ((MainActivity) requireActivity()).replaceFragment(new HomeFragment());
        } else {
            Toast.makeText(requireContext(), "记录更新失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteRecord() {
        if (record == null) {
            Toast.makeText(requireContext(), "记录为空，无法删除", Toast.LENGTH_SHORT).show();
            return;
        }

        // 确认对话框
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setTitle("确认删除")
                .setMessage("确定要删除这条记录吗？")
                .setPositiveButton("删除", (dialog, which) -> {
                    // 从数据库删除
                    RecordDao recordDao = new RecordDao(requireContext());
                    int rowsAffected = recordDao.deleteRecord(record.getId());

                    if (rowsAffected > 0) {
                        Toast.makeText(requireContext(), "记录删除成功", Toast.LENGTH_SHORT).show();
                        // 返回主页
                        ((MainActivity) requireActivity()).replaceFragment(new HomeFragment());
                    } else {
                        Toast.makeText(requireContext(), "记录删除失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }
}