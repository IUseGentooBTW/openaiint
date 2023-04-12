package com.kay.openaiint;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kay.openaiint.api.ApiClient;
import com.kay.openaiint.api.OpenAIApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private List<Chat> chatHistoryList = new ArrayList<>();
    private OpenAIApi openAIApi;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        openAIApi = ApiClient.getClient().create(OpenAIApi.class);

        Button sendButton = findViewById(R.id.send_button);
        final EditText userInput = findViewById(R.id.user_input);
        final LinearLayout chatLayout = findViewById(R.id.chat_layout);
        Button saveButton = findViewById(R.id.save_button);
        Button searchButton = findViewById(R.id.search_button);
        final EditText searchInput = findViewById(R.id.search_input);
        final TextView chatHistory = findViewById(R.id.chat_history);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userText = userInput.getText().toString();
                userInput.setText("");
                String timestamp = dateFormat.format(new Date());
                chatHistoryList.add(new Chat(timestamp, userText, true));

                // ... The rest of the code in sendButton onClickListener remains the same
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ... The rest of the code in saveButton onClickListener remains the same
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchDate = searchInput.getText().toString();
                String searchResult = searchChat(searchDate);
                chatHistory.setText(searchResult);
            }
        });
    }

    private List<Chat> loadChatHistory() {
        return databaseHelper.getAllChats();
    }

    private String searchChat(String query) {
        List<Chat> chats = loadChatHistory();
        StringBuilder result = new StringBuilder();
        boolean chatFound = false;

        for (Chat chat : chats) {
            if (chat.getMessage().toLowerCase().contains(query.toLowerCase())) {
                result.append(chat.getTimestamp()).append(": ").append(chat.getMessage()).append("\n");
                chatFound = true;
            }
        }

        if (!chatFound) {
            result.append("No chat messages found containing the keyword: ").append(query);
        }

        return result.toString();

    }
}
