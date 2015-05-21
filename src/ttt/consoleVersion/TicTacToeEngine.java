package ttt.consoleVersion;

import java.util.ArrayList;
import java.util.List;
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
   * Get next best move for computer. Return int[2] of {score, row, col}
   *
   * @return
   */
  int[] AImove() {
    int[] result = minimax(2, Content.O); // depth, max turn
    return new int[] {result[1], result[2]}; // row, col
  }

  /**
   * Recursive minimax at level of depth for either maximizing or minimizing player. Return int[3]
   * of {score, row, col} Brief explanation : It evaluates all the possible moves the AI can select
   * giving a score to each move. The move with the best score is finally selected.
   *
   * @param depth
   * @param player
   * @return
   */
  private int[] minimax(int depth, Content player) {
    // Generate possible next moves in a List of int[2] of {row, col}.
    List<int[]> nextMoves = generateMoves();

    // mySeed is maximizing; while oppSeed is minimizing
    int bestScore = (player == Content.O) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    int currentScore;
    int bestRow = -1;
    int bestCol = -1;

    if (nextMoves.isEmpty() || depth == 0) {
      // Game over or depth reached, evaluate score
      bestScore = evaluate();
    } else {
      for (int[] move : nextMoves) {
        // Try this move for the current "player"
        gameBoard.gameBoard[move[0]][move[1]].content = player;
        if (player == Content.O) { // computer is maximizing player
          currentScore = minimax(depth - 1, Content.X)[0];
          if (currentScore > bestScore) {
            bestScore = currentScore;
            bestRow = move[0];
            bestCol = move[1];
          }
        } else { // human player is minimizing player
          currentScore = minimax(depth - 1, Content.O)[0];
          if (currentScore < bestScore) {
            bestScore = currentScore;
            bestRow = move[0];
            bestCol = move[1];
          }
        }
        // Undo move
        gameBoard.gameBoard[move[0]][move[1]].content = Content.EMPTY;
      }
    }
    return new int[] {bestScore, bestRow, bestCol};
  }

  /**
   * Find all valid next moves. Return List of moves in int[2] of {row, col} or empty list if game
   * over
   *
   * @return
   */
  private List<int[]> generateMoves() {
    List<int[]> nextMoves = new ArrayList<int[]>(); // allocate List

    // If game over, i.e., no next move
    if (hasWon(Content.O) || hasWon(Content.X)) {
      return nextMoves; // return empty list
    }

    // Search for empty cells and add to the List
    for (int row = 0; row < ROWS; ++row) {
      for (int col = 0; col < COLS; ++col) {
        if (gameBoard.gameBoard[row][col].content == Content.EMPTY) {
          nextMoves.add(new int[] {row, col});
        }
      }
    }
    return nextMoves;
  }

  /**
   * The heuristic evaluation function for the current board
   *
   * @return +100, +10, +1 for EACH 3-, 2-, 1-in-a-line for computer. -100, -10, -1 for EACH 3-, 2-,
   *         1-in-a-line for opponent. 0 otherwise
   */
  private int evaluate() {
    int score = 0;
    // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
    score += evaluateLine(0, 0, 0, 1, 0, 2); // row 0
    score += evaluateLine(1, 0, 1, 1, 1, 2); // row 1
    score += evaluateLine(2, 0, 2, 1, 2, 2); // row 2
    score += evaluateLine(0, 0, 1, 0, 2, 0); // column 0
    score += evaluateLine(0, 1, 1, 1, 2, 1); // column 1
    score += evaluateLine(0, 2, 1, 2, 2, 2); // column 2
    score += evaluateLine(0, 0, 1, 1, 2, 2); // diagonal
    score += evaluateLine(0, 2, 1, 1, 2, 0); // alternate diagonal
    return score;
  }

  /**
   * The heuristic evaluation function for the current board
   *
   * @return +100, +10, +1 for EACH 3-, 2-, 1-in-a-line for computer. -100, -10, -1 for EACH 3-, 2-,
   *         1-in-a-line for opponent. 0 otherwise
   */
  private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
    int score = 0;

    // First cell
    if (gameBoard.gameBoard[row1][col1].content == Content.O) {
      score = 1;
    } else if (gameBoard.gameBoard[row1][col1].content == Content.X) {
      score = -1;
    }

    // Second cell
    switch (gameBoard.gameBoard[row2][col2].content) {
      case O:
        switch (score) {
          case 1: // cell1 belongs to AI
            score = 10;
            break;
          case -1: // cell1 belongs to the human player
            return 0;
          default: // cell1 is empty
            score = 1;
            break;
        }
        break;
      case X:
        switch (score) {
          case -1: // cell1 belongs to AI
            score = -10;
            break;
          case 1: // cell1 belongs to the human player
            return 0;
          default: // cell1 is empty
            score = -1;
            break;
        }
        break;
      default:
        break;
    }

    // Third cell
    if (gameBoard.gameBoard[row3][col3].content == Content.O) {
      if (score > 0) { // cell1 and/or cell2 belong to the AI
        score *= 10;
      } else if (score < 0) { // cell1 and/or cell2 belong to the human player
        return 0;
      } else { // cell1 and cell2 are empty
        score = 1;
      }
    } else if (gameBoard.gameBoard[row3][col3].content == Content.X) {
      if (score < 0) { // cell1 and/or cell2 belong to the AI
        score *= 10;
      } else if (score > 1) { // cell1 and/or cell2 belong to the human player
        return 0;
      } else { // cell1 and cell2 are empty
        score = -1;
      }
    }
    return score;
  }

  /**
   * Winning bit patterns given by the algorithm. Row 1 - 000000111 = 0x007 Row 2 - 000111000 =
   * 0x038 (0x007 << 3) Row 3 - 111000000 = 0x1C0 (0x007 << 6) Column 1 - 001001001 = 0x049 Column 2
   * - 010010010 = 0x092 (0x049 << 1) Column 3 - 100100100 = 0x124 (0x049 << 2) Diagonal 1 -
   * 100010001 = 0x111 Diagonal 2 - 001010100 = 0x054
   */
  private int[] winningPatterns = {0x007, 0x038, 0x1C0, // rows
      0x049, 0x092, 0x124, // columns
      0x111, 0x054 // diagonals
      };

  /**
   * Returns true if thePlayer wins
   * */
  private boolean hasWon(Content thePlayer) {
    int pattern = 0x000; // bit pattern for the 9 cells
    for (int row = 0; row < ROWS; ++row) {
      for (int col = 0; col < COLS; ++col) {
        if (gameBoard.gameBoard[row][col].content == thePlayer) {
          pattern |= (1 << (row * COLS + col));
        }
      }
    }
    for (int winningPattern : winningPatterns) {
      if ((pattern & winningPattern) == winningPattern)
        return true;
    }
    return false;
  }
}
