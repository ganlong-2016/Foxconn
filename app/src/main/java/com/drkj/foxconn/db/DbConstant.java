package com.drkj.foxconn.db;

/**
 * Created by ganlong on 2018/1/30.
 */

public class DbConstant {

    public static final String DB_NAME = "foxconn";

    public static final int DB_VERSION = 1;

    public static final String TABLE_REGION = "region_list";

    public static final String CREATE_TABLE_REGION =
            "create table "+TABLE_REGION+
                    "(_id int auto_increment primary key," +
                    "code text," +
                    "createBy text," +
                    "createDate text," +
                    "createName text," +
                    "id text," +
                    "name text," +
                    "rfCode text," +
                    "parentId text," +
                    "updateBy text," +
                    "updateDate text," +
                    "updateName text," +
                    "type text)";

    public static final String TABLE_EQUIPMENT = "equipment_list";

    public static final String CREATE_TABLE_EQUIPMENT =
            "create table "+TABLE_EQUIPMENT+
            "(_id int auto_increment primary key," +
                    "buildingId text," +
                    "code text," +
                    "createBy text," +
                    "createDate text," +
                    "createName text," +
                    "id text," +
                    "name text," +
                    "rfCode text," +
                    "roomId text," +
                    "storeyId text," +
                    "type text)";

    public static final String TABLE_EQUIPMENTATTRIBUTE = "equipmentAttribute_list";

    public static final String CREATE_TABLE_EQUIPMENTATTRIBUTE =
            "create table "+TABLE_EQUIPMENTATTRIBUTE+
            "(_id int auto_increment primary key," +
                    "createBy text," +
                    "createDate text," +
                    "createName text," +
                    "equipmentId text," +
                    "id text," +
                    "max float," +
                    "min float," +
                    "name text," +
                    "rating float," +
                    "value float," +
                    "type text," +
                    "updateBy text," +
                    "updateName text)";

    public static final String TABLE_EQUIPMENT_FAULT = "equipment_fault";

    public static final String CREATE_TABLE_EQUIPMENT_FAULT =
            "create table "+TABLE_EQUIPMENT_FAULT+
            "(_id int auto_increment primary key," +
                    "content text," +
                    "createBy text," +
                    "createDate text," +
                    "createName text," +
                    "equipmentId text," +
                    "equipmentName text," +
                    "id text," +
                    "picture text," +
                    "recordTime text," +
                    "type text," +
                    "updateBy text," +
                    "updateDate text," +
                    "updateName text" +
                    ")";

    public static final String TABLE_FEEDBACK = "feedback";

    public static final String CREATE_TABLE_FEEDBACK =
            "create table "+TABLE_FEEDBACK+
            "(_id int auto_increment primary key," +
                    "content text," +
                    "createBy text," +
                    "createDate text," +
                    "createName text," +
                    "id text," +
                    "picture text," +
                    "recordTime text," +
                    "regionId text," +
                    "regionName text," +
                    "type text," +
                    "updateBy text," +
                    "updateDate text," +
                    "updateName text" +
                    ")";
}
