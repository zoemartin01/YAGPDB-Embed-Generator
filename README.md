# YAGPDB.xyz Embed Generator

This script allows the easy creation of custom embeds for the use in YAGPDB.xys custom commands from a discord embed json created with
[an Embed Builder like this](https://embedbuilder.nadekobot.me/) or my personal favourite, the one [in the CarlBot Dashboard.](https://carl.gg/)

## Use
#### Run without building
```console
$ git clone https://github.com/zoemartin01/YAGPDB-Embed-Generator.git
$ cd YAGPDB-Embed-Generator
$ ./gradlew run
```

#### Build, then run
```console
$ git clone https://github.com/zoemartin01/YAGPDB-Embed-Generator.git
$ cd YAGPDB-Embed-Generator
$ java -jar build/libs/YAGPDB\ Embed\ Generator-*.jar
```

# Usage
```
-f,--file <arg>   The path of a local json file
OR
-u,--url <arg>    The url pointing to a remote json file

-n,--name <arg>   The name of the YAGPDB var containing the custom embed (optional)
```