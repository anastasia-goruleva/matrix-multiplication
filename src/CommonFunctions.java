import java.util.Collections;
import java.util.List;

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

    public static int getNewDimension(int leftRows, int leftColumns, int rightColumns) {
        int maxDimension = Collections.max(List.of(leftRows, leftColumns, rightColumns));
        return isTwoPower(maxDimension) ? maxDimension : (1 << log2(maxDimension));
    }
}
