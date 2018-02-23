package com.drkj.foxconn.activties;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.drkj.foxconn.BaseActivity;
import com.drkj.foxconn.R;
import com.drkj.foxconn.bean.EquipmentResultBean;
import com.drkj.foxconn.db.DbController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckActivity extends BaseActivity {

    EquipmentResultBean.DataBean bean;
    @BindView(R.id.list_equipment_attr)
    ListView attrList;
    @BindView(R.id.text_equipment_name)
    TextView equipmentNameText;
    @BindView(R.id.text_equipment_code)
    TextView equipmentCodeText;
    @BindView(R.id.text_equipment_location)
    TextView equipmentLocationText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String nfcCode = getIntent().getStringExtra("rfCode");
        bean = DbController.getInstance().queryEquipmentByNfcCode(nfcCode);
        equipmentNameText.setText(bean.getName());
        equipmentCodeText.setText(bean.getCode());
        equipmentLocationText.setText("");
        MyAdapter adapter = new MyAdapter(bean);
        attrList.setAdapter(adapter);
    }

    @OnClick(R.id.image_save_check)
    void saveCheck() {
        for (EquipmentResultBean.DataBean.EquipmentAttributeListBean attributeListBean : bean.getEquipmentAttributeList()) {
            DbController.getInstance().updateEquipmentAttribute(attributeListBean);
        }
    }

    class MyAdapter extends BaseAdapter {
        EquipmentResultBean.DataBean bean;
        public MyAdapter(EquipmentResultBean.DataBean bean){
            this.bean = bean;
        }
        @Override
        public int getCount() {
            return bean.getEquipmentAttributeList().size();
        }

        @Override
        public Object getItem(int position) {
            return bean.getEquipmentAttributeList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder holder = null;
            if (convertView == null) {
                holder = new MyHolder();
                convertView = LayoutInflater.from(CheckActivity.this).inflate(R.layout.item_equipment_attr, null);
                holder.attrNameText = convertView.findViewById(R.id.text_equipment_attr_name);
                holder.attrValueText = convertView.findViewById(R.id.text_equipment_attr_value);
                holder.attrHint = convertView.findViewById(R.id.image_equipment_attr_hint);
                convertView.setTag(holder);
            }else {
                holder = (MyHolder) convertView.getTag();
            }
            holder.attrNameText.setText(bean.getEquipmentAttributeList().get(position).getName());
            return convertView;
        }
    }
    class MyHolder{
        TextView attrNameText;
        EditText attrValueText;
        ImageView attrHint;
    }
}
