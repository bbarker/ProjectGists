
# Running examples

0. `psc.sh bash` (if you want to use this container)
1. `spago build` if needed to update/install deps.
2. Load the exercise(s) of interest, e.g.: `spago repl -- 'chapter11/*purs'`
3. Run an exercise:
    ```

    > import PureScriptBook.ChapterEleven.Indent 
    > indentEg1
    Here is some indented text:
      I am indented
      So am I
        I am even more indented
    unit

    ```
