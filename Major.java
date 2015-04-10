import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Major {
  public static final boolean DEBUG = false;
  public static final boolean DEBUG2 = false;
  public static void main(String[] args) throws Exception {
    String encode = args[0];
    System.out.println("number to encode: " + encode);
    
    ArrayList<String> al = new ArrayList<String>();
    ArrayList<String> words = new ArrayList<String>();

    readDict(al, words, "dict.txt");
    
    if(DEBUG) {
      for (int i = 0; i < al.size(); i++) {
        System.out.println(al.get(i));
      }
      for (int i = 0; i < al.size(); i++) {
        System.out.println(words.get(i));
      }
    }

    int[] wordsIndex = dp(encode, al);

    for (int i = wordsIndex.length - 1; i >= 0; i--) {
      System.out.print(words.get(wordsIndex[i]));
      if (i != 0) {
        System.out.print(", ");
      }
    }
    System.out.println();
  }

  // mutates al
  public static void readDict (ArrayList<String> al, ArrayList<String> words, String filename) {
    String thisLine = null;
      try {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        while ((thisLine = br.readLine()) != null) {
          String[] lineSplit = thisLine.split(" ");
          al.add(lineSplit[0]);
          words.add(lineSplit[1]);
        }       
      } catch(Exception e) {
        e.printStackTrace();
      }
  }

  public static int[] dp (String encode, ArrayList<String> vocabulary) {
    //col is number of words in dict
    //row is length of number
    int col = vocabulary.size();
    int row = encode.length();
    int[][] dpTable = new int[row][col];

    for (int i = 0; i < encode.length(); i++) {
      for (int j = 0; j < vocabulary.size(); j++) {
        String remainderStr = stringSubtract(encode.substring(0, i + 1), vocabulary.get(j));
        if (DEBUG) { System.out.println(encode.substring(0, i + 1)); }
        if (DEBUG) { System.out.println(vocabulary.get(j)); }
        if (DEBUG) { System.out.println("remainderStr: " + remainderStr); }
        if (remainderStr.equals("")) {
          dpTable[i][j] = 1;
        }
        if (!remainderStr.equals("error") && !remainderStr.equals("")) {
          if (DEBUG) { System.out.println("looking up remainder: " + remainderStr); }
          int bestCode = dpLookup(remainderStr, dpTable);
          if (bestCode != 0) {
            dpTable[i][j] = bestCode + 1;
          }
        }
      }
    }

    //printTable(dpTable);

    return dpBacktrace(dpTable, encode, vocabulary);
  }

  public static int[] dpBacktrace (int[][] table, String encode, ArrayList<String> vocabulary) {
    int row = table.length - 1;
    int col = table[0].length - 1; // default col value, in case none is found

    // find initial value from table
    int colVal = Integer.MAX_VALUE;
    for (int i = 0; i < table[0].length; i++) {
      if (table[row][i] != 0 && table[row][i] < colVal) { // TODO: change from zero to inf
        colVal = table[row][i];
        col = i;
      }
    }

    int[] words = new int[table[row][col]];
    int wordsindex = 0;
    for (int i = table[row][col]; i > 0; i--) {
      // save word location
      words[wordsindex] = col;
      wordsindex++;
      String word = vocabulary.get(col);
      if (DEBUG) { System.out.println(table[row][col]); }
      if (DEBUG) { System.out.println(encode + ": " + word); }
      if (DEBUG2) { System.out.println(word.length()); }
      encode = stringSubtract(encode, word);

      if (encode.equals("")) {
        break;
      } else if (encode.equals("error")) {
        if (DEBUG) { System.out.println("encountered invalid string during backtrace"); }
        break;
      }

      int[] coord = dpLookupIndex(encode, table);
      row = coord[0];
      col = coord[1];
    }
    return words;
  }

  // returns col of the smallest non-zero in dpTable
  public static int[] dpLookupIndex (String remainder, int[][] table) {
    if (DEBUG) { System.out.println("remainder: " + remainder.length()); }
    int row = remainder.length() - 1;
    if (row < 0) {
      return new int[0];
    }
    int min = Integer.MAX_VALUE;
    int minIndex = -1;
    for (int i = 0; i < table[0].length; i++) {
      if (table[row][i] != 0 && table[row][i] < min) {
        min = table[row][i];
        minIndex = i;
      }
    }
    if (min != Integer.MAX_VALUE) {
      int[] ret = {row, minIndex};
      if (DEBUG) { System.out.println("dpLookupIndex: row: " + row + " col: " + minIndex + " val: " + min); }
      return ret;
    } else {
      return new int[0];
    }
  }

  // returns value
  public static int dpLookup (String remainder, int[][] table) {
    int row = remainder.length() - 1;
    if (row < 0) {
      return 0;
    }
    int min = Integer.MAX_VALUE;
    //int minIndex;
    for (int i = 0; i < table[0].length; i++) {
      if (table[row][i] != 0 && table[row][i] < min) {
        min = table[row][i];
        //minIndex = i;
      }
    }
    if (min != Integer.MAX_VALUE) {
      return min;
    } else {
      return 0;
    }
  }

  // returns -1 if not found
  public static int getIndexOf (String needle, ArrayList<String> haystack) {
    for (int i = 0; i < haystack.size(); i++) {
      if (needle.equals(haystack.get(i))) {
        return i;
      }
    }
    return -1;
  }

  // subtracts string b from the end of string a
  // tests for stringSubtract
  //System.out.println(stringSubtract("applesauce", "sauce")); // "apple"
  //System.out.println(stringSubtract("20479762", "79762")); // "204"
  //System.out.println(stringSubtract("hi", "hi")); // ""
  //System.out.println(stringSubtract("dumas", "alexandredumas")); // ""
  //System.out.println(stringSubtract("2047976", "5")); // "error"
  public static String stringSubtract (String a, String b) {
    int index;
    for (index = 1; index <= b.length(); index++) {
        if (DEBUG) { System.out.println("a: " + a.charAt(a.length() - index) + " b: " + b.charAt(b.length() - index)); }
      if (a.length() < b.length()) {
        return "error";
      } else if (a.charAt(a.length() - index) == b.charAt(b.length() - index)) {
      } else {
        return "error";
      }
    }
    return a.substring(0, a.length() - b.length());
  }

  public static void printTable (int[][] table) {
    for (int i = 0; i < table.length; i++) {
      for (int j = 0; j < table[0].length; j++) {
        if (table[i][j] == 0) {
          System.out.print("-" + " ");
        } else {
          System.out.print(table[i][j] + " ");
        }
      }
      System.out.println();
    }
  }

}
