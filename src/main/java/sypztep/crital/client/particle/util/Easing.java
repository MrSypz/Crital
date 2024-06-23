package sypztep.crital.client.particle.util;

import java.util.HashMap;

public abstract class Easing {
    public static final HashMap<String, Easing> EASINGS = new HashMap<>();
    public final String name;
    public Easing(String name) {
        this.name = name;
        EASINGS.put(name, this);
    }

    public abstract float ease(float t, float b, float c, float d);
    public static final Easing CUBIC_IN = new Easing("cubicIn") {
        public float ease(float t, float b, float c, float d) {
            return c * (t /= d) * t * t + b;
        }
    };

    public static abstract class Elastic extends Easing {
        private float amplitude;
        private float period;

        public Elastic(String name, float amplitude, float period) {
            super(name);
            this.amplitude = amplitude;
            this.period = period;
        }
        public Elastic(String name) {
            this(name, -1f, 0f);
        }
        public float getPeriod() {
            return period;
        }

        public void setPeriod(float period) {
            this.period = period;
        }

        public float getAmplitude() {
            return amplitude;
        }

        public void setAmplitude(float amplitude) {
            this.amplitude = amplitude;
        }
    }

    public static final Elastic ELASTIC_OUT = new ElasticOut();

    public static class ElasticOut extends Elastic {
        public ElasticOut(float amplitude, float period) {
            super("elasticOut", amplitude, period);
        }

        public ElasticOut() {
            super("elasticOut");
        }

        public float ease(float t, float b, float c, float d) {
            float a = getAmplitude();
            float p = getPeriod();
            if (t == 0) return b;
            if ((t /= d) == 1) return b + c;
            if (p == 0) p = d * .3f;
            float s = 0;
            if (a < Math.abs(c)) {
                a = c;
                s = p / 4;
            } else s = p / (float) (2 * Math.PI) * (float) Math.asin(c / a);
            return a * (float) Math.pow(2, -10 * t) * (float) Math.sin((t * d - s) * (2 * Math.PI) / p) + c + b;
        }
    }
}
