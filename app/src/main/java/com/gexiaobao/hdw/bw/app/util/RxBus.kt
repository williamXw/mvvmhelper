package com.gexiaobao.hdw.bw.app.util

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import java.util.concurrent.ConcurrentHashMap

/**
 * created by : huxiaowei
 * @date : 20220920
 * Describe :
 */
class RxBus private constructor() {

    private val subjectMapper = ConcurrentHashMap<Any, MutableList<Subject<Any>>>()

    private val subscriber = ConcurrentHashMap<Any, ConcurrentHashMap<Any, Observable<*>>>()

    private val subscriptionMapper = ConcurrentHashMap<Any, MutableList<Disposable>>()

    companion object {
        val instance: RxBus by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RxBus()
        }
    }

    private fun isEmpty(collection: Collection<Subject<*>?>?): Boolean {
        return null == collection || collection.isEmpty()
    }

    fun register(@NonNull tag: Any, @NonNull consumer: Consumer<Any>, clazz: Class<*>): Observable<Any> {
        var subjectList = subjectMapper[tag]
        if (null == subjectList) {
            subjectList = ArrayList()
            subjectMapper[tag] = subjectList
        }
        val subject: Subject<Any>
        subject = PublishSubject.create()
        subjectList.add(subject)

        val sub = subject.observeOn(AndroidSchedulers.mainThread())
            .subscribe(consumer) { obj: Throwable -> obj.printStackTrace() }
        var subList: MutableList<Disposable>? = subscriptionMapper[clazz]
        if (null == subList) {
            subList = ArrayList()
        }
        subList.add(sub)
        subscriptionMapper[clazz] = subList

        var subMap: ConcurrentHashMap<Any, Observable<*>>? = subscriber[clazz]
        if (subMap == null) {
            subMap = ConcurrentHashMap()
        }
        subMap[tag] = subject
        subscriber[clazz] = subMap

        return subject
    }


    fun unregister(clazz: Class<*>) {
        unsubscribe(clazz)
        val sub: ConcurrentHashMap<Any, Observable<*>>? = subscriber[clazz]
        if (sub != null) {
            for (tag in sub.keys) {
                val value = sub[tag]
                if (null != value) {
                    unregister(tag)
                }
            }
        }

    }

    private fun unsubscribe(clazz: Class<*>) {
        val sub = subscriptionMapper.remove(clazz)
        if (sub != null && sub.isNotEmpty()) {
            val it = sub.listIterator()
            while (it.hasNext()) {
                val subscriptDispose = it.next()
                subscriptDispose.dispose()
                it.remove()
            }
        }
    }

    private fun unregister(@NonNull tag: Any) {
        subjectMapper[tag]?.remove(tag)
    }

    fun post(@NonNull tag: Any, @NonNull content: Any) {
        val subjectList = subjectMapper[tag]
        if (!isEmpty(subjectList)) {
            if (subjectList != null) {
                for (subject in subjectList) {
                    subject.onNext(content)
                }
            }
        }
    }
}