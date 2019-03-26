#!/usr/bin/env stack
-- stack --resolver lts-13.14 script
{-# LANGUAGE OverloadedStrings #-}

import           Data.Int
import           Data.Maybe                       (fromJust, isJust, listToMaybe)
import           Data.String                      (IsString(..))
import           Data.Text                        (Text, pack, splitOn, unpack)
import           Data.Time.Calendar               (Day(..))
import           Data.Time.Clock                  (UTCTime(..))
import           Data.Typeable

newtype CowMark = CowMark {unCowMark :: Int32}
  deriving (Eq, Ord, Read, Show, Typeable)

newtype TableName = TableName {unTableName :: Text}
  deriving (Eq, Ord, Read, Show)

instance IsString TableName where
  fromString str = TableName $ pack str

type CowRecordKey = (TableName, CowMark, UTCTime)

read' :: Read a => String -> a
read' s = case reads s of
  [(x,"")] -> x
  _  -> error $ " read error, reading: '" ++ s ++ "'"

rep2Key :: String -> Maybe CowRecordKey
rep2Key strKey = do
  splList <- return $ splitOn "_" (pack strKey)
  (tblName, mrkStr, timeStr) <- head3 splList
  miInt <- return $ read' (unpack mrkStr)
  timeOut <- return $ read (unpack timeStr)
  return $ (TableName $ tblName, CowMark miInt, timeOut)
  where
    head3 :: [a] -> Maybe (a, a, a)
    head3 list@(_:x2:xs) = do
      mx1 <- listToMaybe list
      mx2 <- listToMaybe (x2:xs)
      mx3 <- listToMaybe xs
      return (mx1, mx2, mx3)
    head3 _ = Nothing

cowRecKey1 :: CowRecordKey
cowRecKey1 = (
    TableName "SxRecord"
  , CowMark 6531
  , UTCTime (ModifiedJulianDay 3234) 0
  )

cowRecKeyStr1 :: String
cowRecKeyStr1 = "SxRecord_6531_1867-09-25 00:00:00"

main = do
  key1 <- return $ rep2Key cowRecKeyStr1
  print key1

