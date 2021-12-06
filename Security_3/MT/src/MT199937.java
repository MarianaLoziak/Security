import java.math.BigInteger;

public class MT199937 {

    private final static int N = 624;
    private final static int M = 397;
    private final static int MAGIC[] = { 0x0, 0x9908b0df };
    private final static int UPPER_MASK = 0x80000000;
    private final static int LOWER_MASK = 0x7fffffff;
    private final static int FACTOR1 = 1812433253;
    private final static int MASK1   = 0x9d2c5680;
    private final static int MASK2   = 0xefc60000;

    private transient int[] mt;
    private transient int mti;
    private transient boolean compat = false;

    public MT199937() { }
    public MT199937(int seed) {
        setSeed(seed);
    }



    private final void setSeed(int seed) {
        if(mt == null) mt = new int[N];
        mt[0] = seed;
        for (mti = 1; mti < N; mti++) {
            mt[mti] = (FACTOR1 * (mt[mti-1] ^ (mt[mti-1] >>> 30)) + mti);
        }
    }

    protected final synchronized int next(int bits) {
        int y, kk;
        if (mti >= N) {
            for (kk = 0; kk < N-M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk+1] & LOWER_MASK);
                mt[kk] = mt[kk+M] ^ (y >>> 1) ^ MAGIC[y & 0x1];
            }
            for (;kk < N-1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk+1] & LOWER_MASK);
                mt[kk] = mt[kk+(M-N)] ^ (y >>> 1) ^ MAGIC[y & 0x1];
            }
            y = (mt[N-1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N-1] = mt[M-1] ^ (y >>> 1) ^ MAGIC[y & 0x1];

            mti = 0;
        }

        y = mt[mti++];

        y ^= (y >>> 11);
        y ^= (y << 7) & MASK1;
        y ^= (y << 15) & MASK2;
        y ^= (y >>> 18);

        return (y >>> (32-bits));
    }


}
