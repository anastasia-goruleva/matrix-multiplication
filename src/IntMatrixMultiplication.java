/**
 * The {@code MatrixMultiplication} class implements
 * fast multiplication 2 matrices at each other.
 * The {@code MatrixMultiplication} uses Strassen algorithm and
 * parallelize it with the {@link java.util.concurrent.ForkJoinPool}
 *
 * @author Evgeny Usov
 * @author Alexey Falko
 */
public class IntMatrixMultiplication {

    //******************************************************************************************

    public static int[][] multiply(int[][] a, int[][] b) {

        int rowsA = a.length;
        int columnsB = b[0].length;
        int columnsA_rowsB = a[0].length;

        int[][] c = new int[rowsA][columnsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < columnsB; j++) {
                int sum = 0;
                for (int k = 0; k < columnsA_rowsB; k++) {
                    sum += a[i][k] * b[k][j];
                }
                c[i][j] = sum;
            }
        }

        return c;
    }

    //******************************************************************************************

    public static int[][] multiplyTransposed(int[][] a, int[][] b) {

        int rowsA = a.length;
        int columnsB = b[0].length;
        int columnsA_rowsB = a[0].length;

        int columnB[] = new int[columnsA_rowsB];
        int[][] c = new int[rowsA][columnsB];


        for (int j = 0; j < columnsB; j++) {
            for (int k = 0; k < columnsA_rowsB; k++) {
                columnB[k] = b[k][j];
            }

            for (int i = 0; i < rowsA; i++) {
                int rowA[] = a[i];
                int sum = 0;
                for (int k = 0; k < columnsA_rowsB; k++) {
                    sum += rowA[k] * columnB[k];
                }
                c[i][j] = sum;
            }
        }

        return c;
    }

}