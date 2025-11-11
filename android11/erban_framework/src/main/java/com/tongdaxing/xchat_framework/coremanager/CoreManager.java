/**
 * 管理core对象的类。外部应该使用此类的接口来获取某个core对象
 * 它使用CoreFactory来声成core对象实例。
 * 上层未注册core实现类话，注册一个默认实现（调用init函数）
 * 此类不是线程安全的，应该只在主线程调用
 */
package com.tongdaxing.xchat_framework.coremanager;

import android.content.Context;
import android.text.TextUtils;

import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author daixiang
 */
public class CoreManager {

    public static final String TAG = "CoreManager";
    public static final String TAG_EVENT = "CoreManager_Event";

    private static Map<Class<? extends ICoreClient>, CopyOnWriteArraySet<ICoreClient>> clients
            = new HashMap<>();

    private static Map<Class<?>, CopyOnWriteArraySet<Object>> coreEvents = new HashMap<>();

    private static Map<Class<? extends ICoreClient>, Map<String, Method>> clientMethods
            =   new HashMap();

    private static Map<Object, Map<String, Method>> coreEventMethods = new HashMap();

    public CoreManager() {
    }

    public static void init(String logDir) {
        if(!TextUtils.isEmpty(logDir)) {
            MLog.LogOptions options = new MLog.LogOptions();
            options.uniformTag = TAG;
            MLog.initialize(logDir, options);
        }

        MLog.info(TAG, "--------------------------------CoreManager init--------------------------------", new Object[0]);
    }

    public static void onTerminate() {
        MLog.close();
    }


    private static void addClientMethodsIfNeeded(Class<? extends ICoreClient> clientClass) {
        try {
            Map<String, Method> methods = clientMethods.get(clientClass);
            if (methods == null) {
                methods = new HashMap<String, Method>();
                Method[] allMethods = clientClass.getMethods();
                for (Method m : allMethods) {
                    methods.put(m.getName(), m);
                }
                clientMethods.put(clientClass, methods);
            }
        } catch (Throwable throwable) {
            MLog.error(TAG, throwable);
        }

    }

    /**
     * 监听某个接口的回调，监听者需要实现该接口
     * 注意在不需要回调时要用removeClient
     *
     * @param clientClass
     * @param client
     */
    public static void addClient(Class<? extends ICoreClient> clientClass, ICoreClient client) {

        if (clientClass == null || client == null) {
            return;
        }

        CopyOnWriteArraySet<ICoreClient> clientList = clients.get(clientClass);
        if (clientList == null) {
            clientList = new CopyOnWriteArraySet<ICoreClient>();
            clients.put(clientClass, clientList);
        }

        addClientMethodsIfNeeded(clientClass);

        if (clientList.contains(client)) {
            return;
        }

        clientList.add(client);
        //MLog.verbose(TAG, "client(" + client + ") added for " + clientClass.getName());
    }

    @SuppressWarnings("unchecked")
    private static void addClient(ICoreClient client, Class<?> clientClass) {
        if (clientClass == null)
            return;

        Class<?>[] interfaces = clientClass.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if (ICoreClient.class.isAssignableFrom(interfaces[i])) {
                Class<? extends ICoreClient> intf = (Class<? extends ICoreClient>) interfaces[i];
                CoreManager.addClient(intf, client);
                //logger.info("client(" + client + ") added for " + clientClass.getName());
            }
        }

        Class<?> superClass = clientClass.getSuperclass();
        addClient(client, superClass);
    }

    /**
     * 监听所有client声明实现的ICoreClient的接口
     *
     * @param client
     */

    public static void addClientICoreClient(ICoreClient client) {

        if (client == null) {
            return;
        }

        addClient(client, client.getClass());
    }

    /**
     * 移除对象对某个接口的监听
     *
     * @param clientClass
     * @param client
     */
    public static void removeClient(Class<? extends ICoreClient> clientClass, ICoreClient client) {

        if (clientClass == null || client == null) {
            return;
        }

        Set<ICoreClient> clientList = clients.get(clientClass);
        if (clientList == null) {
            return;
        }

        clientList.remove(client);
        //MLog.verbose(TAG, "client(" + client + ") removed for " + clientClass.getName());
    }


    /**
     * 移除该对象所有监听接口
     *
     * @param client
     */
    public static void removeClientICoreClient(ICoreClient client) {

        if (client == null) {
            return;
        }

        Collection<CopyOnWriteArraySet<ICoreClient>> c = clients.values();
        for (Set<ICoreClient> list : c) {
            list.remove(client);
        }

        //MLog.verbose(TAG, "client(" + client + ") removed from all");
    }

    /**
     *  增加Client，支持CoreEvent注解
     *
     * @param client
     */
    public static void addClient(Object client) {
        //MLog.verbose(TAG_EVENT, "AddClient support CoreEvent : " + client);
        if (client == null) {
            MLog.warn(TAG_EVENT, "Don't give me a null client");
            return;
        }

        if (client instanceof ICoreClient) {
            //MLog.verbose(TAG_EVENT, client + " instanceof ICoreClient, add to ICoreClient");
            addClientICoreClient((ICoreClient) client);
        }

        Class<?> originalClass = client.getClass();
        if (originalClass == null) {
            MLog.warn(TAG_EVENT, "Client.getClass() is null");
            return;
        }
        Method[] methods = originalClass.getMethods();

        for (Method method : methods) {
            CoreEvent event = method.getAnnotation(CoreEvent.class);
            if (event != null) {
                Class<?> clientClass = event.coreClientClass();
                //MLog.verbose(TAG_EVENT, "Client =" + client + ", event=" + event + ",method=" + method.getName());
                if (clientClass != null) {
                    addCoreEvents(client, clientClass);
                    addCoreEventMethodsIfNeeded(client, clientClass, method);
                }
            }
        }
    }

    private static void addCoreEvents(Object client, Class<?> clientClass) {
        CopyOnWriteArraySet<Object> clients = coreEvents.get(clientClass);
        if (clients == null) {
            //MLog.verbose(TAG_EVENT, "Clients is null, create new set :" + clientClass);
            clients = new CopyOnWriteArraySet<>();
            coreEvents.put(clientClass, clients);
        }

        clients.add(client);
        //MLog.verbose(TAG_EVENT, "Clients add client " + client + ",size=" + clients.size());
    }

    private static void addCoreEventMethodsIfNeeded(Object client, Class<?> clientClass, /*Class<?> originalClass*/Method m) {
        Map<String, Method> methods = coreEventMethods.get(client);
        if (methods == null) {
            //MLog.verbose(TAG_EVENT, "Client " + client + ",Class " + clientClass + " methods null, create new one");
            methods = new HashMap<String, Method>();
            coreEventMethods.put(client, methods);
        }
        //MLog.verbose(TAG_EVENT, "Client=" + client + ",Class=" + clientClass + ",put method=" + m.getName());
        methods.put(m.getName(), m);
    }

    /**
     *  移除该对象所有监听接口，支持CoreEvent
     *
     * @param client
     */
    public static void removeClient(Object client) {

        if (client == null) {
            return;
        }
        try {
            if (client instanceof ICoreClient) {
            /*if (isDebugSvc())
                MLog.verbose(TAG_EVENT, "Client is ICoreClient, remove core client method");*/
                removeClientICoreClient((ICoreClient) client);
            }

            Collection<CopyOnWriteArraySet<Object>> c = coreEvents.values();
            for (CopyOnWriteArraySet<Object> events : c) {
                events.remove(client);
            }

            coreEventMethods.remove(client);
        } catch (Throwable throwable) {
            MLog.error("CoreManager", "removeClient error! " + throwable);
        }

        //MLog.verbose(TAG_EVENT, "client(" + client + ") removed from all");
    }

    /**
     * 返回监听该接口的对象列表
     *
     * @param clientClass
     * @return
     */
    public static Set<ICoreClient> getClients(Class<? extends ICoreClient> clientClass) {

        if (clientClass == null) {
            return null;
        }

        CopyOnWriteArraySet<ICoreClient> clientList = clients.get(clientClass);
        return clientList;
    }

    public interface ICallBack {
        void onCall(ICoreClient client);
    }

    /**
     * 执行回调接口
     *
     * @param clientClass
     * @param callBack
     */
    public static void notifyClients(Class<? extends ICoreClient> clientClass, ICallBack callBack) {
        if (clientClass == null || callBack == null) {
            return;
        }

        Set<ICoreClient> clientList = CoreManager.getClients(clientClass);
        if (clientList == null) {
            if (clientList == null) {
                return;
            }
        }
        try {
            for (ICoreClient client : clientList) {
                callBack.onCall(client);
            }

        } catch (Exception e) {
            MLog.error(TAG, e.getMessage(), e);
        }
    }

    /**
     * 回调所有监听了该接口的对象。methodName为回调的方法名
     * 注意：所有用addClient和addEventListener注册了此接口的对象都会被回调
     * 注意：methodName所指定函数的参数列表个数必须匹配。目前没有对参数类型严格检查，使用时要注意
     *
     * @param clientClass
     * @param methodName
     * @param args
     */
    public static void notifyClients(Class<? extends ICoreClient> clientClass, String methodName, Object... args) {
        notifyClientsCoreEvents(clientClass, methodName, args);
        if (clientClass == null || methodName == null || methodName.length() == 0) {
            return;
        }

        Set<ICoreClient> clientList = CoreManager.getClients(clientClass);
        if (clientList == null) {
            return;
        }

        try {

            Map<String, Method> methods = clientMethods.get(clientClass);
            Method method = methods.get(methodName);

            if (method == null) {
                MLog.error(TAG, "cannot find client method " + methodName + " for args[" + args.length + "]: " + Arrays.toString(args));
                return;
            } else if (method.getParameterTypes() == null) {
                MLog.error(TAG, "cannot find client method  param:" + method.getParameterTypes() + " for args[" + args.length + "]: " + Arrays.toString(args));
                return;
            } else if (method.getParameterTypes().length != args.length) {
                MLog.error(TAG, "method " + methodName + " param number not matched: method(" + method.getParameterTypes().length + "), args(" + args.length + ")");
                return;
            }
            for (Object c : clientList) {
                try {
                    method.invoke(c, args);
                } catch (Throwable e) {
                    MLog.error(TAG, "Notify clients method invoke error.", e);
                }
            }
        } catch (Throwable e) {
            MLog.error(TAG, "Notify clients error.", e);
        }
    }

    /**
     *  广播CoreEvent注解事件
     *
     * @param clientClass
     * @param methodName
     * @param args
     */
    public static void notifyClientsCoreEvents(Class<? extends ICoreClient> clientClass, String methodName, Object... args) {

        if (clientClass == null || methodName == null || methodName.length() == 0) {
            return;
        }

        Set<Object> clients = coreEvents.get(clientClass);

        if (clients == null) {
            MLog.debug(TAG_EVENT, "core clients is null clientClz:%s", clientClass.getSimpleName());
            return;
        }

        try {
            for (Object c : clients) {
                Map<String, Method> methods = coreEventMethods.get(c);
                if (methods == null) {
                    continue;
                }
                Method method = methods.get(methodName);
                Class<?>[] types = null;
                if (method != null) {
                    types = method.getParameterTypes();//减少创建小对象，减少timeout崩溃
                }

                if (method == null) {
                    continue;
                }else if (types == null) {
                    MLog.error(TAG_EVENT, "Can't find " + c + " has method param null for args[" + args.length + "]: " + args);
                    continue;
                } else if (types.length != args.length) {
                    MLog.error(TAG_EVENT, "Can't find " + c + " has Method " + methodName +
                            " param number not matched: method(" + types.length +
                            "), args(" + args.length + ")");
                    continue;
                }

                try {
                    method.invoke(c, args);
                } catch (Throwable e) {
                    MLog.error(TAG_EVENT, "Notify core events method invoke error class=" + clientClass
                            + ",method=" + methodName
                            + ",args=" + args, e);
                }
            }

        } catch (Throwable e) {
            MLog.error(TAG_EVENT, "Notify core events error class=" + clientClass + ",method=" + methodName
                    + ",args=" + args, e);
        }
    }


    public static <T extends IBaseCore> T getCore(Class<T> cls) {
        return CoreFactory.getCore(cls);
    }

}
