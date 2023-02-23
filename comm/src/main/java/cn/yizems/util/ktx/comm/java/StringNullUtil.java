package cn.yizems.util.ktx.comm.java;

import java.util.Locale;

public class StringNullUtil extends NullUtil<String> {

    private boolean mutable = false;

    public StringNullUtil(String data) {
        super(data);
    }

    public StringNullUtil mutable() {
        this.mutable = true;
        return this;
    }

    public static StringNullUtil of(CharSequence t) {
        if (t == null) {
            return new StringNullUtil(null);
        }
        return new StringNullUtil(t.toString());
    }

    public boolean isNullOrEmpty() {
        return data == null || data.length() == 0;
    }

    public boolean isNullOrBlank() {
        return data == null || data.trim().length() == 0;
    }

    public StringNullUtil trim() {
        if (data == null) {
            return new StringNullUtil(null);
        }
        if (mutable) {
            data = data.trim();
            return this;
        }
        return new StringNullUtil(data.trim());
    }

    public StringNullUtil toUpperCase() {
        if (data == null) {
            return new StringNullUtil(null);
        }
        if (mutable) {
            data = data.toUpperCase();
            return this;
        }
        return new StringNullUtil(data.toUpperCase());
    }

    public StringNullUtil toLowerCase() {
        if (data == null) {
            return new StringNullUtil(null);
        }
        if (mutable) {
            data = data.toLowerCase();
            return this;
        }
        return new StringNullUtil(data.toLowerCase());
    }

    public StringNullUtil replace(CharSequence target, CharSequence replacement) {
        if (data == null) {
            return new StringNullUtil(null);
        }
        if (mutable) {
            data = data.replace(target, replacement);
            return this;
        }
        return new StringNullUtil(data.replace(target, replacement));
    }

    public StringNullUtil replaceAll(String regex, String replacement) {
        if (data == null) {
            return new StringNullUtil(null);
        }
        if (mutable) {
            data = data.replaceAll(regex, replacement);
            return this;
        }
        return new StringNullUtil(data.replaceAll(regex, replacement));
    }

    public int length() {
        if (data == null) {
            return 0;
        }
        return data.length();
    }

    public StringNullUtil substring(int beginIndex) {
        if (data == null) {
            return new StringNullUtil(null);
        }
        if (mutable) {
            data = data.substring(beginIndex);
            return this;
        }
        return new StringNullUtil(data.substring(beginIndex));
    }

    public int indexOf(String str) {
        if (data == null) {
            return -1;
        }
        return data.indexOf(str);
    }

    public int indexOf(String str, int fromIndex) {
        if (data == null) {
            return -1;
        }
        return data.indexOf(str, fromIndex);
    }

    public int lastIndexOf(String str) {
        if (data == null) {
            return -1;
        }
        return data.lastIndexOf(str);
    }

    public int lastIndexOf(String str, int fromIndex) {
        if (data == null) {
            return -1;
        }
        return data.lastIndexOf(str, fromIndex);
    }

    public StringNullUtil format(Object... args) {
        if (data == null) {
            return new StringNullUtil(null);
        }
        if (mutable) {
            data = String.format(data, args);
            return this;
        }
        return new StringNullUtil(String.format(data, args));
    }

    public StringNullUtil format(String locale, Object... args) {
        if (data == null) {
            return new StringNullUtil(null);
        }
        if (mutable) {
            data = String.format(locale, data, args);
            return this;
        }
        return new StringNullUtil(String.format(locale, data, args));
    }

    public StringNullUtil format(Locale locale, String format, Object... args) {
        if (data == null) {
            return new StringNullUtil(null);
        }
        if (mutable) {
            data = String.format(locale, data, args);
            return this;
        }
        return new StringNullUtil(String.format(locale, data, args));
    }

    public static StringNullUtil valueOf(Boolean b) {
        if (b == null) return new StringNullUtil(null);
        return new StringNullUtil(String.valueOf(b));
    }

    public static StringNullUtil valueOf(char c) {
        return new StringNullUtil(String.valueOf(c));
    }

    public static StringNullUtil valueOf(char[] data) {
        if (data == null) return new StringNullUtil(null);
        return new StringNullUtil(String.valueOf(data));
    }

    public static StringNullUtil valueOf(char[] data, int offset, int count) {
        if (data == null) return new StringNullUtil(null);
        return new StringNullUtil(String.valueOf(data, offset, count));
    }

    public static StringNullUtil valueOf(Double d) {
        if (d == null) return new StringNullUtil(null);
        return new StringNullUtil(String.valueOf(d));
    }

    public static StringNullUtil valueOf(Float f) {
        if (f == null) return new StringNullUtil(null);
        return new StringNullUtil(String.valueOf(f));
    }

    public static StringNullUtil valueOf(Integer i) {
        if (i == null) return new StringNullUtil(null);
        return new StringNullUtil(String.valueOf(i));
    }

    public static StringNullUtil valueOf(Long l) {
        if (l == null) return new StringNullUtil(null);
        return new StringNullUtil(String.valueOf(l));
    }

    public static StringNullUtil valueOf(Object obj) {
        if (obj == null) return new StringNullUtil(null);
        return new StringNullUtil(String.valueOf(obj));
    }

}
