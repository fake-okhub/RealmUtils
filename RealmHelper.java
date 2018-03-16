package com.example.com.mvpdemo;

import com.orhanobut.logger.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Josyon on 2018/3/13 0013.
 */

public class RealmHelper {
    private static Realm mRealm ;

    private static class SingletonHolder {
        private static RealmHelper INSTANCE = new RealmHelper(
                mRealm);
    }

    private RealmHelper(Realm realm) {
        this.mRealm = realm;

    }
    
    public static Realm getRealm(Context context) {
        byte[] key = new byte[64];
        new SecureRandom().nextBytes(key);
        Realm.init(context);
        RealmMigration migration = new RealmMigration() {
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

            }
        };
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("realmdb.realm") //文件名
                .schemaVersion(0) //版本号
                .migration(migration)//数据库版本迁移（数据库升级，当数据库中某个表添加字段或者删除字段）
                .deleteRealmIfMigrationNeeded()//声明版本冲突时自动删除原数据库(当调用了该方法时，上面的方法将失效)。
                .build();//创建
        return Realm.getInstance(config);
    }

    /**
     * 获取RealmOperation的单例
     *
     * @return 返回RealmOperation的单例
     */
    public static RealmHelper getInstance() {
        if (mRealm == null) {
            mRealm = App.getmRealm();
        }
        return SingletonHolder.INSTANCE;
    }

    public static Realm getmRealm() {
        return mRealm;
    }

    public static void setmRealm(Realm mRealm) {
        RealmHelper.mRealm = mRealm;
    }

    /**
     * 增加单条数据到数据库中
     *
     * @param bean 数据对象，必须继承了RealmObject
     */
    public void add(final RealmObject bean) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(bean);
            }
        });

    }

    /**
     * 增加多条数据到数据库中
     *
     * @param beans 数据集合，其中元素必须继承了RealmObject
     */
    public void add(final List<? extends RealmObject> beans) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(beans);
            }
        });

    }
    /**
     * 增加多条数据到数据库中
     *
     * @param beans 数据集合，其中元素必须继承了RealmObject
     */
    public void addAsync(final List<? extends RealmObject> beans) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(beans);
            }
        });

    }

    /**
     * 删除数据库中clazz类所属所有元素
     *
     * @param clazz
     */
    public void deleteAll(Class<? extends RealmObject> clazz) {
        final RealmResults<? extends RealmObject> beans = mRealm.where(clazz).findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                beans.deleteAllFromRealm();

            }
        });
    }
    /**
     * 删除数据库中clazz类所属所有元素
     *
     * @param clazz
     */
    public void deleteAllAsync(Class<? extends RealmObject> clazz) {
        final RealmResults<? extends RealmObject> beans = mRealm.where(clazz).findAll();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                beans.deleteAllFromRealm();

            }
        });


    }

    /**
     * 删除数据库中clazz类所属第一个元素
     *
     * @param clazz
     */
    public void deleteFirst(Class<? extends RealmObject> clazz) {
        final RealmResults<? extends RealmObject> beans = mRealm.where(clazz).findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                beans.deleteFirstFromRealm();

            }
        });
    }

    /**
     * 删除数据库中clazz类所属最后一个元素
     *
     * @param clazz
     */
    public void deleteLast(Class<? extends RealmObject> clazz) {
        final RealmResults<? extends RealmObject> beans = mRealm.where(clazz).findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                beans.deleteLastFromRealm();

            }
        });

    }

    /**
     * 删除数据库中clazz类所属数据中某一位置的元素
     *
     * @param clazz
     * @param position
     */
    public void deleteElement(Class<? extends RealmObject> clazz, final int position) {
        final RealmResults<? extends RealmObject> beans = mRealm.where(clazz).findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                beans.deleteFromRealm(position);

            }
        });
    }


    /**
     * 查询数据库中clazz类所属所有数据
     *
     * @param clazz
     * @return
     */
    public List<? extends RealmObject> queryAll(Class<? extends RealmObject> clazz) {
        final RealmResults<? extends RealmObject> beans = mRealm.where(clazz).findAll();
        List<? extends RealmObject> lists = mRealm.copyFromRealm(beans);
        return lists;
    }
    /**
     * 查询数据库中clazz类所属所有数据
     *
     * @param clazz
     * @return
     */
    public List<? extends RealmObject> queryAllAsync(Class<? extends RealmObject> clazz) {
        final RealmResults<? extends RealmObject> beans = mRealm.where(clazz).findAllAsync();
        List<? extends RealmObject> lists = mRealm.copyFromRealm(beans);
        return lists;
    }

    /**
     * 查询满足条件的第一个数据
     *
     * @param clazz
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     */
    public RealmObject queryFirst(Class<? extends RealmObject> clazz, String fieldName, String value) throws NoSuchFieldException {
        final RealmObject bean = mRealm.where(clazz).equalTo(fieldName, value).findFirst();
        return bean;
    }

    /**
     * 查询满足条件的第一个数据
     *
     * @param clazz
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     */
    public RealmObject queryFirst(Class<? extends RealmObject> clazz, String fieldName, int value) throws NoSuchFieldException {
        final RealmObject bean = mRealm.where(clazz).equalTo(fieldName, value).findFirst();
        return bean;
    }

    /**
     * 查询满足条件的所有数据
     *
     * @param clazz
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     */
    public List<? extends RealmObject> queryAll(Class<? extends RealmObject> clazz, String fieldName, String value) throws NoSuchFieldException {
        final RealmResults<? extends RealmObject> beans = mRealm.where(clazz).equalTo(fieldName, value).findAll();
        List<? extends RealmObject> lists = mRealm.copyFromRealm(beans);
        return lists;
    }
    /**
     * 查询满足条件的所有数据
     *
     * @param clazz
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     */
    public List<? extends RealmObject> queryAllAsync(Class<? extends RealmObject> clazz, String fieldName, String value) throws NoSuchFieldException {
        final RealmResults<? extends RealmObject> beans = mRealm.where(clazz).equalTo(fieldName, value).findAllAsync();
        List<? extends RealmObject> lists = mRealm.copyFromRealm(beans);
        return lists;
    }



    /**
     * 查询满足条件的所有数据
     *
     * @param clazz
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     */
    public List<? extends RealmObject> queryAll(Class<? extends RealmObject> clazz, String fieldName, int value) throws NoSuchFieldException {
        final RealmResults<? extends RealmObject> beans = mRealm.where(clazz).equalTo(fieldName, value).findAll();
        List<? extends RealmObject> lists = mRealm.copyFromRealm(beans);
        return lists;
    }
    /**
     * 查询满足条件的所有数据
     *
     * @param clazz
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     */
    public List<? extends RealmObject> queryAllAsync(Class<? extends RealmObject> clazz, String fieldName, int value) throws NoSuchFieldException {
        final RealmResults<? extends RealmObject> beans = mRealm.where(clazz).equalTo(fieldName, value).findAllAsync();
        List<? extends RealmObject> lists = mRealm.copyFromRealm(beans);
        return lists;
    }

    /**
     * 查询数据，排序
     *
     * @param clazz
     * @param fieldName
     * @param isAscendOrDescend true 增序， false 降序
     * @return
     */
    public List<? extends RealmObject> queryAllBySort(Class<? extends RealmObject> clazz, String fieldName,Boolean isAscendOrDescend) {
        Sort sort = isAscendOrDescend ? Sort.ASCENDING : Sort.DESCENDING;
        RealmResults<? extends RealmObject> beans = mRealm.where(clazz).findAll();
        RealmResults<? extends RealmObject> results = beans.sort(fieldName, sort);
        return mRealm.copyFromRealm(results);
    }




    /**
     * 根据 主键 获取 对象，修改对象中的某个属性
     * @param clazz
     * @param primaryKeyName 主键名称
     * @param primaryKeyValue 主键类型，一般为 Integer 和 String
     * @param fieldName 修改方法名称，如setXXX
     * @param newValue 修改对象的值，类型可能为
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void updateParamByPrimaryKey(Class<? extends RealmObject> clazz, String primaryKeyName, Object primaryKeyValue, String fieldName,Object newValue)throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        RealmObject realmObject=null;
        if (primaryKeyValue instanceof Integer){
            int primaryIntKeyValue = (int) primaryKeyValue;
            realmObject = mRealm.where(clazz).equalTo(primaryKeyName, primaryIntKeyValue).findFirst();
        }else if (primaryKeyValue instanceof String){
            String primaryStringKeyValue=(String) primaryKeyValue;
            realmObject = mRealm.where(clazz).equalTo(primaryKeyName, primaryStringKeyValue).findFirst();
        }
        mRealm.beginTransaction();
        if (newValue instanceof Integer){
            Method method = clazz.getMethod(fieldName, int.class);
            method.invoke(realmObject,newValue);
        }else {
            Method method = clazz.getMethod(fieldName, newValue.getClass());
            method.invoke(realmObject,newValue);
        }
        mRealm.commitTransaction();
    }


    /**
     * 根据 主键 获取 对象，修改对象中的  “某些”  属性
     * @param clazz
     * @param primaryKeyName
     * @param primaryKeyValue
     * @param params  属性map，key 保存fieldName,value 为 属性值
     * @throws NullPointerException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void updateParamsByPrimaryKey(Class<? extends RealmObject> clazz, String primaryKeyName, Object primaryKeyValue, Map<String,Object> params) throws NullPointerException ,NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        RealmObject realmObject=null;
        if (primaryKeyValue instanceof Integer){
            int primaryIntKeyValue = (int) primaryKeyValue;
            realmObject = mRealm.where(clazz).equalTo(primaryKeyName, primaryIntKeyValue).findFirst();
        }else if (primaryKeyValue instanceof String){
            String primaryStringKeyValue=(String) primaryKeyValue;
            realmObject = mRealm.where(clazz).equalTo(primaryKeyName, primaryStringKeyValue).findFirst();
        }
        mRealm.beginTransaction();
        if (params==null || params.isEmpty()){
            throw new NullPointerException("It can't be empty by Map");
        }else {
            for (Map.Entry entry : params.entrySet()){
                Method method;
                if (entry.getValue() instanceof Integer){
                    method = clazz.getMethod((String) entry.getKey(), int.class);
                }else {
                    method = clazz.getMethod((String) entry.getKey(), entry.getValue().getClass());
                }
                method.invoke(realmObject,entry.getValue());
            }
        }
        mRealm.commitTransaction();
    }

    /**
     * 更新某个对象的所有数据
     * @param clazz
     * @param lists
     */
    public void updateAll(Class<? extends RealmObject> clazz, List<? extends RealmObject> lists) {
        deleteAll(clazz);
        add(lists);
    }


    /**
     * 更新某个对象的所有数据
     * @param clazz
     * @param realmObject
     */
    public void updateAll(Class<? extends RealmObject> clazz,final RealmObject realmObject) {
        deleteAll(clazz);
        add(realmObject);
    }

}
