package com.drkj.foxconn.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.drkj.foxconn.R;
import com.drkj.foxconn.activties.CheckActivity;
import com.drkj.foxconn.bean.EquipmentResultBean;
import com.drkj.foxconn.bean.RegionResultBean;
import com.drkj.foxconn.db.DbController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


public class OfflineCheckFragment extends Fragment {

    @BindView(R.id.list_equipment)
    ListView listView;
    List<EquipmentResultBean.DataBean> dataBeans;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline_check, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataBeans = DbController.getInstance().queryAllEquipment();
        MyAdapter adapter = new MyAdapter(dataBeans);
        listView.setAdapter(adapter);

    }

    @OnItemClick(R.id.list_equipment)
    void itemClick(int position){
        Intent intent = new Intent(new Intent(getActivity(), CheckActivity.class));
        intent.putExtra("rfCode",dataBeans.get(position).getRfCode());
        startActivity(intent);
    }
    class MyAdapter extends BaseAdapter {
        private List<EquipmentResultBean.DataBean> dataBeans;
        public MyAdapter(List<EquipmentResultBean.DataBean> dataBeans){
            this.dataBeans = dataBeans;
        }
        @Override
        public int getCount() {
            return dataBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_equipment_list, null);
                holder = new MyViewHolder();
                holder.equipmentLocationText = convertView.findViewById(R.id.text_equipment_location);
                holder.equipmentNameText = convertView.findViewById(R.id.text_equipment_name);
                holder.layout = convertView.findViewById(R.id.layout_item);
                holder.buildingNameText = convertView.findViewById(R.id.text_building_name);
                convertView.setTag(holder);
            }else {
                holder = (MyViewHolder) convertView.getTag();
            }
            switch (position%3){
                case 0:
                    holder.layout.setBackgroundResource(R.drawable.ic_orange_bg);
                    break;
                case 1:
                    holder.layout.setBackgroundResource(R.drawable.ic_green_bg);
                    break;
                case 2:
                    holder.layout.setBackgroundResource(R.drawable.ic_blue_bg);
                    break;
            }
            holder.equipmentNameText.setText(dataBeans.get(position).getName());
            RegionResultBean.DataBean dataBean = DbController.getInstance().queryRegionInfoById(dataBeans.get(position).getBuildingId());
            holder.buildingNameText.setText(dataBean.getName());
            RegionResultBean.DataBean dataBean1 = DbController.getInstance().queryRegionInfoById(dataBeans.get(position).getStoreyId());
            RegionResultBean.DataBean dataBean2 = DbController.getInstance().queryRegionInfoById(dataBeans.get(position).getRoomId());
            holder.equipmentLocationText.setText(dataBean1.getName()+"层"+dataBean2.getName()+"号机房");
            return convertView;
        }
    }

    class MyViewHolder {
         TextView equipmentLocationText;
         TextView equipmentNameText;
         TextView buildingNameText;
         LinearLayout layout;
    }
}
