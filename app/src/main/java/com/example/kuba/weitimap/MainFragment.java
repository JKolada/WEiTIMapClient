package com.example.kuba.weitimap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by Kuba on 2016-03-31.
 */
public class MainFragment extends Fragment {

    private static MainActivity mainActivity;
    private SubsamplingScaleImageView SubImageView;
    private FrameLayout frameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        FrameLayout mImageView = inflater.inflate(R.layout.fragment_map_list, container, false);
//        getView().findViewById(R.id.subsamplingscale_image);

        frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_map_list, container, false);
//        SubsamplingScaleImageView SubImageView = (SubsamplingScaleImageView) getView().findViewById(R.id.subsamplingscale_image);
//        SubImageView = (SubsamplingScaleImageView) inflater.inflate(R.id.subsamplingscale_image, container, false);
//        SubImageView.setImage(ImageSource.resource(R.drawable.temp));
//        setupSubLayout(SubImageView);
//        SubImageView.setImage(ImageSource.asset("temp.png"));

        return SubImageView;
    }

    private void setupSubLayout(SubsamplingScaleImageView frameLaSubImageViewyout) {




//        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(recyclerView, ImageUrlUtils.getImageUrls()));


//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//
//        if (MainFragment.this.getArguments().getInt("type") == 1) {
//            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
//        }
    }
//        public static class SimpleStringRecyclerViewAdapter
//                extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {
//
//            private String[] mValues;
//            private RecyclerView mRecyclerView;
//
//            public static class ViewHolder extends RecyclerView.ViewHolder {
//                public final View mView;
//                public final SubsamplingScaleImageView mImageView;
//
//                public ViewHolder(View view) {
//                    super(view);
//                    mView = view;
//                    mImageView = (SubsamplingScaleImageView) view.findViewById(R.id.subsamplingscale_image);
//
//                  mImageView.setImage(ImageSource.resource(R.drawable.weiti_logo));
//                }
//            }
//
//            public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, String[] items) {
//                mValues = items;
//                mRecyclerView = recyclerView;
//            }
//
//            @Override
//            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//                return new ViewHolder(view);
//            }
//
//            @Override
//            public void onViewRecycled(ViewHolder holder) {
//                if (holder.mImageView.getController() != null) {
//                    holder.mImageView.getController().onDetach();
//                }
//                if (holder.mImageView.getTopLevelDrawable() != null) {
//                    holder.mImageView.getTopLevelDrawable().setCallback(null);
////                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
//                }
//            }
//
//            @Override
//            public void onBindViewHolder(final ViewHolder holder, final int position) {
//                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) holder.mImageView.getLayoutParams();
//                if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
//                    layoutParams.height = 200;
//                } else if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
//                    layoutParams.height = 600;
//                } else {
//                    layoutParams.height = 800;
//                }
//                Uri uri = Uri.parse(mValues[position]);
//                holder.mImageView.setImageURI(uri);
//                holder.mImageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mActivity, ViewPagerActivity.class);
//                        intent.putExtra("position", position);
//                        mActivity.startActivity(intent);
//
//                    }
//                });
//            }
//
//            @Override
//            public int getItemCount() {
//                return mValues.length;
//            }
//        }


}
