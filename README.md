# Brainf*** Interpreter

Implementation of a Brainf*** interpreter in Scala, exploring the intriguing world of esoteric programming languages. Brainf***, developed by Urban Miller in 1993, boasts a minimalistic set of instructions—just eight characters in total, making it a unique and challenging language to work with. Despite its simplicity, Brainf*** is Turing complete, allowing the implementation of any algorithm in theory, albeit with significant determination and resource requirements.

## Background

Brainf***'s minimal set of commands includes >, <, +, -, ., [, and ], with all other characters considered comments. This interpreter operates on memory cells containing integers, utilizing a single memory pointer (mp) that points to different memory cells. Commands involve moving the memory pointer, increasing/decreasing the content of the pointed memory cell, and outputting ASCII characters.

## Examples

Explore sophisticated Brainf*** programs provided in the repo, including programs for generating the Sierpinski triangle and the Mandelbrot set. Witness the language's versatility and expressiveness, despite its seemingly limited set of commands.

## Usage

Run the interpreter on Brainf*** programs written in the specified format. The interpreter supports the standard Brainf*** commands, allowing you to experiment with various programs. The provided examples demonstrate the language's potential for creating complex visual patterns and mathematical representations.
To run each:

    run4(load_bff("benchmark.bf"))
    run4(load_bff("sierpinski.bf"))
    run4(load_bff("mandelbrot.bf"))
    run4(load_bff("collatz.bf"))

To measure time taken to run each:

    time_needed(1, run4(load_bff("benchmark.bf")))
    time_needed(1, run4(load_bff("sierpinski.bf"))) 
    time_needed(1, run4(load_bff("mandelbrot.bf")))
    time_needed(1, run4(load_bff("collatz.bf")))



## Additional Resources

Check out a dedicated Windows IDE for Brainfuck programs: Brainf*** IDE on Microsoft Store. Explore the possibilities and challenges of Brainfuck programming, and immerse yourself in the fascinating world of esoteric languages.
