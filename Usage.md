# Introduction #

This tool works in 2 modes:
  * **Privilege-audit mode** gathers information regarding groups and their access to OBIEE presentation privileges.
  * **Dashboard-audit mode** analyses what users and roles have access to dashboards, pages and reports.

Audit results are saved in XML files and, to make them easier to read, HTML pages.

These files will be created in the same directory the tool's JAR resides and the size depends on the number of objects in your webcat. If you prefer to save the results to a different location, please specify the custom location via command line.



# Auditing the webcat through command line #

`java -jar obiee-security-audit <options>`


## Options ##

|Single-letter|Verbose|Feature invoked|
|:------------|:------|:--------------|
|`-h`|`--help`|displaying help|
|`-w=<path>`|`--webcat=<path>`|setting the location of the webcat|
|`-p`|`--privs`|auditing presentation privileges|
|`-d`|`--dashboards`|auditing dashboards|
|`-t`|`--target`|setting output folder where results will be saved|


## Examples ##

  * `java -jar obiee-security-audit --help`

  * `java -jar obiee-security-audit --webcat=<path> --privs --dashboards`

  * `java -jar obiee-security-audit --webcat=<path> --privs`

  * `java -jar obiee-security-audit --webcat=<path> --dashboards`

  * `java -jar obiee-security-audit -w=<path> -p -d --target=<path>`