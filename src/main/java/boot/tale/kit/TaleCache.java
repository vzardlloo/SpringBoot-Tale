package boot.tale.kit;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaleCache {

    /**
     * 默认存储1024个缓存
     */
    private static final int DEFAULT_CACHES = 1024;

    private static final TaleCache INS = new TaleCache();

    public static TaleCache single() {
        return INS;
    }

    private Map<String, CacheObject> cachePool;

    public TaleCache() {
        this(DEFAULT_CACHES);
    }

    public TaleCache(int cacheCount) {
        cachePool = new ConcurrentHashMap<>(cacheCount);
    }

    /**
     * 读取一个缓存
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T get(String key) {
        CacheObject cacheObject = cachePool.get(key);
        if (null != cacheObject) {
            long currentTime = System.currentTimeMillis() / 1000;
            if (cacheObject.getExpired() <= 0 || cacheObject.getExpired() > currentTime) {
                Object result = cacheObject.getValue();
                return (T) result;
            } else {
                //缓存过期删除
                del(key);
            }
        }
        return null;
    }


    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        this.set(key, value, -1);
    }

    /**
     * 设置一个缓存并带过期时间
     *
     * @param key     缓存key
     * @param value   缓存value
     * @param expired 过期时间，单位为秒
     */
    public void set(String key, Object value, long expired) {
        expired = expired > 0 ? System.currentTimeMillis() / 1000 + expired : expired;
        CacheObject cacheObject = new CacheObject(key, value, expired);
        cachePool.put(key, cacheObject);
    }

    /**
     * 设置一个hash缓存
     *
     * @param key
     * @param field
     * @param value
     */
    public void hset(String key, String field, Object value) {
        this.hset(key, field, value, -1);
    }

    /**
     * 设置一个hash缓存并带过期时间
     *
     * @param key
     * @param field
     * @param value
     * @param expired
     */
    public void hset(String key, String field, Object value, long expired) {
        key = key + ":" + field;
        expired = expired > 0 ? System.currentTimeMillis() / 1000 + expired : expired;
        CacheObject cacheObject = new CacheObject(key, value, expired);
        cachePool.put(key, cacheObject);
    }

    /**
     * 根据key删除缓存
     *
     * @param key
     */
    public void del(String key) {
        cachePool.remove(key);
    }

    /**
     * 根据key和field删除缓存
     *
     * @param key
     * @param field
     */
    public void hdel(String key, String field) {
        key = key + ":" + field;
        this.del(key);
    }

    /**
     * 清空缓存
     */
    public void clean() {
        cachePool.clear();
    }


    /**
     * 缓存对象
     */
    static class CacheObject {

        private String key;
        private Object value;
        private long expired;

        public CacheObject(String key, Object value, long expired) {
            this.key = key;
            this.value = value;
            this.expired = expired;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public long getExpired() {
            return expired;
        }
    }

}
