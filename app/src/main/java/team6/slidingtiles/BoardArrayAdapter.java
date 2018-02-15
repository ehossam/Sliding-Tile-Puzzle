package team6.slidingtiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;

import java.util.ArrayList;

/**
 * Created by cheesea on 2/1/18.
 */


public class BoardArrayAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> boardLayout;
    private int height;

    /**
     * constructor for the adapter.
     * @param context context
     * @param boardLayout the current board layout as an ArrayList
     * @param height the height of the containing activity
     */
    BoardArrayAdapter(Context context, ArrayList<String> boardLayout, int height) {
        super();
        this.context = context;
        this.boardLayout = boardLayout;
        this.height = height/5;
    }

    /**
     * @return the number of strings in the board layout
     */
    public int getCount() {
        return boardLayout.size();
    }

    /**
     * @param position the position of the requested string
     * @return the string requested
     */
    public String getItem(int position) {
        return boardLayout.get(position);
    }
    public long getItemId(int position) {
        return 0;
    }

    /**
     * @param position the position of the view in the list
     * @param convertView the view if it already exists
     * @param parent the calling parent
     * @return returns the ImageView
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        ImageView imageView;
        if (convertView == null)
            imageView = (ImageView) inflater.inflate(R.layout.g_image_view, null);
        else
            imageView = (ImageView) convertView;

        imageView.setMinimumHeight(300);
        switch (boardLayout.get(position)){
            case " ":
                imageView.setImageResource(R.drawable.puzzle0);
                imageView.setVisibility(View.INVISIBLE);
                break;
            case "+":
                imageView.setImageResource(R.drawable.puzzle_add);
                break;
            case "-":
                imageView.setImageResource(R.drawable.puzzle_sub);
                break;
            case "=":
                imageView.setImageResource(R.drawable.puzzle_equal);
                break;
            case"/":
                imageView.setImageResource(R.drawable.puzzle_divide);
                break;
            case "*":
                imageView.setImageResource(R.drawable.puzzle_multiply);
                break;
            default:
                imageView.setImageResource(tiles[Integer.valueOf(boardLayout.get(position))]);
                break;
        }
        return imageView;
    }

    /**
     * an array of the ids of the tile drawables
     */
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