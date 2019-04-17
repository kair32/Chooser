package com.AKS.chooser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.AKS.chooser.GLOBAL.FRAGMENTTRANSITION;
import static com.AKS.chooser.GLOBAL.HISTORYVARIANT;
import static com.AKS.chooser.GLOBAL.MENUITEMSHARE;
import static com.AKS.chooser.RVAdapterAsk.PersonViewHolder.CAMERA_REQUEST;
import static com.AKS.chooser.RVAdapterAsk.PersonViewHolder.GALLERY_REQUEST;

public class FragmentAsk extends Fragment {
    private RecyclerView rv;
    private boolean butonckick = false;
    private EditText HeadlineEditText;
    private RVAdapterAsk adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragment = inflater.inflate(R.layout.ask_fragment,container,false);
        adapter = new RVAdapterAsk(this);

        Toolbar toolbar = fragment.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        rv = fragment.findViewById(R.id.rv);
        final LinearLayoutManager llm = new LinearLayoutManager(fragment.getContext());
        rv.setLayoutManager(llm);

        FRAGMENTTRANSITION = getFragmentManager().beginTransaction();
        FRAGMENTTRANSITION
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .addToBackStack(null);

        HeadlineEditText = fragment.findViewById(R.id.editTextHedalineVariant);
        HeadlineEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) { MENUITEMSHARE.setVisible(false); }
        });
        Button button = fragment.findViewById(R.id.button_ask);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getItemCount()>0) {
                    if (!HeadlineEditText.getText().toString().equals("")){
                        butonckick = true;
                        FRAGMENTTRANSITION
                                .replace(R.id.container, new FragmentLoading())
                                .commit();
                        onDestroy();
                        //getActivity().getSupportFragmentManager().popBackStack();
                        if (fragment != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                }else {
                        Toast.makeText(getContext(),getString(R.string.why_headline_clear),Toast.LENGTH_LONG).show();
                    }
                }else Snackbar.make(view, getString(R.string.add_variant), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.add), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AddAdapter();
                            }
                        }).show();
            }
        });
        FloatingActionButton fab = fragment.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MENUITEMSHARE.setVisible(false);
                AddAdapter();
            }
        });
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MENUITEMSHARE = menu.findItem(R.id.action_share);
        if(butonckick) {
            MENUITEMSHARE.setVisible(true);
            butonckick = false;
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (item.getItemId()){
            case R.id.action_settings:
                FRAGMENTTRANSITION
                        .replace(R.id.container, new FragmentSetting())
                        .commit();
                onDestroy();
                return super.onOptionsItemSelected(item);
            case R.id.action_history:
                FRAGMENTTRANSITION
                        .replace(R.id.container, new FragmentHistory())
                        .commit();

                return super.onOptionsItemSelected(item);
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.Share_text) + HeadlineEditText.getText().toString() + getString(R.string.Share_text_2 )+ adapter.ReturnVaariant().text + getString(R.string.Share_text_3) + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                sendIntent.putExtra(Intent.EXTRA_STREAM, adapter.ReturnVaariant().UriImage);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.Share_title)));
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    adapter.LoadImage(selectedImage);
                    rv.setAdapter(adapter);
                    setUpItemTouchHelper();
                }
                return;
            case CAMERA_REQUEST:
                if(resultCode == RESULT_OK) {
                    adapter.takePikture = true;
                    rv.setAdapter(adapter);
                    setUpItemTouchHelper();
                }
                return;
        }
    }

    private void AddAdapter(){
        adapter.AddList(new Variant(getString(R.string.variant), false));
        rv.setAdapter(adapter);
        setUpItemTouchHelper();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(butonckick)
            adapter.ChooseWiner(HeadlineEditText.getText().toString());
        if(HISTORYVARIANT != null || HISTORYVARIANT.size() != 0) {
            for (int i = 0; i < HISTORYVARIANT.size(); i++) {
                if (HISTORYVARIANT.get(i).varChoice != null) {
                    List<Variant> variant = HISTORYVARIANT.get(i).variant;
                    HeadlineEditText.setText(HISTORYVARIANT.get(i).HeadLine);
                    adapter.AddAllList(variant);
                    butonckick = true;
                    rv.setAdapter(adapter);
                    setUpItemTouchHelper();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        rv.setAdapter(adapter);
        setUpItemTouchHelper();
    }

    @Override
    public void onStop() {
        super.onStop();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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