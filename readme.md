![QuickTicket](https://github.com/jkingster/QuickTicket/blob/development/docs/images/quickticket.png)

![Static Badge](https://img.shields.io/badge/stable_version-1.0.3-green) ![Static Badge](https://img.shields.io/badge/contributions-welcome-blue) ![Static Badge](https://img.shields.io/badge/download-link-red?link=https%3A%2F%2Fgithub.com%2Fjkingster%2FQuickTicket%2Freleases%2Ftag%2Fv1.0.0)

# Welcome to the repo of QuickTicket.

###### Your free-to-use ticket management solution. Streamlined and lightweight, effortlessly prioritize tasks, manage employees, and assign tickets with precision. Simplify your workflow whether you're a technician or a time-management enthusiast. Plus, it's free and open-source.

###### Originally made to help me manage a local businesses support requests, I've decided why not provide it everyone else. It mainly serves as a ticket solution to those IT technicians without any popular ticket service at their disposal.

---------------------

> [!CAUTION]
> As of right now QuickTicket does not offer any sort of encryption. It is advised you do not store passwords, security
> keys, or anything of similarity. If you choose to do this, it's at your own risk and I bear no responsibility. You can
> further mitigate potential breaches by practicing proper security and access control. QuickTicket stores an SQLite
> database locally.

# Table of Contents

1. [Download QuickTicket](#download)
2. [Current Features](#current-features)
3. [How To](#how-to)
4. [Troubleshooting](#troubleshooting)
5. [Nerdy Information](#nerdy-informaton)
6. [Changelog](#changelog)

---------------------------------------------------------------------

## [Download](#download)

To download QuickTicket, click the **download** badge up at the top of this readme.md, or click <a href="">this
link.</a>

#### OS Compatability (X may indicate untested)

<table>
<tr>
<td>
Windows 10/11
</td>
<td>
:white_check_mark:
</td>
</tr>
<td>
MacOS
</td>
<td>
:x:
</td>
<tr>


</table>

---------------------------------------

## [Current Features](#current-features)

Right now, QuickTicket aims to serve a simplified approach of managing companies, here is a list of each feature
currently built-in to the most stable release.

* Ticket Creation
    * Set tickets with a low, medium, or high priority.
    * Set tickets with a open, active, paused or resolved ticket status.
    * You can attach an employee to a ticket.
    * You can post comments on a ticket to provide more information.
* Employee Creation
    * Employees can have e-mails, phone numbers (cell and work), title, and misc. information attached to their employee
      profile.
    * Employees can also have a company/department attached to help organize them.
* Company Creation
    * Create a company to help organize departments and employees under it.
* Department Creation
    * Create a department to help organize employees under a department, sub-categorized to companies.

> I am open to new features, so feel free to submit a PR with suggestions. Or create an issue labeled with a suggestion
> tag.

---------------------------------------------

## [How To](#how-to)

Right now, there is more descriptive pages on how to do certain things within QuickTicket. You can see those
documents <a href="">here.</a>.

-------------------------------------

## [Troubleshooting](#troubleshooting)

> [!IMPORTANT]
> Do not create an issue or bug report without first checking the troubleshooting documentation. Some bugs may be
> resolvable with certain actions to be completed.
> You can see all documented troubleshooting pages <a href="">here.</a>

-----------------------------------

## [Versioning](#versioning)
* For baseline, the versioning of QuickTicket tends to follow the guidelines set by <a href="https://semver.org/">semver</a>.
* The convention follows this baseline: X.Y.Z.[N] - [] indicating optional.
  * **X** is the major version.
    * Increments based on major features, database changes, or otherwise breaking changes.
  * **Y** is the minor version.
    * Increments based on "minor" features - this would still include possible database changes but otherwise none-breaking ones.
  * **Z** is the patch version.
    * Increments based on small updates, bug fixes or backwards compatible improvements.
  * **N** is the pre-release identifier.
    * The pre-release identifier is denoted by the following: (0) alpha, (1) beta, (2) rc (release candidate)
    * On the software itself, the pre-release identifier will marked with the actual title of the identifier.
      * E.g. 1.1.0-beta, instead of 1.1.0.1
    * On the build.gradle (primarily), since Windows cannot have identifier titles, we denote the versioning with the number.
      * E.g. 1.1.0.1 would be the same as 1.1.0-beta, but for the publisher version it will show as 1.1.0.1

## [Nerdy Information](#nerdy-information)

You can see all dependencies QuickTicket utilizes by checking the <a href="">build.gradle</a> file.

QuickTicket is built using the following technologies to help streamline the process:

* <a href="https://www.jooq.org/">jOOQ</a> - Lightweight database mapping library.
* <a href="https://www.sqlite.org/">SQLite</a> - a C-language library that implements a small, fast, self-contained,
  high-reliability, full-featured, SQL database engine.
* <a href="https://flywaydb.org/">Flyway</a> - Database migrations made easy.
* There are other minor dependencies we use, but these are the three core parts.

------------------------------------

## [Changelog](#changelog)

### **v1.0.3** - 6/7/2024
- Fixed bug: When clicking the "mark resolved" button on the ticket viewer or dashboard, the resolving comment would not be added if choosing NOT to notify the employee.
- Fixed bug: Notification e-mail of ticket creation discrepancies with the creation date and initial comments were swapped. 

### **v1.0.2** - 5/24/2024
- Fixed bug: Clicking the reset button resets the employee lookup combo boxes.
- Fixed bug: Creating, deleting or updating tickets when a filter is selected now appropriately updates the ticket table.

### **v1.0.1** - 5/8/2024
- Fixed bug: Test SMTP e-mails not sending.
- Fixed bug: Comments would not delete from tickets. Added confirmation.
- Fixed bug: Updating user would not change text field until client restarted.
- Fixed bug: Reset button under employee tab would not reset company and department lookup.