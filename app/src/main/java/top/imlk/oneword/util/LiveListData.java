package top.imlk.oneword.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

/**
 * Created by imlk on 2018/9/29.
 */
public class LiveListData<T> extends LiveData<List<T>> {

    private volatile List mProxyedData = null;

    @Override
    public void setValue(List<T> value) {


        if (value != null) {

            if (value == super.getValue()) {
                value = mProxyedData;
            }

            Object proxyObj = Proxy.newProxyInstance(value.getClass().getClassLoader(), new Class[]{List.class}, new Handler(value));

            super.setValue(((List<T>) proxyObj));

        } else {

            super.setValue(null);
        }

        mProxyedData = value;
    }

    @Nullable
    @Override
    public List<T> getValue() {

        return super.getValue();
    }

    private void setProxyToValue() {
        setValue(super.getValue());
    }

    class Handler implements InvocationHandler {

        private Object proxyedObj;

        public Handler(Object proxyedObj) {
            this.proxyedObj = proxyedObj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Object result = method.invoke(proxyedObj, args);

            if (LiveListData.super.getValue() == proxy)
                switch (method.getName()) {
                    case "retainAll":
                        if (result instanceof Boolean && (((Boolean) result) == false)) {
                            break;
                        }
                    case "add":
                    case "addAll":
                    case "clear":
                    case "remove":
                    case "removeAll":
                    case "replaceAll":
                    case "set":
                    case "sort":
                        setProxyToValue();
                        break;
                }

            return result;
        }
    }
}
