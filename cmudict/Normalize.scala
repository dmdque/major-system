import scala.io.Source

object Normalize {

  def main(args: Array[String]) {
    println("Following is the content read:" );

    Source.fromFile("cmudict.test").getLines.foreach { line =>

        var empty = List()
        var tokens = 0 +: empty
        line.split(" +").toList.foreach{ token =>
          println(token);
          tokens = 0 +: empty // TODO: this is bad
          if (token.equals("Z") || token.equals("S")) {
            tokens = 0 +: tokens
          } else if (token.equals("D") || token.equals("DH") || token.equals("T")) {
            tokens = 1 +: tokens
          } else if (token.equals("N")) {
            tokens = 2 +: tokens
          } else if (token.equals("M")) {
            tokens = 3 +: tokens
          } else if (token.equals("R") || token.equals("ER")) {
            tokens = 5 +: tokens
          } else if (token.equals("CH") || token.equals("JH") || token.equals("ZH") || token.equals("SH") ) {
            tokens = 6 +: tokens
          } else if (token.equals("G") || token.equals("K")) {
            tokens = 7 +: tokens
          } else if (token.equals("F") || token.equals("V")) {
            tokens = 8 +: tokens
          } else if (token.equals("P") || token.equals("B")) {
            println("it's a 9")
            tokens = 9 +: tokens
          }
        }
        tokens.reverse.foreach(print)
        println()
        /*println(s">>> $line")*/
    }
  }
}
