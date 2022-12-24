package com.ada.forcert.utilities;

public enum KeySize {
    BIT_512  ( 512),
    BIT_768  ( 768),
    BIT_1024 (1024),
    BIT_2048 (2048),
    BIT_3072 (3072),
    BIT_4096 (4096);

    private int value;

    KeySize(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return this.value;
    }
}
