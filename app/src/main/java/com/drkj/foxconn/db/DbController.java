package com.drkj.foxconn.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.drkj.foxconn.App;
import com.drkj.foxconn.bean.EndTaskBean;
import com.drkj.foxconn.bean.EquipmentFaultBean;
import com.drkj.foxconn.bean.EquipmentResultBean;
import com.drkj.foxconn.bean.FeedbackBean;
import com.drkj.foxconn.bean.RegionResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganlong on 2018/1/30.
 */

public class DbController {
    private static DbController instance;
    private SqlHelper sqlHelper;

    private DbController() {
        sqlHelper = new SqlHelper(App.getInstance(), DbConstant.DB_NAME, null, DbConstant.DB_VERSION);
    }

    public static DbController getInstance() {
        if (instance == null) {
            instance = new DbController();
        }
        return instance;
    }

    public void saveRegionInfo(RegionResultBean bean) {
        if (bean == null || bean.getData() == null)
            return;

        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        for (RegionResultBean.DataBean dataBean : bean.getData()) {
            ContentValues values = new ContentValues();
            values.put("code", dataBean.getCode());
            values.put("createBy", dataBean.getCreateBy());
            values.put("createDate", dataBean.getCreateDate());
            values.put("createName", dataBean.getCreateName());
            values.put("id", dataBean.getId());
            values.put("name", dataBean.getName());
            values.put("rfCode", dataBean.getRfCode());
            values.put("parentId", dataBean.getParentId());
            values.put("updateBy", dataBean.getUpdateBy());
            values.put("updateDate", dataBean.getUpdateDate());
            values.put("updateName", dataBean.getUpdateName());
            values.put("type", dataBean.getType());
            if (queryRegionById(dataBean.getId()) > 0) {
                db.update(DbConstant.TABLE_REGION, values, "id=?", new String[]{dataBean.getId()});
            } else {
                db.insert(DbConstant.TABLE_REGION, null, values);
            }
        }
    }

    public void saveEquipment(EquipmentResultBean bean) {
        if (bean == null || bean.getData() == null)
            return;
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        for (EquipmentResultBean.DataBean dataBean : bean.getData()) {
            ContentValues values = new ContentValues();
            values.put("buildingId", dataBean.getBuildingId());
            values.put("code", dataBean.getCode());
            values.put("createBy", dataBean.getCreateBy());
            values.put("createDate", dataBean.getCreateDate());
            values.put("createName", dataBean.getCreateName());
            values.put("id", dataBean.getId());
            values.put("name", dataBean.getName());
            values.put("rfCode", dataBean.getRfCode());
            values.put("roomId", dataBean.getRoomId());
            values.put("storeyId", dataBean.getStoreyId());
            values.put("type", dataBean.getType());
            if (queryEquipmentById(dataBean.getId()) > 0) {
                db.update(DbConstant.TABLE_EQUIPMENT, values, "id=?", new String[]{dataBean.getId()});
                db.delete(DbConstant.TABLE_EQUIPMENTATTRIBUTE, "equipmentId=?", new String[]{dataBean.getId()});
            } else {
                db.insert(DbConstant.TABLE_EQUIPMENT, null, values);
            }
            for (EquipmentResultBean.DataBean.EquipmentAttributeListBean listBean : dataBean.getEquipmentAttributeList()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("createBy", listBean.getCreateBy());
                contentValues.put("createDate", listBean.getCreateDate());
                contentValues.put("createName", listBean.getCreateName());
                contentValues.put("equipmentId", listBean.getEquipmentId());
                contentValues.put("id", listBean.getId());
                contentValues.put("max", listBean.getMax());
                contentValues.put("min", listBean.getMin());
                contentValues.put("name", listBean.getName());
                contentValues.put("rating", listBean.getRating());
                contentValues.put("type", listBean.getType());
                contentValues.put("updateBy", listBean.getUpdateBy());
                contentValues.put("updateName", listBean.getUpdateName());
                db.insert(DbConstant.TABLE_EQUIPMENTATTRIBUTE, null, contentValues);
            }
        }
    }

    public EquipmentResultBean.DataBean queryEquipmentByNfcCode(String code) {
        EquipmentResultBean.DataBean bean = new EquipmentResultBean.DataBean();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.query(DbConstant.TABLE_EQUIPMENT, null, "rfCode=?", new String[]{code}, null, null, null);
        if (cursor.moveToFirst()) {
            bean.setBuildingId(cursor.getString(cursor.getColumnIndex("buildingId")));
            bean.setCode(cursor.getString(cursor.getColumnIndex("code")));
            bean.setCreateBy(cursor.getString(cursor.getColumnIndex("createBy")));
            bean.setCreateDate(cursor.getString(cursor.getColumnIndex("createDate")));
            bean.setCreateName(cursor.getString(cursor.getColumnIndex("createName")));
            bean.setId(cursor.getString(cursor.getColumnIndex("id")));
            bean.setName(cursor.getString(cursor.getColumnIndex("name")));
            bean.setRfCode(cursor.getString(cursor.getColumnIndex("rfCode")));
            bean.setRoomId(cursor.getString(cursor.getColumnIndex("roomId")));
            bean.setStoreyId(cursor.getString(cursor.getColumnIndex("storeyId")));
            bean.setType(cursor.getString(cursor.getColumnIndex("type")));
            Cursor cursor1 = db.query(DbConstant.TABLE_EQUIPMENTATTRIBUTE, null, "equipmentId=?", new String[]{bean.getId()}, null, null, null);
            List<EquipmentResultBean.DataBean.EquipmentAttributeListBean> list = new ArrayList<>();
            while (cursor1.moveToNext()) {
                EquipmentResultBean.DataBean.EquipmentAttributeListBean bean1 = new EquipmentResultBean.DataBean.EquipmentAttributeListBean();
                bean1.setCreateBy(cursor1.getString(cursor1.getColumnIndex("createBy")));
                bean1.setCreateDate(cursor1.getString(cursor1.getColumnIndex("createDate")));
                bean1.setCreateName(cursor1.getString(cursor1.getColumnIndex("createName")));
                bean1.setEquipmentId(cursor1.getString(cursor1.getColumnIndex("equipmentId")));
                bean1.setId(cursor1.getString(cursor1.getColumnIndex("id")));
                bean1.setMax(cursor1.getDouble(cursor1.getColumnIndex("max")));
                bean1.setMin(cursor1.getDouble(cursor1.getColumnIndex("min")));
                bean1.setName(cursor1.getString(cursor1.getColumnIndex("name")));
                bean1.setRating(cursor1.getDouble(cursor1.getColumnIndex("rating")));
                bean1.setType(cursor1.getString(cursor1.getColumnIndex("type")));
                bean1.setUpdateBy(cursor1.getString(cursor1.getColumnIndex("updateBy")));
                bean1.setUpdateName(cursor1.getString(cursor1.getColumnIndex("updateName")));
                list.add(bean1);
            }
            bean.setEquipmentAttributeList(list);
            cursor1.close();
        }
        cursor.close();
        return bean;
    }

    public List<EquipmentResultBean.DataBean> queryAllEquipment() {
        List<EquipmentResultBean.DataBean> dataBeans = new ArrayList<>();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.query(DbConstant.TABLE_EQUIPMENT, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            EquipmentResultBean.DataBean bean = new EquipmentResultBean.DataBean();
            bean.setBuildingId(cursor.getString(cursor.getColumnIndex("buildingId")));
            bean.setCode(cursor.getString(cursor.getColumnIndex("code")));
            bean.setCreateBy(cursor.getString(cursor.getColumnIndex("createBy")));
            bean.setCreateDate(cursor.getString(cursor.getColumnIndex("createDate")));
            bean.setCreateName(cursor.getString(cursor.getColumnIndex("createName")));
            bean.setId(cursor.getString(cursor.getColumnIndex("id")));
            bean.setName(cursor.getString(cursor.getColumnIndex("name")));
            bean.setRfCode(cursor.getString(cursor.getColumnIndex("rfCode")));
            bean.setRoomId(cursor.getString(cursor.getColumnIndex("roomId")));
            bean.setStoreyId(cursor.getString(cursor.getColumnIndex("storeyId")));
            bean.setType(cursor.getString(cursor.getColumnIndex("type")));
            Cursor cursor1 = db.query(DbConstant.TABLE_EQUIPMENTATTRIBUTE, null, "equipmentId=?", new String[]{bean.getId()}, null, null, null);
            List<EquipmentResultBean.DataBean.EquipmentAttributeListBean> list = new ArrayList<>();
            while (cursor1.moveToNext()) {
                EquipmentResultBean.DataBean.EquipmentAttributeListBean bean1 = new EquipmentResultBean.DataBean.EquipmentAttributeListBean();
                bean1.setCreateBy(cursor1.getString(cursor1.getColumnIndex("createBy")));
                bean1.setCreateDate(cursor1.getString(cursor1.getColumnIndex("createBy")));
                bean1.setCreateName(cursor1.getString(cursor1.getColumnIndex("createBy")));
                bean1.setEquipmentId(cursor1.getString(cursor1.getColumnIndex("createBy")));
                bean1.setId(cursor1.getString(cursor1.getColumnIndex("createBy")));
                bean1.setMax(cursor1.getDouble(cursor1.getColumnIndex("createBy")));
                bean1.setMin(cursor1.getDouble(cursor1.getColumnIndex("createBy")));
                bean1.setName(cursor1.getString(cursor1.getColumnIndex("createBy")));
                bean1.setRating(cursor1.getDouble(cursor1.getColumnIndex("createBy")));
                bean1.setType(cursor1.getString(cursor1.getColumnIndex("createBy")));
                bean1.setUpdateBy(cursor1.getString(cursor1.getColumnIndex("createBy")));
                bean1.setUpdateName(cursor1.getString(cursor1.getColumnIndex("createBy")));
                list.add(bean1);
            }
            bean.setEquipmentAttributeList(list);
            cursor1.close();
            dataBeans.add(bean);
        }
        cursor.close();
        return dataBeans;
    }

    public void updateEquipmentAttribute(EquipmentResultBean.DataBean.EquipmentAttributeListBean attributeListBean) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("value", attributeListBean.getValue());
        db.update(DbConstant.TABLE_EQUIPMENTATTRIBUTE, values, "equipmentId=? and id=?", new String[]{attributeListBean.getEquipmentId(), attributeListBean.getId()});
    }

    private int queryRegionById(String id) {
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.query(DbConstant.TABLE_REGION, null, "id=?", new String[]{id}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public RegionResultBean.DataBean queryRegionInfoById(String id){
        RegionResultBean.DataBean dataBean = new RegionResultBean.DataBean();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.query(DbConstant.TABLE_REGION, null, "id=?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()){
            dataBean.setCode(cursor.getString(cursor.getColumnIndex("code")));
            dataBean.setCreateBy(cursor.getString(cursor.getColumnIndex("createBy")));
            dataBean.setCreateDate(cursor.getString(cursor.getColumnIndex("createDate")));
            dataBean.setCreateName(cursor.getString(cursor.getColumnIndex("createName")));
            dataBean.setId(cursor.getString(cursor.getColumnIndex("id")));
            dataBean.setName(cursor.getString(cursor.getColumnIndex("name")));
            dataBean.setRfCode(cursor.getString(cursor.getColumnIndex("rfCode")));
            dataBean.setParentId(cursor.getString(cursor.getColumnIndex("parentId")));
            dataBean.setUpdateBy(cursor.getString(cursor.getColumnIndex("updateBy")));
            dataBean.setUpdateDate(cursor.getString(cursor.getColumnIndex("updateDate")));
            dataBean.setUpdateName(cursor.getString(cursor.getColumnIndex("updateName")));
            dataBean.setType(cursor.getString(cursor.getColumnIndex("type")));
        }
        cursor.close();
        return dataBean;
    }
    private int queryEquipmentById(String id) {
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.query(DbConstant.TABLE_EQUIPMENT, null, "id=?", new String[]{id}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public EndTaskBean queryAllAttribute() {
        EndTaskBean endTaskBean = new EndTaskBean();
        List<EndTaskBean.TaskRecordListBean> list = new ArrayList<>();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.query(DbConstant.TABLE_EQUIPMENTATTRIBUTE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            EndTaskBean.TaskRecordListBean recordListBean = new EndTaskBean.TaskRecordListBean();
            recordListBean.setCreateBy(cursor.getString(cursor.getColumnIndex("createBy")));
//                recordListBean.setCreateDate(cursor.getString(cursor.getColumnIndex("createDate")));
            recordListBean.setCreateName(cursor.getString(cursor.getColumnIndex("createName")));
            recordListBean.setEquipmentAttributeId(cursor.getString(cursor.getColumnIndex("id")));
            recordListBean.setEquipmentId(cursor.getString(cursor.getColumnIndex("equipmentId")));
            recordListBean.setUpdateBy(cursor.getString(cursor.getColumnIndex("updateBy")));
            recordListBean.setUpdateName(cursor.getString(cursor.getColumnIndex("updateName")));
            list.add(recordListBean);
        }
        endTaskBean.setTaskRecordList(list);
        cursor.close();
        return endTaskBean;
    }

    public void saveFeedback(FeedbackBean bean) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", bean.getContent());
        values.put("createBy", bean.getCreateBy());
        values.put("createDate", bean.getCreateDate());
        values.put("createName", bean.getCreateName());
        values.put("id", bean.getId());
        values.put("picture", bean.getPicture());
        values.put("recordTime", bean.getRecordTime());
        values.put("regionId", bean.getRegionId());
        values.put("regionName", bean.getRegionName());
        values.put("type", bean.getType());
        values.put("updateBy", bean.getUpdateBy());
        values.put("updateDate", bean.getUpdateDate());
        values.put("updateName", bean.getUpdateName());
        if (!TextUtils.isEmpty(bean.getId())) {
            Cursor cursor = db.query(DbConstant.TABLE_FEEDBACK, null, "id=?", new String[]{bean.getId()}, null, null, null);
            if (cursor.getCount() > 0) {
                db.update(DbConstant.TABLE_FEEDBACK, values, "id=?", new String[]{bean.getId()});
            } else {
                db.insert(DbConstant.TABLE_FEEDBACK, null, values);
            }
            cursor.close();
        } else {
            db.insert(DbConstant.TABLE_FEEDBACK, null, values);
        }
    }

    public List<FeedbackBean> queryAllFeedback() {
        List<FeedbackBean> list = new ArrayList<>();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.query(DbConstant.TABLE_FEEDBACK, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            FeedbackBean feedbackBean = new FeedbackBean();
            feedbackBean.setContent(cursor.getString(cursor.getColumnIndex("content")));
            feedbackBean.setCreateBy(cursor.getString(cursor.getColumnIndex("createBy")));
//                feedbackBean.setCreateDate(cursor.getString(cursor.getColumnIndex("createDate")));
            feedbackBean.setCreateName(cursor.getString(cursor.getColumnIndex("createName")));
            feedbackBean.setId(cursor.getString(cursor.getColumnIndex("id")));
            feedbackBean.setPicture(cursor.getString(cursor.getColumnIndex("picture")));
            feedbackBean.setRecordTime(cursor.getString(cursor.getColumnIndex("recordTime")));
            feedbackBean.setRegionId(cursor.getString(cursor.getColumnIndex("regionId")));
            feedbackBean.setRegionName(cursor.getString(cursor.getColumnIndex("regionName")));
            feedbackBean.setType(cursor.getString(cursor.getColumnIndex("type")));
            feedbackBean.setUpdateBy(cursor.getString(cursor.getColumnIndex("updateBy")));
//                feedbackBean.setUpdateDate(cursor.getString(cursor.getColumnIndex("updateDate")));
            feedbackBean.setUpdateName(cursor.getString(cursor.getColumnIndex("updateName")));
            list.add(feedbackBean);
        }
        cursor.close();
        return list;
    }

    public void deleteFeedback(FeedbackBean bean) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        db.delete(DbConstant.TABLE_FEEDBACK, "id=?", new String[]{bean.getId()});
    }

    public void saveEquipmentFault(EquipmentFaultBean bean) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", bean.getContent());
        values.put("createBy", bean.getCreateBy());
        values.put("createDate", bean.getCreateDate());
        values.put("createName", bean.getCreateName());
        values.put("equipmentId", bean.getEquipmentId());
        values.put("equipmentName", bean.getEquipmentName());
        values.put("id", bean.getId());
        values.put("picture", bean.getPicture());
        values.put("recordTime", bean.getRecordTime());
        values.put("type", bean.getType());
        values.put("updateBy", bean.getUpdateBy());
        values.put("updateDate", bean.getUpdateDate());
        values.put("updateName", bean.getUpdateName());
        if (!TextUtils.isEmpty(bean.getId())) {
            Cursor cursor = db.query(DbConstant.TABLE_EQUIPMENT_FAULT, null, "id=?", new String[]{bean.getId()}, null, null, null);
            if (cursor.getCount() > 0) {
                db.update(DbConstant.TABLE_EQUIPMENT_FAULT, values, "id=?", new String[]{bean.getId()});
            } else {
                db.insert(DbConstant.TABLE_EQUIPMENT_FAULT, null, values);
            }
            cursor.close();
        } else {
            db.insert(DbConstant.TABLE_EQUIPMENT_FAULT, null, values);
        }

    }

    public List<EquipmentFaultBean> queryAllEquipmentFault() {
        List<EquipmentFaultBean> list = new ArrayList<>();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.query(DbConstant.TABLE_EQUIPMENT_FAULT, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            EquipmentFaultBean equipmentFaultBean = new EquipmentFaultBean();
            equipmentFaultBean.setContent(cursor.getString(cursor.getColumnIndex("content")));
            equipmentFaultBean.setCreateBy(cursor.getString(cursor.getColumnIndex("createBy")));
            equipmentFaultBean.setCreateDate(cursor.getString(cursor.getColumnIndex("createDate")));
            equipmentFaultBean.setCreateName(cursor.getString(cursor.getColumnIndex("createName")));
            equipmentFaultBean.setEquipmentId(cursor.getString(cursor.getColumnIndex("equipmentId")));
            equipmentFaultBean.setEquipmentName(cursor.getString(cursor.getColumnIndex("equipmentName")));
            equipmentFaultBean.setId(cursor.getString(cursor.getColumnIndex("id")));
            equipmentFaultBean.setPicture(cursor.getString(cursor.getColumnIndex("picture")));
            equipmentFaultBean.setRecordTime(cursor.getString(cursor.getColumnIndex("recordTime")));
            equipmentFaultBean.setType(cursor.getString(cursor.getColumnIndex("type")));
            equipmentFaultBean.setUpdateBy(cursor.getString(cursor.getColumnIndex("updateBy")));
            equipmentFaultBean.setUpdateDate(cursor.getString(cursor.getColumnIndex("updateDate")));
            equipmentFaultBean.setUpdateName(cursor.getString(cursor.getColumnIndex("updateName")));
            list.add(equipmentFaultBean);
        }
        cursor.close();
        return list;
    }

    public void deleteEquipmentFault(EquipmentFaultBean bean) {
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        db.delete(DbConstant.TABLE_EQUIPMENT_FAULT, "id=?", new String[]{bean.getId()});
    }
}
