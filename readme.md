![QuickTicket](https://github.com/jkingster/QuickTicket/blob/development/docs/images/quickticket.png)

![Static Badge](https://img.shields.io/badge/stable_version-1.0.0-green) ![Static Badge](https://img.shields.io/badge/contributions-welcome-blue) ![Static Badge](https://img.shields.io/badge/download-link-red?link=https%3A%2F%2Fgithub.com%2Fjkingster%2FQuickTicket%2Freleases%2Ftag%2Fv1.0.0)
 

# Welcome to the repo of QuickTicket.

###### Your free-to-use ticket management solution. Streamlined and lightweight, effortlessly prioritize tasks, manage employees, and assign tickets with precision. Simplify your workflow whether you're a technician or a time-management enthusiast. Plus, it's free and open-source.

###### Originally made to help me manage a local businesses support requests, I've decided why not provide it everyone else. It mainly serves as a ticket solution to those IT technicians without any popular ticket service at their disposal.

---------------------

> [!CAUTION]
> As of right now QuickTicket does not offer any sort of encryption. It is advised you do not store passwords, security keys, or anything of similarity. If you choose to do this, it's at your own risk and I bear no responsibility. You can further mitigate potential breaches by practicing proper security and access control. QuickTicket stores an SQLite database locally. 
# Table of Contents

1. [Download QuickTicket](#download)
2. [Current Features](#current-features)
3. [How To](#how-to)
4. [Troubleshooting](#troubleshooting)
5. [Nerdy Information](#nerdy-informaton)
6. [Changelog](#changelog)

---------------------------------------------------------------------

## [Download](#download)
To download QuickTicket, click the **download** badge up at the top of this readme.md, or click <a href="">this link.</a>

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
<tr>
<td>
MacOS
</td>
<tr>
:x:
</tr>
</table>

---------------------------------------

## [Current Features](#current-features)

Right now, QuickTicket aims to serve a simplified approach of managing companies, here is a list of each feature currently built-in to the most stable release.

* Ticket Creation
  * Set tickets with a low, medium, or high priority.
  * Set tickets with a open, active, paused or resolved ticket status.
  * You can attach an employee to a ticket.
  * You can post comments on a ticket to provide more information.
* Employee Creation
  * Employees can have e-mails, phone numbers (cell and work), title, and misc. information attached to their employee profile.
  * Employees can also have a company/department attached to help organize them.
* Company Creation
  * Create a company to help organize departments and employees under it.
* Department Creation
  * Create a department to help organize employees under a department, sub-categorized to companies. 

> I am open to new features, so feel free to submit a PR with suggestions. Or create an issue labeled with a suggestion tag.

---------------------------------------------

## [How To](#how-to)
Right now, there is more descriptive pages on how to do certain things within QuickTicket. You can see those documents <a href="">here.</a>.

-------------------------------------

## [Troubleshooting](#troubleshooting)

> [!IMPORTANT]
> Do not create an issue or bug report without first checking the troubleshooting documentation. Some bugs may be resolvable with certain actions to be completed. 
> You can see all documented troubleshooting pages <a href="">here.</a>

-----------------------------------

## [Nerdy Information](#nerdy-information)

You can see all dependencies QuickTicket utilizes by checking the <a href="">build.gradle</a> file.

QuickTicket is built using the following technologies to help streamline the process:
* <a href="https://www.jooq.org/">jOOQ</a> - Lightweight database mapping library.
* <a href="https://www.sqlite.org/">SQLite</a> - a C-language library that implements a small, fast, self-contained, high-reliability, full-featured, SQL database engine.
* <a href="https://flywaydb.org/">Flyway</a> - Database migrations made easy.
* There are other minor dependencies we use, but these are the three core parts.

------------------------------------

## [Changelog](#changelog)

There is nothing here yet.