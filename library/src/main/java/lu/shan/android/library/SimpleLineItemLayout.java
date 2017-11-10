package lu.shan.android.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Jaren on 2017/9/28.
 */

public class SimpleLineItemLayout extends RelativeLayout {

    private final String TAG = SimpleLineItemLayout.class.getSimpleName();

    private final int DEFAULT_TEXT_SIZE = 28;   // px
    private final int DEFAULT_TEXT_COLOR = android.R.color.black;
    private final int DEFAULT_MARGIN = 16;  // dp
    private final int DEFAULT_ICON_WIDTH = 32;  // dp
    private final int DEFAULT_ICON_HEIGHT = 32;  // dp
    private final int NONE = -1;

    private TextView mNameTv;
    private ImageView mNameIconIv;
    private TextView mContentTv;
    private ImageView mContentIv;
    private TextView mDescriptionTv;
    private SwitchCompat mSwitchSc;
    private RelativeLayout mMainRl;

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    private int mNameTextSize;
    private int mContentTextSize;
    private float mDescriptionTextSize;
    private int mNameTextColor;
    private int mContentTextColor;
    private int mDescriptionTextColor;

    private int mNameTextLeftMargin;
    private int mNameIconLeftMargin;
    private int mDescriptionRightMargin;
    private int mDescriptionTopMargin;

    private int mContentTextRightMargin;
    private int mContentIvRightMargin;

    private int mNameIconIvWidth;
    private int mNameIconIvHeight;
    private int mContentIvWidth;
    private int mContentIvHeight;

    private int marginTop;
    private int marginBottom;

    private int mSwitchMarginRight;

    private int mNameIconResource;
    private int mContentIvResource;
    private String mNameText;
    private String mContentText;
    private String mDescriptionText;

    private boolean mShowSwitch;
    private int mSwitchThumb;
    private int mSwitchTrack;


    public SimpleLineItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.simple_line_item_layout, this, false);
        addView(view);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SimpleLineItemLayout, 0, 0);
        initAttributes(attributes);
        attributes.recycle();
        initViews();
        init();
    }

    @SuppressWarnings("deprecation")
    private void initAttributes(TypedArray attributes) {
        mNameTextSize = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_name_text_size, DEFAULT_TEXT_SIZE);
        mContentTextSize = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_content_text_size, DEFAULT_TEXT_SIZE);
        mDescriptionTextSize = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_description_text_size, DEFAULT_TEXT_SIZE);

        mNameTextColor = attributes.getColor(R.styleable.SimpleLineItemLayout_simple_name_text_color, getResources().getColor(DEFAULT_TEXT_COLOR));
        mContentTextColor = attributes.getColor(R.styleable.SimpleLineItemLayout_simple_content_text_color, getResources().getColor(DEFAULT_TEXT_COLOR));
        mDescriptionTextColor = attributes.getColor(R.styleable.SimpleLineItemLayout_simple_description_text_color, getResources().getColor(DEFAULT_TEXT_COLOR));

        mNameTextLeftMargin = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_name_text_left_margin, DEFAULT_MARGIN);
        mNameIconLeftMargin = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_name_icon_left_margin, DEFAULT_MARGIN);

        mContentTextRightMargin = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_content_text_right_margin, DEFAULT_MARGIN);
        mContentIvRightMargin = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_content_icon_right_margin, DEFAULT_MARGIN);
        mDescriptionRightMargin = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_description_text_right_margin, DEFAULT_MARGIN);
        mDescriptionTopMargin = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_description_text_top_margin, DEFAULT_MARGIN);

        mNameIconIvWidth = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_name_icon_width, DEFAULT_ICON_WIDTH);
        mNameIconIvHeight = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_name_icon_height, DEFAULT_ICON_HEIGHT);

        mContentIvWidth = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_content_icon_width, DEFAULT_ICON_WIDTH);
        mContentIvHeight = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_content_icon_height, DEFAULT_ICON_HEIGHT);

        marginTop = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_margin_top, DEFAULT_MARGIN);
        marginBottom = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_margin_bottom, DEFAULT_MARGIN);

        mSwitchMarginRight = attributes.getDimensionPixelSize(R.styleable.SimpleLineItemLayout_simple_switch_margin_right, DEFAULT_MARGIN);

        mNameIconResource = attributes.getResourceId(R.styleable.SimpleLineItemLayout_simple_name_icon, NONE);
        mContentIvResource = attributes.getResourceId(R.styleable.SimpleLineItemLayout_simple_content_icon, NONE);
        mNameText = attributes.getString(R.styleable.SimpleLineItemLayout_simple_name_text);
        mContentText = attributes.getString(R.styleable.SimpleLineItemLayout_simple_content_text);
        mDescriptionText = attributes.getString(R.styleable.SimpleLineItemLayout_simple_description_text);

        mShowSwitch = attributes.getBoolean(R.styleable.SimpleLineItemLayout_simple_show_switch, false);
        mSwitchThumb = attributes.getResourceId(R.styleable.SimpleLineItemLayout_simple_switch_thumb, NONE);
        mSwitchTrack = attributes.getResourceId(R.styleable.SimpleLineItemLayout_simple_switch_track, NONE);
    }

    private void initViews() {
        mNameTv = findViewById(R.id.tv_name);
        mNameIconIv = findViewById(R.id.iv_name);
        mContentTv = findViewById(R.id.tv_content);
        mContentIv = findViewById(R.id.iv_content);
        mDescriptionTv = findViewById(R.id.tv_description);
        mSwitchSc = findViewById(R.id.sc_switch);
        mMainRl = findViewById(R.id.rl_main);
    }

    private void init() {
        // 总高度
        int totalHeight;

        // 三个text的高度
        int nameHeight = 0;
        int contentHeight = 0;
        int descriptionHeight = 0;

        // 用来测量text高度的paint
        Paint measureTextPaint = new Paint();

        if (!TextUtils.isEmpty(mNameText)) {
            mNameTv.setVisibility(View.VISIBLE);
            mNameTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNameTextSize);
            mNameTv.setTextColor(mNameTextColor);
            mNameTv.setText(mNameText);
            RelativeLayout.LayoutParams params = (LayoutParams) mNameTv.getLayoutParams();
            params.setMargins(mNameTextLeftMargin, 0, 0, 0);
            mNameTv.setLayoutParams(params);
            measureTextPaint.setTextSize(mNameTextSize);
            measureTextPaint.measureText(mNameText);
            nameHeight = -(int) measureTextPaint.getFontMetrics().top;
        } else {
            mNameTv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mContentText)) {
            mContentTv.setVisibility(View.VISIBLE);
            mContentTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContentTextSize);
            mContentTv.setTextColor(mContentTextColor);
            mContentTv.setText(mContentText);
            RelativeLayout.LayoutParams params = (LayoutParams) mContentTv.getLayoutParams();
            params.setMargins(0, 0, mContentTextRightMargin, 0);
            mContentTv.setLayoutParams(params);
            measureTextPaint.setTextSize(mContentTextSize);
            measureTextPaint.measureText(mContentText);
            contentHeight = -(int) measureTextPaint.getFontMetrics().top;
        } else {
            mContentTv.setVisibility(View.GONE);
        }

        if (mShowSwitch) {
            mSwitchSc.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (LayoutParams) mSwitchSc.getLayoutParams();
            params.setMargins(0, 0, mSwitchMarginRight, 0);
            mSwitchSc.setLayoutParams(params);
            if (mSwitchThumb != NONE) {
                mSwitchSc.setThumbResource(mSwitchThumb);
            }
            if (mSwitchTrack != NONE) {
                mSwitchSc.setTrackResource(mSwitchTrack);
            }
        } else {
            mSwitchSc.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mDescriptionText)) {
            mDescriptionTv.setVisibility(View.VISIBLE);
            mDescriptionTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDescriptionTextSize);
            mDescriptionTv.setTextColor(mDescriptionTextColor);
            mDescriptionTv.setText(mDescriptionText);
            RelativeLayout.LayoutParams params = (LayoutParams) mDescriptionTv.getLayoutParams();
            int leftMargin;
            if (mNameIconResource != NONE) {
                leftMargin = mNameTextLeftMargin + mNameIconIvWidth + mNameIconLeftMargin;
            } else {
                leftMargin = mNameTextLeftMargin;
            }
            params.setMargins(leftMargin, mDescriptionTopMargin, mDescriptionRightMargin, 0);
            mDescriptionTv.setLayoutParams(params);

            measureTextPaint.setTextSize(mDescriptionTextSize);
            measureTextPaint.measureText(mDescriptionText);
            descriptionHeight = -(int) measureTextPaint.getFontMetrics().top;
        } else {
            mDescriptionTv.setVisibility(View.GONE);
            descriptionHeight = 0;
        }

        if (mNameIconResource != NONE) {
            mNameIconIv.setVisibility(View.VISIBLE);
            mNameIconIv.setImageResource(mNameIconResource);
            RelativeLayout.LayoutParams params = (LayoutParams) mNameIconIv.getLayoutParams();
            params.width = mNameIconIvWidth;
            params.height = mNameIconIvHeight;
            params.setMargins(mNameIconLeftMargin, 0, 0, 0);
            mNameIconIv.setLayoutParams(params);
        } else {
            mNameIconIv.setVisibility(View.GONE);
            mNameIconIvWidth = 0;
            mNameIconIvHeight = 0;
        }

        if (mContentIvResource != NONE) {
            mContentIv.setVisibility(View.VISIBLE);
            mContentIv.setImageResource(mContentIvResource);
            RelativeLayout.LayoutParams params = (LayoutParams) mContentIv.getLayoutParams();
            params.width = mContentIvWidth;
            params.height = mContentIvHeight;
            params.setMargins(0, 0, mContentIvRightMargin, 0);
            mContentIv.setLayoutParams(params);
        } else {
            mContentIv.setVisibility(View.GONE);
        }

        {
            // 设置总高度
            RelativeLayout.LayoutParams params = (LayoutParams) mMainRl.getLayoutParams();
            params.setMargins(0, marginTop, 0, marginBottom);

            int tempHeight = nameHeight > contentHeight ? nameHeight : contentHeight + descriptionHeight;
            tempHeight = tempHeight > mNameIconIvHeight ? tempHeight : mNameIconIvHeight;

            totalHeight = tempHeight + marginBottom + marginTop;

            Log.d(TAG, "name height:" + nameHeight + ", content height: " + contentHeight + ", description height: " + descriptionHeight + ", margin top: " + marginTop + ", margin bottom: " + marginBottom + ", total height: " + totalHeight);

            mMainRl.setLayoutParams(params);
        }

    }

    public void setNameText(CharSequence name) {
        mNameTv.setText(name);
    }

    public void setNameText(int res) {
        mNameTv.setText(res);
    }

    public void setContentText(CharSequence content) {
        mContentTv.setText(content);
    }

    public void setContentText(int res) {
        mContentTv.setText(res);
    }

    public void setDescriptionText(CharSequence description) {
        mDescriptionTv.setText(description);
    }

    public void setDescriptionText(int res) {
        mDescriptionTv.setText(res);
    }

    public void setSwitchChecked(boolean checked) {
        setSwitchChecked(true, checked);
    }

    public void setSwitchChecked(boolean notifyCheckedChangedListener, boolean checked) {
        if (notifyCheckedChangedListener) {
            mSwitchSc.setChecked(checked);
        } else {
            mSwitchSc.setOnCheckedChangeListener(null);
            mSwitchSc.setChecked(checked);
            mSwitchSc.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }
    }

    public TextView getNameTv() {
        return mNameTv;
    }

    public TextView getContentTv() {
        return mContentTv;
    }

    public TextView getDescriptionTv() {
        return mDescriptionTv;
    }

    public SwitchCompat getSwitch() {
        return mSwitchSc;
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckChangedListener) {
        mSwitchSc.setOnCheckedChangeListener(onCheckChangedListener);
        mOnCheckedChangeListener = onCheckChangedListener;
    }
}

