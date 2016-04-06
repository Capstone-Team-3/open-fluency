@rem Batch file to create the Dictionary index
set CUR_DIR=%~dp0%
set DICTIONARY_BASE_DIR=%CUR_DIR%resources\dictionaries\
@rem set DICTIONARY_BASE_DIR=%CUR_DIR%

set CINDEXER=%DICTIONARY_BASE_DIR%index_search_tools\win\cindex.exe
set SOURCE_PATH=%DICTIONARY_BASE_DIR%japanese_dictionary\jp-en.txt
set INDEX_DIR=%DICTIONARY_BASE_DIR%index\win
set INDEX_PATH=%INDEX_DIR%\jp-en.index

if not exist "%INDEX_DIR%" mkdir "%INDEX_DIR%"
@rem Create the index
start "" "%CINDEXER%" -indexpath %INDEX_PATH% -logskip -maxtrigrams 100000 %SOURCE_PATH%

@rem set CSEARCH=%DICTIONARY_BASE_DIR%index_search_tools\win\csearch.exe
@rem Check the index using
@rem "%CSEARCH%" -indexpath %INDEX_PATH% -m 10 <search_string> 
