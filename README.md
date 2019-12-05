# csc413-secondgame

## Student Name  : Calvin Tam
## Student ID    : 917902523

### Player control
- ←: turn left
- →: turn right
- SPACEBAR: ignite

### Develop environment
- OpenJDK 11.0.5
- Working directory: root

### Compile to run
```bash
$ cd src \
&& javac galacticmail/*.java galacticmail/gameobject/*.java \
&& jar -cvfe ../jar/galacticmail.jar galacticmail.Launcher galacticmail/*.class galacticmail/gameobject/*.class ../resources/* \
&& rm galacticmail/*.class galacticmail/gameobject/*.class \
&& cd .. \
&& java -jar jar/galacticmail.jar
```
