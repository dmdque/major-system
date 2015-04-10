import scala.io.Source

object Normalize {

  def main(args: Array[String]) {
    println("Following is the content read:" );

    // TODO: ignore empasis eg. ER0
    def norm(line: String) : Unit = {
      var tokens: List[Int] = List()
      val word = line.split(" +")(0)
      line.split(" +").toList.foreach{ token =>
        if (token.equals("Z") || token.equals("S")) {
          tokens = 0 +: tokens
        } else if (token.equals("D") || token.equals("DH") || token.equals("T")) {
          tokens = 1 +: tokens
        } else if (token.equals("N") || token.equals("NG")) {
          tokens = 2 +: tokens
        } else if (token.equals("M")) {
          tokens = 3 +: tokens
        } else if (token.equals("R") || token.equals("ER") || token.equals("ER0") || token.equals("ER1") || token.equals("ER2")) { // TODO: make this more general
          tokens = 4 +: tokens
        } else if (token.equals("L")) {
          tokens = 5 +: tokens
        } else if (token.equals("CH") || token.equals("JH") || token.equals("ZH") || token.equals("SH") ) {
          tokens = 6 +: tokens
        } else if (token.equals("G") || token.equals("K")) {
          tokens = 7 +: tokens
        } else if (token.equals("F") || token.equals("V")) {
          tokens = 8 +: tokens
        } else if (token.equals("P") || token.equals("B")) {
          tokens = 9 +: tokens
        }
      }
      //tokens.reverse.foreach(print)
      print(word)
      println()
    }

    Source.fromFile("cmudict.parts").getLines.foreach(norm)
  }
}
