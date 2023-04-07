# Grp_project

 > # code locations 
 >  1. java     -> java/com/example/grp_project
 >  2. xml      -> res
 >  3. manifest -> manifest

## grp_project/Storage/Record class
Class for Storing Game Attempt Record.
### Basic methods
> static Record.read(c : Context) : List<Record> 
 -> Get all completed game records from sharedpreferences.
 
> static Record.readCurrentRecord(c : Context) : Record 
 -> Get uncompleted game records from sharedpreferences due to sudden game exit.
 
> static Record.clearCurrentRecord(c : Context) : void 
 -> Clear uncompleted game records from sharedpreferences due to sudden game exit.
 
> getBoosterUsed() : List<Booster> 
 -> Return booster used from this game record
 
> getLevel() : Level 
 -> Return level info of current record
 
> getLevelInt() : int 
 -> Return integer of level number of current record
 
> getDate() : String
 -> Return String of date of attempts in "dd/mm/yy" forms
 
> isPassed() : boolean 
 -> Return if current record is passed
 
### For Game mode = TIME_MODE/ STEP_MODE/ DAILY_MODE
> getCorrectInput() : String 
 -> Return the correct words that is randomly generated
 
> getGuessRecord() : List<String>
 -> Return a list of string that user guessed
 
> getTimeUsed() : int
 -> Return the total time used for user to guess word. Only recorded in TIME_MODE.
 
### For Game mode = ENDLESS_MODE
> getEndlessCorrectInput(round : int) : String
 -> Return the correct words that is randomly generated at particular round

> getEndlessGuessRecord(round : int) : List<String>
 -> Return a list of string that user guessed at particular round

## grp_project/Storage/Level class
Class stores finalized Level Info
### Methods
> static Level.getLevel(level : int) : Level
 -> Return Level Info at given level number
 
> static Level.DAILY : int, Level.ENDLESS : int
 -> Constant integer attribute representing level number of daily and endless mode
 
> getGameMode() : GameMode 
 -> Return GameMode of current level
 
> getRewards() : Map<Product, Integer>
 -> Return rewards type (key : Product) and corresponding number granted (value : Integer) after level passed
 
> grantReward() : void
 -> Change reward number and store in shared pref
 
## grp_project/Item/GameItem interface
Interface to store basic and finalized info of constant game item
### Subclass
> Product interface -> Info and Number of purchaseable game item

> GameMode enum -> Constant game mode of various level, including STEP_MODE, TIME_MODE, DAILY_MODE, ENDLESS_MODE
 
### Method
> getName() : String -> Return name of item
 
> describe() : String -> Return description of item
 
> getDrawableInt() : int -> Return drawable resources int of item (R.drawable. ...)
 
> getColorInt() : int -> Return color resources int of item (R.color. ...)

## grp_project/Item/Product interface : GameItem
Interface to store Info and Number of purchaseable game item
### Subclass
> Booster enum -> Booster for game level

> Token enum -> Token/ currency of game. COIN Only.

### Method
> static Token.read() / Booster.read() -> read and get number of products from shared pref

> getNumber() : int -> Return number of products user hold

> increaseBy(c : Context, a : int) : void -> Increase number by a and immediately store to shared pref

> decreaseBy(c : Context, a : int) : void -> Decrease number by a and immediately store to shared pref
