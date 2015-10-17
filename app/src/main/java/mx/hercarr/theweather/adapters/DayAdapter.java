package mx.hercarr.theweather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import mx.hercarr.theweather.R;
import mx.hercarr.theweather.model.Day;
import mx.hercarr.theweather.utils.StringUtil;

public class DayAdapter extends BaseAdapter {

    private Context mContext;
    private ViewHolder holder;
    private Day[] mDays;
    private Day day;

    public DayAdapter(Context context, Day[] days) {
        mContext = context;
        mDays = days;
    }

    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder.setIconImageView((ImageView) convertView.findViewById(R.id.iconImageView));
            holder.setTemperatureLabel((TextView) convertView.findViewById(R.id.temperatureLabel));
            holder.setDayLabel((TextView) convertView.findViewById(R.id.dayNameLabel));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        day = mDays[position];

        holder.getIconImageView().setImageResource(StringUtil.getIconID(day.getIcon()));
        holder.getTemperatureLabel().setText(StringUtil.parseDoubleToIntValue(day.getTemperatureMax()));

        if (position == 0) {
            holder.getDayLabel().setText(R.string.today);
        } else {
            holder.getDayLabel().setText(StringUtil.getDayOfTheWeek(day.getTime(), day.getTimezone()));
        }

        return convertView;
    }

    private static class ViewHolder {

        private ImageView iconImageView;
        private TextView temperatureLabel;
        private TextView dayLabel;

        public ImageView getIconImageView() {
            return iconImageView;
        }

        public void setIconImageView(ImageView iconImageView) {
            this.iconImageView = iconImageView;
        }

        public TextView getTemperatureLabel() {
            return temperatureLabel;
        }

        public void setTemperatureLabel(TextView temperatureLabel) {
            this.temperatureLabel = temperatureLabel;
        }

        public TextView getDayLabel() {
            return dayLabel;
        }

        public void setDayLabel(TextView dayLabel) {
            this.dayLabel = dayLabel;
        }

    }

}