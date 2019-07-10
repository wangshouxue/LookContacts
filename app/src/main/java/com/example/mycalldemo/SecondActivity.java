package com.example.mycalldemo;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    TextView bt;
    RecyclerView rv;
    ContactsAdapter adapter;
    ArrayList<ContactsBean> list;
    ContactsBean contactsBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt=findViewById(R.id.bt);
        rv=findViewById(R.id.rv);

        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(manager);

        adapter=new ContactsAdapter(this);
        rv.setAdapter(adapter);
//        getContactInfo();
        testGetAllContact();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setData(list);
            }
        });
    }

    public ArrayList<ContactsBean> testGetAllContact()
    {
        list = new ArrayList<ContactsBean>();
        //获取联系人信息的Uri
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //查询数据，返回Cursor
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if(cursor!=null&&cursor.moveToFirst()){
            while(cursor.moveToNext())
            {
                ContactsBean bean=new ContactsBean();
                //获取联系人的ID
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                //获取联系人的姓名
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                bean.setName(name);

                //获取头像
                Uri imgUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId + "");
                InputStream is = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), imgUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                bean.setBm(bitmap);

                //查询电话类型的数据操作,会有多个
                List<ContactsBean.phoneBean> phoneList=new ArrayList<>();
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
                        null, null);
                while(phones.moveToNext())
                {
                    ContactsBean.phoneBean pBean=new ContactsBean.phoneBean();
                    String phoneNumber = phones.getString(phones.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String lable = phones.getString(
                            phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
                    String phoneNumberType = phones.getString(
                            phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    String type="";
                    switch (Integer.parseInt(phoneNumberType)) {
                        case 0://自定义
                            type=lable;
                            break;
                        case 1:
                            type="住宅电话：";
                            break;
                        case 2:
                            type="手机：";
                            break;
                        case 3:
                            type="单位电话：";
                            break;
                        case 4:
                            type="单位传真：";
                            break;
                        case 5:
                            type="住宅传真：";
                            break;
                        case 6:
                            type="寻呼机：";
                            break;
                        case 7:
                            type="其他：";
                            break;
                        case 12:
                            type="总机：";
                            break;
                    }
                    //添加Phone的信息
                    pBean.setPhone(type+phoneNumber);
                    phoneList.add(pBean);
                }
                bean.setPhoneBeans(phoneList);
                phones.close();

                //查询Email类型的数据操作,会有多个
                List<ContactsBean.emailBean> emailList=new ArrayList<>();
                Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,
                        null, null);
                while (emails.moveToNext())
                {
                    ContactsBean.emailBean eBean=new ContactsBean.emailBean();
                    String emailType = emails.getString(emails.getColumnIndex(
                            ContactsContract.CommonDataKinds.Email.TYPE));
                    String lable = emails.getString(emails.getColumnIndex(
                            ContactsContract.CommonDataKinds.Email.LABEL));
                    String emailAddress = emails.getString(emails.getColumnIndex(
                            ContactsContract.CommonDataKinds.Email.DATA));
                    String type="";
                    switch (emailType){
                        case "1":
                            type="个人email：";
                            break;
                        case "2":
                            type="工作email：";
                            break;
                        default:
                            type=lable+"email：";
                            break;
                    }
                    //添加Email的信息
                    eBean.setEmail(type+emailAddress);
                    emailList.add(eBean);
                }
                bean.setEmailBeans(emailList);
                emails.close();

                //查询==地址==类型的数据操作.StructuredPostal.TYPE_WORK,会有多个
                List<ContactsBean.addBean> addressList=new ArrayList<>();
                Cursor address = getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
                        null, null);
                while (address.moveToNext())
                {
                    ContactsBean.addBean aBean=new ContactsBean.addBean();
                    String addType = address.getString(address.getColumnIndex(
                            ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
                    String lable = address.getString(address.getColumnIndex(
                            ContactsContract.CommonDataKinds.StructuredPostal.LABEL));
                    String workAddress = address.getString(address.getColumnIndex(
                            ContactsContract.CommonDataKinds.StructuredPostal.DATA));
                    String type="";
                    switch (addType){
                        case "1":
                            type="住宅地址：";
                            break;
                        case "2":
                            type="公司地址：";
                            break;
                        default:
                            type=lable+"地址：";
                            break;
                    }
                    //添加address的信息
                    aBean.setAddress(type+workAddress);
                    addressList.add(aBean);
                }
                bean.setAddressBeans(addressList);
                address.close();
                //查询==公司名字==类型的数据操作.Organization.COMPANY  ContactsContract.Data.CONTENT_URI
                String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                String[] orgWhereParams = new String[]{contactId,
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                Cursor orgCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                        null, orgWhere, orgWhereParams, null);
                if (orgCur.moveToFirst()) {
                    //组织名 (公司名字)
                    String company = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                    //职位
                    String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                    bean.setCompany(company);
                    bean.setJob(title);
                }
                orgCur.close();

                //获取生日
                String selection = ContactsContract.Data.CONTACT_ID + " = ? AND " +ContactsContract.Data.MIMETYPE + " = ?";
                String[] eventWhereParams = new String[]{contactId,
                        ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE };
                Cursor eventCur= getContentResolver().query(
                        ContactsContract.Data.CONTENT_URI,
                        null,
                        selection, eventWhereParams, null);
                while (eventCur.moveToNext()) {
                    if (eventCur.getString(eventCur.getColumnIndex(ContactsContract.Data.DATA1)) != null) {
                        String birthday = eventCur.getString(eventCur.getColumnIndex(
                                ContactsContract.Data.DATA1));
                        String lable = eventCur.getString(eventCur.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL));
                        int eventType = eventCur.getInt(eventCur.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE));
                        String type="";
                        switch (eventType){
                            case 0:
                                //自定义
                                type=lable+"：";
                                break;
                            case 1:
                                type="周年纪念日：";
                                break;
                            case 3:
                                type="生日：";
                                break;
                        }
//                        Log.i("===生日",type+"--"+birthday);
                        //暂未添加到集合中
                    }
                }
                eventCur.close();
                //获取即时消息
                String imSel = ContactsContract.Data.CONTACT_ID + " = ? AND " +ContactsContract.Data.MIMETYPE + " = ?";
                String[] imWhereParams = new String[]{contactId,
                        ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE };
                Cursor imCur= getContentResolver().query(
                        ContactsContract.Data.CONTENT_URI,
                        null,
                        imSel, imWhereParams, null);
                while (imCur.moveToNext()) {
                    String data = imCur.getString(imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
                    String lable = imCur.getString(imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.LABEL));
                    int eventType = imCur.getInt(imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));
                    String type="";
                    switch (eventType){
                        case ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ:
                            type="QQ：";
                            break;
                        case ContactsContract.CommonDataKinds.Im.PROTOCOL_AIM:
                            type="AIM：";
                            break;
                        case ContactsContract.CommonDataKinds.Im.PROTOCOL_MSN:
                            type="Windows Live：";
                            break;
                        case ContactsContract.CommonDataKinds.Im.PROTOCOL_YAHOO:
                            type="雅虎：";
                            break;
                        case ContactsContract.CommonDataKinds.Im.PROTOCOL_SKYPE:
                            type="Skype：";
                            break;
                        case ContactsContract.CommonDataKinds.Im.PROTOCOL_GOOGLE_TALK:
                            type="环聊：";
                            break;
                        case ContactsContract.CommonDataKinds.Im.PROTOCOL_ICQ:
                            type="ICQ：";
                            break;
                        case ContactsContract.CommonDataKinds.Im.PROTOCOL_JABBER:
                            type="Jabber：";
                            break;
                        case ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM:
                            //自定义
                            type=lable+"：";
                            break;

                    }
//                    Log.i("===Im",eventType+"--"+type+data);
                    //暂未添加到集合中,ps:自定义的拿不到标签
                }
                imCur.close();
                //备注信息
                String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                String[] noteWhereParams = new String[]{contactId, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                Cursor noteCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                        null, noteWhere, noteWhereParams, null);
                if (noteCur.moveToFirst()) {
                    String remark = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
//                    Log.i("===note",remark);
                    //暂未添加到集合中
                }
                noteCur.close();
                //昵称信息（称呼）
                String nickWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                String[] nickWhereParams = new String[]{contactId, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE};
                Cursor nickCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                        null, nickWhere, nickWhereParams, null);
                if (nickCur.moveToFirst()) {
                    String nickName = nickCur.getString(nickCur.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME));
//                    Log.i("===nick",nickName);
                    //暂未添加到集合中
                }
                nickCur.close();
                //网站信息
                String webWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                String[] webWhereParams = new String[]{contactId, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE};
                Cursor webCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                        null, webWhere, webWhereParams, null);
                while (webCur.moveToNext()) {
                    String home = webCur.getString(webCur.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));
//                    Log.i("===web",home);
                    //暂未添加到集合中
                }
                webCur.close();
                //关系信息
                String relaWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                String[] relaWhereParams = new String[]{contactId, ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE};
                Cursor relaCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                        null, relaWhere, relaWhereParams, null);
                while (relaCur.moveToNext()) {
                    String relation = relaCur.getString(relaCur.getColumnIndex(ContactsContract.CommonDataKinds.Relation.DATA));
                    String lable = relaCur.getString(relaCur.getColumnIndex(ContactsContract.CommonDataKinds.Relation.LABEL));
                    int relaType=relaCur.getInt(relaCur.getColumnIndex(ContactsContract.CommonDataKinds.Relation.TYPE));
                    String type="";
                    switch (relaType){
                        case 0://自定义
                            type=lable+"：";
                            break;
                        case 1:
                            type="助理：";
                            break;
                        case 2:
                            type="兄弟：";
                            break;
                        case 3:
                            type="子女：";
                            break;
                        case 4:
                            type="同居伴侣：";
                            break;
                        case 5:
                            type="父亲：";
                            break;
                        case 6:
                            type="朋友：";
                            break;
                        case 7:
                            type="经理：";
                            break;
                        case 8:
                            type="母亲：";
                            break;
                        case 9:
                            type="父母：";
                            break;
                        case 10:
                            type="合作伙伴：";
                            break;
                        case 11:
                            type="介绍人：";
                            break;
                        case 12:
                            type="亲属：";
                            break;
                        case 13:
                            type="姐妹：";
                            break;
                        case 14:
                            type="配偶：";
                            break;
                    }
//                    Log.i("===relation",relaType+"=="+lable+"--"+relation);
                    //暂未添加到集合中
                }
                relaCur.close();

                list.add(bean);
            }
        }
        cursor.close();
        return list;
    }

//    public List<ContactsBean> getContactInfo(){
//        list = new ArrayList<ContactsBean>();
//        //联系人的Uri，也就是content://com.android.contacts/contacts
//        Uri uri = ContactsContract.Contacts.CONTENT_URI;
//        //指定获取_id和display_name两列数据，display_name即为姓名
//        String[] projection = new String[]{
//                ContactsContract.Contacts._ID,
//                ContactsContract.Contacts.DISPLAY_NAME,
//                ContactsContract.Contacts.CONTACT_CHAT_CAPABILITY
//        };
//        //uri               :查询地址
//        // projection        :查询的数据字段名称
//        // selection         :查询的条件 where id=..
//        // selectionArgs     :查询条件的参数
//        // sortOrder         :排序
//        //根据Uri查询相应的ContentProvider，cursor为获取到的数据集
//        Cursor cursor = getContentResolver().
//                query(uri, projection, null, null, null);
//        int i = 0;
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                Long id = cursor.getLong(0);
//                //获取姓名
//                String name = cursor.getString(1);
//                Log.i("===",cursor.getString(2));
//                //指定获取NUMBER这一列数据
//                String[] phoneProjection = new String[]{
//                        ContactsContract.CommonDataKinds.Phone.NUMBER
//                };
//
//                //根据联系人的ID获取此人的电话号码
//                Cursor phonesCusor = getContentResolver().query(
//                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                        phoneProjection,
//                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
//                        null,
//                        null);
//
//                //因为每个联系人可能有多个电话号码，所以需要遍历
//                if (phonesCusor != null && phonesCusor.moveToFirst()) {
//                    do {
//                        String num = phonesCusor.getString(0);
//
//                        contactsBean = new ContactsBean();
//                        contactsBean.setName(name);
//                        contactsBean.setPhone(num);
//
//                        list.add(contactsBean);
//                    } while (phonesCusor.moveToNext());
//                }
//                i++;
//            } while (cursor.moveToNext());
//        }
//
//        // 获取sim卡的联系人--1
////        try {
////            getSimContact("content://icc/adn", list);
////
////            getSimContact("content://icc/adn/subId/#", list);
////
////            getSimContact("content://icc/sdn", list);
////
////            getSimContact("content://icc/sdn/subId/#", list);
////
////            getSimContact("content://icc/fdn", list);
////
////            getSimContact("content://icc/fdn/subId/#", list);
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//        return list;
//    }
//    private void getSimContact(String adn, List<ContactsBean> list) {
//        // 读取SIM卡手机号,有三种可能:content://icc/adn || content://icc/sdn || content://icc/fdn
//        // 具体查看类 IccProvider.java
//        Cursor cursor = null;
//        try {
//            Uri uri = Uri.parse(adn);
//            cursor = getContentResolver().query(uri, null,
//                    null, null, null);
//            if (cursor != null) {
//                while (cursor.moveToNext()) {
//                    // 取得联系人名字
//                    int nameIndex = cursor.getColumnIndex("name");
//                    // 取得电话号码
//                    int numberIndex = cursor.getColumnIndex("number");
//                    String number = cursor.getString(numberIndex);// 手机号
//                    Log.i("===手机号:" ,number);
//                    if (isPhoneNumber(number)) {// 是否是手机号码
//                        ContactsBean simCardTemp = new ContactsBean();
////                        simCardTemp.setPhone(formatMobileNumber(number))
//                        simCardTemp.setPhone(number);
//                        simCardTemp.setName(cursor.getString(nameIndex));
//                        if (!list.contains(simCardTemp)) {
//                            list.add(simCardTemp);
//                        }
//                    }
//                }
//                cursor.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }
//    public static boolean isPhoneNumber(String input) {// 判断手机号码是否规则
//        String regex = "(1[0-9][0-9]|15[0-9]|18[0-9])\\d{8}";
//        Pattern p = Pattern.compile(regex);
//        return p.matches(regex, input);//如果不是号码，则返回false，是号码则返回true
//    }


}
