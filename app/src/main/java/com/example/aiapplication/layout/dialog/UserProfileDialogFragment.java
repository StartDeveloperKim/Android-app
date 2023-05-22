package com.example.aiapplication.layout.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.aiapplication.R;
import com.example.aiapplication.user.entity.Division;
import com.example.aiapplication.user.entity.Gender;
import com.example.aiapplication.user.entity.User;
import com.example.aiapplication.user.service.UserService;

import java.time.LocalDateTime;

import lombok.SneakyThrows;

public class UserProfileDialogFragment extends DialogFragment {

    private EditText etName;
    private EditText etAge;
    private RadioGroup rgGender;
    private RadioGroup rgStatus;

    private UserProfileDialogListener listener;

    public UserProfileDialogFragment(UserProfileDialogListener listener) {
        this.listener = listener;
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

        Log.i("다이어로그", "OPEN");

        builder.setView(dialogView)
                .setTitle("프로필 추가")
                .setPositiveButton("추가", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO :: 입력받지 못했거나, 선택 받지 못한 항목이 있다면 그것에 대한 오류 메시지를 띄우자

                        String name = etName.getText().toString();
                        int age = Integer.parseInt(etAge.getText().toString());

                        int selectedGenderId = rgGender.getCheckedRadioButtonId();
                        RadioButton selectedGenderRadioButton = dialogView.findViewById(selectedGenderId);
                        String gender = selectedGenderRadioButton.getText().toString();

                        int selectedStatusId = rgStatus.getCheckedRadioButtonId();
                        RadioButton selectedStatusRadioButton = dialogView.findViewById(selectedStatusId);
                        String status = selectedStatusRadioButton.getText().toString();

                        User user = User.builder()
                                .name(name)
                                .age(age)
                                .gender(Gender.getInstance(gender))
                                .division(Division.getInstance(status))
                                .createAt(LocalDateTime.now())
                                .build();

                        onDialogDismissed(user);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        builder.setView(dialogView);

        return builder.create();
    }

    private void onDialogDismissed(User user) {
        if (listener != null) {
            listener.drawTableLayoutByUserInfo(user);
        }
    }


}
