module PureScriptBook.ChapterEleven.Indent where

import Prelude

import Control.Monad.Reader
import Data.Monoid
import Data.Foldable
import Data.Traversable
import Effect (Effect)
import Effect.Console

type Level = Int
type Doc = Reader Level String

line :: String -> Doc
line str = do
  lvl <- ask
  pure $ (power " " lvl) <> str

indent :: Doc -> Doc
indent = local (\l -> l + 2)

cat :: Array Doc -> Doc
cat docs = intercalate delim docs
  where
    delim :: Doc
    delim = local (\l ->  0) (pure "\n")

render :: Doc -> String
render doc = runReader doc 0

-- Example:

indentEg1 :: Effect Unit
indentEg1 = log $ render $ cat [
  line "Here is some indented text:"
, indent $ cat
    [ line "I am indented"
    , line "So am I"
    , indent $ line "I am even more indented"
    ]
]
