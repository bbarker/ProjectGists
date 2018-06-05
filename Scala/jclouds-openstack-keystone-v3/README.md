# Debug procedure

This uses a build tool called `mill`, which has
been included in the repo for convenience. You may
need to `chmod +x mill`. You may need to add the
directory with `mill` to your `PATH` temporarily,
depending on the shell being used. Or you can
install it from the vendor: http://www.lihaoyi.com/mill/.

Two terminals will be needed for the below procedure.

## For opening in an IDE:

Run the following to generate IntelliJ project files:

```
mill mill.scalalib.GenIdea/idea 
```

The project can then be opened in IntelliJ.

## Start up test server

In one terminal, start the test server. By default, this
will run on port 5001:


Note that it will lie about the port being used for some reason:
>Server online at http://localhost:8080/

The port/address  can be configured by modifying
the following lines:

### server

Change the following line as desired

```scala
val bindingFuture = Http().bindAndHandle(route, "localhost", 5001)
```

in `sbh/repeater/src/server/RepeatAll.scala`

### client

in the `OpenRC` class in `sbh/core/src/openstack/OpenRC.scala`:

```scala 
  OS_AUTH_URL: String = "http://localhost:5001/v3",
```

## Attempt connection with jclouds

In another terminal, start the mill scala console:

```
mill -i sbh.core.console
```

Then paste the following lines and press enter:

```scala
import edu.cornell.cac.sbh.core.openstack._
val openrc = OpenRC()
val conn = OpenRC.connect(openrc)
val region = conn.getConfiguredRegions.iterator.next
```

The last line can be repeated to generate more requests; the
request output should be observable from the terminal 
running the test server.


# Testing against a real OpenStack Server

Just change the connection info in the `OpenRC` class in:

`sbh/core/src/openstack/OpenRC.scala`

If you don't want to put a password in the file, you can 
swap out the commenting on the two lines:

```scala
    //readLine(None) //FIXME DEBUG: hardcoding password for testing:
    "foobar".toCharArray
```