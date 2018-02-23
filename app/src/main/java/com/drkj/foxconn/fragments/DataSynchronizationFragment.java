package com.drkj.foxconn.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.drkj.foxconn.App;
import com.drkj.foxconn.R;
import com.drkj.foxconn.bean.EndTaskBean;
import com.drkj.foxconn.bean.EndTaskResultBean;
import com.drkj.foxconn.bean.EquipmentFaultBean;
import com.drkj.foxconn.bean.EquipmentFaultResultBean;
import com.drkj.foxconn.bean.EquipmentResultBean;
import com.drkj.foxconn.bean.FeedbackBean;
import com.drkj.foxconn.bean.FeedbackResultBean;
import com.drkj.foxconn.bean.RegionResultBean;
import com.drkj.foxconn.bean.StartTaskBean;
import com.drkj.foxconn.bean.StartTaskResultBean;
import com.drkj.foxconn.db.DbController;
import com.drkj.foxconn.net.NetClient;
import com.drkj.foxconn.util.SpUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ganlong on 2018/1/16.
 */

public class DataSynchronizationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_synchronization, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.image_download_data)
    void downloadData() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_data_synchronization_hint);
        dialog.setCancelable(false);
        dialog.show();
        NetClient.getInstance().getApi().getRegion(SpUtil.getString(getContext(), "token"))
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Function<RegionResultBean, ObservableSource<EquipmentResultBean>>() {
                    @Override
                    public ObservableSource<EquipmentResultBean> apply(RegionResultBean regionResultBean) throws Exception {
                        DbController.getInstance().saveRegionInfo(regionResultBean);
                        return NetClient.getInstance().getApi().getEquipment(SpUtil.getString(getContext(), "token"));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EquipmentResultBean>() {
                    @Override
                    public void accept(EquipmentResultBean bean) throws Exception {
                        DbController.getInstance().saveEquipment(bean);
                        dialog.findViewById(R.id.avi).setVisibility(View.GONE);
                        dialog.findViewById(R.id.button_confirm).setVisibility(View.VISIBLE);
                        TextView message = dialog.findViewById(R.id.text_message);
                        message.setText("数据同步完成!");
                        dialog.findViewById(R.id.button_confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    @OnClick(R.id.image_upload_data)
    void uploadData() {
        if (TextUtils.isEmpty(SpUtil.getString(getContext(), "taskId"))){
            Toast.makeText(getContext(),"未开始巡检",Toast.LENGTH_SHORT).show();
            return;
        }
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_data_synchronization_hint);
        dialog.setCancelable(false);
        dialog.show();
        EndTaskBean endTaskBean = DbController.getInstance().queryAllAttribute();
//        EndTaskBean endTaskBean = new EndTaskBean();
        List<EquipmentFaultBean> faultBeans = DbController.getInstance().queryAllEquipmentFault();
        List<FeedbackBean> feedbackBeans = DbController.getInstance().queryAllFeedback();
        updateFault(faultBeans);
        updateFeedback(feedbackBeans);
        NetClient.getInstance().getApi().endTask(SpUtil.getString(getContext(), "taskId"), SpUtil.getString(getContext(), "token"), endTaskBean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EndTaskResultBean>() {
                    @Override
                    public void accept(EndTaskResultBean endTaskResultBean) throws Exception {
                        if (endTaskResultBean != null && TextUtils.equals(endTaskResultBean.getRespCode(), "0")) {
                            dialog.findViewById(R.id.avi).setVisibility(View.GONE);
                            dialog.findViewById(R.id.button_confirm).setVisibility(View.VISIBLE);
                            TextView message = dialog.findViewById(R.id.text_message);
                            message.setText("数据同步完成!");
                            dialog.findViewById(R.id.button_confirm).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        } else {

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        dialog.dismiss();
                    }
                });
    }

    @OnClick(R.id.image_start_check)
    void startTask() {
        NetClient.getInstance().getApi().createTask(SpUtil.getString(getActivity(), "token"),new StartTaskBean())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StartTaskResultBean>() {
                    @Override
                    public void accept(StartTaskResultBean startTaskResultBean) throws Exception {
                        if (TextUtils.equals("0", startTaskResultBean.getRespCode())) {
                            Toast.makeText(App.getInstance(), "创建任务成功", Toast.LENGTH_SHORT).show();
                            SpUtil.putString(getContext(), "taskId", startTaskResultBean.getData().getId());
                        } else {
                            Toast.makeText(App.getInstance(), "创建任务失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        Toast.makeText(App.getInstance(), "创建任务失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateFault(List<EquipmentFaultBean> faultBeans){
        for (final EquipmentFaultBean faultBean: faultBeans){
            NetClient.getInstance().getApi().equipmentFault(SpUtil.getString(getContext(),"token"),faultBean)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<EquipmentFaultResultBean>() {
                        @Override
                        public void accept(EquipmentFaultResultBean equipmentFaultResultBean) throws Exception {
                            if (TextUtils.equals(equipmentFaultResultBean.getRespCode(),"0")){
                                DbController.getInstance().deleteEquipmentFault(faultBean);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                        }
                    });
        }
    }

    private void updateFeedback(List<FeedbackBean> feedbackBeans){
        for (final FeedbackBean bean:feedbackBeans){
            NetClient.getInstance().getApi().feedback(SpUtil.getString(getContext(),"token"),bean)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<FeedbackResultBean>() {
                        @Override
                        public void accept(FeedbackResultBean feedbackResultBean) throws Exception {
                            if (TextUtils.equals(feedbackResultBean.getRespCode(),"0")){
                                DbController.getInstance().deleteFeedback(bean);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                        }
                    });
        }
    }
}
