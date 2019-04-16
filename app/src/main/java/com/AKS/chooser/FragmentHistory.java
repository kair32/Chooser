package com.AKS.chooser;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.AKS.chooser.GLOBAL.HISTORYVARIANT;
import static com.AKS.chooser.GLOBAL.DBHEALPER;

public class FragmentHistory extends Fragment {
    private RecyclerView rv;
    private RVAdapterHstory adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.history_fragment,container,false);
        TextView txt = fragment.findViewById(R.id.textView_text_clear_history);
        DBHEALPER.DBread();
        Toolbar toolbar = fragment.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.setting_history));
        toolbar.setNavigationIcon(R.drawable.backspace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        Button button_clear = fragment.findViewById(R.id.button_history_clear);
        if(HISTORYVARIANT == null || HISTORYVARIANT.size() == 0) {txt.setVisibility(View.VISIBLE);button_clear.setVisibility(View.GONE);}
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HISTORYVARIANT!=null) HISTORYVARIANT.clear();
                DBHEALPER.DBclear();
                Toast.makeText(getContext(),getString(R.string.History_clear),Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        rv = fragment.findViewById(R.id.rv_history);
        LinearLayoutManager llm = new LinearLayoutManager(fragment.getContext());
        rv.setLayoutManager(llm);
        if(HISTORYVARIANT!= null) initializeAdapter();
        return fragment;
    }
    private void initializeAdapter(){
        adapter = new RVAdapterHstory(HISTORYVARIANT,this);
        rv.setAdapter(adapter);
        setUpItemTouchHelper();
    }
    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;
            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(getContext(), R.drawable.delete);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                initiated = true;
            }
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                adapter.RemoveList(viewHolder.getAdapterPosition());
            }
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                if (viewHolder.getAdapterPosition() == -1) {
                    return;
                }
                if (!initiated) {
                    init();
                }
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();
                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
                xMark.draw(c);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(rv);
    }
}
