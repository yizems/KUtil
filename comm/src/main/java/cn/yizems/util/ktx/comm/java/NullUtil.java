package cn.yizems.util.ktx.comm.java;

public class NullUtil<T> {

    protected T data;

    public NullUtil(T data) {
        this.data = data;
    }

    public static <T> NullUtil<T> of(T t) {
        return new NullUtil<T>(t);
    }


    public T get() {
        return data;
    }

    public boolean isNull(){
        return data == null;
    }

    public <R> NullUtil<R> map(Mapper<T, R> mapper) {
        if (data == null) {
            return new NullUtil<R>(null);
        }
        return new NullUtil<R>(mapper.apply(data));
    }

    public T getOrElse(T defaultValue) {
        if (data == null) {
            return defaultValue;
        }
        return data;
    }

    public T getOrThrow() throws NullPointerException {
        if (data == null) {
            throw new NullPointerException("data is null");
        }
        return data;
    }

    public void onNull(EmptyConsumer consumer) {
        if (data == null) {
            consumer.accept();
        }
    }
    public void notNull(NotNullConsumer<T> consumer) {
        if (data != null) {
            consumer.accept(data);
        }
    }

    public interface Mapper<T, R> {
        R apply(T t);
    }

    public interface NotNullConsumer<T> {
        void accept(T t);
    }

    public interface EmptyConsumer {
        void accept();
    }

    public StringNullUtil toStringUtil() {
        if (data instanceof String) {
            return new StringNullUtil((String) data);
        }

        if (data instanceof CharSequence) {
            return new StringNullUtil(data.toString());
        }
        if (data == null) {
            return new StringNullUtil(null);
        }
        return new StringNullUtil(data.toString());
    }
}
