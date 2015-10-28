## Motivation

If you are working on multiple projects you probably encounter fact that lots
of garbage is produced. In many cases `clean` target from maven, sbt or any
other buld tool is not enough. This project want to target this issue. By simple
executing `ot-clean` you can get rid of all garbage.

## Usage

Unpack [latest release](https://github.com/ggajos/ot-clean/releases) and add it
to your `path`. You might want to read also [How to efficiently manage PATH variable in Windows](http://ggajos.com/environment-variables-management/).

#### Run

Go to directory you want to clean and type:

```
ot-clean
```

## Roadmap

0.1 - Clean maven project in current directory without using maven
0.2 - Recurrence parameter `r`
0.3 - Support for cleanup supressing for specific directories
0.4 - Customise which directories should be included during cleaning process.