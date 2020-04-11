package xin.banghua.moyuan.Signin;

/**
 * Created by Xu Wei on 2017/4/26.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import xin.banghua.moyuan.bean.AddrBean;

public class ProvinceAdapter extends BaseAdapter {

    private Context context;

    public ProvinceAdapter(Context context) {
        this.context = context;
    }

    private List<AddrBean.ProvinceBean> provinceBeanList = new ArrayList<>();

    public void setProvinceBeanList(List<AddrBean.ProvinceBean> provinceBeanList) {
        this.provinceBeanList = provinceBeanList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return provinceBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return provinceBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = new TextView(context);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setText(provinceBeanList.get(position).getProvince());
        return tv;
    }
}
