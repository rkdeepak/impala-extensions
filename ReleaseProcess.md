## Snapshot build ##

Make sure that in _shared.properties_
that project version ends with `SNAPSHOT`, e.g.

```
project.version=1.0.2-SNAPSHOT
```

and commit change if necessary.

Do build

```
ant clean dist test publishlocal
```

To deploy to Maven snapshots repository: http://oss.sonatype.org/index.html

```
ant mvndeploy
```

## Release build ##

Make sure that in _shared.properties_
that project version **does not end** with `SNAPSHOT`, e.g.

```
project.version=1.0.2
```

Commit the change if necessary.

Fix the carriage returns and line feeds:

```
ant fixcrlf
```

Then commit any changes, if necessary.

Do build with signed artifacts

```
ant clean dist test publishsignlocal
```

To deploy to Maven snapshots repository: http://oss.sonatype.org/index.html

```
ant -Dmaven.release=true mvnsigndeploy
```

See [documentation on how to promote release](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide#SonatypeOSSMavenRepositoryUsageGuide-11.WhatDoPeopleThinkAboutOSSRH).

Finally, manually tag the repository

![http://impala-extensions.googlecode.com/svn/wiki/images/tag.png](http://impala-extensions.googlecode.com/svn/wiki/images/tag.png)