package controller;

import java.util.HashMap;

public interface DataReceiver<T extends HashMap<?, ?>> {
    void setData(T data);
}