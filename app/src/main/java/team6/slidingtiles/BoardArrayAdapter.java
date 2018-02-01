package team6.slidingtiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by cheesea on 2/1/18.
 */

public class BoardArrayAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> boardLayout;
    private int height;

    public BoardArrayAdapter(Context context, ArrayList<String> boardLayout) {
        super();
        this.context = context;
        this.boardLayout = boardLayout;
    }

    public int getCount() {
        return boardLayout.size();
    }

    public String getItem(int position) {
        return boardLayout.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                context.LAYOUT_INFLATER_SERVICE);
        ImageView imageView;
        if (convertView == null) {
            imageView = (ImageView) inflater.inflate(R.layout.g_image_view, null);
        } else {
            imageView = (ImageView) convertView;
        }

        if (boardLayout.get(position).compareTo(" ") == 0)
            imageView.setImageResource(tiles[0]);
        else
            imageView.setImageResource(tiles[Integer.valueOf(boardLayout.get(position))]);
        return imageView;
    }

    void setHeight(int height){
        this.height = height;
    }

    // references to our images
    private Integer[] tiles = {
            R.drawable.puzzle0,
            R.drawable.puzzle1, R.drawable.puzzle2,
            R.drawable.puzzle3, R.drawable.puzzle4,
            R.drawable.puzzle5, R.drawable.puzzle6,
            R.drawable.puzzle7, R.drawable.puzzle8,
            R.drawable.puzzle9, R.drawable.puzzle10,
            R.drawable.puzzle11, R.drawable.puzzle12,
            R.drawable.puzzle13, R.drawable.puzzle14,
            R.drawable.puzzle15, R.drawable.puzzle16,
            R.drawable.puzzle17, R.drawable.puzzle18,
            R.drawable.puzzle19, R.drawable.puzzle20,
            R.drawable.puzzle21, R.drawable.puzzle22,
            R.drawable.puzzle23, R.drawable.puzzle24

    };
}