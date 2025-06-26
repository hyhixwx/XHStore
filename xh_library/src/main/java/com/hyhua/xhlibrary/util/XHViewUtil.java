package com.hyhua.xhlibrary.util;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Deque;

public class XHViewUtil {
    /**
     * 获取指定类型的子View
     */
    public static <T> T findTypeView(@NonNull ViewGroup group, Class<T> cls) {
        Deque<View> deque = new ArrayDeque<>();// 双端队列，基于可变数组实现，支持高效的两端插入和删除操作
        deque.add(group);
        while (!deque.isEmpty()) {
            View node = deque.removeFirst();// 获取头部第一个元素
            if (cls.isInstance(node)) {
                return cls.cast(node);
            } else if (node instanceof ViewGroup) {// 头部第一个元素是ViewGroup，就把它的子View添加到队列尾部
                ViewGroup container = (ViewGroup) node;
                for (int i = 0, count = container.getChildCount(); i < count; i++) {
                    deque.addLast(container.getChildAt(i));
                }
            }
        }
        return null;
    }
}
