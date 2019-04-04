
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @class StringHashMap
 * @author AMH 2017.05.26
 *
 * class that extends a HashMap,
 * to give more granular control with things like
 * converting recordsets with different types
 * to String Only HashMaps, and custom printing
 * output of HashMap
 */
@SuppressWarnings("serial")
public class StringHashMap extends LinkedHashMap<String, String> {

    public StringHashMap(){

    }

    public void putInt(String Key, int Value) {
        String val2 = Integer.toString(Value);
        this.put(Key, val2);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[' + "\n");
        for (Map.Entry<String, String> e : this.entrySet()) {
            String key = e.getKey();
            String value = e.getValue();
            sb.append("\t" + '"' + key + '"' + " => " + value + "\n");
        }
        sb.append("]");
        return sb.toString();
    }
}
