package com.example.aiapplication.layout.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.aiapplication.R;
import com.example.aiapplication.layout.UserActivity;
import com.example.aiapplication.user.dto.UserInfo;
import com.example.aiapplication.user.entity.Division;
import com.example.aiapplication.user.entity.Gender;
import com.example.aiapplication.user.entity.User;

public class UserProfileDialogFragment extends DialogFragment {

    private EditText etName;
    private EditText etAge;
    private RadioGroup rgGender;
    private RadioGroup rgStatus;

    private UserProfileDialogListener listener;
    private User user;

    public UserProfileDialogFragment(UserProfileDialogListener listener) {
        this.listener = listener;
    }

    public UserProfileDialogFragment(UserProfileDialogListener listener, User user) {
        this(listener);
        this.user = user;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.user_dialog, null);

        etName = dialogView.findViewById(R.id.et_name);
        etAge = dialogView.findViewById(R.id.et_age);
        rgGender = dialogView.findViewById(R.id.rg_gender);
        rgStatus = dialogView.findViewById(R.id.rg_status);

        if (user != null) {
            etName.setText(user.getName());
            etAge.setText(String.valueOf(user.getAge()));
            checkGenderRadioBtn(dialogView);
            checkDivisionRadioBtn(dialogView);

            builder.setTitle("프로필 수정")
                    .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.updateUserInfo(getUserInfoAtDialog(dialogView), user.getId());
                        }
                    });
        }else{
            builder.setTitle("프로필 추가")
                    .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.saveUserInfo(getUserInfoAtDialog(dialogView));
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

    private UserInfo getUserInfoAtDialog(View dialogView) {
        // TODO :: 입력받지 못했거나, 선택 받지 못한 항목이 있다면 그것에 대한 오류 메시지를 띄우자

        String name = etName.getText().toString();
        int age = Integer.parseInt(etAge.getText().toString());

        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        RadioButton selectedGenderRadioButton = dialogView.findViewById(selectedGenderId);
        String gender = selectedGenderRadioButton.getText().toString();

        int selectedStatusId = rgStatus.getCheckedRadioButtonId();
        RadioButton selectedStatusRadioButton = dialogView.findViewById(selectedStatusId);
        String status = selectedStatusRadioButton.getText().toString();

        return new UserInfo(name, age, Gender.getInstance(gender), Division.getInstance(status));
    }


}
