public class Lcg {

    public long a;
    public long c;
    public long modulus = (long) Math.pow(2, 32);

    public void findValues(long[] numbers) {
        a = (numbers[2] - numbers[1]) * modularInverse(numbers[1] - numbers[0], modulus) % modulus;
        c = Math.floorMod(numbers[1] - a * numbers[0], modulus);
    }

    public long next(long last, long a, long c) {
        last = Math.floorMod((a * last + c) , modulus);
        return last;
    }

    private static long modularInverse(long number, long modulus) {
        long[] retvals;
        retvals = xgcd(number, modulus);

        if (retvals[0] == 1)
            return Math.floorMod(retvals[1], modulus);
        return -1;
    }

    private static long[] xgcd(long a, long b) {
        long[] retvals = {0, 0, 0};
        long aa[] = {1, 0}, bb[] = {0, 1}, q = 0;
        while (true) {
            q = a / b;
            a = a % b;
            aa[0] = aa[0] - q * aa[1];
            bb[0] = bb[0] - q * bb[1];
            if (a == 0) {
                retvals[0] = b;
                retvals[1] = aa[1];
                retvals[2] = bb[1];
                return retvals;
            }
            ;
            q = b / a;
            b = b % a;
            aa[1] = aa[1] - q * aa[0];
            bb[1] = bb[1] - q * bb[0];
            if (b == 0) {
                retvals[0] = a;
                retvals[1] = aa[0];
                retvals[2] = bb[0];
                return retvals;
            }
            ;
        }
    }
}
