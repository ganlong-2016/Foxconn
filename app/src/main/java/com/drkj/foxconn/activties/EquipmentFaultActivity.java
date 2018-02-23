package com.drkj.foxconn.activties;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.drkj.foxconn.BaseActivity;
import com.drkj.foxconn.R;
import com.drkj.foxconn.bean.EquipmentFaultBean;
import com.drkj.foxconn.db.DbController;
import com.drkj.foxconn.util.FileUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EquipmentFaultActivity extends BaseActivity {
//    @BindView(R.id.recyclerView_pic)
//    RecyclerView recycleListView;
//    private List<String> imagePaths = new ArrayList<>();
//    private PicAdapter adapter;

    @BindView(R.id.edit_content)
    EditText content;
    @BindView(R.id.spinner_type_choose)
    Spinner typeSpinner;
    @BindView(R.id.image_pic)
    ImageView pic;

    private String type = "0";
    private EquipmentFaultBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_fault);
        ButterKnife.bind(this);
        initView();
        bean = new EquipmentFaultBean();
    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayout.HORIZONTAL);
//        recycleListView.setLayoutManager(manager);
//        adapter = new PicAdapter();
//        adapter.setOnItemClickLitener(new OnItemClickLitener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if (position == imagePaths.size())
//                    takePic();
//            }
//
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//                deletePic();
//            }
//        });
//        recycleListView.setAdapter(adapter);
        List<String> data = new ArrayList<>();
        data.add("人为");
        data.add("非人为");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.item_spinner_type,data);
        typeSpinner.setAdapter(arrayAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        type = "0";
                        break;
                    case 1:
                        type = "1";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @OnClick(R.id.image_save_feedback)
    void saveFeedback(){
        bean.setContent(content.getText().toString());
        bean.setType(type);
        bean.setId(System.currentTimeMillis()+"");
        DbController.getInstance().saveEquipmentFault(bean);
        finish();
    }
    @OnClick(R.id.image_pic)
    void takePic() {
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takeIntent, 200);
    }

    private void deletePic() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    pic.setImageBitmap(bitmap);
                    String imageBase = FileUtil.bitmapToString(bitmap);
                    bean.setPicture(imageBase);
//                    String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
//                    String imageUrlPath = FileUtil.saveFile(this, name, bitmap);
//                    imagePaths.add(imageUrlPath);
//                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

//    class PicAdapter extends RecyclerView.Adapter<PicAdapter.MyViewHolder> {
//        private OnItemClickLitener mOnItemClickLitener;
//
//        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
//            this.mOnItemClickLitener = mOnItemClickLitener;
//        }
//
//        @Override
//        public PicAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            PicAdapter.MyViewHolder viewHolder = new PicAdapter.MyViewHolder(LayoutInflater.from(EquipmentFaultActivity.this)
//                    .inflate(R.layout.item_add_pic, null));
//
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(final PicAdapter.MyViewHolder viewHolder, final int position) {
//            if (position == imagePaths.size()) {
//                viewHolder.imageView.setImageResource(R.drawable.ic_add_pic);
//                viewHolder.imageView.setBackgroundColor(getResources().getColor(R.color.white));
//            } else {
//                FileInputStream fis = null;
//                try {
//                    fis = new FileInputStream(imagePaths.get(position));
//                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
//                    viewHolder.imageView.setImageBitmap(bitmap);
//                    viewHolder.imageView.setBackgroundColor(getResources().getColor(R.color.black));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (fis != null) {
//                        try {
//                            fis.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//            }
//            if (mOnItemClickLitener != null) {
//                viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mOnItemClickLitener.onItemClick(viewHolder.itemView, position);
//                    }
//                });
//                viewHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//                        mOnItemClickLitener.onItemLongClick(viewHolder.itemView, position);
//                        return true;
//                    }
//                });
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return imagePaths.size() + 1;
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder {
//            public ImageView imageView;
//
//            public MyViewHolder(View itemView) {
//                super(itemView);
//                imageView = itemView.findViewById(R.id.image_pic_item);
//            }
//        }
//
//    }
//
//    public interface OnItemClickLitener {
//        void onItemClick(View view, int position);
//
//        void onItemLongClick(View view, int position);
//    }
}
