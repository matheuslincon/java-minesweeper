package game;

import java.util.Scanner;


public class Game {
    private Board board;
    private Scanner scan;

    public Game() {
        scan = new Scanner(System.in);
        board = new Board(10,10);
        board.spawnBombs(10);
    }

    public void startGame() {
        Position inputPosition;
        do {
            board.printBoard();
            board.printStatus();
            inputPosition = getPositionInput();
            board.revealCell(inputPosition);
            
        } while(!board.isWon() && !board.isCellBomb(inputPosition));
        board.revealAll();
        board.printBoard();
        if(board.isWon()) {
            System.out.println("Congratulations, You're a Minesweeper Master !!!!");
        } else {
            System.out.println("YOU LOSE !! Keep practicing !");
        }
    }

    public Position getPositionInput() {
        Position input = new Position(0,0);
        do {
            System.out.println("Enter your X axis coordinate, then your Y axis coordinate. Separate them by a space: ");
            if(!scan.hasNextInt()) {
                getStringOrQuit(scan);
                System.out.println("Invalid X coordinate.");
                continue;
            }
            input.x = scan.nextInt();
            if(!scan.hasNextInt()) {
                getStringOrQuit(scan);
                System.out.println("Invalid Y coordinate.");
                continue;
            }
            input.y = scan.nextInt();
            input.x--;
            input.y--;
        } while(!isPositionInputValid(input));
        return input;
    }

    private boolean isPositionInputValid(Position position) {
        if(!board.validPosition(position)) {
            System.out.println("Coordinate not inside the play space!");
            return false;
        }
        if(board.isCellRevealed(position)) {
            System.out.println("That cell is already revealed!");
            return false;
        }
        return true;
    }

    
    public static String getStringOrQuit(Scanner scan) {
        String input = scan.nextLine();
        if(input.equalsIgnoreCase("quit")) {
            System.out.println("You lose 100% of the shots you don't take. Don't quit next time. Bye !");
            System.exit(0);
        }
        return input;
    }
}
