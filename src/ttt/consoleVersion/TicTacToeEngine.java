package ttt.consoleVersion;

import java.util.Scanner;

public class TicTacToeEngine {

  /** instance variables */
  /* a GameBoard object to set up the game board */
  private Board gameBoard;
  /* Definer for the players */
  private Content player;
  /* Definer for the game states */
  private GameStatus status;
  /* Rows and columns of the game board */
  private final int ROWS = Config.BOARD_ROWS;
  private final int COLS = Config.BOARD_COLUMNS;
  /* Scanner */
  private Scanner scanner = new Scanner(System.in);

  /**
   * Starts the engine
   */
  public void start() {
    if (Config.ENABLE_BOT)
      initBotGame();
    else
      initPvPGame();
  }

  /**
   * Start a player versus player game
   */
  private void initPvPGame() {
    gameBoard = new Board();
    initiliaze();
    while (status == GameStatus.PLAYING) {
      move(player);
      gameBoard.printGameBoard();
      handleChanges(player);
      switch (status) {
        case PLAYER_X_WON:
          System.out.println("Player X won! Gratz! ");
          break;
        case PLAYER_O_WON:
          System.out.println("Player O won! Gratz! ");
          break;
        case DRAW:
          System.out.println("The game ended in a draw! Well played by both players! ");
          break;
        default:
          break;
      }
      if (player == Content.X)
        player = Content.O;
      else
        player = Content.X;
    }
  }

  /**
   * Select the players' moves
   *
   * @param competitor
   */
  private void move(Content competitor) {
    boolean isValidMove = false;
    while (!isValidMove) {
      if (competitor == Content.X)
        System.out.println("Player X its time to make your move! <row[1-3]> <column[1-3]> ");
      else
        System.out.println("Player O its time to make your move! <row[1-3]> <column[1-3]> ");
      System.out.println("Choose the desired row : ");
      int rowPicked = scanner.nextInt() - 1;
      System.out.println("Choose the desired column : ");
      int columnPicked = scanner.nextInt() - 1;
      if (moveIsPlayable(rowPicked, columnPicked)) {
        gameBoard.gameBoard[rowPicked][columnPicked].content = competitor;
        gameBoard.lastPlayedRow = rowPicked;
        gameBoard.lastPlayedColumn = columnPicked;
        isValidMove = true;
      } else {
        System.out
            .println("This move is not valid. Please be careful with the move format. Try Again! ");
      }
    }
  }

  /**
   * Start a player versus bot game
   */
  private void initBotGame() {
    gameBoard = new Board();
    initiliaze(); // clear all the cells, set the first player and game state
    while (status == GameStatus.PLAYING) {
      moveBot(player); // use the minimax algorithm to define bot's move
      gameBoard.printGameBoard(); // print the current board
      handleChanges(player); // check for wins - draws etc
      switch (status) { // A little message according to the situation
        case PLAYER_X_WON:
          System.out.println("Player X won! Gratz! ");
          break;
        case PLAYER_O_WON:
          System.out.println("Player O won! Gratz! ");
          break;
        case DRAW:
          System.out.println("The game ended in a draw! Well played by both players! ");
          break;
        default:
          break;
      }
      /* finally change the turn */
      if (player == Content.X)
        player = Content.O;
      else
        player = Content.X;
    }
  }

  /**
   * Check for winners / draw
   * */
  private void handleChanges(Content competitor) {
    if (gameBoard.checkForWinner(competitor)) {
      if (competitor == Content.X)
        status = GameStatus.PLAYER_X_WON;
      else
        status = GameStatus.PLAYER_O_WON;
    } else if (gameBoard.checkForDraw()) {
      status = GameStatus.DRAW;
    } else {
      // do nothing, game is still on
      // basically it is still status = GameStatus.PLAYING;
    }
  }

  /**
   * Select player's and bot's next move
   *
   * @param player
   */
  private void moveBot(Content player) {
    boolean isValidMove = false;
    while (!isValidMove) {
      if (player == Content.X) {
        System.out.println("Player X its time to make your move! <row[1-3]> <column[1-3]> ");
        System.out.println("Choose the desired row : ");
        int rowPicked = scanner.nextInt() - 1; // since the board's cells start from 0
        System.out.println("Choose the desired row : ");
        int columnPicked = scanner.nextInt() - 1; // since the board's cells start from 0
        if (moveIsPlayable(rowPicked, columnPicked)) { // check the move
          gameBoard.gameBoard[rowPicked][columnPicked].content = player;
          gameBoard.lastPlayedRow = rowPicked;
          gameBoard.lastPlayedColumn = columnPicked;
          isValidMove = true;
        } else {
          System.out
              .println("This move is not valid. Please be careful with the move format. Try Again! ");
        }
      } else {
        System.out.println("Its the bot's turn to play! ");
        int rowPicked = AImove()[0]; // this will be explained afterwards!
        int columnPicked = AImove()[1]; // too much work done with the minimax algorithm !
        gameBoard.gameBoard[rowPicked][columnPicked].content = player;
        gameBoard.lastPlayedRow = rowPicked;
        gameBoard.lastPlayedColumn = columnPicked;
        isValidMove = true;
      }
    }
  }

  /**
   * Decide if the specific move can be played
   *
   * @param rowPicked
   * @param columnPicked
   * @return
   */
  private boolean moveIsPlayable(int rowPicked, int columnPicked) {
    return (gameBoard.gameBoard[rowPicked][columnPicked].content == Content.EMPTY)
        && (rowPicked >= 0) && (rowPicked <= 2) && (columnPicked >= 0) && (columnPicked <= 2);
  }

  /**
   * Clear all the board's cells, set the first player to play as X and change the game state to
   * PLAYING
   */
  private void initiliaze() {
    gameBoard.sweepBoard();
    player = Content.X;
    status = GameStatus.PLAYING;
  }

  /**
   * Get next best move for computer. Return int[2] of {row, col,score}
   *
   * @return
   */
  static int[] AImove() {
    return null;
  }
}
