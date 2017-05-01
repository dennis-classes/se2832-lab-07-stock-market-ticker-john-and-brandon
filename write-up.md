# SE2832 Lab 2 Write Up
## Brandon Jackson 
### Email: jacksonbrant@msoe.edu
## Jonathan Kim
### Email: kimj@msoe.edu

## Final Coverage
![final code coverage](/codeCoverage.png)

## Fault Table
|Original Line Number|Fault Description                                                      |Fault Fix                                                                                               |
|--------------------|-----------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
|119                 |The "in the money" sound should be played when the percent change is greater than 1% change as opposed to greater than 0% change | change '>0' to '>1'   |
|122                 |The "sad audio" sound should be played when the percent change is not greater than 1% but plays only at negative values | change '<= -1' to '< 1' |
|151                 |if currentQuote was null the method would run normally, and an exception was thrown if it was not null.|Changed if statement from currentQuote != null to currentQuote == null  |
|185                 |Wrong exception was being thrown                                           |Changed NullPointerException to InvalidAnalysisState|
|187                 |was: return currentQuote.getChange()-currentQuote.getClose(); to return the changeSinceClose, but ChangeSinceClosed is returned by getChange().|changed to return currentQuote.getChange(); |   
|220                 |was return currentQuote.getLastTrade() - currentQuote.getLastTrade(); which always returns 0. |changed to: return currentQuote.getLastTrade() - previousQuote.getLastTrade();  |
|220                 |check for null quotes was missing. Therefore no exception was ever thrown. |added if statement to check if either previousQuote or currentQuote were null              |
