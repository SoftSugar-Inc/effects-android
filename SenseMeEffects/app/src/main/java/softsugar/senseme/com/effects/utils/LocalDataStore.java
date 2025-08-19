package softsugar.senseme.com.effects.utils;

import static com.softsugar.stmobile.model.STEffectLipStickFinish.EFFECT_LIPSTICK_FROST;
import static com.softsugar.stmobile.model.STEffectLipStickFinish.EFFECT_LIPSTICK_LUSTRE;
import static com.softsugar.stmobile.model.STEffectLipStickFinish.EFFECT_LIPSTICK_MATTE;
import static com.softsugar.stmobile.model.STEffectLipStickFinish.EFFECT_LIPSTICK_METAL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.Toaster;
import com.softsugar.stmobile.model.STEffectLipStickFinish;
import com.softsugar.stmobile.params.STFaceShape;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Function;
import softsugar.senseme.com.effects.R;
import softsugar.senseme.com.effects.SenseMeApplication;
import softsugar.senseme.com.effects.StickerTitleEntity;
import softsugar.senseme.com.effects.encoder.mediacodec.utils.CollectionUtils;
import softsugar.senseme.com.effects.entity.BasicBeautyTitleItem;
import softsugar.senseme.com.effects.entity.BasicEffectEntity;
import softsugar.senseme.com.effects.entity.BeautyItemNew;
import softsugar.senseme.com.effects.entity.FilterTitleItem;
import softsugar.senseme.com.effects.entity.MakeUpTitleItem;

import softsugar.senseme.com.effects.view.BeautyItem;
import softsugar.senseme.com.effects.view.BeautyOptionsItem;
import softsugar.senseme.com.effects.view.FilterItem;
import softsugar.senseme.com.effects.view.MakeupItem;
import softsugar.senseme.com.effects.view.StickerItem;
import softsugar.senseme.com.effects.view.StickerState;
import softsugar.senseme.com.effects.view.widget.EffectType;

public class LocalDataStore implements LocalDataStoreI {
    private static final String TAG = "LocalDataStore";

    private LocalDataStore() {
    }

    public static LocalDataStore getInstance() {
        return LocalDataManagerHolder.instance;
    }

    private static class LocalDataManagerHolder {
        @SuppressLint("StaticFieldLeak")
        private static final LocalDataStore instance = new LocalDataStore();
    }

    private ArrayList<FilterItem> selfieList;
    private ArrayList<FilterItem> sceneryList;
    private ArrayList<FilterItem> objectsList;
    private ArrayList<FilterItem> foodsList;
    private ArrayList<FilterItem> textureList;
    private ArrayList<FilterItem> filmList;
    private ArrayList<FilterItem> vintageList;

    private ArrayList<MakeupItem> hairList;
    private ArrayList<MakeupItem> lipList;
    private ArrayList<MakeupItem> blushList;
    private ArrayList<MakeupItem> xrList;
    private ArrayList<MakeupItem> browList;
    private ArrayList<MakeupItem> eyeshadowList;
    private ArrayList<MakeupItem> eyelinerList;
    private ArrayList<MakeupItem> eyelashList;
    private ArrayList<MakeupItem> eyeballList;

    public void prepareAsync() {
        if (CollectionUtils.isEmpty(selfieList)) {// 人物
            String root = getContext().getExternalFilesDir(null) + File.separator;
            textureList = FileUtils.getFileItems(root + Constants.ASSET_FILTER_TEXTURE);
            filmList = FileUtils.getFileItems(root + Constants.ASSET_FILTER_FILM);
            vintageList = FileUtils.getFileItems(root + Constants.ASSET_FILTER_VINTAGE);

            selfieList = FileUtils.getFilterFiles(SenseMeApplication.getContext(), Constants.ASSET_FILTER_PORTRAIT);
            sceneryList = FileUtils.getFilterFiles(SenseMeApplication.getContext(), Constants.ASSET_FILTER_SCENERY);
            objectsList = FileUtils.getFilterFiles(SenseMeApplication.getContext(), Constants.ASSET_FILTER_STILL_LIFE);
            foodsList = FileUtils.getFilterFiles(SenseMeApplication.getContext(), Constants.ASSET_FILTER_FOOD);
        }

        if (CollectionUtils.isEmpty(hairList))
            hairList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_hairdye");

        if (CollectionUtils.isEmpty(lipList))
            lipList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_lip");

        if (null == blushList || blushList.size() == 0) {
            blushList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_blush");
        }
        if (null == xrList || xrList.size() == 0) {
            xrList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_highlight");
        }
        if (null == browList || browList.size() == 0) {
            browList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_brow");
        }
        if (null == eyeshadowList || eyeshadowList.size() == 0) {
            eyeshadowList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeshadow");
        }
        if (null == eyelinerList || eyelinerList.size() == 0) {
            eyelinerList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeliner");
        }
        if (null == eyelashList || eyelashList.size() == 0) {
            eyelashList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyelash");
        }
        if (null == eyeballList || eyeballList.size() == 0) {
            eyeballList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeball");
        }
    }

    @Override
    public EnumMap<EffectType, Float> getMakeupStrengthMap() {
        EnumMap<EffectType, Float> map = new EnumMap<>(EffectType.class);
        ArrayList<EffectType> makeupList = EffectType.Companion.getMakeupList();
        for (EffectType item : makeupList) {
            map.put(item, EffectInfoDataHelper.getInstance().getStrength(item));
        }
        return map;
    }

    @Override
    public EnumMap<EffectType, Integer> getMakeupContentSelectedIndexMap() {
        EnumMap<EffectType, Integer> map = new EnumMap<>(EffectType.class);
        ArrayList<EffectType> makeupList = EffectType.Companion.getMakeupList();
        for (EffectType item : makeupList) {
            map.put(item, EffectInfoDataHelper.getInstance().getContentSelectedIndex(item));
        }
        return map;
    }

    @Override
    public EnumMap<EffectType, Integer> getStyleContentSelectedIndexMap() {
        EnumMap<EffectType, Integer> map = new EnumMap<>(EffectType.class);
        ArrayList<EffectType> styleList = EffectType.Companion.getStyleList();
        for (EffectType item : styleList) {
            map.put(item, EffectInfoDataHelper.getInstance().getContentSelectedIndex(item));
        }
        return map;
    }

    @Override
    public EnumMap<EffectType, Float> getBasicStrengthMap() {
        // 默认参数基础上进行修改
        EnumMap<EffectType, Float> map = EffectType.Companion.getStrengthMap(EffectType.GROUP_EFFECT);

        ArrayList<EffectType> allBasicType = EffectType.Companion.getAllBasicType();
        for (EffectType item : allBasicType) {
            float basicStrength = EffectInfoDataHelper.getInstance().getStrength(item);
            // 数据库无
            if (basicStrength != -2f) {
                map.put(item, basicStrength);
            } else {
                LogUtils.iTag(TAG, item.getDesc() + "set default strength:" + item.getStrength());
                map.put(item, item.getStrength());
            }
        }
        return map;
    }

    @Override
    public List<BasicEffectEntity> getTryOnBoyDefParams() {
        String json = ReadAssetsJsonFileUtils.getJson("localData/tryOn/style2.json");
        return new Gson().fromJson(json, new TypeToken<List<BasicEffectEntity>>() {
        }.getType());
    }

    @Override
    public List<BasicEffectEntity> getTryOnGirlDefParams() {
        String json = ReadAssetsJsonFileUtils.getJson("localData/tryOn/style1.json");
        return new Gson().fromJson(json, new TypeToken<List<BasicEffectEntity>>() {
        }.getType());
    }

    @Override
    public List<StickerItem> getTryOnLipList() {
        List<StickerItem> list = new ArrayList<>();
        list.add(new StickerItem(EFFECT_LIPSTICK_LUSTRE, false, "水润", "file:///android_asset/mock/try_on_lip_zhidi/shuirun.png"));
        list.add(new StickerItem(EFFECT_LIPSTICK_METAL, false, "金属", "file:///android_asset/mock/try_on_lip_zhidi/jinshu.png"));
        list.add(new StickerItem(EFFECT_LIPSTICK_FROST, false, "闪烁", "file:///android_asset/mock/try_on_lip_zhidi/shanshuo.png"));
        list.add(new StickerItem(EFFECT_LIPSTICK_MATTE, false, "雾化", "file:///android_asset/mock/try_on_lip_zhidi/wuhua.png"));
        list.add(new StickerItem(STEffectLipStickFinish.EFFECT_LIPSTICK_CREAMY, false, "自然", "file:///android_asset/mock/try_on_lip_zhidi/ziran.png"));
        return list;
    }

    @Override
    public List<StickerItem> getTryOnLipLineStyleList() {
        List<StickerItem> list = new ArrayList<>();
        list.add(new StickerItem(EFFECT_LIPSTICK_LUSTRE, false, "全部", "file:///android_asset/mock/try_on_lipline_style/all.png"));
        list.add(new StickerItem(EFFECT_LIPSTICK_METAL, false, "正常", "file:///android_asset/mock/try_on_lipline_style/half.png"));
        return list;
    }

    @Override
    public HashMap<EffectType, List<?>> getMakeupListsNew() {
        HashMap<EffectType, List<?>> mMakeupLists = new HashMap<>();
        mMakeupLists.put(EffectType.TYPE_HAIR, hairList);
        mMakeupLists.put(EffectType.TYPE_LIP, lipList);
        mMakeupLists.put(EffectType.TYPE_BLUSH, blushList);
        mMakeupLists.put(EffectType.TYPE_XR, xrList);
        mMakeupLists.put(EffectType.TYPE_EYE_BROW, browList);
        mMakeupLists.put(EffectType.TYPE_EYE_SHADOW, eyeshadowList);
        mMakeupLists.put(EffectType.TYPE_EYE_LINER, eyelinerList);
        mMakeupLists.put(EffectType.TYPE_EYELASH, eyelashList);
        mMakeupLists.put(EffectType.TYPE_EYEBALL, eyeballList);
        return mMakeupLists;
    }

    @Override
    public ArrayList<BasicBeautyTitleItem> getBeautyOptionsList() {
        return getBasicBeautyTitles("json_data/json_base_beauty_titles.json");
    }

    @Override
    public ArrayList<BeautyItem> getAllBaseBeautyItems() {
        ArrayList<BeautyItem> list = new ArrayList<>();
        ArrayList<BasicBeautyTitleItem> beautyOptionsList = LocalDataStore.getInstance().getBeautyOptionsList();
        for (BasicBeautyTitleItem item : beautyOptionsList) {
            String[] assetsPathSubMenus = item.assets_path_sub_menus;
            for (String path:assetsPathSubMenus) {
                ArrayList<BeautyItem> beautyList = LocalDataStore.getInstance().getBeautyList(path, 0);
                list.addAll(beautyList);
            }
        }
        return list;
    }

    public ArrayList<BasicBeautyTitleItem> getBasicBeautyTitles(String jsonAssetPath) {
        return Observable.create((ObservableOnSubscribe<ArrayList<BasicBeautyTitleItem>>) emitter -> {
            String json = loadJSONFromAsset(getContext(), jsonAssetPath);
            Gson gson = new Gson();
            try {
                Type listType = new TypeToken<List<BasicBeautyTitleItem>>() {
                }.getType();
                ArrayList<BasicBeautyTitleItem> titles = gson.fromJson(json, listType);
                for (BasicBeautyTitleItem item : titles) {
                    item.type = EffectType.Companion.getTypeByName(item.enum_name);
                    EffectType type = item.type;

                    type.setCode(item.code);
                    type.setDesc(item.enum_des);
                    if (item.group_id != null) {
                        type.setGroupId(item.group_id);
                    }
                }
                emitter.onNext(titles);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(new Throwable("解析错误=" + jsonAssetPath));
            }
        }).onErrorReturn(new Function<Throwable, ArrayList<BasicBeautyTitleItem>>() {
            @Override
            public ArrayList<BasicBeautyTitleItem> apply(Throwable throwable) throws Throwable {
                return new ArrayList<>();
            }
        }).blockingFirst();
    }

    public ArrayList<BeautyOptionsItem> getBeautyListTitles(String jsonAssetPath) {
        return Observable.create((ObservableOnSubscribe<ArrayList<BeautyOptionsItem>>) emitter -> {
            String json = loadJSONFromAsset(getContext(), jsonAssetPath);
            Gson gson = new Gson();
            try {
                Type listType = new TypeToken<List<BeautyOptionsItem>>() {
                }.getType();
                ArrayList<BeautyOptionsItem> titles = gson.fromJson(json, listType);
                for (BeautyOptionsItem item : titles) {
                    item.type = EffectType.Companion.getTypeByName(item.enum_name);
                    EffectType type = item.type;

                    type.setCode(item.code);
                    type.setDesc(item.enum_des);
                    if (item.group_id != null) {
                        type.setGroupId(item.group_id);
                    }
                }
                emitter.onNext(titles);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(new Throwable("解析错误=" + jsonAssetPath));
            }
        }).onErrorReturn(new Function<Throwable, ArrayList<BeautyOptionsItem>>() {
            @Override
            public ArrayList<BeautyOptionsItem> apply(Throwable throwable) throws Throwable {
                return new ArrayList<>();
            }
        }).blockingFirst();
    }

    public ArrayList<MakeUpTitleItem> getBeautyListTitles2(String jsonAssetPath) {
        return Observable.create((ObservableOnSubscribe<ArrayList<MakeUpTitleItem>>) emitter -> {
            String json = loadJSONFromAsset(getContext(), jsonAssetPath);
            Gson gson = new Gson();
            try {
                Type listType = new TypeToken<List<MakeUpTitleItem>>() {
                }.getType();
                ArrayList<MakeUpTitleItem> titles = gson.fromJson(json, listType);
                for (MakeUpTitleItem item : titles) {
                    item.type = EffectType.Companion.getTypeByName(item.enum_name);
                    EffectType type = item.type;
                    type.setCode(item.code);
                    type.setDesc(item.enum_des);
                    if (item.group_id != null) {
                        type.setGroupId(item.group_id);
                    }
                }
                emitter.onNext(titles);
                emitter.onComplete();
            } catch (Exception e) {
                Toaster.show("解析错误=" + jsonAssetPath);
                emitter.onError(new Throwable("解析错误=" + jsonAssetPath));
            }
        }).onErrorReturn(new Function<Throwable, ArrayList<MakeUpTitleItem>>() {
            @Override
            public ArrayList<MakeUpTitleItem> apply(Throwable throwable) throws Throwable {
                return new ArrayList<>();
            }
        }).blockingFirst();
    }

    @Override
    public ArrayList<StickerTitleEntity> getStickerOptionsListNew() {
        ArrayList<StickerTitleEntity> beautyOptionsItems = Observable.create((ObservableOnSubscribe<ArrayList<StickerTitleEntity>>) emitter -> {
            String json = loadJSONFromAsset(getContext(), "json_data/json_sticker_titles.json");
            Gson gson = new Gson();
            try {
                Type listType = new TypeToken<List<StickerTitleEntity>>() {
                }.getType();
                ArrayList<StickerTitleEntity> titles = gson.fromJson(json, listType);
                for (StickerTitleEntity item : titles) {
                    item.type = EffectType.Companion.getTypeByName(item.enum_name);
                    EffectType type = item.type;
                    if (item.group_id != null) {
                        type.setGroupId(item.group_id);
                    }
                }
                emitter.onNext(titles);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(new Throwable("解析错误="));
            }
        }).onErrorReturn(new Function<Throwable, ArrayList<StickerTitleEntity>>() {
            @Override
            public ArrayList<StickerTitleEntity> apply(Throwable throwable) throws Throwable {
                return new ArrayList<>();
            }
        }).blockingFirst();
        return beautyOptionsItems;
    }



    // 通用物体追踪
    public static ArrayList<StickerItem> getTraceContentList() {
        ArrayList<StickerItem> list = new ArrayList<>();
        list.add(new StickerItem("file:///android_asset/localData/object/object_hi.png", StickerState.DONE_STATE));
        list.add(new StickerItem("file:///android_asset/localData/object/object_happy.png", StickerState.DONE_STATE));
        list.add(new StickerItem("file:///android_asset/localData/object/object_star.png", StickerState.DONE_STATE));
        list.add(new StickerItem("file:///android_asset/localData/object/object_sticker.png", StickerState.DONE_STATE));
        list.add(new StickerItem("file:///android_asset/localData/object/object_love.png", StickerState.DONE_STATE));
        list.add(new StickerItem("file:///android_asset/localData/object/object_sun.png", StickerState.DONE_STATE));
        return list;
    }

    private HashMap<EffectType, List<StickerItem>> stickerContentMap;

    @Override
    public HashMap<EffectType, List<StickerItem>> getStickerContentList() {
        if (stickerContentMap == null || stickerContentMap.isEmpty()) {
            stickerContentMap = new HashMap<>();
            stickerContentMap.put(EffectType.TYPE_STICKER_ADD, FileUtils.getStickerFiles(getContext(), Constants.STICKER_LOCAL));
            stickerContentMap.put(EffectType.TYPE_STICKER_SYNC, FileUtils.getStickerFiles(getContext(), Constants.STICKER_SYNC));
            stickerContentMap.put(EffectType.TYPE_STICKER_LOCAL, FileUtils.getStickerFiles(getContext(), Constants.STICKER_LOCAL));
            stickerContentMap.put(EffectType.TYPE_STICKER_TRACK, getTraceContentList());
        }
        return stickerContentMap;
    }

    @Override
    public ArrayList<FilterTitleItem> getFilterOptionsList() {
        String jsonAssetPath = "json_data/json_filter_titles.json";
        return Observable.create((ObservableOnSubscribe<ArrayList<FilterTitleItem>>) emitter -> {
            String json = loadJSONFromAsset(getContext(), jsonAssetPath);
            Gson gson = new Gson();
            try {
                Type listType = new TypeToken<List<FilterTitleItem>>() {
                }.getType();
                ArrayList<FilterTitleItem> titles = gson.fromJson(json, listType);
                for (FilterTitleItem item : titles) {
                    item.type = EffectType.Companion.getTypeByName(item.enum_name);
                    EffectType type = item.type;
                    type.setDesc(item.enum_des);
                }
                emitter.onNext(titles);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(new Throwable("解析错误=" + jsonAssetPath));
            }
        }).onErrorReturn(new Function<Throwable, ArrayList<FilterTitleItem>>() {
            @Override
            public ArrayList<FilterTitleItem> apply(Throwable throwable) throws Throwable {
                return new ArrayList<>();
            }
        }).blockingFirst();

        // return getBeautyListTitles("json_data/json_filter_titles.json");
    }

    @Override
    public ArrayList<BeautyOptionsItem> getMakeupOptionsList() {
        return getBeautyListTitles("json_data/json_makeup_titles.json");
    }

    @Override
    public ArrayList<MakeUpTitleItem> getMakeupTitleItemList() {
        return getBeautyListTitles2("json_data/json_makeup_titles.json");
    }

    @Override
    public HashMap<EffectType, ArrayList<FilterItem>> getFilterContentList() {
        selfieList = FileUtils.getFilterFiles(SenseMeApplication.getContext(), Constants.ASSET_FILTER_PORTRAIT);
        HashMap<EffectType, ArrayList<FilterItem>> data = new HashMap<>();
        data.put(EffectType.TYPE_TEXTURE, textureList);
        data.put(EffectType.TYPE_FILM, filmList);
        data.put(EffectType.TYPE_VINTAGE, vintageList);
        data.put(EffectType.TYPE_PEOPLE, selfieList);
        data.put(EffectType.TYPE_SCENERY, sceneryList);
        data.put(EffectType.TYPE_FOOD, foodsList);
        data.put(EffectType.TYPE_STILL_LIFE, objectsList);
        return data;
    }

    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            json = sb.toString();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public ArrayList<BeautyItem> getBeautyList(String jsonAssetPath, Integer faceShape) {
        return Observable.create((ObservableOnSubscribe<ArrayList<BeautyItemNew>>) emitter -> {
            String json = loadJSONFromAsset(getContext(), jsonAssetPath);
            Gson gson = new Gson();
            try {
                Type listType = new TypeToken<List<BeautyItemNew>>() {
                }.getType();
                ArrayList<BeautyItemNew> roleList = gson.fromJson(json, listType);
                emitter.onNext(roleList);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(new Throwable("解析错误=" + jsonAssetPath));
            }
        }).onErrorReturn(new Function<Throwable, ArrayList<BeautyItemNew>>() {
            @Override
            public ArrayList<BeautyItemNew> apply(Throwable throwable) throws Throwable {
                //Toaster.show(throwable.getMessage());
                Log.i("lugq", "2223232");
                return new ArrayList<BeautyItemNew>();
            }
        }).map(beautyItemNews -> {
            ArrayList<BeautyItem> mBeautyBaseItem = new ArrayList<>();
            for (BeautyItemNew itemNew : beautyItemNews) {
                EffectType effectType = EffectType.Companion.getTypeByName(itemNew.enum_name);
                assert effectType != null;
                effectType.setStrength(itemNew.def_strength);
                effectType.setDesc(itemNew.enum_des);
                effectType.setCode(itemNew.st_beauty_type);
                int unselectedtIconRes = ResUtil.Companion.getDrawableId(getContext(), itemNew.select_icon_nor_res);
                int selectedtIconRes = ResUtil.Companion.getDrawableId(getContext(), itemNew.select_icon_press_res);
                int text = ResUtil.Companion.getStringId(getContext(), itemNew.display_des_res_id);
                String string = getContext().getString(text);

                BeautyItem beautyItem = new BeautyItem(effectType, string, unselectedtIconRes, selectedtIconRes);
                beautyItem.mutual = itemNew.mutual;
                beautyItem.uid = itemNew.uid;
                beautyItem.need_set_mode = itemNew.need_set_mode;
                beautyItem.beauty_type = itemNew.st_beauty_type;
                beautyItem.mode = itemNew.mode;
                beautyItem.enum_name = itemNew.enum_name;
                beautyItem.need_open_whiten_skin_mask = itemNew.need_open_whiten_skin_mask;
                beautyItem.start_center = itemNew.start_center;
                beautyItem.is_3d_plastic = itemNew.is_3d_plastic;
                beautyItem.other_mutual_beauty_type = itemNew.other_mutual_beauty_type;
                beautyItem.current_use = itemNew.current_use;
                beautyItem.no_seekbar = itemNew.no_seekbar;
                beautyItem.mutual_arr = itemNew.mutual_arr;
                beautyItem.beauty_asset_path = itemNew.beauty_asset_path;
                beautyItem.def_strength = itemNew.def_strength;
                beautyItem.click_goto_enum_num = itemNew.click_goto_enum_num;
                beautyItem.skip_set_when_reset = itemNew.skip_set_when_reset;

                if (faceShape != null) {
                    // 方脸
                    if (faceShape == STFaceShape.ST_FACE_SHAPE_SQUARE) {
                        if (beautyItem.beauty_type == 303) {
                            beautyItem.def_strength = 0.25f;
                        }
                        if (beautyItem.beauty_type == 320) {
                            beautyItem.def_strength = 0.7f;
                        }
                    }

                    // 圆脸
                    if (faceShape == STFaceShape.ST_FACE_SHAPE_ROUND) {
                        if (beautyItem.beauty_type == 302) {
                            beautyItem.def_strength = 0.2f;
                        }
                        if (beautyItem.beauty_type == 320) {
                            beautyItem.def_strength = 0.6f;
                        }
                    }

                    // 长脸
                    if (faceShape == STFaceShape.ST_FACE_SHAPE_LONG) {
                        if (beautyItem.beauty_type == 203) {
                            beautyItem.def_strength = 0.5f;
                        }
                        if (beautyItem.beauty_type == 320) {
                            beautyItem.def_strength = 0.2f;
                        }
                    }

                    // 长方脸
                    if (faceShape == STFaceShape.ST_FACE_SHAPE_RECTANGLE) {
                        if (beautyItem.beauty_type == 203) {
                            beautyItem.def_strength = 0.5f;
                        }
                        if (beautyItem.beauty_type == 320) {
                            beautyItem.def_strength = 0.7f;
                        }
                    }
                }

                mBeautyBaseItem.add(beautyItem);
            }

            return mBeautyBaseItem;
        }).blockingFirst();
    }
    private static Context getContext() {
        return SenseMeApplication.getContext();
    }
}
