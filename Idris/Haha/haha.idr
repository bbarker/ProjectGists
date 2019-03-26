module Main

import Effects
import Effect.StdIO

hello : Eff () [STDIO]
hello = let ha = StdIO.putStr "ha" in ha *> ha

main : IO ()
main = run hello
