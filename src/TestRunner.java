public class TestRunner {
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
                    throw new InvalidArgumentException(String.format("Algorithms are not implemented for type %s, " +
                                    "type have to be int or double",
                            args[1]));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage TestRunner <size> <type>");
        } catch (NumberFormatException e) {
            System.out.println("Matrix size has to be integer number");
        } catch (InvalidArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
