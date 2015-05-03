package cseki.sudoku;

import java.util.IntSummaryStatistics;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * Created by cseki on 03/05/2015.
 */
public class Sudoku {

    enum ERRORS {
        NOT_A_NUMBER("Invalif character: "),
        INVALID_NUMBER("Invalid number: "),
        DUPLICATE_NUMBERS_IN_LINE("Duplicate numbers in line: "),
        DUPLICATE_NUMBERS_IN_ROW("Duplicate numbers in row: "),
        INVALID_BOX("Invalid box at(x,y): ");

        private String message;

        ERRORS(String message){
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }

    }

    Integer[][] sudoku;

    private Sudoku(Integer[][] sudoku){
        this.sudoku = sudoku;
    }

    public static Optional<Sudoku> build(String[][] input){

        Integer[][] sudoku = new Integer[9][9];
        for(int i = 0;i < 9;i++)
        {
            for(int j = 0; j < 9; j++){
                try{
                    Integer num = Integer.parseInt(input[i][j]);
                    if (num > 0 && num < 10){
                        sudoku[i][j] = num;
                    } else {
                        System.out.println(ERRORS.INVALID_NUMBER.getMessage() + num);
                        return Optional.empty();
                    }
                } catch(NumberFormatException e){
                    System.out.println(ERRORS.NOT_A_NUMBER.getMessage() + input[i][j]);
                    Optional.empty();
                }

            }
        }
        return Optional.of(new Sudoku(sudoku));
    }

    public Boolean validate(){
        return validateLines()
                && validateRows()
                && validateBoxes();

    }

    public boolean validateLines(){
        return IntStream.
                rangeClosed(0, 8).
                parallel().
                mapToObj(this::validateLine).
                anyMatch(b -> b);
    }

    public boolean validateRows(){
        return IntStream.
                rangeClosed(0,8).
                parallel().
                mapToObj(this::validateRow).
                anyMatch(b -> b);
    }

    public boolean validateBoxes(){
        Predicate<Boolean> isValid = b -> b;
        IntPredicate isDiv3 = n -> n % 3 == 0;
        return IntStream.
                rangeClosed(0,6).
                filter(isDiv3).
                parallel().
                mapToObj(i ->
                                IntStream.
                                        rangeClosed(0,6).
                                        filter(isDiv3).
                                        parallel().
                                        mapToObj(j -> validateBox(i, j)).
                                        anyMatch(isValid)
                ).
                anyMatch(isValid);

    }

    public boolean validateLine(int line){
        IntSummaryStatistics stat = IntStream
                .rangeClosed(0, 8)
                .map(i -> this.sudoku[line][i])
                .distinct()
                .summaryStatistics();
        return checkSumAndCount(String.valueOf(line), stat, lineError);
    }

    public boolean validateRow(int row){
        IntSummaryStatistics stat = IntStream
                .rangeClosed(0, 8)
                .map(i -> this.sudoku[i][row])
                .distinct()
                .summaryStatistics();
        return checkSumAndCount(String.valueOf(row), stat, rowError);
    }

    public boolean checkSumAndCount(String num, IntSummaryStatistics stat, Function<String, String> errorProvider){
        if (stat.getSum() == 45 && stat.getCount() == 9){
            return true;
        } else {
            System.out.println(errorProvider.apply(num));
            return false;
        }
    }

    public boolean validateBox(int x, int y) {
        IntSummaryStatistics stat = IntStream
                .rangeClosed(0, 8)
                .map(i -> this.sudoku[x + i / 3][y + i % 3])
                .distinct()
                .summaryStatistics();
        return checkSumAndCount(String.valueOf(x) + ", " + String.valueOf(y), stat, boxError);
    }

    private Function<String, String> lineError = line -> ERRORS.DUPLICATE_NUMBERS_IN_LINE.getMessage() + line;

    private Function<String, String> rowError = row -> ERRORS.DUPLICATE_NUMBERS_IN_ROW.getMessage() + row;

    private Function<String, String> boxError = box -> ERRORS.INVALID_BOX.getMessage() + box;

}
