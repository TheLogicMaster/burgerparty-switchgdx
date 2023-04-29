package com.agateau.burgerparty.utils;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Interpolation;

public class InterpolationArgumentDefinition extends ArgumentDefinition<Interpolation> {

    private static Map<String, Interpolation> sMap = new HashMap<String, Interpolation>();

    private static void initMap() {
        sMap.put("bounce", Interpolation.bounce);
        sMap.put("bounceIn", Interpolation.bounceIn);
        sMap.put("bounceOut", Interpolation.bounceOut);
        sMap.put("circle", Interpolation.circle);
        sMap.put("circleIn", Interpolation.circleIn);
        sMap.put("circleOut", Interpolation.circleOut);
        sMap.put("elastic", Interpolation.elastic);
        sMap.put("elasticIn", Interpolation.elasticIn);
        sMap.put("elasticOut", Interpolation.elasticOut);
        sMap.put("exp10", Interpolation.exp10);
        sMap.put("exp10In", Interpolation.exp10In);
        sMap.put("exp10Out", Interpolation.exp10Out);
        sMap.put("exp5", Interpolation.exp5);
        sMap.put("exp5In", Interpolation.exp5In);
        sMap.put("exp5Out", Interpolation.exp5Out);
        sMap.put("fade", Interpolation.fade);
        sMap.put("linear", Interpolation.linear);
        sMap.put("pow2", Interpolation.pow2);
        sMap.put("pow2In", Interpolation.pow2In);
        sMap.put("pow2Out", Interpolation.pow2Out);
        sMap.put("pow3", Interpolation.pow3);
        sMap.put("pow3In", Interpolation.pow3In);
        sMap.put("pow3Out", Interpolation.pow3Out);
        sMap.put("pow4", Interpolation.pow4);
        sMap.put("pow4In", Interpolation.pow4In);
        sMap.put("pow4Out", Interpolation.pow4Out);
        sMap.put("pow5", Interpolation.pow5);
        sMap.put("pow5In", Interpolation.pow5In);
        sMap.put("pow5Out", Interpolation.pow5Out);
        sMap.put("sine", Interpolation.sine);
        sMap.put("sineIn", Interpolation.sineIn);
        sMap.put("sineOut", Interpolation.sineOut);
        sMap.put("swing", Interpolation.swing);
        sMap.put("swingIn", Interpolation.swingIn);
        sMap.put("swingOut", Interpolation.swingOut);
    }

    InterpolationArgumentDefinition(Interpolation defaultValue) {
        super(Interpolation.class, defaultValue);
        if (sMap.isEmpty()) {
            initMap();
        }
    }

    @Override
    public Argument parse(StreamTokenizer tokenizer) {
        try {
            tokenizer.nextToken();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        Interpolation value;
        if (tokenizer.ttype == StreamTokenizer.TT_WORD) {
            value = sMap.get(tokenizer.sval);
            assert(value != null);
        } else {
            assert(this.defaultValue != null);
            tokenizer.pushBack();
            value = this.defaultValue;
        }
        return new BasicArgument(Interpolation.class, value);
    }

}
