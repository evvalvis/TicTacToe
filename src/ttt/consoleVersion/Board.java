package ttt.consoleVersion;

public class Board {

  /** An array of Cell objects to set up the game board */
  protected Cell[][] gameBoard;
  /** Standard constants */
  public static final int BOARD_ROWS = Config.BOARD_ROWS;
  public static final int BOARD_COLUMNS = Config.BOARD_COLUMNS;
  /** Last played row and column. They will help while checking for the thriving victor */
  public int lastPlayedRow;
  public int lastPlayedColumn;

  /**
   * Constructor
   */
  public Board() {
    gameBoard = new Cell[BOARD_ROWS][BOARD_COLUMNS];
    for (int i = 0; i < BOARD_ROWS; i++) {
      for (int j = 0; j < BOARD_COLUMNS; j++) {
        gameBoard[i][j] = new Cell(i, j);
      }
    }
  }

  /**
   * This method cleans the game board
   */
  public void sweepBoard() {
    for (int i = 0; i < BOARD_ROWS; i++) {
      for (int j = 0; j < BOARD_COLUMNS; j++) {
        gameBoard[i][j].sweep();
      }
    }
  }

  /**
   * This method returns the number of occupied cells
   *
   * @return
   */
  public int getUsedCellsCount() {
    int count = 0;
    for (int i = 0; i < BOARD_ROWS; i++) {
      for (int j = 0; j < BOARD_COLUMNS; j++) {
        if (gameBoard[i][j].getCellContent() != Content.EMPTY)
          count++;
      }
    }
    return count;
  }

  /**
   * Check if the game ended in a draw. Specifically , check if there are no empty cells left.
   *
   * @return
   */
  public boolean checkForDraw() {
    for (int i = 0; i < BOARD_ROWS; i++) {
      for (int j = 0; j < BOARD_COLUMNS; j++) {
        if (gameBoard[i][j].getCellContent() == Content.EMPTY)
          return false;
      }
    }
    return true;
  }

  /**
   * Check for the thriving victor Winning ways checked : c = content c | c | c | | | | | | c c | |
   * c | | | c | | | c --------- --------- --------- --------- --------- -------- --------- --------
   * | | c | c | c | | | c | | c | c | | | c | | | c --------- --------- --------- ---------
   * --------- -------- --------- -------- | | | | c | c | c c | | | | c c | | | c | | | c
   *
   * @param content
   */
  public boolean checkForWinner(Content content) {
    return (((gameBoard[lastPlayedRow][0].content == content)
        && (gameBoard[lastPlayedRow][1].content == content) && (gameBoard[lastPlayedRow][2].content == content))
        || ((gameBoard[0][lastPlayedColumn].content == content)
            && (gameBoard[1][lastPlayedColumn].content == content) && (gameBoard[2][lastPlayedColumn].content == content))
        || ((gameBoard[0][0].content == content) && (gameBoard[1][1].content == content) && (gameBoard[2][2].content == content)) || ((gameBoard[2][0].content == content)
        && (gameBoard[1][1].content == content) && (gameBoard[0][2].content == content)));
  }

  /**
   * This method prints the game board in the console
   */
  public void printGameBoard() {
    for (int i = 0; i < BOARD_ROWS; i++) {
      for (int j = 0; j < BOARD_COLUMNS; j++) {
        gameBoard[i][j].printCellContent();
        if (j < BOARD_COLUMNS - 1)
          System.out.print("|");
      }
      System.out.println("");
      /** Change the line for the next row */
      if (i < BOARD_ROWS - 1)
        System.out.print("-----------------------");
      System.out.println("");
    }
  }
}
