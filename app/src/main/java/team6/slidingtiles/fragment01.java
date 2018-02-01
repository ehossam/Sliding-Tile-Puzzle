package team6.slidingtiles;


import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Context;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment01 extends Fragment {


    //global variables


    public fragment01() {
        // Required empty public constructor
    }

    //to do get a flag into this fragment and check that this movement can be really dome
    //check with a simple and and for the conditions

    public static final String ARG_BOARD_ARRAY = "boardArray";
    public int layoutH;

    SelectionHandler selectionHandler;
    public ArrayList<String> boardArray;
    public GridView boardGrid;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment01, container, false);
        selectionHandler = (SelectionHandler) getActivity();
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public static fragment01 newInstance() {
        fragment01 f = new fragment01();
        return f;
    }

    public void setBoardLayout(ArrayList<String> boardLayout) {
        BoardArrayAdapter adapter = new BoardArrayAdapter(getView().getContext(), boardLayout);
        adapter.setHeight(getView().getHeight());
        boardGrid = getView().findViewById(R.id.board_grid);
        boardGrid.invalidateViews();
        boardGrid.setAdapter(adapter);
        boardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(selectionHandler.handleSelection(i)) {
                }

            }
        });
    }

    public interface SelectionHandler{
        boolean handleSelection(int position);
    }
}