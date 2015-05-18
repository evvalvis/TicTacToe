package ttt.consoleVersion;

public class Cell {

  /** instance variables */
  private int row;
  private int column;
  public Content content;

  /**
   * Constructor
   * 
   * @param row
   * @param column
   */
  public Cell(int row, int column) {
    this.row = row;
    this.column = column;
    sweep();
  }

  /**
   * This method cleans the current cell
   */
  public void sweep() {
    content = Content.EMPTY;
  }

  /**
   * Default setter for the row
   * 
   * @param row
   */
  public void setRow(int row) {
    this.row = row;
  }

  /**
   * Default getter for the row
   * 
   * @return
   */
  public int getRow() {
    return this.row;
  }

  /**
   * Default setter for the column
   * 
   * @param column
   */
  public void setColumn(int column) {
    this.column = column;
  }

  /**
   * Default getter for the column
   * 
   * @return
   */
  public int getColumn() {
    return this.column;
  }

  /**
   * This method sets the cell's content
   * 
   * @param content
   */
  public void setCellContent(Content content) {
    this.content = content;
  }

  /**
   * This method returns the cell's content
   * 
   * @return
   */
  public Content getCellContent() {
    return content;
  }

  /**
   * This method prints the cell's content to the console
   */
  public void printCellContent() {
    switch (this.content) {
      case EMPTY:
        System.out.print("   ");
        break;
      case X:
        System.out.print(" X ");
        break;
      case O:
        System.out.print(" O ");
        break;
    }
  }
}
