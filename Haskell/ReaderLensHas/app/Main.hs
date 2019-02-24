#!/usr/bin/env stack
-- stack --resolver lts-8.12 script
{-# LANGUAGE FlexibleContexts #-}
{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE TemplateHaskell #-}
{-# LANGUAGE MultiParamTypeClasses #-}
{-# LANGUAGE FunctionalDependencies #-}
import Control.Concurrent.Async.Lifted.Safe
import Control.Monad.Reader
import Control.Concurrent.STM
import Say
import Control.Lens
import Prelude hiding (log)

data Env = Env
  { envLog :: !(String -> IO ())
  , envBalance :: !(TVar Int)
  }

makeLensesWith camelCaseFields ''Env

modify :: (MonadReader env m, HasBalance env (TVar Int), MonadIO m)
       => (Int -> Int)
       -> m ()
modify f = do
  env <- ask
  liftIO $ atomically $ modifyTVar' (env^.balance) f

logSomething :: (MonadReader env m, HasLog env (String -> IO ()), MonadIO m)
             => String
             -> m ()
logSomething msg = do
  env <- ask
  liftIO $ (env^.log) msg

main :: IO ()
main = do
  ref <- newTVarIO 4
  let env = Env
        { envLog = sayString
        , envBalance = ref
        }
  runReaderT
    (concurrently
      (modify (+ 1))
      (logSomething "Increasing account balance"))
    env
  balance <- readTVarIO ref
  sayString $ "Final balance: " ++ show balance
