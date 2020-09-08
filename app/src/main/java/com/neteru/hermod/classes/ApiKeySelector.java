package com.neteru.hermod.classes;

import java.util.Random;

public class ApiKeySelector {
    private String[] keyList = new String[]{
                    "ef51b5f2819344b1adec17f1324a36a8",
                    "b81b3a7a71f14bc2aec68f78a6c3c9d5",
                    "529c47b8a4b34b26a11a0c4e4de9a368",
                    "1367a0de0b714fde85d8ae6bc1ac74d6",
                    "dd957098fb944564b2ec7788f4afce73"

    };

    private ApiKeySelector(){}

    public static ApiKeySelector getInstance(){
        return new ApiKeySelector();
    }

    public String getKey(){
        return keyList[new Random().nextInt(keyList.length)];
    }
}
