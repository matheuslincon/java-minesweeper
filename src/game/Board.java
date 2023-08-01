package game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;


public class Board {
    private Cell[][] cells;
    private int width;
    private int height;
    private int bombCount;
    private int revealedTotal;
    
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        bombCount = 0;
        cells = new Cell[width][height];
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                cells[x][y] = new Cell();
            }
        }
        revealedTotal = 0;
    }

    public void printBoard() {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                System.out.print(cells[x][y].getColouredString() + "  ");
            }
            System.out.println(" |" + (y + 1));
        }
        for(int x = 0; x < width; x++) {
            System.out.print("_  ");
        }
        System.out.println();
        for(int x = 0; x < width; x++) {
            System.out.print((x+1) + " ");
            if(x+1 < 10)
                System.out.print(" ");
        }
        System.out.println();
    }

    public void printStatus() {
        System.out.println(revealedTotal + " revealed of " + (height*width)
                            + " with " + bombCount + " bombs!");
    }

    public boolean isCellBomb(Position position) {
        return cells[position.x][position.y].getIsBomb();
    }

    public boolean isCellRevealed(Position position) {
        return cells[position.x][position.y].getIsRevealed();
    }

    public Cell getCellAt(Position position) {
        return cells[position.x][position.y];
    }

    public void revealCell(Position position) {
        if(cells[position.x][position.y].getNeighbours() != 0) {
            revealedTotal++;
            cells[position.x][position.y].reveal();
        } else {
            List<Position> revealedCells = floodFillReveal(position);
            List<Position> borderRevealedCells = revealAroundListOfPoints(revealedCells);
            revealedTotal += revealedCells.size() + borderRevealedCells.size();
        }
    }

    public void spawnBombs(int maxBombs) {
        Random rand = new Random();
        for(int i = 0; i < maxBombs; i++) {
            addBomb(new Position(rand.nextInt(width), rand.nextInt(height)));
        }
    }

    public boolean isWon() {
        return revealedTotal + bombCount == width*height;
    }

    public void revealAll() {
        for(int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[0].length; x++) {
                cells[x][y].reveal();
            }
        }
    }

    private boolean addBomb(Position position) {
        if(getCellAt(position).getIsBomb()) return false;

        int minX = Math.max(0,position.x-1);
        int maxX = Math.min(width-1,position.x+1);
        int minY = Math.max(0,position.y-1);
        int maxY = Math.min(height-1,position.y+1);
        
        for(int y1 = minY; y1 <= maxY; y1++) {
            for(int x1 = minX; x1 <= maxX; x1++) {
                cells[x1][y1].addNeighbour();
            }
        }

        getCellAt(position).setAsBomb();
        bombCount++;
        return true;
    }

    public boolean validPosition(Position position)
    {
        return position.x >= 0 && position.y >= 0 && position.x < width && position.y < height;
    }

    private List<Position> floodFillReveal(Position position)
    {
        // Visiting array
        int[][] vis =new int[width][height];

        // Initialing all as zero
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                vis[x][y] = 0;
            }
        }
        // List of points that have been flood filled to be returned
        List<Position> changedPoints =  new ArrayList<>();

        // Creating queue for breadth first search
        Queue<Position> positionQueue = new LinkedList<>();

        // Adds the selected position as the first to evaluate
        positionQueue.add(position);
        vis[position.x][position.y] = 1;

        // Until queue is empty
        while (!positionQueue.isEmpty())
        {
            // Extracting front position from the queue.
            Position positionToReveal = positionQueue.remove();
            getCellAt(positionToReveal).reveal();
            changedPoints.add(positionToReveal);

            // For Upside Pixel or Cell
            checkFloodFillToCell(new Position(positionToReveal.x+1,positionToReveal.y),vis,positionQueue);
            // For Downside Pixel or Cell
            checkFloodFillToCell(new Position(positionToReveal.x-1,positionToReveal.y),vis,positionQueue);
            // For Right side Pixel or Cell
            checkFloodFillToCell(new Position(positionToReveal.x,positionToReveal.y+1),vis,positionQueue);
            // For Left side Pixel or Cell
            checkFloodFillToCell(new Position(positionToReveal.x,positionToReveal.y-1),vis,positionQueue);
        }
        return changedPoints;
    }

    private void checkFloodFillToCell(Position position, int[][] vis, Queue<Position> positionQueue) {
        if (validPosition(position)) {
            if (vis[position.x][position.y] == 0
                    && !getCellAt(position).getIsRevealed()
                    && getCellAt(position).getNeighbours() == 0) {
                positionQueue.add(position);
            }
            vis[position.x][position.y] = 1;
        }
    }

    
    private List<Position> revealAroundListOfPoints(List<Position> points) {
        List<Position> changedCells = new ArrayList<>();
        for(Position p : points) {
            List<Position> revealedCells = revealAllAroundPoint(p);
            changedCells.addAll(revealedCells);
        }
        return changedCells;
    }

    
    private List<Position> revealAllAroundPoint(Position position) {
        List<Position> changedCells = new ArrayList<>();

        int minX = Math.max(0,position.x -1);
        int maxX = Math.min(width-1,position.x +1);
        int minY = Math.max(0,position.y -1);
        int maxY = Math.min(height-1,position.y +1);

        for(int y1 = minY; y1 <= maxY; y1++) {
            for (int x1 = minX; x1 <= maxX; x1++) {
                if(!cells[x1][y1].getIsRevealed() && cells[x1][y1].getNeighbours()>0) {
                    changedCells.add(new Position(x1,y1));
                    cells[x1][y1].reveal();
                }
            }
        }
        return changedCells;
    }
}
