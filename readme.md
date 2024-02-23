# QuickTicket


-------------------------


![Static Badge](https://img.shields.io/badge/Version-0.2.2--alpha-blue) 
![Static Badge](https://img.shields.io/badge/Pull%20Requests-Welcome-red)

## Description

Your free-to-use ticket management solution. Streamlined and lightweight, effortlessly prioritize tasks, manage employees, and assign tickets with precision. Simplify your workflow whether you're a technician or a time-management enthusiast. Plus, it's free and open-source.

Originally made to help me manage IT support requests, I've decided to actually put effort into making it something productive. This will allow IT technicians without any popular ticket service at their disposal to utilize this free software.

## Installation

> [!IMPORTANT]
> As of right now, the installation has only been tested on a Windows environment. No cross-platform tests have been run. If anyone is willing to work on this implementation, feel free to reach out.


Head over to the latest releases, or click <a href="https://www.github.com/QuickTicket/tags">here</a>. Find the latest release denoted at the top, or feel free to download older versions. Support will be offered if it can be. Right now we are standing at current version **0.2.2-alpha**. We are not in a production environment, yet.

Download the .msi installer, run it, select the directory you want the installation files to be stored. From there a shortcut and start-menu icon will be created. Run the QuickTicket.exe, and all the necessary components and files will be generated in your AppData directory. 

QuickTicket utilizes the lightweight SQLite database for easy local storage to house your tickets and employee management. It creates a basic db file along with a properties file to store general information.

## Current Features
Right now QuickTicket provides the current features:
* Ticket creation along with setting priority and status of the ticket.
* Employee creation, along with tying the employee to specific tickets and giving general information: e-mail, title, department, name, etc.
* E-mail notifications. You can set up your SMTP e-mail provider and send out e-mail notifications to the designated employee when a ticket is created.
> [!CAUTION]
> As of right now, QuickTicket utilizes SQLite and **NO** information is encrypted/hashed. This means the authentication information provided for SMTP configuration can be viewed by anyone with a technical background. It is advised you store the database file in a secure environment with the proper permissions and security enabled. I take no responsibility if this information is leaked or used maliciously. You can set the database path in your properties file found in your AppData directory.

## Nerdy Information
![Static Badge](https://img.shields.io/badge/Language-Java-darkgreen) ![Static Badge](https://img.shields.io/badge/JDK-17-blue) ![Static Badge](https://img.shields.io/badge/JavaFX%20SDK-17.0.6-blue) ![Static Badge](https://img.shields.io/badge/jOOQ-3.18.6-darkred) ![Static Badge](https://img.shields.io/badge/Database%20(SQLite--JDBC)-3.42.0.0-red) ![Static Badge](https://img.shields.io/badge/Gradle-8.4-yellow)

## Changelog
**0.2.2-alpha**
- Fixed bug: Deleting tickets was not updating the corresponding ticket count correctly. Previous bug from 0.2.1-alpha.

**0.2.1-alpha**
- Fixed bug: Marking tickets opened/resolved now updates the corresponding ticket count correctly.

**0.2.0-alpha**
- New feature: Added an e-mail button to automatically open the mail app with a predefined subject related to the targeted ticket.
- New feature: Added buttons to quickly resolve/re-open tickets along with potential other features.
- New bug: Marking tickets opened/resolved doesn't update the ticket count labels at the top.

**0.1.3-alpha**
- Fixed bug: Introduced a bug in 0.1.2-alpha (not really a bug, I'm just dumb) where if any screen is exited the entire application shuts down. See <a href="https://github.com/jkingster/QuickTicket/issues/4">here</a>.

**0.1.2-alpha**
- Fixed bug: When creating a new ticket and marking it to send e-mail notifications, it hangs and stops responding. See <a href="https://github.com/jkingster/QuickTicket/issues/3">here</a>.

**0.1.1-alpha**
- Fixed bug: Updated SQL query to ignore trigger creation if existing. See <a href="https://github.com/jkingster/QuickTicket/commit/3b2e35735e532f108966770b56466f358a0c154a">here</a>.

## Roadmap
To see the roadmap/future plans for QuickTicket, click <a href="https://github.com/jkingster/QuickTicket/blob/master/roadmap.md">here</a>.