import java.util.Arrays;
import java.util.Random;

public class IntAlgorithmsTest {
    public static int[][] randomMatrix(int m, int n) {
        int[][] a = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = new Random().nextInt(100);
            }
        }
        return a;
    }

    public static void printMatrix(int[][] a) {
        for (int i = 0; i < a[0].length; i++) {
            System.out.print("-------");
        }
        System.out.println();
        for (int[] anA : a) {
            System.out.print("|");
            for (int anAnA : anA) {
                System.out.printf("%4d |", anAnA);
            }

            System.out.println();
            for (int i = 0; i < a[0].length; i++) {
                System.out.print("-------");
            }
            System.out.println();
        }
    }


    public static void test(int n, int m, int l) {

        int[][] a = randomMatrix(n, l);
        int[][] b = randomMatrix(l, m);
        long start, end;

        //****************************************
        //	TEST 1
        start = System.currentTimeMillis();
        int[][] matrixByStrassenFJ = IntStrassen.multiStrassenForkJoin(a, b);
        end = System.currentTimeMillis();
        System.out.printf("Strassen Fork-Join Multiply [A:%dx%d; B:%dx%d]: \tElapsed: %dms\n", n, l, l, m, end - start);
        //****************************************

        //****************************************
        //	TEST 2
        start = System.currentTimeMillis();
        int nn = IntStrassen.getNewDimension(a, b);

        int[][] a_n = IntStrassen.addition2SquareMatrix(a, nn);
        int[][] b_n = IntStrassen.addition2SquareMatrix(b, nn);

        int[][] temp = IntStrassen.multiStrassen(a_n, b_n, nn);
        int[][] matrixByStrassen = IntStrassen.getSubmatrix(temp, n, m);
        end = System.currentTimeMillis();
        System.out.printf("Strassen Multiply [A:%dx%d; B:%dx%d]: \tElapsed: %dms\n", n, l, l, m, end - start);
        //****************************************

        //****************************************
        //	TEST 3
        start = System.currentTimeMillis();
        int[][] matrixByUsual = IntMatrixMultiplication.multiply(a, b);
        end = System.currentTimeMillis();
        System.out.printf("Usual Multiply [A:%dx%d; B:%dx%d]: \tElapsed: %dms\n", n, l, l, m, end - start);
        //****************************************

        //****************************************
        //	TEST 4
        start = System.currentTimeMillis();
        int[][] matrixByUsualTransposed = IntMatrixMultiplication.multiplyTransposed(a, b);
        end = System.currentTimeMillis();
        System.out.printf("Usual Multiply Transposed [A:%dx%d; B:%dx%d]: \tElapsed: %dms\n", n, l, l, m, end - start);
        //****************************************

        System.out.println("Matrices are equal: " + Arrays.deepEquals(matrixByStrassenFJ, matrixByStrassen));
        System.out.println("Matrices are equal: " + Arrays.deepEquals(matrixByStrassenFJ, matrixByUsual));
        System.out.println("Matrices are equal: " + Arrays.deepEquals(matrixByStrassenFJ, matrixByUsualTransposed));
    }
}
