```
mvn package
```

Will output:
/target/all-haxe/out/html5/bin

Create an http_server there and open `index.html` at the browser.
Will will see the demo. kotlin -> jtransc -> haxe/lime -> webgl

In `pom.xml`, change `<target>lime:js:program.js</target>` with `<target>lime:swf:program.swf</target>`
to compile swf version.