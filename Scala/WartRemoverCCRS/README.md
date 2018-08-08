# CCRS: Cornell Container Runner Service

Built on Akka-http, Scala.js, Autowire and [Monadic-HTML](https://github.com/OlivierBlanvillain/monadic-html).

This project is a fork of [full-stack-scala](https://github.com/OlivierBlanvillain/full-stack-scala), which in turn is essentially a fork of [Scalajs-React TodoMVC Example](https://github.com/tastejs/todomvc/tree/gh-pages/examples/scalajs-react) enhanced with a back-end and the proper sbt configuration for development/deployment.

- `model`: Shared Todo model
- `web-client`: Scala.js/mhtml client
- `web-server`: Akka-http server
- `web-static`: Static files

#### Development:
    
```
sbt ~web-server/re-start
```

The output of the above command will list the address to enter in a browser.

#### Deployment:

* On the VM where this is being deployed, use as few sensitive credentials as possible
(read: none). In case the system is compromised, this should limit the damage. This
includes things like SSH keys linked to a github account that has write access to any
repository, or read access to any repository other than this CCRS repository.
* TODO: send logs to external server


```
sbt web-server/assembly
java -jar target/scala-2.12/web-server-assembly-1.0-SNAPSHOT.jar
```
