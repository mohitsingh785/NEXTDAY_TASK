package com.mohit.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("RESULTTTTTTT :"+ users);
        recyclerView = findViewById(R.id.usersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(this, users, user -> {
            // Handle favorite/unfavorite logic
        });
        recyclerView.setAdapter(adapter);

        loadUsers();
        Button showFavoritesButton = findViewById(R.id.show_favorites_button);
        showFavoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoriteUsersActivity.class);
            startActivity(intent);
        });
    }

    private void loadUsers() {
        String url = "https://reqres.in/api/users?page=2";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            User user = new User(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("email"),
                                    jsonObject.getString("first_name"),
                                    jsonObject.getString("last_name"),
                                    jsonObject.getString("avatar")
                            );
                            users.add(user);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
