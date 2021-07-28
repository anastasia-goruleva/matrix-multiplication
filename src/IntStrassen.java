import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * The {@code MatrixMultiplication} class implements
 * fast multiplication 2 matrices at each other.
 * The {@code MatrixMultiplication} uses Strassen algorithm and
 * parallelize it with the {@link java.util.concurrent.ForkJoinPool}
 *
 * @author Evgeny Usov
 * @author Alexey Falko
 */
public class IntStrassen {

    private static int[][] summation(int[][] a, int[][] b) {

        int n = a.length;
        int m = a[0].length;
        int[][] c = new int[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                c[i][j] = a[i][j] + b[i][j];
            }
        }
        return c;
    }

    //******************************************************************************************

    private static int[][] subtraction(int[][] a, int[][] b) {

        int n = a.length;
        int m = a[0].length;
        int[][] c = new int[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                c[i][j] = a[i][j] - b[i][j];
            }
        }
        return c;
    }

    //******************************************************************************************

    public static int[][] addition2SquareMatrix(int[][] a, int n) {

        int[][] result = new int[n][n];

        for (int i = 0; i < a.length; i++) {
            System.arraycopy(a[i], 0, result[i], 0, a[i].length);
        }
        return result;
    }

    //******************************************************************************************

    public static int[][] getSubmatrix(int[][] a, int n, int m) {
        int[][] result = new int[n][m];
        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, result[i], 0, m);
        }
        return result;
    }

    //******************************************************************************************

    private static void splitMatrix(int[][] a, int[][] a11, int[][] a12, int[][] a21, int[][] a22) {

        int n = a.length >> 1;

        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, a11[i], 0, n);
            System.arraycopy(a[i], n, a12[i], 0, n);
            System.arraycopy(a[i + n], 0, a21[i], 0, n);
            System.arraycopy(a[i + n], n, a22[i], 0, n);
        }
    }

    //******************************************************************************************

    private static int[][] collectMatrix(int[][] a11, int[][] a12, int[][] a21, int[][] a22) {

        int n = a11.length;
        int[][] a = new int[n << 1][n << 1];

        for (int i = 0; i < n; i++) {
            System.arraycopy(a11[i], 0, a[i], 0, n);
            System.arraycopy(a12[i], 0, a[i], n, n);
            System.arraycopy(a21[i], 0, a[i + n], 0, n);
            System.arraycopy(a22[i], 0, a[i + n], n, n);
        }

        return a;
    }

    //******************************************************************************************

    /**
     * Multi-threaded matrix multiplication
     * algorithm by Strassen
     */
    private static class myRecursiveTask extends RecursiveTask<int[][]> {
        private static final long serialVersionUID = -433764214304695286L;

        int n;
        int[][] a;
        int[][] b;

        public myRecursiveTask(int[][] a, int[][] b, int n) {
            this.a = a;
            this.b = b;
            this.n = n;
        }

        /**
         * @return the integer matrix by
         * multiplying 2 matrices at each other
         */
        @Override
        protected int[][] compute() {
            if (n <= 128) {
                return IntMatrixMultiplication.multiplyTransposed(a, b);
            }

            n >>= 1;

            int[][] a11 = new int[n][n];
            int[][] a12 = new int[n][n];
            int[][] a21 = new int[n][n];
            int[][] a22 = new int[n][n];

            int[][] b11 = new int[n][n];
            int[][] b12 = new int[n][n];
            int[][] b21 = new int[n][n];
            int[][] b22 = new int[n][n];

            splitMatrix(a, a11, a12, a21, a22);
            splitMatrix(b, b11, b12, b21, b22);

            myRecursiveTask task_p1 = new myRecursiveTask(summation(a11, a22), summation(b11, b22), n);
            myRecursiveTask task_p2 = new myRecursiveTask(summation(a21, a22), b11, n);
            myRecursiveTask task_p3 = new myRecursiveTask(a11, subtraction(b12, b22), n);
            myRecursiveTask task_p4 = new myRecursiveTask(a22, subtraction(b21, b11), n);
            myRecursiveTask task_p5 = new myRecursiveTask(summation(a11, a12), b22, n);
            myRecursiveTask task_p6 = new myRecursiveTask(subtraction(a21, a11), summation(b11, b12), n);
            myRecursiveTask task_p7 = new myRecursiveTask(subtraction(a12, a22), summation(b21, b22), n);

            task_p1.fork();
            task_p2.fork();
            task_p3.fork();
            task_p4.fork();
            task_p5.fork();
            task_p6.fork();
            task_p7.fork();

            int[][] p1 = task_p1.join();
            int[][] p2 = task_p2.join();
            int[][] p3 = task_p3.join();
            int[][] p4 = task_p4.join();
            int[][] p5 = task_p5.join();
            int[][] p6 = task_p6.join();
            int[][] p7 = task_p7.join();

            int[][] c11 = summation(summation(p1, p4), subtraction(p7, p5));
            int[][] c12 = summation(p3, p5);
            int[][] c21 = summation(p2, p4);
            int[][] c22 = summation(subtraction(p1, p2), summation(p3, p6));

            return collectMatrix(c11, c12, c21, c22);
        }

    }

    //******************************************************************************************

    public static int[][] multiStrassenForkJoin(int[][] a, int[][] b) {

        int nn = getNewDimension(a, b);
        int[][] a_n = addition2SquareMatrix(a, nn);
        int[][] b_n = addition2SquareMatrix(b, nn);

        myRecursiveTask task = new myRecursiveTask(a_n, b_n, nn);
        ForkJoinPool pool = new ForkJoinPool();
        int[][] fastFJ = pool.invoke(task);

        return getSubmatrix(fastFJ, a.length, b[0].length);
    }

    //******************************************************************************************

    @Deprecated
    /**
     * Single-threaded matrix multiplication
     * algorithm by Strassen
     * */
    public static int[][] multiStrassen(int[][] a, int[][] b, int n) {
        if (n <= 128) {
            return IntMatrixMultiplication.multiplyTransposed(a, b);
        }

        n = n >> 1;

        int[][] a11 = new int[n][n];
        int[][] a12 = new int[n][n];
        int[][] a21 = new int[n][n];
        int[][] a22 = new int[n][n];

        int[][] b11 = new int[n][n];
        int[][] b12 = new int[n][n];
        int[][] b21 = new int[n][n];
        int[][] b22 = new int[n][n];

        splitMatrix(a, a11, a12, a21, a22);
        splitMatrix(b, b11, b12, b21, b22);

        int[][] p1 = multiStrassen(summation(a11, a22), summation(b11, b22), n);
        int[][] p2 = multiStrassen(summation(a21, a22), b11, n);
        int[][] p3 = multiStrassen(a11, subtraction(b12, b22), n);
        int[][] p4 = multiStrassen(a22, subtraction(b21, b11), n);
        int[][] p5 = multiStrassen(summation(a11, a12), b22, n);
        int[][] p6 = multiStrassen(subtraction(a21, a11), summation(b11, b12), n);
        int[][] p7 = multiStrassen(subtraction(a12, a22), summation(b21, b22), n);

        int[][] c11 = summation(summation(p1, p4), subtraction(p7, p5));
        int[][] c12 = summation(p3, p5);
        int[][] c21 = summation(p2, p4);
        int[][] c22 = summation(subtraction(p1, p2), summation(p3, p6));

        return collectMatrix(c11, c12, c21, c22);
    }

    //******************************************************************************************

    public static int getNewDimension(int[][] a, int[][] b) {
        return 1 << CommonFunctions.log2(Collections.max(Arrays.asList(a.length, a[0].length, b[0].length)));
    }
}