package com.example.aiapplication.layout.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.DialogFragment;

import com.example.aiapplication.R;
import com.example.aiapplication.user.dto.UserInfo;
import com.example.aiapplication.user.entity.Division;
import com.example.aiapplication.user.entity.Gender;
import com.example.aiapplication.user.entity.User;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class UserProfileDialogFragment extends DialogFragment {

    private EditText etName;
    private EditText etAge;
    private RadioGroup rgGender;
    private RadioGroup rgStatus;

    private ToggleButton activeButton;

    private UserProfileDialogListener listener;
    private User user;
    private Long activeUserId;

    public UserProfileDialogFragment(UserProfileDialogListener listener) {
        this.listener = listener;
    }

    public UserProfileDialogFragment(UserProfileDialogListener listener, User user, Long activeUerId) {
        this(listener);
        this.user = user;
        this.activeUserId = activeUerId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.user_dialog, null);

        etName = dialogView.findViewById(R.id.et_name);

        etAge = dialogView.findViewById(R.id.et_age);
        etAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNumberPicker(view);
            }
        });

        rgGender = dialogView.findViewById(R.id.rg_gender);
        rgStatus = dialogView.findViewById(R.id.rg_status);

        if (user != null) {
            etName.setText(user.getName());
            etAge.setText(String.valueOf(user.getAge()));
            checkGenderRadioBtn(dialogView);
            checkDivisionRadioBtn(dialogView);
            setToggleButton(dialogView);

            builder.setTitle("프로필 수정")
                    .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isAllDataInput(dialogView)) {
                                listener.updateUserInfo(getUserInfoAtDialog(dialogView), user.getId());
                            }
                        }
                    })
                    .setNeutralButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.removeUser(user.getId());
                        }
                    });
        }else{
            builder.setTitle("프로필 추가")
                    .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isAllDataInput(dialogView)) {
                                listener.saveUserInfo(getUserInfoAtDialog(dialogView));
                            }
                        }
                    });
        }

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setView(dialogView);

        return builder.create();
    }


    private void setNumberPicker(View view) {
        final NumberPicker numberPicker = new NumberPicker(view.getContext());
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);
        numberPicker.setWrapSelectorWheel(false);

        AlertDialog.Builder numberPickerDialog = new AlertDialog.Builder(view.getContext());
        numberPickerDialog.setTitle("나이 선택");

        LinearLayout numberPickerLayout = new LinearLayout(view.getContext());
        numberPickerLayout.setGravity(Gravity.CENTER);
        numberPickerLayout.addView(numberPicker);
        numberPickerDialog.setView(numberPickerLayout);

        numberPickerDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etAge.setText(String.valueOf(numberPicker.getValue()));
            }
        });

        numberPickerDialog.setNegativeButton("취소", null);
        numberPickerDialog.show();
    }


    private void checkDivisionRadioBtn(View dialogView) {
        RadioButton divisionRadioBtn;
        if (user.equalsDivision(Division.CHILDREN)) {
            divisionRadioBtn = dialogView.findViewById(R.id.rb_child);
        } else if (user.equalsDivision(Division.SENIOR)) {
            divisionRadioBtn = dialogView.findViewById(R.id.rb_elderly);
        }else{
            divisionRadioBtn = dialogView.findViewById(R.id.rb_pregnant);
        }
        divisionRadioBtn.setChecked(true);
    }

    private void checkGenderRadioBtn(View dialogView) {
        RadioButton genderRadioBtn;
        if (user.equalsGender(Gender.MALE)) {
            genderRadioBtn = dialogView.findViewById(R.id.rb_male);
        }else{
            genderRadioBtn = dialogView.findViewById(R.id.rb_female);
        }
        genderRadioBtn.setChecked(true);
    }

    private boolean isAllDataInput(View view) {
        if (etName.length() != 0 && etAge.getText() != null && rgGender.getCheckedRadioButtonId() != RadioGroup.NO_ID && rgStatus.getCheckedRadioButtonId() != RadioGroup.NO_ID) {
            return true;
        }else{
            Toast.makeText(view.getContext(), "프로필이 저장되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private UserInfo getUserInfoAtDialog(View dialogView) {
        String name = etName.getText().toString();
        int age = Integer.parseInt(etAge.getText().toString());

        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        RadioButton selectedGenderRadioButton = dialogView.findViewById(selectedGenderId);
        String gender = selectedGenderRadioButton.getText().toString();

        int selectedStatusId = rgStatus.getCheckedRadioButtonId();
        RadioButton selectedStatusRadioButton = dialogView.findViewById(selectedStatusId);
        String status = selectedStatusRadioButton.getText().toString();

        if (activeButton != null) {
            boolean checked = activeButton.isChecked();
            return new UserInfo(name, age, Gender.getInstance(gender), Division.getInstance(status), checked);
        }

        return new UserInfo(name, age, Gender.getInstance(gender), Division.getInstance(status));
    }

    private void setToggleButton(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.toggle_linear); // LinearLayout의 ID를 알맞게 변경해주세요

        activeButton = new ToggleButton(view.getContext());

        activeButton.setTextOff("프로필 비 활성화");
        activeButton.setTextOn("프로필 활성화");
        activeButton.setChecked((user.getId() == activeUserId));

        linearLayout.addView(activeButton);
    }



}
