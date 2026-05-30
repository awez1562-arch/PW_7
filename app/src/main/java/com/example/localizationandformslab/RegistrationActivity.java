package com.example.localizationandformslab;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "Registration";

    private EditText etFio, etLogin, etEmail, etPhone;
    private EditText etPassword, etConfirmPassword, etBirthDate;
    private Button btnSelectDate, btnRegister;
    private Spinner spinnerGroup;
    private CheckBox cbAgree;

    private String selectedBirthDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Инициализация элементов
        etFio = findViewById(R.id.etFio);
        etLogin = findViewById(R.id.etLogin);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etBirthDate = findViewById(R.id.etBirthDate);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        spinnerGroup = findViewById(R.id.spinnerGroup);
        cbAgree = findViewById(R.id.cbAgree);
        btnRegister = findViewById(R.id.btnRegister);

        // Настройка Spinner
        String[] groups = {"ИНС-б-о-24-1", "ИНС-б-о-24-2", "ИНС-б-о-25"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                groups
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(adapter);

        // Обработчик выбора даты
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        etBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Обработчик кнопки регистрации
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterClick();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedBirthDate = String.format("%02d.%02d.%04d",
                                dayOfMonth, month + 1, year);
                        etBirthDate.setText(selectedBirthDate);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void onRegisterClick() {
        String fio = etFio.getText().toString().trim();
        String login = etLogin.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String birthDate = etBirthDate.getText().toString().trim();
        boolean isAgreed = cbAgree.isChecked();

        boolean isValid = true;

        // Валидация ФИО (только кириллица, пробелы и дефис)
        if (!fio.matches("^[А-Яа-яЁё\\s-]+$")) {
            etFio.setError("Только русские буквы, пробелы и дефис");
            Log.w(TAG, "Неверный формат ФИО: " + fio);
            isValid = false;
        }

        // Валидация логина (только латиница)
        if (!login.matches("^[A-Za-z]+$")) {
            etLogin.setError("Только латинские буквы");
            Log.w(TAG, "Неверный формат логина: " + login);
            isValid = false;
        }

        // Валидация email
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            etEmail.setError("Неверный формат email");
            Log.w(TAG, "Неверный формат email: " + email);
            isValid = false;
        }

        // Валидация телефона (формат +7XXXXXXXXXX)
        if (!phone.matches("^\\+7\\d{10}$")) {
            etPhone.setError("Формат: +7XXXXXXXXXX");
            Log.w(TAG, "Неверный формат телефона: " + phone);
            isValid = false;
        }

        // Валидация пароля (минимум 6 символов, хотя бы одна цифра и заглавная буква)
        if (password.length() < 6) {
            etPassword.setError("Пароль должен быть не менее 6 символов");
            Log.w(TAG, "Слишком короткий пароль");
            isValid = false;
        } else if (!password.matches(".*\\d.*") || !password.matches(".*[A-Z].*")) {
            etPassword.setError("Пароль должен содержать хотя бы одну цифру и заглавную букву");
            Log.w(TAG, "Пароль не соответствует требованиям сложности");
            isValid = false;
        }

        // Проверка совпадения паролей
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Пароли не совпадают");
            Log.w(TAG, "Пароли не совпадают");
            isValid = false;
        }

        // Валидация даты рождения
        if (!birthDate.matches("^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[012])\\.(19|20)\\d{2}$")) {
            etBirthDate.setError("Формат: ДД.ММ.ГГГГ");
            Log.w(TAG, "Неверный формат даты: " + birthDate);
            isValid = false;
        } else {
            // Проверка диапазона дат (1900 - текущий год)
            try {
                String[] parts = birthDate.split("\\.");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);

                Calendar birthCalendar = Calendar.getInstance();
                birthCalendar.set(year, month - 1, day);

                Calendar now = Calendar.getInstance();
                Calendar minDate = Calendar.getInstance();
                minDate.set(1900, 0, 1);

                if (birthCalendar.after(now) || birthCalendar.before(minDate)) {
                    etBirthDate.setError("Дата должна быть между 1900 и текущей датой");
                    Log.w(TAG, "Дата вне допустимого диапазона: " + birthDate);
                    isValid = false;
                }
            } catch (Exception e) {
                etBirthDate.setError("Неверная дата");
                Log.w(TAG, "Ошибка парсинга даты: " + e.getMessage());
                isValid = false;
            }
        }

        // Проверка согласия
        if (!isAgreed) {
            cbAgree.setError("Необходимо согласие");
            Log.w(TAG, "Согласие не отмечено");
            isValid = false;
        }

        if (isValid) {
            Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_LONG).show();
            Log.i(TAG, "Регистрация успешна: " + fio);
            // Здесь можно сохранить данные или перейти к следующей активности
        } else {
            Toast.makeText(this, "Исправьте ошибки в форме", Toast.LENGTH_LONG).show();
        }
    }
}