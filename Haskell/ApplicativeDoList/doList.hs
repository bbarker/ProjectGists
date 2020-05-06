#!/usr/bin/env stack
{- stack script --nix --resolver lts-14.20
  --nix-packages zlib
  --no-nix-pure
-}

{-# LANGUAGE ApplicativeDo #-}
{-# LANGUAGE ScopedTypeVariables #-}


-- | do
-- |   x <- a
-- |   y <- b
-- |   return (f x y)
-- |
-- |
-- | Translates to:
-- |
-- | (\x y -> f x y) <$> a <*> b

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

doList2 :: [Int]
doList2 = do
  let x1 = pure 10
  let x2 = 9 : x1
  let x3 = 8 : x2
  7 : x3
  -- This gets us what we want, but I feel it is kind of cheating
  -- as we're not using <*> and just using a lot of lets and conses.

doList3 :: [Int]
doList3 = do
  x1 <- pure 10
  x2 <- pure 9
  x3 <- pure 8
  x4 <- pure 7
  [x1,x2,x3,x4]

appList :: [Int]
appList = join $ (\x y z w -> [x,y,z,w])
  <$> (pure 10) <*> (pure 9) <*> (pure 8) <*> (pure 7)


main = do
  putStrLn $ show doList1
  putStrLn "The next two lists should be equal:"
  putStrLn $ show doList3
  putStrLn $ show appList