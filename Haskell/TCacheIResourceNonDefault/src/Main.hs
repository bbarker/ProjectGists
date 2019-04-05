{-# LANGUAGE DuplicateRecordFields #-}
{-# LANGUAGE ScopedTypeVariables #-}

module Main where

import           Control.Concurrent             (threadDelay)
import qualified Data.ByteString.Lazy.Char8     as DBS (pack, unpack)
import           Data.List                      ((!!))
import           Data.List.Split                (splitOn)
import           Data.Map                       as DM
import           Data.Maybe                     (fromJust)
import           Data.TCache
import           Data.TCache.Defs
import           Data.Text                      (Text)
import qualified Data.Text                      as Txt
import           Data.Time
import           Text.Read                        (read)

main :: IO ()
main = do
  withResources [] items
  _ <- insertFarmRec farmRecSx1
  _ <- syncCache
  threadDelay 1000000

timeKey :: Text
timeKey = Txt.pack "Time"
tempKey :: Text
tempKey = Txt.pack "temp"
actKey :: Text
actKey = Txt.pack "act"

timeParseSmaXtec :: String -> Maybe UTCTime
timeParseSmaXtec = parseTimeM True defaultTimeLocale fmt
  where fmt = "%F %H:%M:%S"

formatFdsTime :: FormatTime t => t -> String
formatFdsTime = formatTime defaultTimeLocale canonicalTimeFmt
  where canonicalTimeFmt = "%F %H:%M:%S"

data HoboRecord = HoboRecord {
  time  :: UTCTime
} deriving (Eq, Read, Show, Typeable)

data FarmRecord = SxRec   {sxRec   :: SxRecord  } |
                  HoboRec {hoboRec :: HoboRecord}
                  deriving (Read, Show, Typeable)

instance Indexable FarmRecord where
  key SxRec  {sxRec   = SxRecord   {time=rowId}}  = formatFdsTime rowId
  key HoboRec{hoboRec = HoboRecord{time=rowId}} = formatFdsTime rowId

instance Serializable FarmRecord where
  serialize = DBS.pack . show
  deserialize = read . DBS.unpack

data SxRecord = SxRecord {
  time  :: UTCTime
, temp  :: Maybe Double
, act   :: Maybe Double
} deriving (Eq, Read, Show, Typeable)

instance Ord SxRecord where
  (SxRecord t1 _ _) `compare` (SxRecord t2 _ _) = t1 `compare` t2

-- TODO: use IsString str => MapRow str
fromRow :: Map Text Text -> Maybe SxRecord
fromRow mr = do
  rowTime  <- DM.lookup timeKey mr
  rowTemp  <- DM.lookup tempKey mr
  rowAct   <- DM.lookup actKey mr
  dateTime <- timeParseSmaXtec (Txt.unpack rowTime)
  return (SxRecord dateTime (readMay rowTemp) (readMay rowAct))


-- TODO: consider implications of using 'error' here.
insertFarmRec :: FarmRecord -> IO ()
insertFarmRec rec = withResource rec storeIt
 where
  storeIt :: Maybe FarmRecord -> FarmRecord
  storeIt (Just rc) = rc
  storeIt _ = error "The FarmRecord does not exist"


rowStr1 :: String
rowStr1 = "2018-12-13 13:10:00,39.28,3.96"
rowList1 :: [String]
rowList1 = splitOn "," rowStr1

row1 :: Map Text Text
row1 = DM.fromList [
  (timeKey,   Txt.pack (rowList1 !! 0))
  , (tempKey, Txt.pack (rowList1 !! 1))
  , (actKey,  Txt.pack (rowList1 !! 2))
  ]

sxRec1 :: Maybe SxRecord
sxRec1 = fromRow row1

farmRecSx1 :: FarmRecord
farmRecSx1 = SxRec (fromJust sxRec1)

items :: [Maybe FarmRecord] -> [FarmRecord]
items _ = [farmRecSx1]

