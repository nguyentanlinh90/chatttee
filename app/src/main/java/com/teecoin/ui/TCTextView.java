package com.teecoin.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.AttributeSet;

import com.teecoin.R;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

public class TCTextView extends AppCompatTextView {

    String font_id;
    String icon_id;


    public TCTextView(Context context) {
        super(context);
        initAttrs(context, null, 0);
    }

    public TCTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs, 0);
    }

    public TCTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray attributes = context.getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.TCTextView, 0,
                            defStyle);

            try {
                font_id = attributes.getText(R.styleable.TCTextView_font_id).toString();
                if (!TCUtils.isEmpty(font_id)) {

                    setTypeface(TCUtils.getTypeface(String.format("%1$s%2$s", TCConstant.FONT_PATH, font_id), context));
                }
            } catch (Exception e) {
            }

            try {
                icon_id = attributes.getString(R.styleable.TCTextView_icon_id);
                setText(Html.fromHtml(icon_id));
            } catch (Exception e) {
            }

            if (attributes != null)
                attributes.recycle();
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

}
