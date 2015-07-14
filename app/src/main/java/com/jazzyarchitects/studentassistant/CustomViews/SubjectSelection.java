package com.jazzyarchitects.studentassistant.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.R;

/**
 * Created by Jibin_ism on 12-Jul-15.
 */
public class SubjectSelection extends CardView {

    Subject subject;
    private String NAMESPACE="http://schemas.android.com/apk/res/android";
    int textResource;
    AttributeSet attr=null;
    CardView cardView;
    TextView subjectName;

    public SubjectSelection(Context context) {
        this(context,null);
    }

    public SubjectSelection(Subject subject, Context context) {
        this(context,null);
        this.subject=subject;
    }
    public SubjectSelection(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubjectSelection(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attr=attrs;
        initialize(attrs);
    }

    public void initialize(AttributeSet attrs){
        inflate(getContext(), R.layout.layout_subject_selection, this);
        cardView = (CardView)findViewById(R.id.card);
        subjectName = (TextView)cardView.findViewById(R.id.subjectName);

        if(attrs!=null){
            textResource=attrs.getAttributeResourceValue(NAMESPACE,"text",0);
            if(textResource!=0){
                subjectName.setText(textResource);
            }
        }
    }

    public void set(){
        subjectName.setText(subject.getSubject());

        subjectName.setBackgroundColor(subject.getColor());
        if(Constants.isColorDark(subject.getColor())){
            subjectName.setTextColor(Color.WHITE);
        }

    }

    public Subject getSuject(){
        return this.subject;
    }

    public void setSubject(Subject subject){
        this.subject=subject;
        this.cardView.setTag(subject);
        set();
    }

    public void setDragStartListener(OnLongClickListener listener){
        this.cardView.setOnLongClickListener(listener);
    }
}
