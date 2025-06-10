package models.utils;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
// import java.time.OffsetDateTime;
// import java.time.format.DateTimeFormatter;
// import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.logData.logData;
import models.mongoDB.mongoDBParseHistory;

public class arrayListCloner {
    public static final <V> ArrayList<V> deepCopy(ArrayList<V> original) {
        if(original == null) return null;
        ArrayList<V> copy = new ArrayList<>();
        
        for (V item : original) {
            copy.add(arrayListCloner.deepCopyValue(item)); // Recursively deep copy elements
        }
        
        return copy;
    }

    @SuppressWarnings("unchecked")
    private static final <V> V deepCopyValue(V value) {
        if (value == null) {
            return null;
        } else if (value instanceof String || value instanceof Integer || value instanceof Double || value instanceof Boolean || value instanceof mongoDBParseHistory || value instanceof logData) {
            return value; // Immutable objects, safe to return directly
        } else if (value instanceof ArrayList) {
            return (V) arrayListCloner.deepCopy((ArrayList<?>) value); // Recursively copy HashMap
        } else if (value instanceof HashMap) {
            return (V) arrayListCloner.deepCopyHashMap((HashMap<?, ?>) value); // Recursively copy HashMap
        } else if (value instanceof AbstractMap.SimpleEntry) {
            return (V) arrayListCloner.deepCopySimpleEntry((AbstractMap.SimpleEntry<?, ?>) value); // Recursively copy SimpleEntry
        } else if (value instanceof ObservableList) {
            return (V) arrayListCloner.deepCopyObservableList((ObservableList<?>) value); // Recursively copy ObservableList
        } else if (value instanceof Date) {
            return (V) new Date(((Date) value).getTime());
        } else {
            throw new IllegalArgumentException("Unsupported type for deep copy: " + value.getClass());
        }
    }

    @SuppressWarnings("unchecked")
    private static final <K, V> HashMap<K, V> deepCopyHashMap(HashMap<K, V> original) {
        if(original == null) return null;
        HashMap<K, V> copy = new HashMap<>();

        for (Map.Entry<K, V> entry : original.entrySet()) {
            copy.put(entry.getKey(), arrayListCloner.deepCopyValue(entry.getValue()));
        }

        return copy;
    }

    @SuppressWarnings("unchecked")
    private static final <K, V> AbstractMap.SimpleEntry<K, V> deepCopySimpleEntry(AbstractMap.SimpleEntry<K, V> entry) {
        if(entry == null) return null;
        return new AbstractMap.SimpleEntry<>(entry.getKey(), arrayListCloner.deepCopyValue(entry.getValue()));
    }

    @SuppressWarnings("unchecked")
    private static final <T> ObservableList<T> deepCopyObservableList(ObservableList<T> original) {
        if(original == null) return null;
        ObservableList<T> copy = FXCollections.observableArrayList();

        for (T item : original) {
            copy.add(arrayListCloner.deepCopyValue(item));
        }

        return copy;
    }
}
