package learn.shendy.kontry.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import learn.shendy.kontry.R;

public class RoundedImageView extends AppCompatImageView {

    private static final float DEFAULT_RADIUS = 20f;
//    private static final ScaleType[] SCALE_TYPES = new ScaleType[]{
//            ScaleType.MATRIX,
//            ScaleType.FIT_XY,
//            ScaleType.FIT_START,
//            ScaleType.FIT_CENTER,
//            ScaleType.FIT_END,
//            ScaleType.CENTER,
//            ScaleType.CENTER_CROP,
//            ScaleType.CENTER_INSIDE
//    };

    private Drawable mDrawable;
    private Bitmap mBitmap;
    private Paint mBitmapPaint;

    private RectF mBounds;
    private float mRadius;
//    private ScaleType mScaleType;


    public RoundedImageView(Context context) {
        super(context);

        init(null);
    }

    public RoundedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public RoundedImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

//    public RoundedImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//
//        init(attrs);
//    }

    // MARK: Setup Methods

    private void init(@Nullable AttributeSet attrs) {
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new RectF();

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.RoundedImageView);
//        int scaleTypeIndex = ta.getInt(R.styleable.RoundedImageView_android_scaleType, -1);
        float radiusAttr = ta.getDimension(R.styleable.RoundedImageView_radius, DEFAULT_RADIUS);
        mDrawable = ta.getDrawable(R.styleable.RoundedImageView_android_src);

//        setScaleType(scaleTypeIndex >= 0 ? SCALE_TYPES[scaleTypeIndex] : ScaleType.FIT_CENTER);
        updateRadius(radiusAttr);
        updateBitmap();

        ta.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int imageHeight, imageWidth;
        if (mBitmap == null) {
            imageWidth = imageHeight = 0;
        } else {
            imageWidth = mBitmap.getWidth();
            imageHeight = mBitmap.getHeight();
        }

        int width = getMeasurement(widthMeasureSpec, imageWidth);
        int height = getMeasurement(heightMeasureSpec, imageHeight);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            int imageWidth, imageHeight;
            imageWidth = imageHeight = 0;

            if (mBitmap != null) {
                imageWidth = mBitmap.getWidth();
                imageHeight = mBitmap.getHeight();
            }

            // We need to center the image
            int left = (w - imageWidth) / 2;
            int top = (h - imageHeight) / 2;

            mBounds.set(left, top, (left + imageWidth), (top + imageHeight));

            /*
                Offset the shader to draw the Bitmap inside the rect
                Without this, the bitmap will be at 0,0 in the view
             */
            // I don't get this, will understand it later
            if (mBitmapPaint.getShader() != null) {
                Matrix m = new Matrix();
                m.setTranslate(left, top);
                mBitmapPaint.getShader().setLocalMatrix(m);
            }
        }
    }

//    @Override
//    public void setImageDrawable(@Nullable Drawable drawable) {
//        mDrawable = drawable;
//        updateBitmap();
////        super.setImageDrawable(drawable);
//    }

//    @Override
//    public void setScaleType(ScaleType scaleType) {
//        assert scaleType != null;
//
//        if (mScaleType != scaleType) {
//            mScaleType = scaleType;
//
//            switch (scaleType) {
//                case CENTER:
//                case CENTER_CROP:
//                case CENTER_INSIDE:
//                case FIT_CENTER:
//                case FIT_START:
//                case FIT_END:
//                case FIT_XY:
//                    super.setScaleType(ScaleType.FIT_XY);
//                    break;
//                default:
//                    super.setScaleType(scaleType);
//                    break;
//            }
//
//            requestLayout();
//        }
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmapPaint != null) {
            canvas.drawRoundRect(mBounds, mRadius, mRadius, mBitmapPaint);
        }
    }

    // MARK: Helper Methods

    private int getMeasurement(int measureSpec, int imageSize) {
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                return specSize;

            case MeasureSpec.AT_MOST:
                return Math.min(specSize, imageSize);

            case MeasureSpec.UNSPECIFIED:
                return imageSize;

            default:
                return 0;
        }
    }

    private void updateRadius(float radius) {
        if (mRadius != radius) {
            mRadius = radius;
            invalidate();
        }
    }

    private void updateBitmap() {
        Bitmap bitmap = ((BitmapDrawable) mDrawable).getBitmap();

        if (mBitmap != bitmap) {
            mBitmap = bitmap;

            if (mBitmap != null) {
                BitmapShader shader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                mBitmapPaint.setShader(shader);
            } else {
                mBitmapPaint.setShader(null);
            }

            requestLayout();
        }
    }
}
