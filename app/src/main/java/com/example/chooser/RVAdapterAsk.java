package com.example.chooser;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ScrollingTabContainerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.support.v4.app.ActivityCompat.postponeEnterTransition;
import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static com.example.chooser.GLOBAL.DBHEALPER;
import static com.example.chooser.GLOBAL.MENUITEMSHARE;
import static com.example.chooser.GLOBAL.POSITIONLOADIMAGE;

public class RVAdapterAsk extends RecyclerView.Adapter<RVAdapterAsk.PersonViewHolder> {

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        static final int GALLERY_REQUEST = 1;
        static final int CAMERA_REQUEST = 2;
        CardView cv;
        EditText personName;
        ImageView clearImage;
        ImageView ImageViewAttach;
        TextView EmptyTextView;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            personName = itemView.findViewById(R.id.editTextVariantAsk);
            clearImage = itemView.findViewById(R.id.imageView_attachAsk_button);
            ImageViewAttach = itemView.findViewById(R.id.imageView_attach_ask);
            EmptyTextView = itemView.findViewById(R.id.empty_textView_Ask);
            personName.addTextChangedListener(new TextWatcher(){
                @Override public void afterTextChanged(Editable s) {}
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if(i < getItemCount())//надеюсь это больге не понадобится
                    if(variants.get(getAdapterPosition()).text!=null)
                    if(!variants.get(getAdapterPosition()).text.equals(personName.getText().toString()))MENUITEMSHARE.setVisible(false);
                    variants.get(getAdapterPosition()).text = personName.getText().toString();//это возможно костыль, его надо убрать
                }
            });
            clearImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final requestPermisions requestPermisions = new requestPermisions(mContext.getContext());
                    AlertDialog.Builder adb = new AlertDialog.Builder(mContext.getActivity());
                    LayoutInflater inflater = mContext.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog, null);
                    adb.setView(dialogView);
                    final AlertDialog alert = adb.create();
                    alert.show();
                    CardView cardViewCamera = dialogView.findViewById(R.id.dialog_cardView_camera);
                    CardView cardViewGallery = dialogView.findViewById(R.id.dialog_cardView_gallery);
                    cardViewCamera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(requestPermisions.requestCamera()) {
                                createDirectory();
                                POSITIONLOADIMAGE = getAdapterPosition();
                                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                StrictMode.setVmPolicy(builder.build());
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri());
                                mContext.startActivityForResult(intent, CAMERA_REQUEST);
                                alert.dismiss();
                            }
                        }
                    });
                    cardViewGallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(requestPermisions.requestStorage()){
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                POSITIONLOADIMAGE = getAdapterPosition();
                                mContext.startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                                alert.dismiss();
                            }
                        }
                    });
                }
            });

        }
        File directory;
        private void createDirectory() {
            directory = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    mContext.getActivity().getPackageName());
            if (!directory.exists())
                directory.mkdirs();
        }
        private Uri generateFileUri() {
            File file = new File(directory.getPath() + "/" + "photo_"
                            + System.currentTimeMillis() + ".jpg");
            LoadImage(Uri.fromFile(file));
            return Uri.fromFile(file);
        }
    }

    static List<Variant> variants;
    private FragmentAsk mContext;
    RVAdapterAsk(FragmentAsk  context){
        this.mContext = context;
        if (variants == null) variants = new ArrayList<>();
    }

    public void AddAllList(List<Variant> variantss){
        variants = new ArrayList<>();
        variants = variantss;
        //this.variants = variantss;
        //notifyDataSetChanged();
    }
    public void AddList(Variant variant){
        if (variants == null) variants = new ArrayList<>();
        variants.add(variant);
        //notifyItemRangeChanged(0, variants.size()-1);
        //notifyItemInserted(variants.size()-1);
    }
    public void ChooseWiner(String textHeadline){
        int position = new Random().nextInt(variants.size());
        for (int i = 0 ; i < variants.size(); i++)  variants.get(i).variant = false;
        notifyItemRangeChanged(0, variants.size());
        variants.get(position).variant = true;
        DBHEALPER.DB_add(variants, textHeadline);
        //notifyItemChanged(position);
        //notifyItemRangeChanged(position, variants.size()-1);
    }
    public void RemoveList(int position){
        MENUITEMSHARE.setVisible(false);
        if (variants != null) {
            variants.remove(position);
            notifyItemRemoved(position);
            //notifyItemRangeChanged(position, variants.size());если будут баги при удалении то необходимо вернуть
        }
    }
    public Variant ReturnVaariant() {
        for (int z = 0; z < variants.size(); z++) {
            if (variants.get(z).variant)
                return variants.get(z);
        }
        return variants.get(0);
    }
    public void LoadImage(Uri UriImage){
        variants.get(POSITIONLOADIMAGE).UriImage = UriImage;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_ask_layout, viewGroup, false);
        final PersonViewHolder pvh = new PersonViewHolder(v);
        if( viewGroup.getChildCount() == variants.size()-1) {
            pvh.cv.setVisibility(View.INVISIBLE);
            Visibles(pvh.cv);
        }
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {
        personViewHolder.cv.setBackgroundColor(Color.WHITE);
        if(variants.get(personViewHolder.getAdapterPosition()).variant) {
            personViewHolder.cv.setBackgroundColor(Color.GREEN);
        }
        personViewHolder.personName.setHint(i+1 + " " +variants.get(i).hint);
        personViewHolder.personName.setText(variants.get(i).text);
        if(variants.get(i).UriImage != null) {
            Display display = mContext.getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
                Picasso.get().load(variants.get(i).UriImage)
                        .rotate(getOrientation(variants.get(i).UriImage))
                        .resize(width, width)
                        .centerInside()
                        .into(personViewHolder.ImageViewAttach, new Callback() {
                            @Override public void onSuccess() {
                                personViewHolder.ImageViewAttach.setVisibility(View.VISIBLE);
                            }
                            @Override public void onError(Exception e) {
                                personViewHolder.EmptyTextView.setVisibility(View.VISIBLE);
                            }});
        }
    }
    private int getOrientation(Uri path){//узнаем ориентацию изображения для ее дальнейшего переворота
        ExifInterface exif;
        int orientation = 0;
        try {
            exif = new ExifInterface(getRealPathFromURI(path));
            orientation = exif.getAttributeInt( ExifInterface.TAG_ORIENTATION, 1 );
        }
        catch ( IOException e ) { e.printStackTrace(); }

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                orientation = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                orientation = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                orientation = 90;
                break;
        }
        return orientation;
    }
    public String getRealPathFromURI(Uri contentUri) {//преобразует URI в путь к файлу
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = mContext.getActivity().getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    public void Visibles(final View cardView){
            cardView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    cardView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (cardView.getVisibility() == View.INVISIBLE) {
                                int cx = Math.abs((cardView.getLeft() - cardView.getRight()) / 2);
                                int cy = Math.abs((cardView.getTop() - cardView.getBottom()) / 2);
                                int finalRadius = Math.max(cardView.getWidth(), cardView.getHeight());
                                Animator anim = ViewAnimationUtils.createCircularReveal(cardView, cx, cy, 0, finalRadius);
                                cardView.setVisibility(View.VISIBLE);
                                anim.start();
                            }
                        }
                    });
                }
            });
    }
    @Override
    public int getItemCount() {
        return variants.size();
    }
    }