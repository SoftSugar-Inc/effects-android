package softsugar.senseme.com.effects.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import softsugar.senseme.com.effects.R;
import softsugar.senseme.com.effects.activity.BaseActivity;
import softsugar.senseme.com.effects.utils.Constants;
import softsugar.senseme.com.effects.utils.ContextHolder;
import softsugar.senseme.com.effects.utils.MultiLanguageUtils;
import softsugar.senseme.com.effects.view.BeautyItem;
import softsugar.senseme.com.effects.view.widget.TipToast;

public class BeautyItemAdapter extends RecyclerView.Adapter {

    ArrayList<BeautyItem> mBeautyItem;
    private int mSelectedPosition = 0;
    Context mContext;

    public BeautyItemAdapter(Context context, ArrayList<BeautyItem> list) {
        mContext = context;
        mBeautyItem = list;
    }

    public ArrayList<BeautyItem> getData() {
        return mBeautyItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beauty_item, null);
        return new BeautyItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final BeautyItemViewHolder viewHolder = (BeautyItemViewHolder) holder;
        final BeautyItem entity = mBeautyItem.get(position);
        // 三级菜单选项不显示num
        if (entity.no_seekbar) {
            viewHolder.tvBottomStrength.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.tvBottomStrength.setVisibility(View.VISIBLE);
        }
        viewHolder.mName.setText(mBeautyItem.get(position).getText());

        int strengthI = Math.round(mBeautyItem.get(position).getProgress() * 100);
        viewHolder.tvBottomStrength.setText(strengthI + "");

        viewHolder.mName.setTextColor(Color.parseColor("#A7A7A7"));
        viewHolder.tvBottomStrength.setTextColor(Color.parseColor("#A7A7A7"));

        viewHolder.mImage.setImageResource(mBeautyItem.get(position).unselectedIconRes);

        holder.itemView.setSelected(mSelectedPosition == position);
        if (mSelectedPosition == position) {
            viewHolder.tvBottomStrength.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.mName.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.mImage.setImageResource(mBeautyItem.get(position).selectedIconRes);
        }

        holder.itemView.setOnClickListener(view -> {
            if (mBeautyItem.get(position).uid.equals(Constants.UID_GAN_SKIN)) {
                if (mContext instanceof BaseActivity) {
                    BaseActivity aty = (BaseActivity) mContext;
                }
            }
            mSelectedPosition = position;
            mListener.onItemClick(position, entity);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mBeautyItem.size();
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

    static class BeautyItemViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView mImage;
        TextView mName;
        TextView tvBottomStrength;

        public BeautyItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mName = itemView.findViewById(R.id.beauty_item_description);
            tvBottomStrength = itemView.findViewById(R.id.tv_bottom_strength);
            mImage = itemView.findViewById(R.id.beauty_item_iv);
        }
    }

    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void onItemClick(int position, BeautyItem item);
    }
}
