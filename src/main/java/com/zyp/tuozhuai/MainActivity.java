package com.zyp.tuozhuai;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;


/**
 * Created by zyp on 2018/6/21  上午 8:52
 */
public class MainActivity extends AppCompatActivity {

    private GridLayout mGridLayout;
    private int        index;
    private View       drawView;
    /**
     * 文字开始拖拽时的监听
     */
    private View.OnLongClickListener ocl = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            drawView = v;
            v.startDrag(null, new View.DragShadowBuilder(v), null, 0);
            return false;
        }
    };

    /**
     * 给gridview设置进入他的子view区域时的监听
     */
    private View.OnDragListener      odl = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                /**
                 * 当开始按下时，创建出矩形数组，并对数组赋值
                 */
                case DragEvent.ACTION_DRAG_STARTED:
                    initRects();
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    /**
                     * 监听是哪一个子view在进行拖拽事件，并返回当前的i
                     *
                     * drawView即textview在进行长按监听时的view
                     */
                    int touchIndex = getTouchIndex(event);
                    if (touchIndex > -1 && drawView != null && drawView != mGridLayout.getChildAt(touchIndex)) {
                        mGridLayout.removeView(drawView);
                        mGridLayout.addView(drawView, touchIndex);
                    }
                    break;

                default:
                    break;
            }
            /**
             * 这里一定要返回true，要不然gridview的监听事情会不执行
             */
            return true;
        }
    };

    private int getTouchIndex(DragEvent event) {
        for (int i = 0; i < mRects.length; i++) {
            if (mRects[i].contains((int) event.getX(), (int) event.getY())) {
                return i;
            }
        }
        return -1;
    }

    private Rect[] mRects;

    private void initRects() {
        mRects = new Rect[mGridLayout.getChildCount()];
        for (int i = 0; i < mRects.length; i++) {

            View childView = mGridLayout.getChildAt(i);
            Rect rect = new Rect(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getBottom());
            mRects[i] = rect;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 给mGridLayout设置拖拽监听
         */
        mGridLayout = (GridLayout) findViewById(R.id.gridlayout);
        mGridLayout.setOnDragListener(odl);

    }

    /**
     * 点击button按钮，设置文字及背景
     *
     * @param view
     */
    public void addItem(View view) {
        TextView tv = new TextView(MainActivity.this);
        tv.setText(index + "");
        tv.setBackgroundResource(R.drawable.shap_tv_bg);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels / 4 - 10;
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;

        tv.setLayoutParams(layoutParams);
        tv.setGravity(Gravity.CENTER);
        layoutParams.setMargins(5, 5, 5, 5);

        index++;

        /**
         * 给tv设置拖拽监听
         */
        tv.setOnLongClickListener(ocl);

        mGridLayout.addView(tv, 0);


    }
}
