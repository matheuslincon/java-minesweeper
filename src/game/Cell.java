package game;

public class Cell {
    private boolean isBomb;
    private boolean isRevealed;
    private int neighbours;
    
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";

    public Cell() {
        resetCell();
    }

    public void resetCell() {
        isBomb = false;
        isRevealed = false;
        neighbours = 0;
    }

    public void setAsBomb() {
        isBomb = true;
    }

    public boolean getIsBomb() {
        return isBomb;
    }

    public void reveal() {
        isRevealed = true;
    }


    public boolean getIsRevealed() {
        return isRevealed;
    }

    public void addNeighbour() {
        neighbours++;
    }

    public int getNeighbours() {
        return neighbours;
    }


    public String toString() {
        if(getIsRevealed()) {
            if(getIsBomb()) {
                return "B";
            } else {
                return "" + neighbours;
            }
        } else {
           return "*";
        }
    }

    public String getColouredString() {
        String str = toString();
        if(isRevealed) {
          if(isBomb) str = colourString(str, ANSI_RED);
            else if(neighbours == 0) str = colourString(str, ANSI_GREEN);
            else str = colourString(str, ANSI_YELLOW);
        } else {
            str = colourString(str, ANSI_BLUE);
        }
        return str;
    }

    private String colourString(String str, String colour) {
        return colour + str + ANSI_RESET;
    }
  
}
