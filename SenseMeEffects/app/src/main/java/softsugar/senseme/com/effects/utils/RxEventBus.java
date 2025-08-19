package softsugar.senseme.com.effects.utils;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class RxEventBus {

    // 模型加载完成通知，模型加载完成后才可以做美妆相关
    public static final PublishSubject<Boolean> modelsLoaded = PublishSubject.create();

    // 震动
    public static final PublishSubject<Boolean> vibrator = PublishSubject.create();
}
