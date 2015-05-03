package cseki.sudoku;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by cseki on 03/05/2015.
 */
public class SudokuChecker {

    enum ERRORS{
        NO_INPUT("There were no input. Usuage: java -jar sudoku.jar <path to file>"),
        INVALID_PATH("The given path is incorrect: "),
        IOERROR("There was an error during reading the file. Operation has stopped. Exception: "),
        INVALID_SUDOKU_SIZE("The given input doesn't match with a 9x9 sudoku");

        private String message;

        ERRORS(String message){
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(ERRORS.NO_INPUT.getMessage());
            System.exit(1);
        }
        SudokuChecker sudokuChecker = new SudokuChecker();
        Optional<String[][]> rawSudoku = sudokuChecker.loadFile(args[0]);
        Boolean result = rawSudoku
                .flatMap(Sudoku::build)
                .map(Sudoku::validate)
                .orElse(Boolean.FALSE);
        if (result){
            System.out.println("VALID");
            System.exit(0);
        } else{
            System.out.println("INVALID");
            System.exit(1);
        }
    }


    public Optional<String[][]> loadFile(String path){
        String[][] result = new String[9][9];
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader reader = new BufferedReader(fileReader);
            int i = 0;
            while (reader.ready()){
                result[i++] = readLine(reader.readLine());
            }

        }catch (FileNotFoundException e){
            System.out.println(ERRORS.INVALID_PATH + path);
            return Optional.empty();
        }catch (IOException e){
            System.out.println(ERRORS.IOERROR + e.getMessage());
            return Optional.empty();
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println(ERRORS.INVALID_SUDOKU_SIZE.getMessage());
            return Optional.empty();
        }
        return Optional.of(result);
    }

    private static String[] readLine(String line){
        String[] splitted = line.split(",");
        if (splitted.length != 9){
            throw new ArrayIndexOutOfBoundsException("Sudoku line max length is 9");
        }
        return splitted;
    }

}
