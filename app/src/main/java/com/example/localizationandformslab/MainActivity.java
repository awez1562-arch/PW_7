package com.example.localizationandformslab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button btnRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        btnRegistration = findViewById(R.id.btnRegistration);

        // Получаем массив из ресурсов
        String[] groupmates = getResources().getStringArray(R.array.groupmates_array);

        // Создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                groupmates
        );

        // Применяем адаптер к ListView
        listView.setAdapter(adapter);

        // Обработчик нажатия на элемент списка
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this,
                        "Выбрано: " + selected,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Переход к форме регистрации
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}