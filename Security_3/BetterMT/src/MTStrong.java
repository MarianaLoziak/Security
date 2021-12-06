public class MTStrong {

    private final static int N = 624;
    private final static int M = 397;
    private final static long MAGIC[] = { 0x0, 0x9908b0df };
    private final static long UPPER_MASK = 0x80000000;
    private final static long LOWER_MASK = 0x7fffffff;
    private final static long MAGIC_FACTOR1 = 1812433253;
    private final static long MAGIC_FACTOR2 = 1664525;
    private final static long MAGIC_FACTOR3 = 1566083941;
    private final static long B = 0x9d2c5680;
    private final static long C = 0xefc60000;
    private final static long MAGIC_SEED    = 19650218;
    private final static long DEFAULT_SEED = 5489L;

    private transient long[] mt;
    private transient int mti;
    private transient boolean compat = false;

/*    public MTStrong() {
    }
    public MTStrong(int seed) {
        setSeed(seed);
    }*/
    public MTStrong(long[] buf) {
        setSeed(buf);
    }


    private final void setSeed(long seed) {
        if(mt == null) mt = new long[N];
        mt[0] = seed;
        for (mti = 1; mti < N; mti++) {
            mt[mti] = (MAGIC_FACTOR1 * (mt[mti-1] ^ (mt[mti-1] >>> 30)) + mti);
        }
    }

    public final synchronized void setSeed(long[] buf) {
        int length = buf.length;
        if (length == 0) throw new IllegalArgumentException("Seed buffer may not be empty");
        // ---- Begin Mersenne Twister Algorithm ----
        int i = 1, j = 0, k = (N > length ? N : length);
        setSeed(MAGIC_SEED);
        for (; k > 0; k--) {
            mt[i] = (mt[i] ^ ((mt[i-1] ^ (mt[i-1] >>> 30)) * MAGIC_FACTOR2)) + buf[j] + j;
            i++; j++;
            if (i >= N) { mt[0] = mt[N-1]; i = 1; }
            if (j >= length) j = 0;
        }
        for (k = N-1; k > 0; k--) {
            mt[i] = (mt[i] ^ ((mt[i-1] ^ (mt[i-1] >>> 30)) * MAGIC_FACTOR3)) - i;
            i++;
            if (i >= N) { mt[0] = mt[N-1]; i = 1; }
        }
        mt[0] = UPPER_MASK; // MSB is 1; assuring non-zero initial array
        // ---- End Mersenne Twister Algorithm ----
    }

    protected final synchronized long next(int bits) {
        // ---- Begin Mersenne Twister Algorithm ----
        int kk;
        long y;
        if (mti >= N) {             // generate N words at one time

            // In the original C implementation, mti is checked here
            // to determine if initialisation has occurred; if not
            // it initialises this instance with DEFAULT_SEED (5489).
            // This is no longer necessary as initialisation of the
            // Java instance must result in initialisation occurring
            // Use the constructor MTRandom(true) to enable backwards
            // compatible behaviour.

            for (kk = 0; kk < N-M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk+1] & LOWER_MASK);
                mt[kk] = mt[kk+M] ^ (y >>> 1) ^ MAGIC[(int)y & 0x1];
            }
            for (;kk < N-1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk+1] & LOWER_MASK);
                mt[kk] = mt[kk+(M-N)] ^ (y >>> 1) ^ MAGIC[(int)y & 0x1];
            }
            y = (mt[N-1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N-1] = mt[M-1] ^ (y >>> 1) ^ MAGIC[(int)y & 0x1];

            mti = 0;
        }

        y = mt[mti++];

        // Tempering
        y ^= (y >>> 11);
        y ^= (y << 7) & B;
        y ^= (y << 15) & C;
        y ^= (y >>> 18);
        // ---- End Mersenne Twister Algorithm ----
        return (int)(y >>> (32-bits));
    }
}
