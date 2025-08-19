package softsugar.senseme.com.effects.db;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import softsugar.senseme.com.effects.db.entity.DBBeautyEntity;
import softsugar.senseme.com.effects.db.entity.DBFilterEntity;
import softsugar.senseme.com.effects.db.entity.DBStyleEntity;
import softsugar.senseme.com.effects.state.AtyStateContext;
import softsugar.senseme.com.effects.state.CameraAtyState;
import softsugar.senseme.com.effects.utils.ContextHolder;
import softsugar.senseme.com.effects.view.BeautyItem;

public class DBManager {
    private static final String TAG = "DBManager";

    private final EffectsDao effectsDao;

    public DBManager() {
        effectsDao = AppDatabase.Companion.getInstance(ContextHolder.getContext()).beautyDao();
    }

    public void clearFilter() {
        Log.i(TAG, "clear filter table");
        effectsDao.clearFilter();
    }

    // 存储滤镜信息
    public void insertFilter(String groupEnumName, int selectedPosition, String path, float strength) {
        if (!(AtyStateContext.getInstance().getState() instanceof CameraAtyState)) {
            return;
        }
        DBFilterEntity dbFilter = new DBFilterEntity();
        dbFilter.id = "filter";
        dbFilter.filterPath = path;
        dbFilter.groupEnumName = groupEnumName;
        dbFilter.selectedPosition = selectedPosition;
        dbFilter.filterStrength = strength;
        dbFilter.time = System.currentTimeMillis();
        Log.i(TAG, "db insert filter " + " groupEnumName=" + groupEnumName + " groupEnumName + " + " path=" + path + " strength=" + strength);
        effectsDao.insertFilter(dbFilter);
    }

    public long getFilterTime() {
        if (!(AtyStateContext.getInstance().getState() instanceof CameraAtyState)) {
            return 0;
        }
        DBFilterEntity filter = effectsDao.getFilter();
        if (filter == null) {
            return 0;
        }
        return filter.time;
    }

    public long getStyleTime() {
        if (!(AtyStateContext.getInstance().getState() instanceof CameraAtyState)) {
            return 0;
        }
        DBStyleEntity style = effectsDao.getStyle();
        if (style == null) {
            return 0;
        }
        return style.time;
    }

    public void updateFilterStrength(float strength) {
        if (!(AtyStateContext.getInstance().getState() instanceof CameraAtyState)) {
            return;
        }
        DBFilterEntity filter = effectsDao.getFilter();
        if (filter == null) {
            return;
        }
        filter.filterStrength = strength;
        filter.time = System.currentTimeMillis();
        Log.i(TAG, "db insert filter " + " groupEnumName=" + filter.groupEnumName + " groupEnumName + " + " path=" + filter.filterPath + " strength=" + strength);
        effectsDao.insertFilter(filter);
    }

    public DBFilterEntity queryFilter() {
        if (!(AtyStateContext.getInstance().getState() instanceof CameraAtyState)) {
            return null;
        }
        DBFilterEntity filter = effectsDao.getFilter();
        if (filter == null) {
            Log.i(TAG, "filter info is null.");
            return null;
        }
        Log.i(TAG, filter.toString());
        return filter;
    }

    public void insertStyle(String uid, String enumName, int selectedPosition, String path, float filterStrength, float makeUpStrength) {
        if (!(AtyStateContext.getInstance().getState() instanceof CameraAtyState)) {
            return;
        }
        DBStyleEntity dbStyleEntity = new DBStyleEntity();
        dbStyleEntity.id = "style";
        dbStyleEntity.path = path;
        dbStyleEntity.selectedPosition = selectedPosition;
        dbStyleEntity.enumName = enumName;
        dbStyleEntity.uid = uid;
        dbStyleEntity.filterStrength = filterStrength;
        dbStyleEntity.makeupStrength = makeUpStrength;
        dbStyleEntity.time = System.currentTimeMillis();
        effectsDao.insertStyle(dbStyleEntity);
    }

    public void updateStyleStrength(float filterStrength, float makeStrength) {
        if (!(AtyStateContext.getInstance().getState() instanceof CameraAtyState)) {
            return;
        }
        DBStyleEntity style = effectsDao.getStyle();
        if (style == null) {
            return;
        }
        style.filterStrength = filterStrength;
        style.makeupStrength = makeStrength;
        style.time = System.currentTimeMillis();
        effectsDao.insertStyle(style);
    }

    public void clearStyle() {
        DBStyleEntity style = effectsDao.getStyle();
        if (style == null) {
            style = new DBStyleEntity();
        }
        style.path = "";
        style.makeupStrength = 0;
        style.filterStrength = 0;
        style.selectedPosition = -1;
        style.time = System.currentTimeMillis();
        effectsDao.insertStyle(style);
    }

    public void insertBaseBeauty(BeautyItem beautyItem) {
        if (!(AtyStateContext.getInstance().getState() instanceof CameraAtyState)) {
            return;
        }
        DBBeautyEntity dbBeauty = Observable.just(beautyItem).map(beautyItem1 -> {
            DBBeautyEntity dbBeautyEntity = new DBBeautyEntity();
            dbBeautyEntity.id = beautyItem1.uid;
            dbBeautyEntity.strength = beautyItem1.getProgress();
            dbBeautyEntity.current_use = beautyItem1.current_use;
            dbBeautyEntity.beauty_type = beautyItem1.beauty_type;
            dbBeautyEntity.is_3d_plastic = beautyItem1.is_3d_plastic;
            dbBeautyEntity.mode = beautyItem1.mode;
            dbBeautyEntity.beauty_asset_path = beautyItem1.beauty_asset_path;
            dbBeautyEntity.skip_set_when_restart = beautyItem1.skip_set_when_restart;
            dbBeautyEntity.timestamp = System.currentTimeMillis();
            return dbBeautyEntity;
        }).blockingFirst();
        if (dbBeauty.id == null || TextUtils.isEmpty(dbBeauty.id)) {
            return;
        }
        effectsDao.insertBeauty(dbBeauty);
    }

    public DBBeautyEntity queryBaseBeauty(String uid) {
        if (!(AtyStateContext.getInstance().getState() instanceof CameraAtyState)) {
            return null;
        }
        List<DBBeautyEntity> allBeautyItem = effectsDao.getAllBeautyItem();
        for (DBBeautyEntity item : allBeautyItem) {
            if (item.id.equals(uid)) {  // 假设 BeautyItem 有一个 getUid() 方法
                return item;
            }
        }
        return null;  // 如果没有找到匹配的 uid，则返回 null
    }

    public ArrayList<BeautyItem> queryBaseBeauty() {
        List<DBBeautyEntity> allBeautyItem = effectsDao.getAllBeautyItem();
        for (DBBeautyEntity item : allBeautyItem) {
            Log.i(TAG, item.toString());
        }
        ArrayList<BeautyItem> beautyItems = Observable.just(allBeautyItem).map(dbBeautyEntities -> {
            ArrayList<BeautyItem> beautyList = new ArrayList<>();
            for (DBBeautyEntity dbBeauty : dbBeautyEntities) {
                BeautyItem beautyItem = new BeautyItem();
                beautyItem.uid = dbBeauty.id;
                beautyItem.mode = dbBeauty.mode;
                beautyItem.current_use = dbBeauty.current_use;
                beautyItem.timestamp = dbBeauty.timestamp;
                beautyItem.beauty_type = dbBeauty.beauty_type;
                beautyItem.beauty_asset_path = dbBeauty.beauty_asset_path;
                beautyItem.setProgress(dbBeauty.strength);
                beautyList.add(beautyItem);
            }
            return beautyList;
        }).blockingFirst();
        return beautyItems;
    }

    public DBStyleEntity queryStyle() {
        DBStyleEntity style = effectsDao.getStyle();
        if (style == null) {
            Log.i(TAG, "style info is null.");
            return null;
        }
        Log.i(TAG, style.toString());
        return style;
    }

    public Observable<BeautyItem> observableMap(DBBeautyEntity dbBeauty) {
        return Observable.just(dbBeauty).map(new Function<DBBeautyEntity, BeautyItem>() {
            @Override
            public BeautyItem apply(DBBeautyEntity dbBeauty) throws Throwable {
                if (dbBeauty == null || dbBeauty.id == null) {
                    return null;
                }
                BeautyItem item = new BeautyItem();
                item.uid = dbBeauty.id;
                item.setProgress(dbBeauty.strength);
                item.beauty_type = dbBeauty.beauty_type;
                return item;
            }
        });
    }

    public BeautyItem queryBaseBeauty(ArrayList<BeautyItem> datas, String uid) {
        for (BeautyItem item : datas) {
            if (item.uid.equals(uid)) {  // 假设 BeautyItem 有一个 getUid() 方法
                return item;
            }
        }
        return null;  // 如果没有找到匹配的 uid，则返回 null
    }

}
