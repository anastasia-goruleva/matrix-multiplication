public class CommonFunctions {
    public static int log2(int x) {
        int result = 1;
        while ((x >>= 1) != 0) {
            result++;
        }

        return result;
    }

    public static boolean isTwoPower(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
}
