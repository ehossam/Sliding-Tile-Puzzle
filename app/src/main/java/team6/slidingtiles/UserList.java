package team6.slidingtiles;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserList extends ArrayAdapter<UserScore>{

    private Activity context;
    List<UserScore> userList;

    public UserList(@NonNull Activity context, List<UserScore> userList) {
        super(context, R.layout.list_layout, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);

        TextView textViewName = listViewItem.findViewById(R.id.textViewName);
        TextView textViewScore = listViewItem.findViewById(R.id.textViewScore);

        UserScore userScore = userList.get(position);
        textViewName.setText(userScore.getName());
        textViewScore.setText(Integer.toString(userScore.getScore()));

        return listViewItem;

    }
}
