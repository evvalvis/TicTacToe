package ttt.consoleVersion;

public class Main {

  public static void main(String[] args) {
    Config config = new Config();
    config.load("resources/config.properties");
    System.out.println("Welcome to tic tac toe engine !");
    String bot = (Config.ENABLE_BOT) ? "enabled" : "disabled";
    System.out.println("The game will start with the bot " + bot);
    TicTacToeEngine ttt = new TicTacToeEngine();
    ttt.start();
  }
}
