## Status

[![Build Status](https://travis-ci.org/ggajos/ot-clean.svg?branch=master)](https://travis-ci.org/ggajos/ot-clean)

## Motivation

If you are working on multiple projects you probably encounter fact that lots
of garbage is produced. In many cases `clean` target from maven, sbt or any
other buld tool is not enough. This project want to target this issue. By simple
executing `ot-clean` you can get rid of all garbage.

## Usage

Unpack [latest release](https://github.com/ggajos/ot-clean/releases) and add it
to your `path`. You might want to read also [How to efficiently manage PATH variable in Windows](http://ggajos.com/environment-variables-management/).

### .clean.yml

You can create `.clean.yml` file and specify which files/dirs you would like to
delete. Similar to `.gitignore` concept but using well known `yml` file standard.

```
deletes:
 - target
 - subproject/target/logs
 - ../logs
```

#### Run

Go to directory you want to clean and type. No worries, by default it is going to
display only information what could be cleaned up. No data will be lost.

```
ot-clean
```

## Roadmap

* ✓ 0.1 - Clean maven project in current directory without using maven
* ✓ 0.2 - Recurrence parameter `r`
* ✓ 0.3 - Add support to delete custom directories via .clean.yml
* 0.4 - Support wildcards