package team6.slidingtiles;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
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
public class BoardFragment extends Fragment {
    public BoardFragment() {
        // Required empty public constructor
    }

    private SelectionHandler selectionHandler;


    private GridView boardGrid;
    private GestureDetectorCompat detector;
    public float diffX;
    public float diffY;
    public float X1;
    public float Y1;
    public float X2;
    public float Y2;
    public int sizeX;
    public int sizeY;


    public int sizeperx;
    public int sizepery;

    public int i1;
    public int i2;
    public int j1;
    public int j2;
    public int i;

    int starti;
    int endi;

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
        View v = inflater.inflate(R.layout.fragment_board, container, false);
        selectionHandler = (SelectionHandler) getActivity();

        return v;
    }


    /**
     * @param savedInstanceState the saved instance state to be restored
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        selectionHandler.fragmentReady();
    }

    /**
     * @return a new instance of this fragment
     */
    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    /**
     * changes the boardLayout displayed in the gridview
     * @param boardLayout the boardLayout ArrayList to be displayed in the gridview
     */
    public void setBoardLayout(ArrayList<String> boardLayout) {
        final BoardArrayAdapter adapter = new BoardArrayAdapter
                (getView().getContext(), boardLayout, 700);
        boardGrid = getView().findViewById(R.id.board_grid);
        boardGrid.invalidateViews();
        boardGrid.setAdapter(adapter);
        boardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectionHandler.handleSelection(i);
            }
        });


        sizeX=boardGrid.getWidth();
        sizeY=boardGrid.getHeight();

        sizeperx=sizeX/5;
        sizepery=sizeY/5;

        detector = new GestureDetectorCompat(this.getContext(), new MyGestureListener());


        boardGrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return false;
            }
        });
    }
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {

            X1= event1.getX();
            X2= event2.getX();

            Y1=event1.getY();
            Y2=event2.getY();

            diffY = event2.getY() - event1.getY();
            diffX = event2.getX() - event1.getX();



            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
            }
            return true;
        }
    }

    private void onSwipeLeft() {
        for(i=1;i<=5;i++){
            if((X1>=sizeperx*(i-1))&&(X1<=sizeperx*(i)))
                j1=i;
        }

        for(int i=1;i<=5;i++){
            if((X2>=sizeperx*(i-1))&&(X2<=sizeperx*(i)))
                j2=i;
        }


        for(int i=1;i<=5;i++){
            if((Y1>=sizepery*(i-1))&&(Y1<=sizepery*(i)))
                i1=i;
        }


        i2=i1;

        i1=i1-1;
        j1=j1-1;
        i2=i2-1;
        j2=j2-1;
        starti=5*i1+j1;
        endi=5*i2+j2;
        selectionHandler.handleSWipe(starti,endi);

    }

    private void onSwipeRight() {

        for(i=1;i<=5;i++){
            if((X1>=sizeperx*(i-1))&&(X1<=sizeperx*(i)))
                j1=i;
        }

        for(i=1;i<=5;i++){
            if((X2>=sizeperx*(i-1))&&(X2<=sizeperx*(i)))
                j2=i;
        }


        for(i=1;i<=5;i++){
            if((Y1>=sizepery*(i-1))&&(Y1<=sizepery*(i)))
                i1=i;
        }


        i2=i1;

        i1=i1-1;
        j1=j1-1;
        i2=i2-1;
        j2=j2-1;
        starti=5*i1+j1;
        endi=5*i2+j2;
        selectionHandler.handleSWipe(starti,endi);


    }

    private void onSwipeTop() {

        for(i=1;i<=5;i++){
            if((X1>=sizeperx*(i-1))&&(X1<=sizeperx*(i)))
                j1=i;
        }

        j2=j1;

        for(i=1;i<=5;i++){
            if((Y1>=sizepery*(i-1))&&(Y1<=sizepery*(i)))
                i1=i;
        }


        for(i=1;i<=5;i++){
            if((Y2>=sizepery*(i-1))&&(Y2<=sizepery*(i)))
                i2=i;
        }
        i1=i1-1;
        j1=j1-1;
        i2=i2-1;
        j2=j2-1;
        starti=5*i1+j1;
        endi=5*i2+j2;
        selectionHandler.handleSWipe(starti,endi);


    }

    private void onSwipeBottom() {

        for(i=1;i<=5;i++){
            if((X1>=sizeperx*(i-1))&&(X1<=sizeperx*(i)))
                j1=i;
        }

        j2=j1;

        for(i=1;i<=5;i++){
            if((Y1>=sizepery*(i-1))&&(Y1<=sizepery*(i)))
                i1=i;
        }


        for(i=1;i<=5;i++){
            if((Y2>=sizepery*(i-1))&&(Y2<=sizepery*(i)))
                i2=i;
        }

        i1=i1-1;
        j1=j1-1;
        i2=i2-1;
        j2=j2-1;
        starti=5*i1+j1;
        endi=5*i2+j2;
        selectionHandler.handleSWipe(starti,endi);

    }

    /**
     * interface to handle communication from the fragment to activity
     */
    public interface SelectionHandler{
        boolean handleSWipe(int start, int end);
        boolean handleSelection(int position);
        void fragmentReady();

    }

}