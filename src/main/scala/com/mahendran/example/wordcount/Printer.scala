package com.mahendran.example.wordcount

trait Printer {
  def print(string: String): Unit = {
    println(s"""\n \t $string \t\n """)
  }
}
