package com.AKS.chooser;

import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import static com.AKS.chooser.GLOBAL.DBHEALPER;
import static com.AKS.chooser.GLOBAL.HISTORYVARIANT;

public class RVAdapterHstory extends RecyclerView.Adapter<RVAdapterHstory.QuestionViewHolder> {

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView VariantTextView;
        TextView HeadlineTextView;
        TextView EmptyTextView;
        ImageView variantImage;

        QuestionViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv_history);
            VariantTextView = itemView.findViewById(R.id.textView_variant_history);
            EmptyTextView = itemView.findViewById(R.id.empty_textView_History);
            HeadlineTextView = itemView.findViewById(R.id.textView_headline_history);
            variantImage = itemView.findViewById(R.id.imageView_history);
        }
    }

    List<VariantHistory> question;
    private FragmentHistory mContext;

    RVAdapterHstory(List<VariantHistory> question,FragmentHistory  context){
        this.mContext = context;
        this.question = question;
    }
    public void RemoveList(int position){
        if (question != null) {
            question.remove(position);
            DBHEALPER.DBclear();
            for (int j = 0; j< question.size(); j++)
            {
                DBHEALPER.DB_add(question.get(j).variant,question.get(j).HeadLine);
            }
            if(question.size()== 0) mContext.getActivity().getSupportFragmentManager().popBackStack();
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, question.size());
        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RVAdapterHstory.QuestionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_history_layout, viewGroup, false);
        RVAdapterHstory.QuestionViewHolder pvh = new RVAdapterHstory.QuestionViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(@NonNull final QuestionViewHolder questionViewHolder, final int i) {
        questionViewHolder.HeadlineTextView.setText(question.get(i).HeadLine);
        for (int j = 0; j < question.get(i).variant.size(); j ++){
            if(question.get(i).variant.get(j).variant) {
                questionViewHolder.VariantTextView.setText(question.get(i).variant.get(j).text);
                if (question.get(i).variant.get(j).UriImage!=null)
                    Picasso.get().load(question.get(i).variant.get(j).UriImage)
                            .rotate(getOrientation(question.get(i).variant.get(j).UriImage))
                            //.resize(width, width)
                            //.centerInside()
                            .into(questionViewHolder.variantImage, new Callback() {
                                @Override public void onSuccess() {
                                    questionViewHolder.variantImage.setVisibility(View.VISIBLE);
                                }
                                @Override public void onError(Exception e) {
                                    questionViewHolder.EmptyTextView.setVisibility(View.VISIBLE);
                                }});
            }
        }
        questionViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HISTORYVARIANT.get(i).varChoice = true;
                mContext.getActivity().getSupportFragmentManager().popBackStack();
            }
        });
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

    @Override public int getItemCount() {
        return question.size();
    }
}