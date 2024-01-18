package com.mohit.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoriteUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;

    private List<User> users = new ArrayList<>();

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_users);

        sharedPreferences = getSharedPreferences("Favorites", Context.MODE_PRIVATE);

        recyclerView = findViewById(R.id.favoriteUsersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new UserAdapter(this, users, user -> {
            // Handle click on favorite user (if needed)
        });
        recyclerView.setAdapter(adapter);
        loadUsers();
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

                            if(sharedPreferences.getBoolean(String.valueOf(user.getId()), false)) {
                                users.add(user);
                            }

                        }

                        adapter.notifyDataSetChanged();

                        // Check if the list is empty and show a toast if needed
                        if (users.isEmpty()) {
                            Toast.makeText(this, "Currently no items to show on fav", Toast.LENGTH_SHORT).show();
                        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private User parseUserDataFromJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int id = jsonObject.getInt("id");
            String email = jsonObject.getString("email");
            String firstName = jsonObject.getString("first_name");
            String lastName = jsonObject.getString("last_name");
            String avatar = jsonObject.getString("avatar");
            return new User(id, email, firstName, lastName, avatar);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
