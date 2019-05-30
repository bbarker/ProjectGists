module PureScriptBook.ChapterEleven.ParensBalanced where

import Prelude

import Control.Monad.List.Trans (ListT(..), Step(..), foldl, iterate)
import Control.Monad.State

import Data.Array (snoc)
import Data.Foldable
import Data.String

testParens :: String -> Boolean
testParens str =??? takeWhile + traverse_ with State somehow ???
  where
    parens :: Array Char
    parens = filter isParen (toCharArray str)
    parensSign :: Array Int
    parensSign = map parensToInt parens
    parensToInt :: Char -> Boolean
    parensToInt '(' = 1
    parensToInt ')' = -1
    parensToInt _ = 0
    isParen :: Char -> Boolean
    isParen '(' = true
    isParen ')' = true
    isParent _ = false





-- takeWhileMRec :: forall m a. MonadRec m =>
--   (a -> m Boolean) -> ListT m a -> ListT m a
-- takeWhileMRec p (ListT mstep) = ListT (mstep >>= go)
--   where
--   go :: Step a (ListT m a) -> m (Step a (ListT m a))
--   go Done        = pure Done
--   go (Skip l)    = pure $ Skip $ takeWhileMRec p <$> l
--   go (Yield a l) = p a <#> \b ->
--     if b then Yield a (takeWhileMRec p <$> l) else Done

-- foldlWhileMRec :: forall m a b. => MonadRec m =>
--   (b -> a -> b) -> (b -> m Boolean) -> ListT m a -> m b
-- foldlWhileMRec f p list = takeWhileMRec


foldlWhileM :: forall m a b. Foldable m => Monad m =>
  (a -> b -> m b) -> (b -> m Boolean) -> ListT m a -> m b
foldlWhileM f p (ListT mstep) = ListT (mstep >>= go)
  where
    go :: Step a (ListT m a) -> m (Step a (ListT m a))
    go Done        = pure Done
    go (Skip l)    = pure $ Skip $ foldlWhileM p <$> l
    go (Yield a l) = p a <#> \b ->
      if b then Yield a (foldlWhileM p <$> l) else Done



{-
// Author: Asad Saeeduddin (masaeedu)

module Main where

import Prelude

import Control.Monad.List.Trans (ListT(..), Step(..), foldl, iterate)
import Control.Monad.State (class MonadState, evalState, get, modify)
import Data.Array (snoc)
import Effect (Effect)
import Effect.Console (logShow)

takeWhileM :: forall m a. Monad m => (a -> m Boolean) -> ListT m a -> ListT m a
takeWhileM f (ListT mstep) = ListT (mstep >>= go)
  where
  go :: Step a (ListT m a) -> m (Step a (ListT m a))
  go Done        = pure Done
  go (Skip l)    = pure $ Skip $ takeWhileM f <$> l
  go (Yield a l) = f a <#> \b -> if b then Yield a (takeWhileM f <$> l) else Done

numbers :: forall m. Monad m => ListT m Int
numbers = iterate (_ + 1) 0

someNumbers :: forall m. MonadState Int m => ListT m Int
someNumbers = takeWhileM predicate numbers
  where
  predicate i = do
    _ <- modify (_ + i)
    sum <- get
    pure $ (sum <= 10)

result :: forall m. MonadState Int m => m (Array Int)
result = foldl snoc [] someNumbers

main :: Effect Unit
main = logShow $ flip evalState 0 $ result
-- [0, 1, 2, 3, 4]


 -}
