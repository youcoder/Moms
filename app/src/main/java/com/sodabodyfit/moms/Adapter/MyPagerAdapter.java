package com.sodabodyfit.moms.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sodabodyfit.moms.Common.CardInfo;
import com.sodabodyfit.moms.Common.DividerItemDecoration;
import com.sodabodyfit.moms.ExpandWorkoutActivity;
import com.sodabodyfit.moms.R;

import java.util.ArrayList;

public class MyPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<CardInfo> listItems = new ArrayList<CardInfo>();
    private float y1, y2;
    private int expandIndex = -1;
    private View vTop, vBottom;
    private static final int MIN_DISTANCE = 15;
    private static final int ANIM_DURATION = 200;

    public MyPagerAdapter(Context context, ArrayList<CardInfo> list) {
        this.context = context;
        this.listItems = list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_card, null);

        try {
            final CardInfo item = listItems.get(position);

            final CardView cvCategory = (CardView) view.findViewById(R.id.card_category);
            final CardView cvContent = (CardView) view.findViewById(R.id.card_content);
            ImageView ivCategory = (ImageView) view.findViewById(R.id.img_category);
            TextView tvCount = (TextView) view.findViewById(R.id.txt_item_count);
            tvCount.setText(item.listWorkout.size() + " WORKOUTS");
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recycler);
            recycler.setLayoutManager(layoutManager);
            recycler.addItemDecoration(new DividerItemDecoration(context));

            SimpleWorkoutAdapter adapter = new SimpleWorkoutAdapter(context, item.listWorkout);
            recycler.setAdapter(adapter);

            cvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandView(cvCategory, cvContent, position);
                }
            });

            cvCategory.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch(event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            y1 = event.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            y2 = event.getY();
                            float offset = Math.abs(y2 - y1);
                            if (y2 >  y1 && offset > MIN_DISTANCE) {
                                collapseView(cvCategory, cvContent, position);
                            } else if( y1 > y2  && offset > MIN_DISTANCE) {
                                expandView(cvCategory, cvContent, position);
                            } else {
                                if (isCollapsed(position)) {
                                    expandView(cvCategory, cvContent, position);
                                } else {
                                    Intent intent = new Intent(context, ExpandWorkoutActivity.class);
                                    intent.putExtra("category_id", position);
                                    intent.putExtra("image", item.detailPhotoId);
                                    intent.putExtra("workout", item.listWorkout);
                                    context.startActivity(intent);
//                                    context.getgetActivity().overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                                }
                            }
                            break;
                    }
                    return true;
                }
            });
            cvCategory.setTag(position);

            Glide.with(context)
                    .load(item.cardPhotoId)
                    .into(ivCategory);

            container.addView(view);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    private void expandView(View viewTop, View viewBottom, int position) {

        if (isCollapsed(position)) {
            if (expandIndex != -1) {
                collapseView(vTop, vBottom, expandIndex);
            }
            viewTop.animate()
                    .translationY(context.getResources().getDimensionPixelSize(R.dimen.trans_up))
                    .setDuration(ANIM_DURATION)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();

            viewBottom.animate()
                    .translationY(context.getResources().getDimensionPixelSize(R.dimen.trans_down))
                    .setDuration(ANIM_DURATION)
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
            expandIndex = position;
            vTop = viewTop;
            vBottom = viewBottom;
        }
    }

    private void collapseView(View viewTop, View viewBottom, int position) {

        if (!isCollapsed(position)) {
            viewTop.animate()
                    .translationY(0)
                    .setDuration(ANIM_DURATION)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
            viewBottom.animate()
                    .translationY(0)
                    .setDuration(ANIM_DURATION)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
            expandIndex = -1;
        }
    }

    private boolean isCollapsed(int position) {
        return expandIndex != position;
    }

    public void setData(ArrayList<CardInfo> list) {
        this.listItems = list;
    }
}