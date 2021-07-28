import java.util.Arrays;
import java.util.Random;

public class DoubleAlgorithmsTest {
    public static void printMatrix(double[][] a) {
        for (int i = 0; i < a[0].length; i++) {
            System.out.print("-------");
        }
        System.out.println();
        for (double[] anA : a) {
            System.out.print("|");
            for (double anAnA : anA) {
                System.out.printf("%4d |", anAnA);
            }

            System.out.println();
            for (int i = 0; i < a[0].length; i++) {
                System.out.print("-------");
            }
            System.out.println();
        }
    }

    public static double[][] randomMatrix(int m, int n) {
        double[][] a = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = new Random().nextInt(100);
            }
        }
        return a;
    }

    public static void test(int n, int m, int l) {

        double[][] a = randomMatrix(n, l);
        double[][] b = randomMatrix(l, m);
        long start, end;

        //****************************************
        //	TEST 1
        start = System.currentTimeMillis();
        double[][] matrixByStrassenFJ = DoubleStrassen.multiStrassenForkJoin(a, b);
        end = System.currentTimeMillis();
        System.out.printf("Strassen Fork-Join Multiply [A:%dx%d; B:%dx%d]: \tElapsed: %dms\n", n, l, l, m, end - start);
        //****************************************

        //****************************************
        //	TEST 2
        start = System.currentTimeMillis();
        int nn = DoubleStrassen.getNewDimension(a, b);

        double[][] a_n = DoubleStrassen.addition2SquareMatrix(a, nn);
        double[][] b_n = DoubleStrassen.addition2SquareMatrix(b, nn);

        double[][] temp = DoubleStrassen.multiStrassen(a_n, b_n, nn);
        double[][] matrixByStrassen = DoubleStrassen.getSubmatrix(temp, n, m);
        end = System.currentTimeMillis();
        System.out.printf("Strassen Multiply [A:%dx%d; B:%dx%d]: \tElapsed: %dms\n", n, l, l, m, end - start);
        //****************************************

        //****************************************
        //	TEST 3
        start = System.currentTimeMillis();
        double[][] matrixByUsual = MatrixMultiplicationDouble.multiply(a, b);
        end = System.currentTimeMillis();
        System.out.printf("Usual Multiply [A:%dx%d; B:%dx%d]: \tElapsed: %dms\n", n, l, l, m, end - start);
        //****************************************

        //****************************************
        //	TEST 4
        start = System.currentTimeMillis();
        double[][] matrixByUsualTransposed = MatrixMultiplicationDouble.multiplyTransposed(a, b);
        end = System.currentTimeMillis();
        System.out.printf("Usual Multiply Transposed [A:%dx%d; B:%dx%d]: \tElapsed: %dms\n", n, l, l, m, end - start);
        //****************************************

        System.out.println("Matrices are equal: " + Arrays.deepEquals(matrixByStrassenFJ, matrixByStrassen));
        System.out.println("Matrices are equal: " + Arrays.deepEquals(matrixByStrassenFJ, matrixByUsual));
        System.out.println("Matrices are equal: " + Arrays.deepEquals(matrixByStrassenFJ, matrixByUsualTransposed));

    }
}
