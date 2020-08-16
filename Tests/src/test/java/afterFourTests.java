import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.geekbrains.tests.ArraysHandler;

import java.util.Arrays;

public class afterFourTests {



    @Test
    public void afterFourSuccess() {
        int[] init = {0, 2, 4, 9, 1, 54, 5, 54};
        ArraysHandler arraysHandler = new ArraysHandler();
        int[] expected = {9, 1, 54, 5, 54};
        int[] actual = arraysHandler.afterFour(init);
        boolean equals = Arrays.equals(expected, actual);
        Assertions.assertTrue(equals);
//        Assertions.assertEquals(expected,actual); expected: [I@305fd85d<[9, 1, 54, 5, 54]> but was: [I@458c1321<[9, 1, 54, 5, 54]>
    }

    @Test
    public void afterFourFailed() {
        int[] init = {12, 5, 4, 9, 8};
        ArraysHandler arraysHandler = new ArraysHandler();
        int[] expected = {4, 6};
        int[] actual = arraysHandler.afterFour(init);
        boolean equals = Arrays.equals(expected, actual);
        Assertions.assertFalse(equals);
//        Assertions.assertEquals(expected,actual); expected: [I@305fd85d<[9, 1, 54, 5, 54]> but was: [I@458c1321<[9, 1, 54, 5, 54]>
    }

    @Test
    public void afterFourCallingException() {
        int[] init = {15, 8, 9, 5};
        ArraysHandler arraysHandler = new ArraysHandler();
        Assertions.assertThrows(RuntimeException.class, () -> {
            int[] actual = arraysHandler.afterFour(init);
        });
    }

    @Test
    public void afterFourCallingWrongException() {
        int[] init = {15, 8, 9, 5};
        ArraysHandler arraysHandler = new ArraysHandler();
        Assertions.assertThrows(ArithmeticException.class, () -> { // есть ли вариант проверить "бросает не такое исключение"?
            int[] actual = arraysHandler.afterFour(init);
        });
    }
}
