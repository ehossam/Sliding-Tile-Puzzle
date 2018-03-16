package team6.slidingtiles;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.jar.Attributes;


/**
 * A simple {@link Fragment} subclass.
 */
public class AiBoardFragment extends Fragment {
    public AiBoardFragment() {
        // Required empty public constructor
    }

    private AiSelectionHandler aiSelectionHandler;


    private GridView boardGrid;

    /**
     *
     * @param inflater the layout inflater to be used
     * @param container the layout containing the fragment
     * @param savedInstanceState the saved instance state if there is one
     * @return returns the new fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ai_board, container, false);
        aiSelectionHandler = (AiSelectionHandler) getActivity();

        return v;
    }

    /**
     * @param savedInstanceState the saved instance state to be restored
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        aiSelectionHandler.aiFragmentReady();
    }

    /**
     * @return a new instance of this fragment
     */
    public static AiBoardFragment newInstance() {
        return new AiBoardFragment();
    }

    /**
     * changes the boardLayout displayed in the gridview
     * @param boardLayout the boardLayout ArrayList to be displayed in the gridview
     */
    public void setBoardLayout(ArrayList<String> boardLayout) {
        if (getView() != null) {
            final BoardArrayAdapter adapter = new BoardArrayAdapter
                    (getView().getContext(), boardLayout, getView().getHeight(), getView().getWidth());
            boardGrid = getView().findViewById(R.id.board_grid_ai);
            boardGrid.invalidateViews();
            boardGrid.setAdapter(adapter);
        }
    }

    /**
     * interface to handle communication from the fragment to activity
     */
    public interface AiSelectionHandler{
        void aiFragmentReady();
    }

}