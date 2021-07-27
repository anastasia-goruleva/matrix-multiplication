public class StrassenRunner {
    private static boolean isTwoPower(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    private static class InvalidArgumentException extends RuntimeException {
        public InvalidArgumentException() {
        }

        public InvalidArgumentException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        try {
            int size = Integer.parseInt(args[0]);
            if (!isTwoPower(size)) {
                throw new InvalidArgumentException("Size is not power of two");
            }

            switch (args[1].toLowerCase()) {
                case "int":
                    MatrixMultiplicationInt.test(size, size, size);
                    break;

                case "double":
                    MatrixMultiplicationDouble.test(size, size, size);
                    break;

                default:
                    throw new InvalidArgumentException(String.format("Algorithms are not implemented for type %s", args[1]));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
