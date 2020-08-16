import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.geekbrains.tests.ArraysHandler;

public class HasFourAndOneTest {
    /*
    Хотел сначала так, но наткнулся на то, что у него запятая является разделителем по умолчанию, и он берёт только первые 2 числа и естественно тесты падают
    тут нужно экранировать запятую или можно как-то изменить разделитель по умолчанию?
        @CsvSource({
                "{1,4,1,4,1,4,4,4,1,4}, true",
                "{1,1,1,1,1,1,1}, false",
                "{1,5,4,21,84,1,15,4}, false"
        })

        @ParameterizedTest
        public void masshasFourAndOneCheck(int[] init, boolean expected) {
            ArraysHandler arraysHandler = new ArraysHandler();
            boolean actual=arraysHandler.hasFourAndOne(init);
            Assertions.assertEquals(expected, actual);
        }
     */
    @Test
    public void masshasFourAndOneCheckSuccess() {
        ArraysHandler arraysHandler = new ArraysHandler();
        int[] init = {1, 4, 1, 4, 1, 4, 4, 4, 1, 4};
        boolean actual = arraysHandler.hasFourAndOne(init);
        boolean expected = true;
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void masshasFourAndOneCheckFailOnlyOne() {
        ArraysHandler arraysHandler = new ArraysHandler();
        int[] init = {1,1,1,1,1,1,1};
        boolean actual = arraysHandler.hasFourAndOne(init);
        boolean expected = false;
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void masshasFourAndOneCheckFailRandomNumbers() {
        ArraysHandler arraysHandler = new ArraysHandler();
        int[] init = {1,5,4,21,84,1,15,4};
        boolean actual = arraysHandler.hasFourAndOne(init);
        boolean expected = false;
        Assertions.assertEquals(expected, actual);
    }

}
