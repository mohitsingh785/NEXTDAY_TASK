package com.mohit.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;
    private LayoutInflater inflater;
    private OnItemClickListener listener;
    private SharedPreferences sharedPreferences;

    public UserAdapter(Context context, List<User> users, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.users = users;
        this.listener = listener;
        this.sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.idTextView.setText("ID: " + user.getId());
        holder.emailTextView.setText("Email: " + user.getEmail());
        holder.nameTextView.setText("Name: " + user.getFirstName() + " " + user.getLastName());
        Glide.with(holder.avatarImageView.getContext())
                .load(user.getAvatar())
                .into(holder.avatarImageView);

        updateFavoriteIcon(holder.favoriteIcon, user);
        holder.favoriteIcon.setOnClickListener(v -> {
            toggleFavoriteStatus(user);
            updateFavoriteIcon(holder.favoriteIcon, user);
            listener.onItemClick(user);
        });
    }

    private void toggleFavoriteStatus(User user) {
        boolean isFav = isFavorite(user);
        sharedPreferences.edit().putBoolean(String.valueOf(user.getId()), !isFav).apply();
    }

    private void updateFavoriteIcon(ImageView icon, User user) {
        icon.setImageResource(isFavorite(user) ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);
    }

    private boolean isFavorite(User user) {
        return sharedPreferences.getBoolean(String.valueOf(user.getId()), false);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatarImageView;
        public TextView idTextView;
        public TextView nameTextView;
        public TextView emailTextView;
        public ImageView favoriteIcon;

        public UserViewHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.user_image);
            idTextView = itemView.findViewById(R.id.user_idd);
            nameTextView = itemView.findViewById(R.id.user_name);
            emailTextView = itemView.findViewById(R.id.user_emaill);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }
}
