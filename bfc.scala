object M5b {

// for timing purposes
def time_needed[T](n: Int, code: => T) = {
  val start = System.nanoTime()
  for (i <- 0 until n) code
  val end = System.nanoTime()
  (end - start)/(n * 1.0e9)
}


type Mem = Map[Int, Int]

import io.Source
import scala.util._

def load_bff(name: String) : String = {
    Try(Source.fromFile(name)("ISO-8859-1").mkString).
        getOrElse {""}
}

def sread(mem: Mem, mp: Int) : Int = {
    mem.getOrElse(mp, 0)
}

def write(mem: Mem, mp: Int, v: Int) : Mem = {
    mem.updated(mp, v)
}


def jumpRight(prog: String, pc: Int, level: Int) : Int = {
    if (pc > prog.length - 1){
        pc
    }
    else{
        prog.charAt(pc) match{
            case '[' =>jumpRight(prog, pc + 1, level + 1)
            case ']'=>
                if (level == 0){
                    pc + 1
                } else{
                jumpRight(prog, pc + 1, level - 1)
                }
            case _ => jumpRight(prog, pc + 1, level)

        }
    }
    
}


def jumpLeft(prog: String, pc: Int, level: Int) : Int = {
    if (pc < 0){
        pc
    }
    else{
        prog.charAt(pc) match{
            case ']' =>jumpLeft(prog, pc - 1, level + 1)
            case '['=>
                if (level == 0){
                    pc + 1
                } else{
                jumpLeft(prog, pc - 1, level - 1)
                }
            case _ => jumpLeft(prog, pc - 1, level)

        }
    }
}


def jtable(pg: String) : Map[Int, Int] = {
  jtableCompute(pg, 0, Map[Int, Int]())
}

def jtableCompute(pg: String, pc: Int, map: Map[Int, Int]) : Map[Int, Int] = {
  if (pc < 0 || pc > pg.length - 1){
    map
  }
  else{
    pg.charAt(pc) match {
      case '[' => jtableCompute(pg, pc + 1, map.updated(pc, jumpRight(pg, pc+1, 0)))
      case ']' => jtableCompute(pg, pc + 1, map.updated(pc, jumpLeft(pg, pc-1, 0)))
      case _ => jtableCompute(pg, pc + 1, map)
    }
  }
}

def optimise(program: String): String = {
  program.replaceAll("""[^<>+\-.\[\]]""", "").replaceAll("""\[-\]""", "0")
}

def combine(s: String) : String = {
    val result1 = """\++""".r.replaceAllIn(s, m => combineHelper(m.matched.length, '+'))
    val result2 = """\-+""".r.replaceAllIn(result1, m => combineHelper(m.matched.length, '-'))
    val result3 = """\<+""".r.replaceAllIn(result2, m => combineHelper(m.matched.length, '<'))
    """\>+""".r.replaceAllIn(result3, m => combineHelper(m.matched.length, '>'))

}

def combineHelper(length : Int, command : Char) : String = {
    length match{
        case _ if length > 26 => command +: 'Z' +: combineHelper(length - 26, command)
        case _ => command +: ('A' + (length - 1)).toChar.toString
    }
}


def compute4(pg: String, tb: Map[Int, Int], pc: Int, mp: Int, mem: Mem) : Mem = {
  if (pc > pg.length - 1 || pc < 0){
        mem
    }
    else{
        pg.charAt(pc) match{
          case '>' => compute4(pg, tb, pc + 2, mp + (pg(pc + 1).toInt - 'A'.toInt + 1), mem)
          case '<' => compute4(pg, tb, pc + 2, mp - (pg(pc + 1).toInt - 'A'.toInt + 1), mem)
          case '+' => 
            if(mem.contains(mp)){
                compute4(pg, tb, pc + 2, mp, write(mem, mp, mem(mp) + (pg(pc + 1).toInt - 'A'.toInt + 1)))
            }
            else{
                compute4(pg, tb, pc + 2, mp, write(mem, mp, (pg(pc + 1).toInt - 'A'.toInt + 1)))
            }
            
          case '0' =>compute4(pg, tb, pc + 1, mp, write(mem, mp, 0))
          case '-' => 
            if(mem.contains(mp)){
              compute4(pg, tb, pc + 2, mp, write(mem, mp, mem(mp) - (pg(pc + 1).toInt - 'A'.toInt + 1)))
            }
            else{
              compute4(pg, tb, pc + 2, mp, write(mem, mp, -(pg(pc + 1).toInt - 'A'.toInt + 1)))
            }
          case '.' => 
            print(mem(mp).toChar)
            compute4(pg, tb, pc + 1, mp, mem)
          case '[' =>
            if (sread(mem, mp) == 0){
                compute4(pg, tb, tb(pc), mp, mem)
            }
            else{
                compute4(pg, tb, pc + 1, mp, mem)
            }
          case ']' =>
            if (sread(mem, mp) != 0){
                compute4(pg, tb, tb(pc), mp, mem)
            }
            else{
                compute4(pg, tb, pc + 1, mp, mem)
            }
          case _ => compute4(pg, tb, pc + 1, mp, mem)

        }
    }
}

def run4(pg: String, m: Mem = Map()) = {
    val optimised = combine(optimise(pg))
    compute4(optimised, jtable(optimised), 0, 0, m)
}



// testcases
// time_needed(1, run4(load_bff("benchmark.bf")))
// time_needed(1, run4(load_bff("sierpinski.bf"))) 
// time_needed(1, run4(load_bff("mandelbrot.bf")))
// time_needed(1, run4(load_bff("collatz.bf")))



}
