import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Major {
  public static final boolean DEBUG = false;
  public static void main(String[] args) throws Exception {
    String encode = args[0];
    System.out.println("number to encode: " + encode);
    
    ArrayList<String> al = new ArrayList<String>();
    ArrayList<String> words = new ArrayList<String>();

    readDict(al, "dict-num.txt");
    readDict(words, "dict.txt");
    
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
      System.out.println(words.get(wordsIndex[i]));
    }
  }

  // mutates al
  public static void readDict (ArrayList<String> al, String filename) {
    String thisLine = null;
      try {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        while ((thisLine = br.readLine()) != null) {
          al.add(thisLine);
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
    int[][] dpTable = new int[col][row];

    for (int i = 0; i < encode.length(); i++) {
      for (int j = 0; j < vocabulary.size(); j++) {
        String remainderStr = stringSubtract(encode.substring(0, i + 1), vocabulary.get(j));
        if (DEBUG) { System.out.println(encode.substring(0, i + 1)); }
        if (DEBUG) { System.out.println(vocabulary.get(j)); }
        if (DEBUG) { System.out.println("remainderStr: " + remainderStr); }
        if (remainderStr.equals("")) {
          dpTable[j][i] = 1;
        }
        if (!remainderStr.equals("error") && !remainderStr.equals("")) {
          if (DEBUG) { System.out.println("remainderStr again: " + remainderStr); }
          dpTable[j][i] = dpLookup(remainderStr, dpTable) + 1;
        }
      }
    }

    printTable(dpTable);

    return dpBacktrace(dpTable, encode, vocabulary);
  }

  public static int[] dpBacktrace (int[][] table, String encode, ArrayList<String> vocabulary) {
    int row = table[0].length - 1;
    int col = table.length - 1; // default col value, in case none is found

    int colVal = Integer.MAX_VALUE;
    for (int i = 0; i < table.length; i++) {
      if (table[i][row] != 0 && table[i][row] < colVal) { // TODO: change from zero to inf
        colVal = table[i][row];
        col = i;
      }
    }

    int[] words = new int[table[col][row]];
    int wordsindex = 0;
    for (int i = table[col][row]; i > 0; i--) {
      String word = vocabulary.get(col);
      if (DEBUG) { System.out.println(table[col][row]); }
      if (DEBUG) { System.out.println(encode + ": " + word); }
      words[wordsindex] = col;
      wordsindex++;
      encode = stringSubtract(encode, word);
      if(encode.equals("")) {
        break;
      }
      int[] coord = dpLookupIndex(encode, table);
      col = coord[0];
      row = coord[1];
    }
    return words;
  }

  // returns largest...
  public static int[] dpLookupIndex (String remainder, int[][] table) {
    int row = remainder.length() - 1;
    if (row < 0) {
      return new int[0];
    }
    for (int i = 0; i < table.length; i++) {
      if (table[i][row] != 0) {
        // TODO: max/min
        int[] ret = {i, row};
        return ret;
      }
    }
    return new int[0];
  }

  // returns largest...
  public static int dpLookup (String remainder, int[][] table) {
    int row = remainder.length() - 1;
    if (row < 0) {
      return 0;
    }
    for (int i = 0; i < table.length; i++) {
      if (table[i][row] != 0) {
        // TODO: max
        return table[i][row];
      }
    }
    return 0;
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
    for (int i = 0; i < table[0].length; i++) {
      for (int j = 0; j < table.length; j++) {
        System.out.print(table[j][i] + "\t");
      }
      System.out.println();
    }
  }

}
