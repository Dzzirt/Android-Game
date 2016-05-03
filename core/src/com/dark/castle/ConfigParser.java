package com.dark.castle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by DzzirtNik on 03.05.2016.
 */
public class ConfigParser {


    public ConfigParser() {
        JsonValue cfg = new JsonReader().parse(Gdx.files.internal("config.json"));
    }
}
