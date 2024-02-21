# QuickTicket


-------------------------


![Static Badge](https://img.shields.io/badge/Version-1.0.0--beta-blue) 
![Static Badge](https://img.shields.io/badge/Pull%20Requests-Welcome-red)

## Description

Your free-to-use ticket management solution. Streamlined and lightweight, effortlessly prioritize tasks, manage employees, and assign tickets with precision. Simplify your workflow whether you're a technician or a time-management enthusiast. Plus, it's free and open-source.

## Installation

> [!IMPORTANT]
> As of right now, the installation has only been tested on a Windows environment. No cross-platform tests have been ran. If anyone is willing to work on this implementation, feel free to reach out.


Head over to the latest releases, or click <a href="https://www.github.com/QuickTicket/tags">here</a>. Find the latest relasee denoted at the top, or feel free to download older versions. Support will be offered if it can be. Right now we are standing at current version **1.0.0-beta**. We are not in a production environment, yet.

Download the .msi installer, run it, select the directory you want the installation fiels to be stored. From there a shortcut and start-menu icon will be created. Run the QuickTicket.exe, and all the necessary components and files will be generated in your AppData directory. 

QuickTicket utilizes the lightweight SQLite database for easy local storage to house your tickets and employee management. It creates a basic db file along with a properties file to store general informaton.

## Current Features
Right now QuickTicket provides the current features:
* Ticket creation along with setting priority and status of the ticket.
* Employee creation, along with tying the employee to specific tickets and giving general information: e-mail, title, department, name, etc.
* E-mail notifications. You can setup your SMTP e-mail provider and send out e-mail notifications to the designated employee when a ticket is created.
> [!CAUTION]
> As of right now, QuickTicket utilizes SQLite and **NO** informaton is encrypted/hashed. This means the authentication information provided for SMTP configuration can be viewed by anyone with a technical background. It is advised you store the database file in a secure environment with the proper permissions and security enabled. I take no responsibility if this information is leaked or used maliciously. You can set the database path in your properties file found in your AppData directory.

## Nerdy Information
![Static Badge](https://img.shields.io/badge/Language-Java-darkgreen) ![Static Badge](https://img.shields.io/badge/JDK-17-blue) ![Static Badge](https://img.shields.io/badge/JavaFX%20SDK-17.0.6-blue) ![Static Badge](https://img.shields.io/badge/jOOQ-3.18.6-darkred) ![Static Badge](https://img.shields.io/badge/Database%20(SQLite--JDBC)-3.42.0.0-red) ![Static Badge](https://img.shields.io/badge/Gradle-8.4-yellow)

## Changelog

There is no information yet.

## Roadmap
To see the roadmap/future plans for QuickTicket, click <a href="https://github.com/jkingster/QuickTicket/blob/master/roadmap.md">here</a>.