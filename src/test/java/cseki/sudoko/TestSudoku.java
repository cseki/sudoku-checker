package cseki.sudoko;

import cseki.sudoku.Sudoku;
import cseki.sudoku.SudokuChecker;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * Created by cseki on 03/05/2015.
 */
public class TestSudoku {

    @Test
    public void testValid(){
        Sudoku valid = TestCasesSudoku.valid;
        assertTrue("Valid sudoku",valid.validate());
    }

    @Test
    public void testOnlyValidRows(){
        Sudoku onlyValidRows = TestCasesSudoku.onlyValidRows;
        assertTrue("Invalid sudoku", !onlyValidRows.validate());
    }

    @Test
    public void testinvalidBoxes(){
        Sudoku invalidBoxes = TestCasesSudoku.validRowsAndColumns;
        assertTrue("Invalid sudoku", !invalidBoxes.validate());
    }

    @Test
    public void testLoadValidFile(){
        SudokuChecker checker = new SudokuChecker();
        Optional<String[][]> input = checker.loadFile("src/test/resources/valid.txt");
        String[][] rawSudoku = input.get();
        assertEquals(rawSudoku.length, 9);
    }

    @Test
    public void testLoadInvalidRangeFile(){
        SudokuChecker checker = new SudokuChecker();
        Optional<String[][]> input = checker.loadFile("src/test/resources/invalid_range.txt");
        assertTrue("invalid_range.txt suppose to be invalid", !input.isPresent());
    }

    @Test
    public void testLoadInvalidRangeTooManyLinesFile(){
        SudokuChecker checker = new SudokuChecker();
        Optional<String[][]> input = checker.loadFile("src/test/resources/invalid_range_too_many_lines.txt");
        assertTrue("invalid_range_too_many_lines.txt suppose to be invalid", !input.isPresent());
    }

    @Test
    public void testInvalidNumber(){
        SudokuChecker checker = new SudokuChecker();
        Optional<String[][]> input = checker.loadFile("src/test/resources/invalid_number.txt");
        Optional<Sudoku> sudoku = Sudoku.build(input.get());
        assertTrue("There is an invalid line in the fist line", !sudoku.isPresent());
    }

    @Test
    public void testInvalidPath(){
        SudokuChecker checker = new SudokuChecker();
        Optional<String[][]> input = checker.loadFile("there_is_no_file.txt");
        assertTrue("there is no file like this in this project",! input.isPresent());

    }
}
