#!/usr/bin/env stack
{- stack script --nix --resolver lts-14.20
  --nix-packages zlib
  --no-nix-pure
-}

{-# LANGUAGE ApplicativeDo #-}
{-# LANGUAGE ScopedTypeVariables #-}

module Main where

import Control.Monad (join)

cons :: a -> [a] -> [a]
cons x xs = x : xs

doList1 :: [Int]
doList1 = do
  x1 <- pure 10
  x2 :: Int <- 9 : pure x1
  x3 :: Int <- 8 : pure x2 -- Now we are cons'ing each prior number
  x4 :: Int <- 7 : pure x3 -- and again. Not really what we want!
  -- So we see that even though x2 :: Int, it has extra context surrounding it (prior ints):
  pure x4
  

appList :: [Int]
appList = join $ (\x y -> x : pure y) <$> (pure 10) <*> (pure 9)


main = do
  putStrLn $ show doList1
  putStrLn "The next two lists should be equal:"
  putStrLn $ show doList1
  putStrLn $ show appList